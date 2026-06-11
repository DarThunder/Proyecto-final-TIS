/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.edu.uv.vehiculo.controller;

import java.util.List;
import mx.edu.uv.vehiculo.entity.Marca;
import mx.edu.uv.vehiculo.entity.VehiculoEntity;
import mx.edu.uv.vehiculo.entity.VehiculoFullEntity;
import mx.edu.uv.vehiculo.service.VehiculoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ACA SE HACEN TODAS LAS PETICIONES WEB
 * 
 * @author Gustavo López
 */
@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {
    
    private final VehiculoService vs;

    public VehiculoController(VehiculoService vs) {
        this.vs = vs;
    }
    
    @GetMapping("/marcas")
    public List<Marca> listarMarcas() {
        return vs.obtenerMarcas();
    }
    
    /**
     * ENDPOINT PARA VER LA LISTA DE VEHICULOS DE UN USUARIO POR SU ID
     * 
     * El usuario ingresa un ID dentro de la URL y devuelve la lista con los vehiculos registrados a él
     * 
     * @param idUsuario
     * @return 
     */
    @GetMapping("/usuario-vehiculos/{idUsuario}")
    public List<VehiculoFullEntity> listarVehiculosPorIDController(@PathVariable Integer idUsuario) {
        return vs.obtenerVehiculosPorIDService(idUsuario);
    }
    
    /**
     * ENDPOINT PARA REGISTRAR UN NUEVO VEHICULO
     * 
     * El usuario ingresa los valores del nuevo vehiculo dentro del body de postman y en base a eso se crea
     * un nuevo objeto vehiculo provisional
     * 
     * @param vehiculo
     * @return 
     */
    @PostMapping("/registrar-vehiculo")
    public ResponseEntity<?> registrarNuevoVehiculoController(@RequestBody VehiculoEntity vehiculo){
        // HACEMOS UN TRY CATCH YA QUE NO NOS DEJA MANDAR A LLAMAR A LA OTRA FUNCIÓN ASI NADAMÁS
        try {
            vs.registrarNuevoVehiculoService(vehiculo);
            // EL RESPONSE ENTITY SIRVE PARA CHECAR LA RESPUESTA QUE SE OBTENGA DE LA PETICIÓN
            return ResponseEntity.ok("Registro Exitoso de Nuevo Vehiculo: "+ vehiculo.getPlaca());
            
        } catch (IllegalArgumentException iae) {
            // ERROR EN CASO DE QUE FALTE ALGUN PARAMETRO EN EL BODY
            return ResponseEntity.badRequest().body(iae.getMessage());
            
        } catch (Exception e) {
            // ERROR POR SI LA BASE DE DATOS TIENE ALGUN PROBLEMA
            return ResponseEntity.internalServerError().body("Ocurrió un error al guardar el vehículo.");
        }
        
    }
}
