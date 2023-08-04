package dev.garmoza.shopsample.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@Configuration
public class LocaleConfig {

    @PostConstruct
    public void configureDefaultLocale() {
        // Always use english locale no matter what the locale of the OS is,
        // e.g. when printing error messages for validation
        // Note that this apparently can't be configured with a LocaleResolver but has to be explicitly
        // set as default locale here (or on JVM level)
        LocaleContextHolder.setDefaultLocale(Locale.ENGLISH);
    }
}
