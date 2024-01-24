package az.baxtiyargil.graphqldemo.configuration;

import az.baxtiyargil.graphqldemo.model.Filter;
import az.baxtiyargil.graphqldemo.service.TariffPackageService;
import com.blazebit.persistence.integration.graphql.GraphQLEntityViewSupport;
import com.blazebit.persistence.integration.graphql.GraphQLEntityViewSupportFactory;
import com.blazebit.persistence.view.EntityViewManager;
import graphql.GraphQL;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.graphql.data.GraphQlArgumentBinder;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;

import static az.baxtiyargil.graphqldemo.model.constant.ApplicationConstants.GRAPH_QUERY;
import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@RequiredArgsConstructor
public class GraphQLProvider {

    private final EntityViewManager evm;
    private final TariffPackageService tariffPackageService;

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
        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring().build();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(ExtendedScalars.Json)
                .type(GRAPH_QUERY, builder -> builder.dataFetcher("findTariffPackageById",
                        dataFetchingEnvironment -> tariffPackageService.findById(
                                graphQLEntityViewSupport.createSetting(dataFetchingEnvironment),
                                dataFetchingEnvironment.getArgument("id"))
                ))
                .type(GRAPH_QUERY, builder -> builder.dataFetcher("findAllTariffPackages",
                        dataFetchingEnvironment -> tariffPackageService.findAll(
                                graphQLEntityViewSupport.createSetting(dataFetchingEnvironment))
                ))
                .type(GRAPH_QUERY, builder -> builder.dataFetcher("findBerries",
                        dataFetchingEnvironment -> tariffPackageService.findBerries(
                                dataFetchingEnvironment.getArgument("id"))))
                .type(GRAPH_QUERY, builder -> builder.dataFetcher("search",
                        dataFetchingEnvironment -> tariffPackageService.search(
                                graphQLEntityViewSupport.createSetting(dataFetchingEnvironment),
                                (Filter) Objects.requireNonNull(new GraphQlArgumentBinder().bind(
                                        dataFetchingEnvironment,
                                        "filter", ResolvableType.forMethodParameter(getMethodParameter()))))))
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

    private MethodParameter getMethodParameter() throws ClassNotFoundException {
        var clazz = Class.forName("az.baxtiyargil.graphqldemo.service.TariffPackageService");
        var method = Arrays.stream(clazz.getDeclaredMethods())
                .filter(as -> as.getName().equals("search"))
                .findAny().get();
        return new MethodParameter(method, 1);
    }

}
