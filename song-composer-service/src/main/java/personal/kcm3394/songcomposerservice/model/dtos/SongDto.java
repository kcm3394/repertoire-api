package personal.kcm3394.songcomposerservice.model.dtos;

import lombok.Getter;
import lombok.Setter;
import personal.kcm3394.songcomposerservice.model.Language;
import personal.kcm3394.songcomposerservice.model.Type;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SongDto {

    private Long id;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotNull(message = "Song must be associated with existing composer")
    private ComposerDto composer;

    private String containingWork;
    private String duration;

    @NotNull(message = "Song must be assigned a language")
    private Language language;

    private Type type;
}
