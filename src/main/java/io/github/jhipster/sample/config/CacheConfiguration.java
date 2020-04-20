package io.github.jhipster.sample.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Properties;

import io.github.jhipster.sample.util.JHipsterProperties;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Factory;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.ehcache.jsr107.EhcacheCachingProvider;

import javax.cache.CacheManager;
import javax.inject.Singleton;

@Factory
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());

        // TODO add caffiene configuration
    }

    @Singleton
    public CacheManager cacheManager(ApplicationContext applicationContext) {
        CacheManager cacheManager = new EhcacheCachingProvider().getCacheManager(
            null, applicationContext.getClassLoader(), new Properties());
        customizeCacheManager(cacheManager);
        return cacheManager;
    }

    private void customizeCacheManager(CacheManager cm) {
        createCache(cm, io.github.jhipster.sample.repository.UserRepository.USERS_BY_LOGIN_CACHE);
        createCache(cm, io.github.jhipster.sample.repository.UserRepository.USERS_BY_EMAIL_CACHE);
        createCache(cm, io.github.jhipster.sample.domain.User.class.getName());
        createCache(cm, io.github.jhipster.sample.domain.Authority.class.getName());
        createCache(cm, io.github.jhipster.sample.domain.User.class.getName() + ".authorities");
        createCache(cm, io.github.jhipster.sample.domain.BankAccount.class.getName());
        createCache(cm, io.github.jhipster.sample.domain.Label.class.getName());
        createCache(cm, io.github.jhipster.sample.domain.Operation.class.getName());
        createCache(cm, io.github.jhipster.sample.domain.BankAccount.class.getName() + ".operations");
        createCache(cm, io.github.jhipster.sample.domain.Label.class.getName() + ".operations");
        createCache(cm, io.github.jhipster.sample.domain.Operation.class.getName() + ".labels");
        // jhipster-needle-ehcache-add-entry
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
