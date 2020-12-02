package personal.kcm3394.songcomposerservice.model.dtos;

import lombok.Getter;
import lombok.Setter;
import personal.kcm3394.songcomposerservice.model.Epoch;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
public class ComposerDto {

    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private LocalDate birthDate;
    private LocalDate deathDate;
    private Epoch epoch;
}
