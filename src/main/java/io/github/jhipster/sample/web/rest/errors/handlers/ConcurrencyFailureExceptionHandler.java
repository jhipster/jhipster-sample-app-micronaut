package io.github.jhipster.sample.web.rest.errors.handlers;

import io.github.jhipster.sample.web.rest.errors.ErrorConstants;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import org.springframework.dao.ConcurrencyFailureException;
import org.zalando.problem.*;
import javax.inject.Singleton;

@Singleton
public class ConcurrencyFailureExceptionHandler extends ProblemHandler implements ExceptionHandler<ConcurrencyFailureException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, ConcurrencyFailureException exception) {
        Problem problem = Problem.builder()
            .withStatus(Status.CONFLICT)
            .with(MESSAGE_KEY, ErrorConstants.ERR_CONCURRENCY_FAILURE)
            .build();

        return create(problem, request, exception);
    }
}
