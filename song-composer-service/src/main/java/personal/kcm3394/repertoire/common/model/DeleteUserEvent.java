package personal.kcm3394.repertoire.common.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor //needed for Jackson to know how to construct
public class DeleteUserEvent {

    private Long userId;
}
