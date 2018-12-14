package uk.ac.soton.mr2n17.demo;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;
import java.util.function.Function;

@Component
public class RequestHandler {

    private final Validator validator;

    public RequestHandler(Validator validator) {
        this.validator = validator;
    }

    public <BODY> Mono<ServerResponse> requireValidBody(Function<Mono<BODY>, Mono<ServerResponse>> block,
                                                        ServerRequest request, Class<BODY> bodyClass) {
        return request
                .bodyToMono(bodyClass)
                .flatMap(body -> {
                    Set<ConstraintViolation<BODY>> violations = validator.validate(body);
                    if (violations.isEmpty())
                        return block.apply(Mono.just(body));
                    else
                        return Mono.error(new ConstraintViolationException(violations));
                });
    }
}
