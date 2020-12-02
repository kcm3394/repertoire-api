package personal.kcm3394.songcomposerservice.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Repertoire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "repertoire_song",
            joinColumns = @JoinColumn(name = "repertoire_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id"))
    private Set<Song> repertoire;
}
