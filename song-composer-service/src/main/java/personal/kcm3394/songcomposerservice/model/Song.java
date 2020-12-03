package personal.kcm3394.songcomposerservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    @NotNull
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "composer_id")
    @NotNull
    @JsonIgnoreProperties("compositions")
    private Composer composer;

    private String containingWork;
    private String duration;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Language language;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return id.equals(song.id) &&
                title.equals(song.title) &&
                composer.equals(song.composer) &&
                language == song.language;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, composer, language);
    }
}
