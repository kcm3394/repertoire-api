package personal.kcm3394.repertoireapi.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Repertoire {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany
    @JoinTable(name = "repertoire_song",
            joinColumns = @JoinColumn(name = "repertoire_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id"))
    private List<Song> songs;

    @OneToOne(mappedBy = "repertoire")
    private AppUser appUser;

    public Repertoire() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
