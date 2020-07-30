package pl.lodz.p.it.boorger.utils;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class MessageProvider {

    private static MessageSource messageSource;

    public static String getTranslatedText(String key, String language) {
        Locale locale;
        if(language.isBlank())
            locale = new Locale("pl");
        else locale = new Locale(language);
        return messageSource.getMessage(key, null, locale);
    }

    public static void setMessageSource(MessageSource messageSource) {
        MessageProvider.messageSource = messageSource;
    }
}
