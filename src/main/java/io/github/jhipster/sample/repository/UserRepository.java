package io.github.jhipster.sample.repository;

import io.github.jhipster.sample.domain.User;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.annotation.EntityGraph;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.util.List;
import java.util.Optional;
import java.time.Instant;

/**
 * Micronaut Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public static String USERS_BY_LOGIN_CACHE = "usersByLogin";

    public static String USERS_BY_EMAIL_CACHE = "usersByEmail";
    

    public Optional<User> findOneByActivationKey(String activationKey);

    public List<User> findAllByActivatedFalseAndCreatedDateBefore(Instant dateTime);

    public Optional<User> findOneByResetKey(String resetKey);
    

    public Optional<User> findOneByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = "authorities")
    public Optional<User> findOneById(Long id);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = "usersByLogin")
    public Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = "usersByEmail")
    public Optional<User> findOneByEmail(String email);

    public Page<User> findAllByLoginNot(String login, Pageable pageable);

    public void update(@Id Long id, Instant createdDate);
}
