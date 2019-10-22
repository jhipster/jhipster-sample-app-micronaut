package io.github.jhipster.sample.web.rest.errors;

import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.rules.SecurityRule;
import org.springframework.dao.ConcurrencyFailureException;

import javax.validation.Valid;

@Controller("/test")
@Secured(SecurityRule.IS_ANONYMOUS)
public class ExceptionTranslatorTestController {

    @Get("/concurrency-failure")
    public void concurrencyFailure() {
        throw new ConcurrencyFailureException("test concurrency failure");
    }

    @Post("/method-argument")
    public void methodArgument(@Valid @Body TestDTO testDTO) {
    }

    @Get("/missing-servlet-request-part")
    public void missingServletRequestPartException(@Part String part) {
    }

    @Get("/missing-servlet-request-parameter")
    public void missingServletRequestParameterException(@QueryValue String param) {
    }

    @Get("/access-denied")
    @Secured("ROLE_ADMIN")
    public void accessdenied() {
    }

    @Get("/unauthorized")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public void unauthorized() {

    }

    @Get("/response-status")
    public void exceptionWithResponseStatus() {
        throw new TestResponseStatusException();
    }

    @Get("/internal-server-error")
    public void internalServerError() {
        throw new RuntimeException();
    }

    @SuppressWarnings("serial")
    public static class TestResponseStatusException extends RuntimeException {
    }

}
