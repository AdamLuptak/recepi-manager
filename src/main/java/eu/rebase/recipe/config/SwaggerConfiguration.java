package eu.rebase.recipe.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OperationCustomizer customGlobalHeaders() {

        return (Operation operation, HandlerMethod handlerMethod) -> {

            var schema = new StringSchema();
            schema._default("1");
            Parameter xUserid = new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .schema(schema)
                    .name("X-USERID")
                    .description("header description2")
                    .required(true);

            operation.addParametersItem(xUserid);

            return operation;
        };
    }
}
