package personal.kcm3394.repertoireapi.domain;

import personal.kcm3394.repertoireapi.domain.enums.Fach;

public class CreateUserRequest {

    private Fach fach;
    private String username;
    private String password;
    private String confirmPassword;

    public CreateUserRequest() {
    }

    public Fach getFach() {
        return fach;
    }

    public void setFach(Fach fach) {
        this.fach = fach;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
