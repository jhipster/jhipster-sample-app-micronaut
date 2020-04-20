package io.github.jhipster.sample.repository;

import io.github.jhipster.sample.domain.BankAccount;


import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;


import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.List;

/**
 * Micronaut Data  repository for the BankAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public abstract class BankAccountRepository implements JpaRepository<BankAccount, Long> {
    
    private EntityManager entityManager;


    @Query("select bankAccount from BankAccount bankAccount where bankAccount.user.login = :username ")
    abstract List<BankAccount> findByUserIsCurrentUser(String username);

    public BankAccountRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public BankAccount mergeAndSave(BankAccount bankAccount) {
        bankAccount = entityManager.merge(bankAccount);
        return save(bankAccount);
    }

}
