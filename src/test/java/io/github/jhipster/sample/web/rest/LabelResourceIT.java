package io.github.jhipster.sample.web.rest;

import io.github.jhipster.sample.domain.BankAccount;
import io.github.jhipster.sample.domain.Label;
import io.github.jhipster.sample.repository.LabelRepository;
import io.micronaut.context.annotation.Property;
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

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Label createEntity() {
        Label label = new Label();
        label.setLabel(DEFAULT_LABEL);
        return label;
    }

    @BeforeEach
    public void initTest() {
        label = createEntity();
    }

    @AfterEach
    public void cleanUpTest() {
        labelRepository.deleteAll();
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
//
//    @Test
//    @Transactional
//    public void getAllLabels() throws Exception {
//        // Initialize the database
//        labelRepository.saveAndFlush(label);
//
//        // Get all the labelList
//        restLabelMockMvc.perform(get("/api/labels?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(label.getId().intValue())))
//            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
//    }
//
//    @Test
//    @Transactional
//    public void getLabel() throws Exception {
//        // Initialize the database
//        labelRepository.saveAndFlush(label);
//
//        // Get the label
//        restLabelMockMvc.perform(get("/api/labels/{id}", label.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(label.getId().intValue()))
//            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
//    }
//
//    @Test
//    @Transactional
//    public void getNonExistingLabel() throws Exception {
//        // Get the label
//        restLabelMockMvc.perform(get("/api/labels/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateLabel() throws Exception {
//        // Initialize the database
//        labelRepository.saveAndFlush(label);
//
//        int databaseSizeBeforeUpdate = labelRepository.findAll().size();
//
//        // Update the label
//        Label updatedLabel = labelRepository.findById(label.getId()).get();
//        // Disconnect from session so that the updates on updatedLabel are not directly saved in db
//        em.detach(updatedLabel);
//        updatedLabel.setLabel(UPDATED_LABEL);
//
//        restLabelMockMvc.perform(put("/api/labels")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(updatedLabel)))
//            .andExpect(status().isOk());
//
//        // Validate the Label in the database
//        List<Label> labelList = labelRepository.findAll();
//        assertThat(labelList).hasSize(databaseSizeBeforeUpdate);
//        Label testLabel = labelList.get(labelList.size() - 1);
//        assertThat(testLabel.getLabel()).isEqualTo(UPDATED_LABEL);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingLabel() throws Exception {
//        int databaseSizeBeforeUpdate = labelRepository.findAll().size();
//
//        // Create the Label
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restLabelMockMvc.perform(put("/api/labels")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(label)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Label in the database
//        List<Label> labelList = labelRepository.findAll();
//        assertThat(labelList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    public void deleteLabel() throws Exception {
//        // Initialize the database
//        labelRepository.saveAndFlush(label);
//
//        int databaseSizeBeforeDelete = labelRepository.findAll().size();
//
//        // Delete the label
//        restLabelMockMvc.perform(delete("/api/labels/{id}", label.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isNoContent());
//
//        // Validate the database is empty
//        List<Label> labelList = labelRepository.findAll();
//        assertThat(labelList).hasSize(databaseSizeBeforeDelete - 1);
//    }

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
