package personal.kcm3394.songcomposerservice.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import personal.kcm3394.songcomposerservice.model.Epoch;

@Component
public class StringToEpochConverter implements Converter<String, Epoch> {

    //todo handle exception globally with @RestControllerAdvice, remove try-catch block

    @Override
    public Epoch convert(String s) {
        try {
            return Epoch.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Epoch.OTHER;
        }
    }
}
