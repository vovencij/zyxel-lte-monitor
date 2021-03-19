package eu.vshor.zyxel;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class LteMetricsSource {

  private static final Logger log = LoggerFactory.getLogger(LteMetricsSource.class);
  private final RestTemplate restTemplate;
  private final ZyxelConfig zyxelConfig;

  public LteMetricsSource(RestTemplate restTemplate, ZyxelConfig modemConfig) throws IOException, URISyntaxException {
    this.restTemplate = restTemplate;
    this.zyxelConfig = modemConfig;
    login();
  }

  private void login() {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://" + zyxelConfig.host + "/log/in")
        .queryParam("un", zyxelConfig.user)
        .queryParam("pw", zyxelConfig.password)
        .queryParam("rd", "/diagnostic.xml")
        .queryParam("rd2", "/diagnostic.xml")
        .queryParam("Nrd", 1)
        .queryParam("Ntime", System.currentTimeMillis() / 1000)
        .queryParam("csrftok", "");
    var url = builder.build().toUri();
    restTemplate.getForObject(url, Diag.class);
  }

  @NotNull
  public LteMetrics getLteMetrics() {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://" + zyxelConfig.host + "/diagnostic.xml");

    var url = builder.build().toUri();

    Diag firstTry = restTemplate.execute(url, HttpMethod.GET, null, (response -> {
      if (MediaType.TEXT_HTML.equals(response.getHeaders().getContentType())) {
        log.info("Received html response. Probably need to re-login.");
        return null;
      }
      return new HttpMessageConverterExtractor<>(Diag.class, restTemplate.getMessageConverters()).extractData(response);
    }));

    if (firstTry != null) {
      return firstTry.getLteMetrics();
    }

    log.info("Trying to login");
    login();
    return getLteMetrics();
  }

  @XmlRootElement(name = "diag")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Diag {
    @XmlElement(name = "item")
    public List<DiagItem> items;

    public Optional<String> getString(String name) {
      return items.stream().filter(di -> name.equals(di.name)).findFirst().map(di -> di.value);
    }

    public Long getLong(String name) {
      return getString(name).map(Long::parseLong).orElse(0L);
    }

    public LteMetrics getLteMetrics() {
      LteMetrics result = new LteMetrics();
      result.cellid = getLong("cellid");
      result.rssiscc = getLong("rssiscc");
      result.rsrpscc = getLong("rsrpscc");
      result.rsrqscc = getLong("rsrqscc");
      result.sinrscc = getLong("sinrscc");
      result.earfcnscc = getLong("earfcnscc");
      result.rsrp = getLong("rsrp");
      result.rsrq = getLong("rsrq");
      result.sinr = getLong("sinr");
      result.antenna = getString("antenna").map(String::strip).orElse("unknown");
      return result;
    }

  }

  @XmlRootElement(name = "item")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class DiagItem {
    @XmlAttribute(name = "name")
    public String name;
    @XmlValue
    public String value;

    @Override
    public String toString() {
      return "DiagItem{" +
          "name='" + name + '\'' +
          ", value='" + value + '\'' +
          '}';
    }
  }
}
