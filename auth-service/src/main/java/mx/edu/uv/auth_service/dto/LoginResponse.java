package mx.edu.uv.auth_service.dto;

public record LoginResponse (
    int idUsuario,
    int idRol,
    String rol,
    String username,
    String nombreCompleto,
    int idTipoUsuario,
    String tipoUsuario,
    String token
){}
