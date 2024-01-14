package az.baxtiyargil.graphqldemo.configuration;

import az.baxtiyargil.graphqldemo.model.entity.TariffPackage;
import az.baxtiyargil.graphqldemo.repository.TariffPackageBLZRepository;
import com.blazebit.persistence.integration.graphql.GraphQLEntityViewSupport;
import com.blazebit.persistence.integration.graphql.GraphQLEntityViewSupportFactory;
import com.blazebit.persistence.integration.graphql.GraphQLRelayConnection;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.persistence.view.Sorters;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.TypeRuntimeWiring;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@RequiredArgsConstructor
public class GraphQLProvider {

    private final EntityViewManager evm;
    private final TariffPackageBLZRepository tariffPackageRepository;

    private GraphQL graphQL;
    private GraphQLSchema schema;
    private GraphQLEntityViewSupport graphQLEntityViewSupport;

    @PostConstruct
    public void init() throws IOException {
        ClassPathResource resource = new ClassPathResource("graphql/query.graphqls");
        String sdl = FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream(), UTF_8));
        this.schema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(schema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        GraphQLEntityViewSupportFactory graphQLEntityViewSupportFactory = new GraphQLEntityViewSupportFactory(true, true);
        graphQLEntityViewSupportFactory.setImplementRelayNode(false);
        graphQLEntityViewSupportFactory.setDefineRelayNodeIfNotExist(true);
        this.graphQLEntityViewSupport = graphQLEntityViewSupportFactory.create(typeRegistry, evm);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring().type(TypeRuntimeWiring.newTypeWiring("Query")
                        .dataFetcher("findAllTariffPackages", dataFetchingEnvironment -> {
                            EntityViewSetting<TariffPackage, ?> setting = graphQLEntityViewSupport.createPaginatedSetting(dataFetchingEnvironment);
                            setting.addAttributeSorter("id", Sorters.ascending());
                            if (setting.getMaxResults() == 0) {
                                return new GraphQLRelayConnection<>(Collections.emptyList());
                            }
                            return new GraphQLRelayConnection<>(tariffPackageRepository.findAll(setting));
                        })
                        .dataFetcher("findTariffPackageById", dataFetchingEnvironment -> tariffPackageRepository.findById(graphQLEntityViewSupport.createSetting(dataFetchingEnvironment), dataFetchingEnvironment.getArgument("id")))
                )
                .build();
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .type("Query", builder -> builder.dataFetcher("findTariffPackageById", dataFetchingEnvironment -> tariffPackageRepository.findById(graphQLEntityViewSupport.createSetting(dataFetchingEnvironment), dataFetchingEnvironment.getArgument("id"))))
                .build();
    }

    @Bean
    public GraphQLSchema getSchema() {
        return schema;
    }

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

}