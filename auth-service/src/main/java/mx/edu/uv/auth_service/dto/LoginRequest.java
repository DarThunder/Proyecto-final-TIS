package mx.edu.uv.auth_service.dto;

public class LoginRequest {
    private String password;
    private String username;

    public LoginRequest(){}
    public LoginRequest(String password, String username){
        this.password = password;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
