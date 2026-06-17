package mx.edu.uv.auth_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import mx.edu.uv.auth_service.dto.LoginResponse;
import mx.edu.uv.auth_service.entity.UserEntity;
import mx.edu.uv.auth_service.repo.AuthRepository;
import mx.edu.uv.auth_service.sec.JwtUtils;

@Service
public class AuthService {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthRepository authRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginResponse autenticar(String username, String password){
        UserEntity user = authRepository.buscarPorUsername(username);

        if(user == null){
            throw new RuntimeException("Usuario no encontrado o inactivo");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Contraseña inválida");
        }

        String token = jwtUtils.generateTokenFromUsername(user.getUsername(), user.getIdRol());
        String nombreCompleto = user.getNombre() + " " + user.getApellidoPaterno() + (user.getApellidoMaterno() != null ? " " + user.getApellidoMaterno() : "");

        return new LoginResponse(user.getIdUsuario(), user.getIdRol(), user.getRol(), user.getUsername(), nombreCompleto, user.getIdTipoUsuario(), user.getTipoUsuario(), token);
    }
}
