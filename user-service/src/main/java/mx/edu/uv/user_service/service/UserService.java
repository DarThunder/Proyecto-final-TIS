package mx.edu.uv.user_service.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import mx.edu.uv.user_service.dto.UserRequest;
import mx.edu.uv.user_service.entity.UserEntity;
import mx.edu.uv.user_service.repo.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String register(UserRequest request){
        if(userRepository.existeUsuarioPorCorreoOUsername(request.getUsername(), request.getCorreo())){
            throw new RuntimeException("Usuario con mismo correo o username ya registrado");
        }
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

    public boolean status(String claveUsuario){
        Boolean status = userRepository.estatusUsuario(claveUsuario);

        return status != null && status;
    }
}