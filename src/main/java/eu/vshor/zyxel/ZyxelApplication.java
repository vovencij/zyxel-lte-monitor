package eu.vshor.zyxel;

import java.net.CookieHandler;
import java.net.CookieManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@ComponentScan("eu.vshor.zyxel")
@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
public class ZyxelApplication {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.additionalMessageConverters(new Jaxb2RootElementHttpMessageConverter()).build();
  }

  public static void main(String[] args) {
    CookieHandler.setDefault(new CookieManager());
    SpringApplication.run(ZyxelApplication.class, args);
  }


}
