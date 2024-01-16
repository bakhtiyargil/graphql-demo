package az.baxtiyargil.graphqldemo.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "az.baxtiyargil.graphqldemo.client")
public class ApplicationConfiguration {
}
