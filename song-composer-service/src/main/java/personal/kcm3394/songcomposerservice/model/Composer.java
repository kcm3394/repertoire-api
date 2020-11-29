package personal.kcm3394.songcomposerservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Composer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    @NotNull
    private String name;

    private LocalDate birthDate;
    private LocalDate deathDate;

    @Enumerated(EnumType.STRING)
    private Epoch epoch;

    @OneToMany(
            mappedBy = "composer",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnoreProperties("composer")
    private Set<Song> compositions;
}
