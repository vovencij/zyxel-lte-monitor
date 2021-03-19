package eu.vshor.zyxel;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="zyxel")
@Component
public class ZyxelConfig {

    public String host;
    public String user;
    public String password;

}
