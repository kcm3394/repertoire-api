package personal.kcm3394.repertoireapi.domain;

import com.sun.istack.NotNull;
import personal.kcm3394.repertoireapi.domain.enums.Fach;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class AppUser {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    @NotNull
    private String username;

    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private Fach fach;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Song> repertoire;

    public AppUser() {
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Fach getFach() {
        return fach;
    }

    public void setFach(Fach fach) {
        this.fach = fach;
    }

    public List<Song> getRepertoire() {
        return repertoire;
    }

    public void setRepertoire(List<Song> repertoire) {
        this.repertoire = repertoire;
    }
}
