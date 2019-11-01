package io.github.jhipster.sample.message;

import io.github.jhipster.sample.config.MessagesBundleMessageSource;
import io.micronaut.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.exceptions.ConfigurationException;
import org.thymeleaf.messageresolver.AbstractMessageResolver;
import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.util.Validate;

import javax.inject.Singleton;

@Singleton
public class MicronautThymeMessageResolver extends AbstractMessageResolver  {

    private final StandardMessageResolver standardMessageResolver;
    private MessageSource messageSource;

    public MicronautThymeMessageResolver(MessagesBundleMessageSource messagesBundleMessageSource) {
        super();
        this.standardMessageResolver = new StandardMessageResolver();
        this.messageSource = messagesBundleMessageSource;
    }

    public final String resolveMessage(
        final ITemplateContext context, final Class<?> origin, final String key, final Object[] messageParameters) {

        Validate.notNull(context.getLocale(), "Locale in context cannot be null");
        Validate.notNull(key, "Message key cannot be null");

        /*
         * FIRST STEP: Look for the message using template-based resolution
         */
        if (context != null) {

            checkMessageSourceInitialized();

            try {
                return this.messageSource.getMessage(key, MessageSource.MessageContext.of(context.getLocale())).orElse(null);
            } catch (NoSuchMessageException e) {
                // Try other methods
            }

        }

        /*
         * SECOND STEP: Look for the message using origin-based resolution, delegated to the StandardMessageResolver
         */
        if (origin != null) {
            // We will be disabling template-based resolution when delegating in order to use only origin-based
            final String message =
                this.standardMessageResolver.resolveMessage(context, origin, key, messageParameters, false, true, true);
            if (message != null) {
                return message;
            }
        }


        /*
         * NOT FOUND, return null
         */
        return null;

    }

    /*
     * Check the message source has been set.
     */
    private void checkMessageSourceInitialized() {
        if (this.messageSource == null) {
            throw new ConfigurationException(
                "Cannot initialize " + MicronautThymeMessageResolver.class.getSimpleName() +
                    ": MessageSource has not been set. Either define this object as " +
                    "a Spring bean (which will automatically set the MessageSource) or, " +
                    "if you instance it directly, set the MessageSource manually using its "+
                    "corresponding setter method.");
        }
    }


    public String createAbsentMessageRepresentation(
        final ITemplateContext context, final Class<?> origin, final String key, final Object[] messageParameters) {
        return this.standardMessageResolver.createAbsentMessageRepresentation(context, origin, key, messageParameters);
    }
}
