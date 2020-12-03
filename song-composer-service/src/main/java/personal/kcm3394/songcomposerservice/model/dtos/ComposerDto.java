package personal.kcm3394.songcomposerservice.model.dtos;

import lombok.Getter;
import lombok.Setter;
import personal.kcm3394.songcomposerservice.model.Epoch;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class ComposerDto {

    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private LocalDate birthDate;
    private LocalDate deathDate;

    @NotNull(message = "Composer must be assigned an epoch")
    private Epoch epoch;
}
