package io.github.jhipster.sample.web.rest;

import io.github.jhipster.sample.domain.Operation;
import io.github.jhipster.sample.repository.OperationRepository;
import io.github.jhipster.sample.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.sample.util.HeaderUtil;
import io.github.jhipster.sample.util.PaginationUtil;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.transaction.annotation.ReadOnly;





import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.jhipster.sample.domain.Operation}.
 */
@Controller("/api")
public class OperationResource {

    private final Logger log = LoggerFactory.getLogger(OperationResource.class);

    private static final String ENTITY_NAME = "operation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OperationRepository operationRepository;

    public OperationResource(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    /**
     * {@code POST  /operations} : Create a new operation.
     *
     * @param operation the operation to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new operation, or with status {@code 400 (Bad Request)} if the operation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/operations")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Operation> createOperation(@Body Operation operation) throws URISyntaxException {
        log.debug("REST request to save Operation : {}", operation);
        if (operation.getId() != null) {
            throw new BadRequestAlertException("A new operation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Operation result = operationRepository.save(operation);
        URI location = new URI("/api/operations/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /operations} : Updates an existing operation.
     *
     * @param operation the operation to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated operation,
     * or with status {@code 400 (Bad Request)} if the operation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the operation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/operations")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Operation> updateOperation(@Body Operation operation) throws URISyntaxException {
        log.debug("REST request to update Operation : {}", operation);
        if (operation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Operation result = operationRepository.update(operation);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, operation.getId().toString()));
    }

    /**
     * {@code GET  /operations} : get all the operations.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of operations in body.
     */
    @Get("/operations{?eagerload}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<List<Operation>> getAllOperations(HttpRequest request, Pageable pageable, @Nullable Boolean eagerload) {
        log.debug("REST request to get a page of Operations");
        Page<Operation> page;
        if (eagerload != null && eagerload) {
            page = operationRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = operationRepository.findAll(pageable);
        }
        return HttpResponse.ok(page.getContent()).headers(headers ->
            PaginationUtil.generatePaginationHttpHeaders(headers, UriBuilder.of(request.getPath()), page));
    }

    /**
     * {@code GET  /operations/:id} : get the "id" operation.
     *
     * @param id the id of the operation to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the operation, or with status {@code 404 (Not Found)}.
     */
    @Get("/operations/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<Operation> getOperation(@PathVariable Long id) {
        log.debug("REST request to get Operation : {}", id);
        
        return operationRepository.findOneWithEagerRelationships(id);
    }

    /**
     * {@code DELETE  /operations/:id} : delete the "id" operation.
     *
     * @param id the id of the operation to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/operations/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteOperation(@PathVariable Long id) {
        log.debug("REST request to delete Operation : {}", id);
        operationRepository.deleteById(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
