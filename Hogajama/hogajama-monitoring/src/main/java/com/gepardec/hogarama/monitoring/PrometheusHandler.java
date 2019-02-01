package com.gepardec.hogarama.monitoring;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.prometheus.client.Summary;
import org.jboss.resteasy.annotations.GZIP;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;

@Path("/metrics")
//@GZIP
@ApplicationScoped
public class PrometheusHandler {
    private static final Logger logger = LoggerFactory.getLogger(PrometheusHandler.class);

    private CollectorRegistry prometheusRegistry;

    @PostConstruct
    public void init(){
        Metrics metrics = new Metrics();
        prometheusRegistry = CollectorRegistry.defaultRegistry;
        new HogaramaExports().register();
        Metrics.requestsTotal.labels("init_requests").inc();
    }

    @GET
    @Produces(TextFormat.CONTENT_TYPE_004)
    public Response getMetrics() throws IOException{
        Summary.Timer requestTimer = Metrics.requestLatency.labels("monitoring_self").startTimer();
        Metrics.requestsTotal.labels("monitoring_self").inc();
        HttpServletRequest request = ResteasyProviderFactory.getContextData(HttpServletRequest.class);
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