package personal.kcm3394.repertoireapi.domain;

import personal.kcm3394.repertoireapi.domain.enums.Fach;

public class AppUserDTO {

    private Long id;
    private String username;
    private Fach fach;
    private Repertoire repertoire;

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

    public Repertoire getRepertoire() {
        return repertoire;
    }

    public void setRepertoire(Repertoire repertoire) {
        this.repertoire = repertoire;
    }
}
