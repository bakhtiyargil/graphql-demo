package az.baxtiyargil.graphqldemo.repository.query;

import az.baxtiyargil.graphqldemo.client.BerryResponse;
import az.baxtiyargil.graphqldemo.client.PokemonBerryClient;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GraphQLQuery implements GraphQLQueryResolver {

    private final PokemonBerryClient berryClient;

    public BerryResponse getBerryResponse(Integer id) {
        return berryClient.getBerryInfo(id);
    }

}
