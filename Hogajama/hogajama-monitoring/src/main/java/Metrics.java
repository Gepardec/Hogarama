import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.exporter.common.TextFormat;

import javax.enterprise.context.SessionScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static io.prometheus.client.hotspot.DefaultExports.initialize;

@SessionScoped
@Path("/")
public class Metrics {


    private final Counter promRequestsTotal = Counter.build()
            .name("requests_total")
            .help("Total number of requests.")
            .register();

    {
        initialize();
    }

    @GET()
    @Path("/hello-world")
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHello() {
        promRequestsTotal.inc();
        return "hello, world";
    }

    @GET()
    @Path("/metrics")
    @Produces(MediaType.TEXT_PLAIN)
    public StreamingOutput metrics() {



        return output -> {
            try (Writer writer = new OutputStreamWriter(output)) {
                TextFormat.write004(writer, CollectorRegistry.defaultRegistry.metricFamilySamples());
            }
        };
    }
}