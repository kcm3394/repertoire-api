package personal.kcm3394.repertoireapi.domain.dtos;

import personal.kcm3394.repertoireapi.domain.Composer;
import personal.kcm3394.repertoireapi.domain.enums.Language;
import personal.kcm3394.repertoireapi.domain.enums.Status;
import personal.kcm3394.repertoireapi.domain.enums.Type;

public class SongDTO {

    private Long id;
    private String title;
    private ComposerDTO composer;
    private String containingWork;
    private String duration;
    private Language language;
    private Type type;

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

    public ComposerDTO getComposer() {
        return composer;
    }

    public void setComposer(ComposerDTO composer) {
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
