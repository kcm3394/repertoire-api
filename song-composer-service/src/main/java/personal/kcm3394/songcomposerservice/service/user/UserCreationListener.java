package personal.kcm3394.songcomposerservice.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import personal.kcm3394.repertoire.common.model.CreateUserEvent;
import personal.kcm3394.songcomposerservice.config.JmsConfig;
import personal.kcm3394.songcomposerservice.model.Repertoire;
import personal.kcm3394.songcomposerservice.service.RepertoireService;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCreationListener {

    private final RepertoireService repertoireService;

    @JmsListener(destination = JmsConfig.USER_CREATION_QUEUE)
    public void listen(CreateUserEvent event) {
        Long userId = event.getUserId();

        Repertoire repertoire = repertoireService.findRepertoireByUserId(userId);
        if (repertoire != null) {
            //todo custom error response
            log.error("Repertoire for user " + userId + " already exists");
        }

        log.info("Creating empty repertoire for user " + userId);
        Repertoire newRepertoire = Repertoire.builder()
                .userId(userId)
                .repertoire(new HashSet<>())
                .build();
        repertoireService.saveRepertoire(newRepertoire);
    }
}
