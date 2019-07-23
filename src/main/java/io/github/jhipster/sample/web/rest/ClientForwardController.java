package io.github.jhipster.sample.web.rest;

import io.micronaut.http.annotation.Get;
import org.springframework.stereotype.Controller;

@Controller
public class ClientForwardController {

    /**
     * Forwards any unmapped paths (except those containing a period) to the client {@code index.html}.
     * @return forward to client {@code index.html}.
     */
    @Get("/**/{path:[^\\.]*}")
    public String forward(String path) {
        return "forward:/";
    }
}
