package az.baxtiyargil.graphqldemo.configuration;

import az.baxtiyargil.graphqldemo.model.Filter;
import az.baxtiyargil.graphqldemo.service.TariffPackageService;
import com.blazebit.persistence.integration.graphql.GraphQLEntityViewSupport;
import com.blazebit.persistence.integration.graphql.GraphQLEntityViewSupportFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.GraphQL;
import graphql.scalars.ExtendedScalars;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@RequiredArgsConstructor
public class GraphQLProvider {

    private final EntityViewManager evm;
    private final TariffPackageService tariffPackageService;
    private static final ObjectMapper MAPPER = new ObjectMapper();

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
        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .scalar(ExtendedScalars.Json)
                .build();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(ExtendedScalars.Json)
                .type("Query", builder -> builder.dataFetcher("findTariffPackageById",
                        dataFetchingEnvironment -> tariffPackageService.findById(
                                graphQLEntityViewSupport.createSetting(dataFetchingEnvironment),
                                dataFetchingEnvironment.getArgument("id"))
                ))
                .type("Query", builder -> builder.dataFetcher("findAllTariffPackages",
                        dataFetchingEnvironment -> tariffPackageService.findAll(
                                graphQLEntityViewSupport.createSetting(dataFetchingEnvironment))
                ))
                .type("Query", builder -> builder.dataFetcher("findBerries",
                        dataFetchingEnvironment -> tariffPackageService.findBerries(
                                dataFetchingEnvironment.getArgument("id"))))
                .type("Query", builder -> builder.dataFetcher("search",
                        dataFetchingEnvironment -> tariffPackageService.search(
                                graphQLEntityViewSupport.createSetting(dataFetchingEnvironment),
                                dataFetchingEnvironment.getArgument("filter"))))
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

    public static final GraphQLScalarType FILTER = GraphQLScalarType.newScalar()
            .name("Filter")
            .description("Custom scalar for filter data")
            .coercing(new Coercing<Filter, Filter>() {

                @SneakyThrows
                @Override
                public Filter serialize(@NotNull Object input) {
                    return MAPPER.readValue(input.toString(), Filter.class);
                }

                @SneakyThrows
                @NotNull
                @Override
                public Filter parseValue(@NotNull Object input) {
                    return MAPPER.readValue(input.toString(), Filter.class);
                }

                @SneakyThrows
                @NotNull
                @Override
                public Filter parseLiteral(@NotNull Object input) {
                    return MAPPER.readValue(input.toString(), Filter.class);
                }
            }).build();

}
