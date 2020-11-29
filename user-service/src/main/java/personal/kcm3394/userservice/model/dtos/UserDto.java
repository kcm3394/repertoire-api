package personal.kcm3394.userservice.model.dtos;

import lombok.Getter;
import lombok.Setter;
import personal.kcm3394.userservice.model.Fach;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String username;
    private Fach fach;
}
