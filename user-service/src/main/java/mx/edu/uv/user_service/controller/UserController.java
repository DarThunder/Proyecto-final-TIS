package mx.edu.uv.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import mx.edu.uv.user_service.dto.UserEditRequest;
import mx.edu.uv.user_service.dto.UserRequest;
import mx.edu.uv.user_service.dto.UserResponse;
import mx.edu.uv.user_service.service.UserService;

//Endpoints bajo el path /api/user
@RestController
@RequestMapping("/api/user")
public class UserController {
    //Aqui se autoinicializa UserService, que es donde está la logica
    @Autowired
    private UserService userService;

    //Endpoint POST para registrar a los usuarios
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest, @RequestHeader("Authorization") String authHeader) {
        //Se usa try por si hubo un error atraparlo y que no se caiga el servicio
        try {
            String token = authHeader.substring(7);
            //Se le pasa el UserRequest a la función register para que registre al usuario y lo meta la db
            String response = userService.register(userRequest, token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            //Cualquier error que haya pasado se manda con un 400 para que sepa que algo fallo
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/{idUsuario}/exist")
    public ResponseEntity<?> existUser(@PathVariable int idUsuario){
        boolean exist = userService.exist(idUsuario);
        return ResponseEntity.ok(exist);
    }

    //Endpoint GET para saber el estatus de cualquier usuario en base a su clave
    @GetMapping("/{claveUsuario}/status")
    public ResponseEntity<?> statusUser(@PathVariable String claveUsuario){
        //Se le pasa la clave de usuario al userService y retorna el estatus en un boleano
        boolean active = userService.status(claveUsuario);
        return ResponseEntity.ok(active);
    }

    //Endpoint GET para recuperar el perfil completo de un usuario dado su id
    @GetMapping("/profile/{idUsuario}")
    public ResponseEntity<?> perfUser(@PathVariable int idUsuario){
        //Se usa try por si hubo un error atraparlo y que no se caiga el servicio
        try{
            //Se le pasa el idUsuario para recuperar la información del perfil desde la db
            UserResponse user = userService.perf(idUsuario);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            //Cualquier error que haya pasado se manda con un 400 para que sepa que algo fallo
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    //Endpoint PUT para editar la información de un usuario, a excepción de su username y password
    @PutMapping("/{idUsuario}")
    public ResponseEntity<?> editUser(@PathVariable int idUsuario, @RequestBody UserEditRequest editRequest) {
        //Se usa try por si hubo un error atraparlo y que no se caiga el servicio
        try {
            //Se le pasa el idUsuario y el editRequest para aplicar los cambios al usuario
            String response = userService.edit(idUsuario, editRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            //Cualquier error que haya pasado se manda con un 400 para que sepa que algo fallo
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    //Endpoint PATCH para hacer un toggle al status de un usuario dado su id
    @PatchMapping("/{idUsuario}/estatus")
    public ResponseEntity<?> changeStatusUser(@PathVariable int idUsuario, @RequestHeader("Authorization") String authHeader) {
        //Se usa try por si hubo un error atraparlo y que no se caiga el servicio
        try {
            //Se extrae el token del Header
            String token = authHeader.substring(7);
            //Se le pasa el idUsuario y el token para hacerle un toggle al user
            String response = userService.changeStatus(idUsuario, token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            //Cualquier error que haya pasado se manda con un 400 para que sepa que algo fallo
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
