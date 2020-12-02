package personal.kcm3394.songcomposerservice.model.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RepertoireDto {

    private Long id;
    private Long userId;
    private List<SongDto> repertoire;
}
