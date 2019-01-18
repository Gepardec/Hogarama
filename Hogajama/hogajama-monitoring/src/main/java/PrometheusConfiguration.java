import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.MetricsServlet;

public class PrometheusConfiguration {
    CollectorRegistry metricRegistry() {
        return CollectorRegistry.defaultRegistry;
    }

}


