package io.github.jhipster.sample.web.rest;

import io.github.jhipster.sample.domain.BankAccount;
import io.github.jhipster.sample.repository.BankAccountRepository;
import io.github.jhipster.sample.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.sample.util.HeaderUtil;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import javax.annotation.Nullable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.jhipster.sample.domain.BankAccount}.
 */
@Controller("/api")
public class BankAccountResource {

    private final Logger log = LoggerFactory.getLogger(BankAccountResource.class);

    private static final String ENTITY_NAME = "bankAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BankAccountRepository bankAccountRepository;

    public BankAccountResource(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    /**
     * {@code POST  /bank-accounts} : Create a new bankAccount.
     *
     * @param bankAccount the bankAccount to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new bankAccount, or with status {@code 400 (Bad Request)} if the bankAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/bank-accounts")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<BankAccount> createBankAccount(@Body BankAccount bankAccount) throws URISyntaxException {
        log.debug("REST request to save BankAccount : {}", bankAccount);
        if (bankAccount.getId() != null) {
            throw new BadRequestAlertException("A new bankAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BankAccount result = bankAccountRepository.mergeAndSave(bankAccount);
        URI location = new URI("/api/bank-accounts/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /bank-accounts} : Updates an existing bankAccount.
     *
     * @param bankAccount the bankAccount to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated bankAccount,
     * or with status {@code 400 (Bad Request)} if the bankAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bankAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/bank-accounts")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<BankAccount> updateBankAccount(@Body BankAccount bankAccount) throws URISyntaxException {
        log.debug("REST request to update BankAccount : {}", bankAccount);
        if (bankAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BankAccount result = bankAccountRepository.mergeAndSave(bankAccount);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, bankAccount.getId().toString()));
    }

    /**
     * {@code GET  /bank-accounts} : get all the bankAccounts.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of bankAccounts in body.
     */
     @Get("/bank-accounts")
     @ExecuteOn(TaskExecutors.IO)
    public Iterable<BankAccount> getAllBankAccounts(HttpRequest request) {
        log.debug("REST request to get all BankAccounts");
        return bankAccountRepository.findAll();
    }

    /**
     * {@code GET  /bank-accounts/:id} : get the "id" bankAccount.
     *
     * @param id the id of the bankAccount to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the bankAccount, or with status {@code 404 (Not Found)}.
     */
    @Get("/bank-accounts/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<BankAccount> getBankAccount(@PathVariable Long id) {
        log.debug("REST request to get BankAccount : {}", id);
        
        return bankAccountRepository.findById(id);
    }

    /**
     * {@code DELETE  /bank-accounts/:id} : delete the "id" bankAccount.
     *
     * @param id the id of the bankAccount to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/bank-accounts/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteBankAccount(@PathVariable Long id) {
        log.debug("REST request to delete BankAccount : {}", id);
        bankAccountRepository.deleteById(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
