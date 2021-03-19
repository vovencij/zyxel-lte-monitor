package eu.vshor.zyxel;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MetricUpdater {
  private final MeterRegistry meterRegistry;
  private final LteMetricsSource metricsSource;
  private static final Logger log = LoggerFactory.getLogger(MetricUpdater.class);
  private final Map<String, LteMetrics> metrics = new HashMap<>();

  public MetricUpdater(MeterRegistry meterRegistry, LteMetricsSource metricsSource) {
    this.meterRegistry = meterRegistry;
    this.metricsSource = metricsSource;
  }

  @Scheduled(fixedRate = 10_000)
  public void updateMetrics() {
    var currentMeasurement = metricsSource.getLteMetrics();

    String cellId = String.valueOf(currentMeasurement.cellid);
    String antenna = currentMeasurement.antenna;
    String metricKey = cellId + "_" + antenna;

    var lastMetrics = metrics.computeIfAbsent(metricKey, (k) -> currentMeasurement);
    lastMetrics.update(currentMeasurement);
    //reset all others
    metrics.entrySet().stream().filter(e -> !metricKey.equals(e.getKey())).map(Map.Entry::getValue).forEach(LteMetrics::reset);

    var tags = List.of(
        Tag.of("lte.cell.id", cellId),
        Tag.of("antenna", antenna));
    meterRegistry.gauge("lte.rsrp", tags, lastMetrics.rsrp, lastMetrics::getRsrp);
    meterRegistry.gauge("lte.rsrq", tags, lastMetrics.rsrq, lastMetrics::getRsrq);
    meterRegistry.gauge("lte.earfcnscc", tags, lastMetrics.earfcnscc, lastMetrics::getEarfcnscc);
    meterRegistry.gauge("lte.rsrpscc", tags, lastMetrics.rsrpscc, lastMetrics::getRsrpscc);
    meterRegistry.gauge("lte.rsrqscc", tags, lastMetrics.rsrqscc, lastMetrics::getRsrqscc);
    meterRegistry.gauge("lte.sinr", tags, lastMetrics.sinr, lastMetrics::getSinr);
    meterRegistry.gauge("lte.sinrscc", tags, lastMetrics.sinrscc, lastMetrics::getSinrscc);
    log.trace("Sinr: {}, rsrq: {}, rsrp: {}", lastMetrics.sinr, lastMetrics.rsrq, lastMetrics.rsrp);
  }
}
