package personal.kcm3394.songcomposerservice.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import personal.kcm3394.songcomposerservice.model.Language;

@Component
public class StringToLanguageConverter implements Converter<String, Language> {

    //todo handle exception globally with @RestControllerAdvice, remove try-catch block

    @Override
    public Language convert(String s) {
        try {
            return Language.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Language.OTHER;
        }
    }
}

//https://www.baeldung.com/spring-enum-request-param