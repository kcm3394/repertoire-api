package personal.kcm3394.songcomposerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import personal.kcm3394.repertoire.common.model.CreateUserEvent;
import personal.kcm3394.repertoire.common.model.DeleteUserEvent;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JmsConfig {

    public static final String USER_CREATION_QUEUE = "user-creation";
    public static final String USER_DELETION_QUEUE = "user-deletion";

    @Bean //allow Jackson to serialize/deserialize TestMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");

        Map<String, Class<?>> idMapping = new HashMap<>();
        idMapping.put(CreateUserEvent.class.getName(), CreateUserEvent.class);
        idMapping.put(DeleteUserEvent.class.getName(), DeleteUserEvent.class);
        converter.setTypeIdMappings(idMapping);

        return converter;
    }
}
