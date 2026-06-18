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
import org.springframework.web.bind.annotation.PutMapping;
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

    /**
     * ENDPOINT DE PRUEBA PARA RETORNAR TODAS LAS MARCAS DE VEHICULOS
     *
     * @return
     */
    @GetMapping("/marcas")
    public List<Marca> listarMarcas() {
        return vs.obtenerMarcas();
    }

    /**
     * ENDPOINT PARA VER LA LISTA DE VEHICULOS DE UN USUARIO POR SU ID
     *
     * El usuario ingresa un ID dentro de la URL y devuelve la lista con los
     * vehiculos registrados a él
     *
     * @param idUsuario
     * @return
     */
    @GetMapping("/usuario-vehiculos/{idUsuario}")
    public ResponseEntity<?> listarVehiculosPorIDController(@PathVariable Integer idUsuario) {
        try {
            List<VehiculoFullEntity> vehiculos = vs.obtenerVehiculosPorIDService(idUsuario);
            return ResponseEntity.ok(vehiculos);

        } catch (IllegalArgumentException iae) {
            // CAPTURA EL ERROR QUE SE MANDA DESDE EL SERVICE
            return ResponseEntity.badRequest().body(iae.getMessage());

        } catch (Exception e) {
            // ERROR POR SI LA BASE DE DATOS TIENE ALGUN PROBLEMA
            return ResponseEntity.internalServerError().body("Ocurrió un error al listar los vehiculos.");
        }
    }

    /**
     * ENDPOINT PARA REGISTRAR UN NUEVO VEHICULO
     *
     * El usuario ingresa los valores del nuevo vehiculo dentro del body de
     * postman y en base a eso se crea un nuevo objeto vehiculo provisional
     *
     * @param vehiculo
     * @return
     */
    @PostMapping("/registrar-vehiculo")
    public ResponseEntity<?> registrarNuevoVehiculoController(@RequestBody VehiculoEntity vehiculo) {
        // HACEMOS UN TRY CATCH YA QUE NO NOS DEJA MANDAR A LLAMAR A LA OTRA FUNCIÓN ASI NADAMÁS
        try {
            vs.registrarNuevoVehiculoService(vehiculo);
            // EL RESPONSE ENTITY SIRVE PARA CHECAR LA RESPUESTA QUE SE OBTENGA DE LA PETICIÓN
            return ResponseEntity.ok("Registro Exitoso de Nuevo Vehiculo: " + vehiculo.getPlaca());

        } catch (IllegalArgumentException iae) {
            // ERROR EN CASO DE QUE FALTE ALGUN PARAMETRO EN EL BODY
            return ResponseEntity.badRequest().body(iae.getMessage());

        } catch (Exception e) {
            // ERROR POR SI LA BASE DE DATOS TIENE ALGUN PROBLEMA
            return ResponseEntity.internalServerError().body("Ocurrió un error al guardar el vehículo.");
        }
    }

    /**
     * ENDPOINT PARA REGRESAR EL IDVEHICULO PASANDO LA PLACA
     *
     * El usuario mete la placa en la url y el endpoint le regresa el idVehiculo
     * correspondiente.
     *
     * @param placa
     * @return
     */
    @GetMapping("/placa/{placa}/id-vehiculo")
    public ResponseEntity<?> obtenerIdVehiculoDePlacaController(@PathVariable String placa) {
        try {
            String idVehiculo = vs.obtenerIdVehiculoDePlacaService(placa);
            return ResponseEntity.ok(idVehiculo);

        } catch (IllegalArgumentException iae){
            return ResponseEntity.badRequest().body(iae.getMessage());
            
        } catch (Exception e) {
            // ERROR EN CASO DE QUE HAYA PASADO ALGO AL BUSCAR LA PLACA
            return ResponseEntity.internalServerError().body("Error al encontrar la placa");
        }
    }

    /**
     * ENDPOINT PARA REGRESAR VEHICULO CON EL ID
     *
     * @param idVehiculo
     * @return
     */
    @GetMapping("/obtener-vehiculo/{idVehiculo}")
    public ResponseEntity<?> obtenerVehiculoDeID(@PathVariable Integer idVehiculo) {
        try {
            VehiculoFullEntity vehiculo = vs.obtenerVehiculoDeIDService(idVehiculo);
            return ResponseEntity.ok(vehiculo);
            
        } catch (IllegalArgumentException iae){
            return ResponseEntity.badRequest().body(iae.getMessage());
            
        } catch (Exception e) {
            // ERROR EN CASO DE QUE HAYA PASADO ALGO AL BUSCAR EL VEHICULO
            return ResponseEntity.internalServerError().body("Error al encontrar el vehiculo");
        }
    }

    /**
     * ENDPOINT PARA ACTUALIZAR UN VEHICULO EXISTENTE PASANDOLE SU ID
     *
     * El usuario mete el ID en la URL y a su vez mete los datos que quiere
     * actualizar dentro del Body
     *
     * @param idVehiculo
     * @param vehiculoNuevo
     * @return
     */
    @PutMapping("/actualizar-vehiculo/{idVehiculo}")
    public ResponseEntity<?> actualizarVehiculoController(
            @PathVariable Integer idVehiculo, @RequestBody VehiculoEntity vehiculoNuevo) {
        // HACEMOS UN TRY CATCH YA QUE NO NOS DEJA MANDAR A LLAMAR A LA OTRA FUNCIÓN ASI NADAMÁS
        try {
            vs.actualizarVehiculoService(idVehiculo, vehiculoNuevo);
            // EL RESPONSE ENTITY SIRVE PARA CHECAR LA RESPUESTA QUE SE OBTENGA DE LA PETICIÓN
            return ResponseEntity.ok("Actualización exitosa de vehiculo con el ID: " + idVehiculo);

        } catch (IllegalArgumentException iae) {
            // ERROR EN CASO DE QUE FALTE ALGUN PARAMETRO EN EL BODY
            return ResponseEntity.badRequest().body(iae.getMessage());

        } catch (Exception e) {
            // ERROR POR SI LA BASE DE DATOS TIENE ALGUN PROBLEMA
            return ResponseEntity.internalServerError().body("Ocurrió un error al actualizar el vehículo.");
        }
    }

    /**
     * ENDPOINT PARA ACTUALIZAR EL ESTADO DE UN VEHICULO EXISTENTE
     *
     * En vez de mandarle todo el objeto de vehiculo, solo se manda el ID y el
     * Estado.
     *
     * @param idVehiculo
     * @param estatus
     * @return
     */
    @PutMapping("/actualizar-vehiculo/estatus/{idVehiculo}")
    public ResponseEntity<?> actualizarEstatusVehiculoController(
            @PathVariable Integer idVehiculo, @RequestBody Boolean estatus) {
        try {
            vs.actualizarEstatusVehiculoService(idVehiculo, estatus);
            // EL RESPONSE ENTITY SIRVE PARA CHECAR LA RESPUESTA QUE SE OBTENGA DE LA PETICIÓN
            return ResponseEntity.ok("Actualización exitosa del estatus del vehiculo: " + idVehiculo + " a: " + estatus);

        } catch (IllegalArgumentException iae) {
            // ERROR EN CASO DE QUE FALTE ALGUN PARAMETRO EN EL BODY
            return ResponseEntity.badRequest().body(iae.getMessage());

        } catch (Exception e) {
            // ERROR POR SI LA BASE DE DATOS TIENE ALGUN PROBLEMA
            return ResponseEntity.internalServerError().body("Ocurrió un error al actualizar el estatus del vehículo.");
        }
    }
}
