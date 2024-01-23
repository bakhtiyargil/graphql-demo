package az.baxtiyargil.graphqldemo.configuration.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Getter
@Component
@RefreshScope
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "blaze.query.search")
public class QueryFilterProperties {

    final HashMap<String, String> restrictions = new HashMap<>();

}
