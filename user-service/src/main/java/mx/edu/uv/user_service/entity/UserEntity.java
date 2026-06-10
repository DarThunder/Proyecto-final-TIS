package mx.edu.uv.user_service.entity;

import java.time.LocalDateTime;

public class UserEntity {

    private int idUsuario;
    private int idRol;
    private int idTipoUsuario;
    private int idProgramaEducativo;
    private String claveUsuario;
    private String estatus;
    private LocalDateTime tiempoCreacion;
    private String username;
    private String password;
    private String correo;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;

    public UserEntity() {
    }

    public UserEntity(String nombre, String apellidoPaterno, String apellidoMaterno, String correo, String username, String password, String claveUsuario, String estatus, LocalDateTime tiempoCreacion, int idRol, int idTipoUsuario) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.correo = correo;
        this.username = username;
        this.password = password;
        this.claveUsuario = claveUsuario;
        this.estatus = estatus;
        this.tiempoCreacion = tiempoCreacion;
        this.idRol = idRol;
        this.idTipoUsuario = idTipoUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdRol() {
        return idRol;
    }

    public int getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public int getIdProgramaEducativo() {
        return idProgramaEducativo;
    }

    public String getClaveUsuario() {
        return claveUsuario;
    }

    public String getEstatus() {
        return estatus;
    }

    public LocalDateTime getTiempoCreacion() {
        return tiempoCreacion;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public void setIdTipoUsuario(int idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public void setIdProgramaEducativo(int idProgramaEducativo) {
        this.idProgramaEducativo = idProgramaEducativo;
    }

    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public void setTiempoCreacion(LocalDateTime tiempoCreacion) {
        this.tiempoCreacion = tiempoCreacion;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
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

}
