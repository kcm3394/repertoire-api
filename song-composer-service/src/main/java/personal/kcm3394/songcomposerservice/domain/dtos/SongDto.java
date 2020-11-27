package personal.kcm3394.songcomposerservice.domain.dtos;

import lombok.Getter;
import lombok.Setter;
import personal.kcm3394.songcomposerservice.domain.Language;
import personal.kcm3394.songcomposerservice.domain.Type;

@Getter
@Setter
public class SongDto {

    private Long id;
    private String title;
    private ComposerDto composer;
    private String containingWork;
    private String duration;
    private Language language;
    private Type type;
}
