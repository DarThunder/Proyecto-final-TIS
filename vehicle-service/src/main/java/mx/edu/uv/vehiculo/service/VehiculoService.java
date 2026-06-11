/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.edu.uv.vehiculo.service;

import java.util.List;
import mx.edu.uv.vehiculo.entity.Marca;
import mx.edu.uv.vehiculo.entity.VehiculoEntity;
import mx.edu.uv.vehiculo.entity.VehiculoFullEntity;
import mx.edu.uv.vehiculo.repository.VehiculoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * ACA SE HACEN TODAS LAS FUNCIONES Y OPERACIÓNES CMOPLEJAS PARA EL 
 * MICROSERVICIO DE VEHICULO.
 * 
 * LAS VALIDACIONES DEL TOKEN SE HACEN DENTRO DE LA CARPETA /vehiculo/sec
 * 
 * @author Gustavo López
 */
@Service
public class VehiculoService {
    
    private final VehiculoRepository vr;

    public VehiculoService(VehiculoRepository vr) {
        this.vr = vr;
    }
    
    /**
     * PRUEBA RETORNO DE TODAS LAS MARCAS
     * 
     * NO SE HACE NINGUNA VALICADIÓN
     * 
     * @return 
     */
    public List<Marca> obtenerMarcas() {
        return vr.obtenerMarcasRepository();
    }
    
    /**
     * ACA SE HACE LA VALIDACIÓN DEL ID DEL USUARIO Y EN BASE A ESO
     * SI ES VALIDO RETORNA LA LISTA DE VEHICULOS A SU NOMBRE
     * 
     * @param idUsuario
     * @return 
     */
    public List<VehiculoFullEntity> obtenerVehiculosPorIDService(Integer idUsuario){
        // VALIDACIÓN DEL ID DEL USUARIO
        if(idUsuario != null && idUsuario > 0){
            return vr.obtenerVehiculosPorIDRepository(idUsuario);
        }
         // SI EL ID ES NULL,  MENOR O IGUAL QUE 0 MANDA UNA EXCEPCIÓN.
        throw new IllegalArgumentException("Ese ID no existe");
    }
    
     /**
      * VALIDACIÓN PREVIA AL REGISTRO DE UN NUEVO VEHICULO
      * 
      * 1. Primero se checa si el ID del Usuario no sea null o que sea mayor a 0.
      * 2. Se valida que los demás valores no vengan vacios.
      * 3. Se valida que no hayan vehiculos con la misma placa
      * 4. Se valida que el mismo usuario solo tenga 4 vehiculos activos.
      * 5. Si todo sale bien se crea el nuevo vehiculo con un idVehiculo creado automaticamente y un estatus activo.
      * 
      * @param vehiculo 
      */
     public void registrarNuevoVehiculoService (VehiculoEntity vehiculo){
            if(vehiculo.getIdUsuario() != null && vehiculo.getIdUsuario() > 0){
               if(vehiculo.getClaveVehiculo() != null){
                   if(vehiculo.getPlaca() != null){
                        if(validacionPlaca(vehiculo.getPlaca())){
                       
                        }
                        throw new IllegalArgumentException("Esa placa ya esta registrada, utilice otra"); 
                    }
                    throw new IllegalArgumentException("Campo de placa vacio, ingrese un valor"); 
               }
               throw new IllegalArgumentException("Campo de clave vacio, ingresa un valor");
            } 
            throw new IllegalArgumentException("Ese ID no existe");
     }
     
     /**
      * VALIDACIÓN DEL NUMERO DE VEHICULOS ACTIVOS
      * 
      * @param idUsuario
      * @return true (Si es menor que 4) / false (Si es igual a 4)
      */
     public boolean usuarioTiene4Vehiculos(Integer idUsuario){
         Integer nVehiculos = vr.usuarioTiene4VehiculosRepository(idUsuario);
         if(nVehiculos >= 4){
             return false;
         }
         return true;
     }
     
     /**
      * VALIDACIÓN DE PLACAS REPETIDAS
      * 
      * @param placa
      * @return true (Si es 0) / false (Si es mayor 0)
      */
     public boolean validacionPlaca(String placa){
         Integer nVehiculosPlaca = vr.validacionPlacaRepository(placa);
         if(nVehiculosPlaca > 0){
             return false;
         }
         return true;
     }
     
    //EDITAR VEHICULO
    
    //CAMBIAR ESTATUS DE VEHICULO
    
}
