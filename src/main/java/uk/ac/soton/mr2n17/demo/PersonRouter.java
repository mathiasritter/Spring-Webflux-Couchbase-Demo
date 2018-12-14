package uk.ac.soton.mr2n17.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
public class PersonRouter {

    @Bean
    public RouterFunction<ServerResponse> route(PersonHandler handler) {
        return RouterFunctions
                .route(GET("/persons").and(accept(MediaType.APPLICATION_JSON)), handler::getAllPersons)
                .andRoute(POST("/persons").and(accept(MediaType.APPLICATION_JSON)), handler::createPerson)
                .andRoute(GET("/persons/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::getPerson)
                .andRoute(PUT("/persons/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::updatePerson)
                .andRoute(DELETE("/persons/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::deletePerson);
    }

}
