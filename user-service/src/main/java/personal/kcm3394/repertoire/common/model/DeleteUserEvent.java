package personal.kcm3394.repertoire.common.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor //needed for Jackson to know how to construct
@AllArgsConstructor
@Builder
public class DeleteUserEvent {

    private Long userId;
}
