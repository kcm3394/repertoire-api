package personal.kcm3394.userservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateUserRequest {

    private Fach fach;
    private String username;
    private String password;
    private String confirmPassword;
}
