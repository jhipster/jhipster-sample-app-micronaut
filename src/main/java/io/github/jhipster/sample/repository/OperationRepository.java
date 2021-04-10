package io.github.jhipster.sample.repository;

import io.github.jhipster.sample.domain.Operation;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;


// TODO what is MN equivalent?
// import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Micronaut Data  repository for the Operation entity.
 */
@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {


    @Query(value = "select distinct operation from Operation operation left join fetch operation.labels",
        countQuery = "select count(distinct operation) from Operation operation")
    public Page<Operation> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct operation from Operation operation left join fetch operation.labels")
    public List<Operation> findAllWithEagerRelationships();

    @Query("select operation from Operation operation left join fetch operation.labels where operation.id =:id")
    public Optional<Operation> findOneWithEagerRelationships(Long id);
}
