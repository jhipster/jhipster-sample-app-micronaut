package io.github.jhipster.sample.web.rest;


import io.github.jhipster.sample.domain.Operation;
import io.github.jhipster.sample.repository.OperationRepository;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Integration tests for the {@Link OperationResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OperationResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    @Inject
    private OperationRepository operationRepository;

    @Inject @Client("/")
    RxHttpClient client;

    private Operation operation;

    @BeforeEach
    public void initTest() {
        operation = createEntity();
    }

    @AfterEach
    public void cleanUpTest() {
        operationRepository.deleteAll();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operation createEntity() {
        Operation operation = new Operation()
            .date(DEFAULT_DATE)
            .description(DEFAULT_DESCRIPTION)
            .amount(DEFAULT_AMOUNT);
        return operation;
    }


    @Test
    public void createOperation() throws Exception {
        int databaseSizeBeforeCreate = operationRepository.findAll().size();

        // Create the Operation
        HttpResponse<Operation> response = client.exchange(HttpRequest.POST("/api/operations", operation), Operation.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeCreate + 1);
        Operation testOperation = operationList.get(operationList.size() - 1);

        assertThat(testOperation.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOperation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertEquals(testOperation.getAmount().compareTo(DEFAULT_AMOUNT), 0);
    }

    @Test
    public void createOperationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = operationRepository.findAll().size();

        // Create the Operation with an existing ID
        operation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        HttpResponse<Operation> response = client.exchange(HttpRequest.POST("/api/operations", operation), Operation.class)
            .onErrorReturn(t -> (HttpResponse<Operation>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationRepository.findAll().size();
        // set the field null
        operation.setDate(null);

        // Create the Operation, which fails.

        HttpResponse<Operation> response = client.exchange(HttpRequest.POST("/api/operations", operation), Operation.class)
            .onErrorReturn(t -> (HttpResponse<Operation>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationRepository.findAll().size();
        // set the field null
        operation.setAmount(null);

        // Create the Operation, which fails.

        HttpResponse<Operation> response = client.exchange(HttpRequest.POST("/api/operations", operation), Operation.class)
            .onErrorReturn(t -> (HttpResponse<Operation>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllOperations() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get the operationList w/ all the operations
        List<Operation> operations = client.retrieve(HttpRequest.GET("/api/operations?eagerload=true"), Argument.listOf(Operation.class)).blockingFirst();
        Operation testOperation = operations.get(0);


        assertThat(testOperation.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOperation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertEquals(testOperation.getAmount().compareTo(DEFAULT_AMOUNT), 0);
    }
    
    @Test
    public void getOperation() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get the operation
        Operation testOperation = client.retrieve(HttpRequest.GET("/api/operations/" + this.operation.getId()), Operation.class).blockingFirst();


        assertThat(testOperation.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOperation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertEquals(testOperation.getAmount().compareTo(DEFAULT_AMOUNT), 0);
    }

    @Test
    public void getNonExistingOperation() throws Exception {
        // Get the operation
        HttpResponse<Operation> response = client.exchange(HttpRequest.GET("/api/operations/"+ Long.MAX_VALUE), Operation.class)
            .onErrorReturn(t -> (HttpResponse<Operation>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }
    
    @Test
    public void updateOperation() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        int databaseSizeBeforeUpdate = operationRepository.findAll().size();

        // Update the operation
        Operation updatedOperation = operationRepository.findById(operation.getId()).get();

        updatedOperation
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT);

        HttpResponse<Operation> response = client.exchange(HttpRequest.PUT("/api/operations", updatedOperation), Operation.class)
            .onErrorReturn(t -> (HttpResponse<Operation>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
        Operation testOperation = operationList.get(operationList.size() - 1);

        assertThat(testOperation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOperation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertEquals(testOperation.getAmount().compareTo(UPDATED_AMOUNT), 0);
    }

    @Test
    public void updateNonExistingOperation() throws Exception {
        int databaseSizeBeforeUpdate = operationRepository.findAll().size();

        // Create the Operation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        HttpResponse<Operation> response = client.exchange(HttpRequest.PUT("/api/operations", operation), Operation.class)
            .onErrorReturn(t -> (HttpResponse<Operation>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteOperation() throws Exception {
        // Initialize the database with one entity
        operationRepository.saveAndFlush(operation);

        int databaseSizeBeforeDelete = operationRepository.findAll().size();

        // Delete the operation
        HttpResponse<Operation> response = client.exchange(HttpRequest.DELETE("/api/operations/"+ operation.getId()), Operation.class)
            .onErrorReturn(t -> (HttpResponse<Operation>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        // Validate the database is now empty
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Operation.class);
        Operation operation1 = new Operation();
        operation1.setId(1L);
        Operation operation2 = new Operation();
        operation2.setId(operation1.getId());
        assertThat(operation1).isEqualTo(operation2);
        operation2.setId(2L);
        assertThat(operation1).isNotEqualTo(operation2);
        operation1.setId(null);
        assertThat(operation1).isNotEqualTo(operation2);
    }
}
