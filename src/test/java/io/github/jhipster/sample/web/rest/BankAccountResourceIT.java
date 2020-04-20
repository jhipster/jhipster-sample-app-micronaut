package io.github.jhipster.sample.web.rest;


import io.github.jhipster.sample.domain.BankAccount;
import io.github.jhipster.sample.repository.BankAccountRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Integration tests for the {@Link BankAccountResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankAccountResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);

    @Inject
    private BankAccountRepository bankAccountRepository;

    @Inject @Client("/")
    RxHttpClient client;

    private BankAccount bankAccount;

    @BeforeEach
    public void initTest() {
        bankAccount = createEntity();
    }

    @AfterEach
    public void cleanUpTest() {
        bankAccountRepository.deleteAll();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankAccount createEntity() {
        BankAccount bankAccount = new BankAccount()
            .name(DEFAULT_NAME)
            .balance(DEFAULT_BALANCE);
        return bankAccount;
    }


    @Test
    public void createBankAccount() throws Exception {
        int databaseSizeBeforeCreate = bankAccountRepository.findAll().size();

        // Create the BankAccount
        HttpResponse<BankAccount> response = client.exchange(HttpRequest.POST("/api/bank-accounts", bankAccount), BankAccount.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeCreate + 1);
        BankAccount testBankAccount = bankAccountList.get(bankAccountList.size() - 1);

        assertThat(testBankAccount.getName()).isEqualTo(DEFAULT_NAME);
        assertEquals(testBankAccount.getBalance().compareTo(DEFAULT_BALANCE), 0);
    }

    @Test
    public void createBankAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bankAccountRepository.findAll().size();

        // Create the BankAccount with an existing ID
        bankAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        HttpResponse<BankAccount> response = client.exchange(HttpRequest.POST("/api/bank-accounts", bankAccount), BankAccount.class)
            .onErrorReturn(t -> (HttpResponse<BankAccount>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankAccountRepository.findAll().size();
        // set the field null
        bankAccount.setName(null);

        // Create the BankAccount, which fails.

        HttpResponse<BankAccount> response = client.exchange(HttpRequest.POST("/api/bank-accounts", bankAccount), BankAccount.class)
            .onErrorReturn(t -> (HttpResponse<BankAccount>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankAccountRepository.findAll().size();
        // set the field null
        bankAccount.setBalance(null);

        // Create the BankAccount, which fails.

        HttpResponse<BankAccount> response = client.exchange(HttpRequest.POST("/api/bank-accounts", bankAccount), BankAccount.class)
            .onErrorReturn(t -> (HttpResponse<BankAccount>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllBankAccounts() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get the bankAccountList w/ all the bankAccounts
        List<BankAccount> bankAccounts = client.retrieve(HttpRequest.GET("/api/bank-accounts?eagerload=true"), Argument.listOf(BankAccount.class)).blockingFirst();
        BankAccount testBankAccount = bankAccounts.get(0);


        assertThat(testBankAccount.getName()).isEqualTo(DEFAULT_NAME);
        assertEquals(testBankAccount.getBalance().compareTo(DEFAULT_BALANCE), 0);
    }
    
    @Test
    public void getBankAccount() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get the bankAccount
        BankAccount testBankAccount = client.retrieve(HttpRequest.GET("/api/bank-accounts/" + this.bankAccount.getId()), BankAccount.class).blockingFirst();


        assertThat(testBankAccount.getName()).isEqualTo(DEFAULT_NAME);
        assertEquals(testBankAccount.getBalance().compareTo(DEFAULT_BALANCE), 0);
    }

    @Test
    public void getNonExistingBankAccount() throws Exception {
        // Get the bankAccount
        HttpResponse<BankAccount> response = client.exchange(HttpRequest.GET("/api/bank-accounts/"+ Long.MAX_VALUE), BankAccount.class)
            .onErrorReturn(t -> (HttpResponse<BankAccount>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }
    
    @Test
    public void updateBankAccount() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();

        // Update the bankAccount
        BankAccount updatedBankAccount = bankAccountRepository.findById(bankAccount.getId()).get();

        updatedBankAccount
            .name(UPDATED_NAME)
            .balance(UPDATED_BALANCE);

        HttpResponse<BankAccount> response = client.exchange(HttpRequest.PUT("/api/bank-accounts", updatedBankAccount), BankAccount.class)
            .onErrorReturn(t -> (HttpResponse<BankAccount>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
        BankAccount testBankAccount = bankAccountList.get(bankAccountList.size() - 1);

        assertThat(testBankAccount.getName()).isEqualTo(UPDATED_NAME);
        assertEquals(testBankAccount.getBalance().compareTo(UPDATED_BALANCE), 0);
    }

    @Test
    public void updateNonExistingBankAccount() throws Exception {
        int databaseSizeBeforeUpdate = bankAccountRepository.findAll().size();

        // Create the BankAccount

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        HttpResponse<BankAccount> response = client.exchange(HttpRequest.PUT("/api/bank-accounts", bankAccount), BankAccount.class)
            .onErrorReturn(t -> (HttpResponse<BankAccount>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteBankAccount() throws Exception {
        // Initialize the database with one entity
        bankAccountRepository.saveAndFlush(bankAccount);

        int databaseSizeBeforeDelete = bankAccountRepository.findAll().size();

        // Delete the bankAccount
        HttpResponse<BankAccount> response = client.exchange(HttpRequest.DELETE("/api/bank-accounts/"+ bankAccount.getId()), BankAccount.class)
            .onErrorReturn(t -> (HttpResponse<BankAccount>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        // Validate the database is now empty
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        assertThat(bankAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankAccount.class);
        BankAccount bankAccount1 = new BankAccount();
        bankAccount1.setId(1L);
        BankAccount bankAccount2 = new BankAccount();
        bankAccount2.setId(bankAccount1.getId());
        assertThat(bankAccount1).isEqualTo(bankAccount2);
        bankAccount2.setId(2L);
        assertThat(bankAccount1).isNotEqualTo(bankAccount2);
        bankAccount1.setId(null);
        assertThat(bankAccount1).isNotEqualTo(bankAccount2);
    }
}
