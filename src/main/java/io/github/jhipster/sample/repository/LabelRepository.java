package io.github.jhipster.sample.repository;

import io.github.jhipster.sample.domain.Label;


import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;



/**
 * Micronaut Data  repository for the Label entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {

}
