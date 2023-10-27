package com.gepardec.hogarama.rest;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.core.providerfactory.ResteasyProviderFactoryImpl;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gepardec.hogarama.domain.metrics.Metrics;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Summary;
import io.prometheus.client.exporter.common.TextFormat;
import io.prometheus.client.hotspot.DefaultExports;
import jakarta.annotation.PostConstruct;

@Path("/metrics")
//@GZIP
@ApplicationScoped
public class PrometheusHandler {
    private static final Logger logger = LoggerFactory.getLogger(PrometheusHandler.class);

    private CollectorRegistry prometheusRegistry;
    
    @PostConstruct
    public void init(){

        prometheusRegistry = CollectorRegistry.defaultRegistry;
        DefaultExports.initialize();
    }

    @GET
    @Produces(TextFormat.CONTENT_TYPE_004)
    public Response getMetrics() throws IOException{
        
        Summary.Timer requestTimer = Metrics.requestLatency.labels("hogarama_monitoring", "get_metrics").startTimer();
        Metrics.requestsTotal.labels("hogarama_monitoring", "get_metrics").inc();
        ResteasyProviderFactory resteasyProviderFactory = ResteasyProviderFactoryImpl.getInstance();
        HttpServletRequest request = resteasyProviderFactory.getContextData(HttpServletRequest.class);
        StringWriter writer = new StringWriter();
        TextFormat.write004(writer, prometheusRegistry.filteredMetricFamilySamples(parse(request)));
        logger.info("GET Request {}", request);
        writer.flush();
        requestTimer.observeDuration();
        return Response.ok(writer.toString()).build();
    }

    private Set<String> parse(HttpServletRequest req) {
        String[] includedParam = req.getParameterValues("name[]");
        if (includedParam == null) {
            return Collections.emptySet();
        } else {
            return new HashSet<>(Arrays.asList(includedParam));
        }
    }
}
