package pl.lodz.p.it.boorger.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class StaticContextInitializer {

    @Autowired
    private MessageSource messageSource;

    @PostConstruct
    public void initialize() {
        MessageProvider.setMessageSource(messageSource);
    }
}
