package personal.kcm3394.repertoireapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import org.hibernate.annotations.Nationalized;
import personal.kcm3394.repertoireapi.domain.enums.Language;
import personal.kcm3394.repertoireapi.domain.enums.Type;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    @NotNull
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "composer_song",
                joinColumns = @JoinColumn(name = "composer_id"),
                inverseJoinColumns = @JoinColumn(name = "song_id"))
    @NotNull
    @JsonIgnoreProperties("compositions")
    private Composer composer;

    private String containingWork;
    private String duration;

    @Enumerated(EnumType.STRING)
    private Language language;

    @Enumerated(EnumType.STRING)
    private Type type;

    public Song() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Composer getComposer() {
        return composer;
    }

    public void setComposer(Composer composer) {
        this.composer = composer;
    }

    public String getContainingWork() {
        return containingWork;
    }

    public void setContainingWork(String containingWork) {
        this.containingWork = containingWork;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
