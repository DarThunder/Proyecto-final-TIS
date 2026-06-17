package mx.edu.uv.user_service.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import mx.edu.uv.user_service.dto.UserEditRequest;
import mx.edu.uv.user_service.dto.UserRequest;
import mx.edu.uv.user_service.dto.UserResponse;
import mx.edu.uv.user_service.entity.UserEntity;
import mx.edu.uv.user_service.repo.UserRepository;
import mx.edu.uv.user_service.sec.JwtUtils;

@Service
public class UserService {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String register(UserRequest request, String token){
        int idRol = jwtUtils.getIdRolFromJwtToken(token);
        if (idRol != 1) {
            throw new RuntimeException("Acceso denegado: Solo los administradores pueden registrar usuarios");
        }

        if (request.getNombre() == null || request.getNombre().trim().isEmpty() || request.getNombre().length() > 50) {
            throw new RuntimeException("El nombre es obligatorio y debe tener máximo 50 caracteres");
        }

        if (request.getApellidoPaterno() == null || request.getApellidoPaterno().trim().isEmpty() || request.getApellidoPaterno().length() > 50) {
            throw new RuntimeException("El apellido paterno es obligatorio y debe tener máximo 50 caracteres");
        }

        if (request.getApellidoMaterno() != null && request.getApellidoMaterno().length() > 50) {
            throw new RuntimeException("El apellido materno no puede exceder los 50 caracteres");
        }

        if (request.getUsername() == null || request.getUsername().trim().isEmpty() || request.getUsername().length() > 50) {
            throw new RuntimeException("El username es obligatorio y debe tener máximo 50 caracteres");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty() || request.getPassword().length() > 100) {
            throw new RuntimeException("La contraseña es obligatoria y debe tener máximo 100 caracteres");
        }

        if (request.getCorreo() == null || request.getCorreo().trim().isEmpty() || request.getCorreo().length() > 100) {
            throw new RuntimeException("El correo es obligatorio y debe tener máximo 100 caracteres");
        }

        if (request.getIdRol() <= 0 || request.getIdTipoUsuario() <= 0 || request.getIdProgramaEducativo() <= 0) {
            throw new RuntimeException("Los identificadores de catálogo deben ser válidos y mayores a 0");
        }

        //regex para ver si el correo es "username alfanumerico y caracteres especiales" + @ + "nombre de dominio alfanumerico y caracteres especiales" + . + "dominio alfabetico de 2 a 6 charas"
        String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (request.getCorreo() == null || !request.getCorreo().matches(regexEmail)) {
            throw new RuntimeException("El formato del correo es inválido");
        }

        if(userRepository.existeUsuarioPorCorreoOUsername(request.getUsername(), request.getCorreo())){
            throw new RuntimeException("Usuario con mismo correo o username ya registrado");
        }

        //La clave se forma con las iniciales del nombre completo + un número aleatorio de 3 dígitos
        String iniciales = request.getNombre().substring(0,1) + request.getApellidoPaterno().substring(0, 1);
        if (request.getApellidoMaterno() != null && !request.getApellidoMaterno().trim().isEmpty()) {
            iniciales += request.getApellidoMaterno().substring(0, 1);
        }
        String claveGenerada = (iniciales + "-" + ((int)(Math.random() * 900) + 100)).toUpperCase();

        String passwdHash = passwordEncoder.encode(request.getPassword());

        UserEntity newUser = new UserEntity(request.getNombre(), request.getApellidoPaterno(), request.getApellidoMaterno(), request.getCorreo(), request.getUsername(), passwdHash, claveGenerada, "1", LocalDateTime.now(), request.getIdRol(), request.getIdTipoUsuario());
        newUser.setIdProgramaEducativo(request.getIdProgramaEducativo());
        userRepository.insertarUsuario(newUser);
        return "Usuario registrado exitosamente con clave: " + claveGenerada;
    }

    public boolean exist(int username){
        Boolean exist = userRepository.existeUsuario(username);
        return exist != null && exist;
    }

    public boolean status(String claveUsuario){
        //estatusUsuario puede regresar null si la clave no existe, en ese caso se trata como inactivo
        Boolean status = userRepository.estatusUsuario(claveUsuario);
        return status != null && status;
    }

    public UserResponse perf(int idUsuario){
        UserResponse user = userRepository.perfilUsuario(idUsuario);
        if (user == null){
            throw new RuntimeException("Usuario con id "+ idUsuario + " no existe");
        }
        return user;
    }

    public String edit(int idUsuario, UserEditRequest request) {
        if (request.getNombre() == null || request.getNombre().trim().isEmpty() || request.getNombre().length() > 50) {
            throw new RuntimeException("El nombre es obligatorio y debe tener máximo 50 caracteres");
        }

        if (request.getApellidoPaterno() == null || request.getApellidoPaterno().trim().isEmpty() || request.getApellidoPaterno().length() > 50) {
            throw new RuntimeException("El apellido paterno es obligatorio y debe tener máximo 50 caracteres");
        }

        if (request.getApellidoMaterno() != null && request.getApellidoMaterno().length() > 50) {
            throw new RuntimeException("El apellido materno no puede exceder los 50 caracteres");
        }

        if (request.getCorreo() == null || request.getCorreo().trim().isEmpty() || request.getCorreo().length() > 100) {
            throw new RuntimeException("El correo es obligatorio y debe tener máximo 100 caracteres");
        }

        if (request.getTelefono() != null && request.getTelefono().length() > 15) {
            throw new RuntimeException("El teléfono no puede exceder los 15 caracteres");
        }

        if (request.getIdRol() <= 0 || request.getIdTipoUsuario() <= 0 || request.getIdProgramaEducativo() <= 0) {
            throw new RuntimeException("Los identificadores de catálogo deben ser válidos y mayores a 0");
        }

        //regex para ver si el correo es "username alfanumerico y caracteres especiales" + @ + "nombre de dominio alfanumerico y caracteres especiales" + . + "dominio alfabetico de 2 a 6 charas"
        String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"; 
        if (request.getCorreo() == null || !request.getCorreo().matches(regexEmail)) {
            throw new RuntimeException("El formato del correo es inválido");
        }

        if (userRepository.correoUsado(request.getCorreo(), idUsuario)) {
            throw new RuntimeException("El correo ya está registrado en otra cuenta");
        }

        UserEntity updateUser = new UserEntity();
        updateUser.setIdUsuario(idUsuario);
        updateUser.setIdRol(request.getIdRol());
        updateUser.setIdTipoUsuario(request.getIdTipoUsuario());
        updateUser.setIdProgramaEducativo(request.getIdProgramaEducativo());
        updateUser.setNombre(request.getNombre());
        updateUser.setApellidoPaterno(request.getApellidoPaterno());
        updateUser.setApellidoMaterno(request.getApellidoMaterno());
        updateUser.setCorreo(request.getCorreo());
        updateUser.setTelefono(request.getTelefono()); 
        updateUser.setTiempoActualizacion(LocalDateTime.now());
        userRepository.actualizarUsuario(updateUser);

        return "Usuario actualizado correctamente";
    }

    public String changeStatus(int idUsuario, String token) {
        //Solo el rol 1 (administrador) puede cambiar el estatus de otros usuarios
        int idRol = jwtUtils.getIdRolFromJwtToken(token);
        if (idRol != 1) {
            throw new RuntimeException("Acceso denegado: Solo los administradores pueden cambiar el estatus de un usuario");
        }

        userRepository.cambiarEstatusUsuario(idUsuario);
        return "Estatus del usuario actualizado correctamente";
    }
}