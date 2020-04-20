package io.github.jhipster.sample.web.rest;


import io.github.jhipster.sample.domain.Label;
import io.github.jhipster.sample.repository.LabelRepository;

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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Integration tests for the {@Link LabelResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LabelResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    @Inject
    private LabelRepository labelRepository;

    @Inject @Client("/")
    RxHttpClient client;

    private Label label;

    @BeforeEach
    public void initTest() {
        label = createEntity();
    }

    @AfterEach
    public void cleanUpTest() {
        labelRepository.deleteAll();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Label createEntity() {
        Label label = new Label()
            .label(DEFAULT_LABEL);
        return label;
    }


    @Test
    public void createLabel() throws Exception {
        int databaseSizeBeforeCreate = labelRepository.findAll().size();

        // Create the Label
        HttpResponse<Label> response = client.exchange(HttpRequest.POST("/api/labels", label), Label.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeCreate + 1);
        Label testLabel = labelList.get(labelList.size() - 1);

        assertThat(testLabel.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    public void createLabelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = labelRepository.findAll().size();

        // Create the Label with an existing ID
        label.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        HttpResponse<Label> response = client.exchange(HttpRequest.POST("/api/labels", label), Label.class)
            .onErrorReturn(t -> (HttpResponse<Label>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = labelRepository.findAll().size();
        // set the field null
        label.setLabel(null);

        // Create the Label, which fails.

        HttpResponse<Label> response = client.exchange(HttpRequest.POST("/api/labels", label), Label.class)
            .onErrorReturn(t -> (HttpResponse<Label>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllLabels() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

        // Get the labelList w/ all the labels
        List<Label> labels = client.retrieve(HttpRequest.GET("/api/labels?eagerload=true"), Argument.listOf(Label.class)).blockingFirst();
        Label testLabel = labels.get(0);


        assertThat(testLabel.getLabel()).isEqualTo(DEFAULT_LABEL);
    }
    
    @Test
    public void getLabel() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

        // Get the label
        Label testLabel = client.retrieve(HttpRequest.GET("/api/labels/" + this.label.getId()), Label.class).blockingFirst();


        assertThat(testLabel.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    public void getNonExistingLabel() throws Exception {
        // Get the label
        HttpResponse<Label> response = client.exchange(HttpRequest.GET("/api/labels/"+ Long.MAX_VALUE), Label.class)
            .onErrorReturn(t -> (HttpResponse<Label>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }
    
    @Test
    public void updateLabel() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

        int databaseSizeBeforeUpdate = labelRepository.findAll().size();

        // Update the label
        Label updatedLabel = labelRepository.findById(label.getId()).get();

        updatedLabel
            .label(UPDATED_LABEL);

        HttpResponse<Label> response = client.exchange(HttpRequest.PUT("/api/labels", updatedLabel), Label.class)
            .onErrorReturn(t -> (HttpResponse<Label>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeUpdate);
        Label testLabel = labelList.get(labelList.size() - 1);

        assertThat(testLabel.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    public void updateNonExistingLabel() throws Exception {
        int databaseSizeBeforeUpdate = labelRepository.findAll().size();

        // Create the Label

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        HttpResponse<Label> response = client.exchange(HttpRequest.PUT("/api/labels", label), Label.class)
            .onErrorReturn(t -> (HttpResponse<Label>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteLabel() throws Exception {
        // Initialize the database with one entity
        labelRepository.saveAndFlush(label);

        int databaseSizeBeforeDelete = labelRepository.findAll().size();

        // Delete the label
        HttpResponse<Label> response = client.exchange(HttpRequest.DELETE("/api/labels/"+ label.getId()), Label.class)
            .onErrorReturn(t -> (HttpResponse<Label>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        // Validate the database is now empty
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Label.class);
        Label label1 = new Label();
        label1.setId(1L);
        Label label2 = new Label();
        label2.setId(label1.getId());
        assertThat(label1).isEqualTo(label2);
        label2.setId(2L);
        assertThat(label1).isNotEqualTo(label2);
        label1.setId(null);
        assertThat(label1).isNotEqualTo(label2);
    }
}
