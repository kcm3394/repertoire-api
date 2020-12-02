package personal.kcm3394.userservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class UpdateUserRequest {

    private Fach fach;

    @NotBlank(message = "Username cannot be blank")
    private String username;
}
