package com.gepardec.hogarama.domain.metrics;

import io.prometheus.client.Counter;
import io.prometheus.client.Summary;

public class Metrics {

    public static final Summary requestLatency = Summary.build()
            .name("hogarama_requests_latency_seconds")
            .help("Request latency in seconds.").labelNames("hogarama_module", "request_type").register();

    public static final Counter requestFailures = Counter.build()
            .name("hogarama_requests_failures_total")
            .help("Request failures.").labelNames("hogarama_module", "request_type").register();

    public static final Counter requestsTotal = Counter.build()
            .name("hogarama_requests_total")
            .help("Request.").labelNames("hogarama_module", "request_type").register();
}
