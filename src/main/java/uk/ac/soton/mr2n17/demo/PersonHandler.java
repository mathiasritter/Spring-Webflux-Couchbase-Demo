package uk.ac.soton.mr2n17.demo;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.Validator;
import java.net.URI;

@Component
public class PersonHandler {

    private final PersonService personService;
    private final RequestHandler requestHandler;
    private final Validator validator;

    public PersonHandler(PersonService personService, RequestHandler requestHandler, Validator validator) {
        this.personService = personService;
        this.requestHandler = requestHandler;
        this.validator = validator;
    }

    public Mono<ServerResponse> getAllPersons(ServerRequest request) {

        return personService
                .findAll()
                .collectList()
                .flatMap(p -> ServerResponse.ok().syncBody(p));
    }

    public Mono<ServerResponse> createPerson(ServerRequest request) {

        return requestHandler.requireValidBody(
                body -> {
                    return body
                            .flatMap(personService::create)
                            .flatMap(p -> ServerResponse.created(URI.create("/posts/" + p.getId())).build());
                    }, request, Person.class);
    }

    public Mono<ServerResponse> getPerson(ServerRequest request) {

        return personService
                .find(request.pathVariable("id"))
                .flatMap(p -> ServerResponse.ok().syncBody(p));
    }

    public Mono<ServerResponse> updatePerson(ServerRequest request) {

        return requestHandler.requireValidBody(
                body -> {
                    return body
                            .flatMap(p -> personService.update(request.pathVariable("id"), p))
                            .flatMap(p -> ServerResponse.ok().build());
                }, request, Person.class);
    }

    public Mono<ServerResponse> deletePerson(ServerRequest request) {

        return personService
                .delete(request.pathVariable("id"))
                .then(ServerResponse.ok().build());
    }
}
