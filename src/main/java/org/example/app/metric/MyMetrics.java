package org.example.app.metric;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyMetrics {
  private final MeterRegistry registry;
  private static final String COUNTER_METRIC_NAME = "files.counter";
  private static final String HEATMAP_METRIC_NAME = "files.heatmap";
  private static final String HISTOGRAM_METRIC_NAME = "files.histogram";

  public void getFilesCounter(long duration, String type) {
    DistributionSummary.builder(COUNTER_METRIC_NAME)
        .baseUnit("milliseconds")
        .description("Number of file counter by type")
        .tag("type", type)
        .register(registry)
        .record(duration);
  }

  public void getFilesHeatmap(long duration, String type) {
    DistributionSummary.builder(HEATMAP_METRIC_NAME)
        .baseUnit("milliseconds")
        .description("Summary of file heatmap by type")
        .tag("heatmap_type", type)
        .serviceLevelObjectives(10, 50, 100, 200, 300, 400, 500, 1000, 10000)
        .register(registry)
        .record(duration);
  }

  public void getFilesQuantile(long duration, String type) {
    DistributionSummary.builder(HISTOGRAM_METRIC_NAME)
        .baseUnit("milliseconds")
        .description("Summary of file histogram by type")
        .tag("type", type)
        .publishPercentiles(0.5, 0.75, 0.95, 0.99)
        .register(registry)
        .record(duration);
  }

  public void updateMetrics(long duration, String type) {
    getFilesCounter(duration, type);
    getFilesHeatmap(duration, type);
    getFilesQuantile(duration, type);
  }
}
