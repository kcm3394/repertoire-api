package personal.kcm3394.repertoireapi.domain;

import com.sun.istack.NotNull;
import org.hibernate.annotations.Nationalized;
import personal.kcm3394.repertoireapi.domain.enums.Epoch;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Composer {

    @Id
    @GeneratedValue
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
    private List<Song> compositions;

    public Composer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String completeName) {
        this.name = completeName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
    }

    public Epoch getEpoch() {
        return epoch;
    }

    public void setEpoch(Epoch epoch) {
        this.epoch = epoch;
    }

    public List<Song> getCompositions() {
        return compositions;
    }

    public void setCompositions(List<Song> compositions) {
        this.compositions = compositions;
    }
}
