package personal.kcm3394.songcomposerservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;

@Entity
@Getter
@Setter
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
    private Language language;

    @Enumerated(EnumType.STRING)
    private Type type;
}
