docker run --name prometheus -d --mount type=bind,source=/tmp/prometheus.yml,target=/etc/prometheus/prometheus.yml -p 9090:9090  prom/prometheus

global:
  scrape_interval:     15s # By default, scrape targets every 15 seconds.

  # Attach these labels to any time series or alerts when communicating with
  # external systems (federation, remote storage, Alertmanager).
  external_labels:
    monitor: 'codelab-monitor'

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
scrape_configs:
  # The job name is added as a label job=<job_name> to any timeseries scraped from this config.
  - job_name: 'prometheus'

    # Override the global default and scrape targets from this job every 5 seconds.
    scrape_interval: 5s

    static_configs:
      - targets: ['10.0.0.47:9090']


  - job_name: 'hogarama'

    scrape_interval: 5s
    metrics_path: /hogarama-monitoring/metrics

    static_configs:
      - targets: ['10.0.0.47:8080']

