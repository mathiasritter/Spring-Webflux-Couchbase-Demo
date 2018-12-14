package uk.ac.soton.mr2n17.demo;

import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.couchbase.client.java.error.DocumentDoesNotExistException;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes{

    private final Map<Class<?>, HttpStatus> exceptionMappings = Map.of(
            DocumentAlreadyExistsException.class, HttpStatus.CONFLICT,
            DocumentDoesNotExistException.class, HttpStatus.NOT_FOUND,
            ConstraintViolationException.class, HttpStatus.BAD_REQUEST
    );

    public GlobalErrorAttributes() {
        super(true);
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, includeStackTrace);
        HttpStatus status = getStatusForError(super.getError(request));
        errorAttributes.put("status", status.value());
        errorAttributes.put("error", status.getReasonPhrase());
        return errorAttributes;
    }

    private HttpStatus getStatusForError(Throwable error) {

        if (error instanceof ResponseStatusException) {
            return ((ResponseStatusException) error).getStatus();
        }

        ResponseStatus responseStatus = AnnotatedElementUtils
                .findMergedAnnotation(error.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            return responseStatus.code();
        }

        return exceptionMappings.getOrDefault(error.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
