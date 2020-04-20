package io.github.jhipster.sample.repository;

import io.github.jhipster.sample.domain.Label;


import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;


import javax.persistence.EntityManager;
import javax.transaction.Transactional;

/**
 * Micronaut Data  repository for the Label entity.
 */
@SuppressWarnings("unused")
@Repository
public abstract class LabelRepository implements JpaRepository<Label, Long> {
    
    private EntityManager entityManager;


    public LabelRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Label mergeAndSave(Label label) {
        label = entityManager.merge(label);
        return save(label);
    }

}
