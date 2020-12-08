package personal.kcm3394.songcomposerservice.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import personal.kcm3394.repertoire.common.model.DeleteUserEvent;
import personal.kcm3394.songcomposerservice.config.JmsConfig;
import personal.kcm3394.songcomposerservice.model.Repertoire;
import personal.kcm3394.songcomposerservice.service.RepertoireService;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDeletionListener {

    private final RepertoireService repertoireService;

    @JmsListener(destination = JmsConfig.USER_DELETION_QUEUE)
    public void listen(DeleteUserEvent event) {
        Long userId = event.getUserId();

        Repertoire repertoire = repertoireService.findRepertoireByUserId(userId);
        if (repertoire != null) {
            log.info("Deleting repertoire for user " + userId);
            repertoireService.deleteRepertoire(repertoire.getId());
        } else {
            //todo custom error response
            log.error("Repertoire for user " + userId + " does not exist");
        }
    }
}
