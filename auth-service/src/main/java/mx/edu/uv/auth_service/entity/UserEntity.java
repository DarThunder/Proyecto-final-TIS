package mx.edu.uv.auth_service.entity;

public class UserEntity {
    private int idUsuario;
    private int idRol;
    private String rol;
    private String username;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private int idTipoUsuario;
    private String tipoUsuario;
    private String password;

    public UserEntity() {}

    public UserEntity(int idUsuario, int idRol, String rol, String username, String nombre, String apellidoPaterno, String apellidoMaterno, int idTipoUsuario, String tipoUsuario){
        this.idUsuario = idUsuario;
        this.idRol = idRol;
        this.rol = rol;
        this.username = username;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.idTipoUsuario = idTipoUsuario;
        this.tipoUsuario = tipoUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdRol() {
        return idRol;
    }

    public String getRol() {
        return rol;
    }

    public String getUsername() {
        return username;
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

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
