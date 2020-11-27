package personal.kcm3394.songcomposerservice.domain.dtos;

import lombok.Getter;
import lombok.Setter;
import personal.kcm3394.songcomposerservice.domain.Epoch;

import java.time.LocalDate;

@Getter
@Setter
public class ComposerDto {

    private Long id;
    private String name;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private Epoch epoch;
}
