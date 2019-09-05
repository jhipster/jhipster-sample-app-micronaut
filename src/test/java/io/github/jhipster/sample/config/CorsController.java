package io.github.jhipster.sample.config;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller
public class CorsController {

    @Get("/api/test-cors")
    public void testCorsOnApiPath() {
    }

    @Get("/test/test-cors")
    public void testCorsOnOtherPath() {
    }
}