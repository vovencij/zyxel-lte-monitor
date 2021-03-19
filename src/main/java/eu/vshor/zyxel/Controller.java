package eu.vshor.zyxel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  private final LteMetricsSource metricsSource;

  public Controller(LteMetricsSource metricsSource) {
    this.metricsSource = metricsSource;
  }

  @GetMapping(path = "/diagnostic")
  public LteMetrics run() {
    return metricsSource.getLteMetrics();
  }

}
