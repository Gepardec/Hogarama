package com.gepardec.hogarama.monitoring;

import io.prometheus.client.Counter;
import io.prometheus.client.Summary;

public class Metrics {

    public static final Summary requestLatency = Summary.build()
            .name("hogarama_requests_latency_seconds")
            .help("Request latency in seconds.").register();

    public static final Counter requestFailures = Counter.build()
            .name("hogarama_requests_failures_total")
            .help("Request failures.").register();

    public static final Counter requestsTotal = Counter.build()
            .name("hogarama_requests_total")
            .help("Request.").register();

    public static final Counter uploadedFilesSucceeded = Counter.build()
            .name("hogarama_upload_file_success")
            .help("Total files uploaded to S3.").register();

    public static final Counter tmpFilesNotCleared = Counter.build()
            .name("hogarama_temp_files_not_cleared")
            .help("Total files that could not be removed from cache").register();

}
