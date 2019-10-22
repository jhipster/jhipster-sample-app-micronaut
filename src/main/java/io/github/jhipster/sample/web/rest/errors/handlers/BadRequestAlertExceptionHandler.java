package io.github.jhipster.sample.web.rest.errors.handlers;

import io.github.jhipster.sample.web.rest.errors.BadRequestAlertException;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Singleton
public class BadRequestAlertExceptionHandler extends ProblemHandler implements ExceptionHandler<BadRequestAlertException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, BadRequestAlertException exception) {
        return create(exception, request, exception);
    }
}
