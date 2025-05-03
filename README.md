# Petrosiun-Artem-2-Sem-HW


### SLI
- sum(rate(http_server_requests_seconds_count{status="201"}[1m])) by (application) / sum(rate(http_server_requests_seconds_count[1m])) by (application)

### Histogram Response

- sum(rate(  files_heatmap_milliseconds_bucket[$__rate_interval])) by (le)


### Requests Count

- files_requests_total{type="upload"}

### AVG Response

- sum(files_requests_total{type="upload"})  / sum(files_requests_total)
