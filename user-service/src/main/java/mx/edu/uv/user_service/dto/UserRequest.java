package mx.edu.uv.user_service.dto;

import java.util.Optional;

public class UserRequest {
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correo;
    private String username;
    private String password;
    private int idRol;
    private int idTipoUsuario;
    private int idProgramaEducativo;

    public UserRequest() {}

    public UserRequest(String nombre, String apellidoPaterno, String apellidoMaterno, String correo, String username, String password, int idRol, int idTipoUsuario){
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.correo = correo;
        this.username = username;
        this.password = password;
        this.idRol = idRol;
        this.idTipoUsuario = idTipoUsuario;
    }

    public int getIdRol() {
        return idRol;
    }

    public String getUsername() {
        return username;
    }

    public String getCorreo() {
        return correo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public int getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public int getIdProgramaEducativo() { return idProgramaEducativo; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public void setIdTipoUsuario(int idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setIdProgramaEducativo(int idProgramaEducativo) { this.idProgramaEducativo = idProgramaEducativo; }
}
