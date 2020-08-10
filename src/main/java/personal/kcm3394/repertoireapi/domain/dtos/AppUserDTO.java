package personal.kcm3394.repertoireapi.domain.dtos;

import personal.kcm3394.repertoireapi.domain.Song;
import personal.kcm3394.repertoireapi.domain.enums.Fach;

import java.util.Set;

public class AppUserDTO {

    private Long id;
    private String username;
    private Fach fach;
    private Set<SongDTO> repertoire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Fach getFach() {
        return fach;
    }

    public void setFach(Fach fach) {
        this.fach = fach;
    }

    public Set<SongDTO> getRepertoire() {
        return repertoire;
    }

    public void setRepertoire(Set<SongDTO> repertoire) {
        this.repertoire = repertoire;
    }
}
