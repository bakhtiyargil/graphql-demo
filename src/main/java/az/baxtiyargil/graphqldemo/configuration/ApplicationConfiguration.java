package az.baxtiyargil.graphqldemo.configuration;

import az.baxtiyargil.graphqldemo.configuration.properties.QueryFilterProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "az.baxtiyargil.graphqldemo.client")
@EnableConfigurationProperties(QueryFilterProperties.class)
public class ApplicationConfiguration {
}
