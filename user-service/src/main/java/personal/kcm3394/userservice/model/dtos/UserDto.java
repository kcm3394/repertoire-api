package personal.kcm3394.userservice.model.dtos;

import lombok.Getter;
import lombok.Setter;
import personal.kcm3394.userservice.model.Fach;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserDto {

    private Long id;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    private Fach fach;
}
