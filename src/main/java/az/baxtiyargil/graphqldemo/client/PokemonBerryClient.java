package az.baxtiyargil.graphqldemo.client;

import feign.Logger;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "pokemon-berry-client",
        url = "${application.client.pokemon-berry.url}",
        primary = false,
        configuration = PokemonBerryClient.FeignConfiguration.class
)
public interface PokemonBerryClient {

    @GetMapping("/v2/berry/{id}")
    BerryResponse getBerryInfo(@PathVariable Integer id);

    class FeignConfiguration {

        @Bean
        Logger.Level feignLoggerLevel() {
            return Logger.Level.BASIC;
        }

    }

}
