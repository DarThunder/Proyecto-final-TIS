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
import org.springframework.web.client.RestTemplate;

/**
 * ACA SE HACEN TODAS LAS FUNCIONES Y OPERACIÓNES CMOPLEJAS PARA EL
 * MICROSERVICIO DE VEHICULO.
 *
 * LAS VALIDACIONES DEL TOKEN DE LOGIN SE HACEN DENTRO DE LA CARPETA
 * /vehiculo/sec Y FUERON HECHAS POR Ernesto Caraza
 *
 * @author Gustavo López
 */
@Service
public class VehiculoService {

    private final VehiculoRepository vr;
    private final RestTemplate rt;

    public VehiculoService(VehiculoRepository vr, RestTemplate rt) {
        this.vr = vr;
        this.rt = rt;
    }

    /**
     * LLAMADA A ENDPOINT DE USER PARA VALIDAR QUE UN USUARIO EXISTE
     *
     * @param idUsuario
     * @return
     */
    public Boolean usuarioExiste(Integer idUsuario) {
        try {
            // CUANDO LLAMAMOS A LOS ENDPOINTS EL NOMBRE QUE SE USA ES api-"servicio".
            // NO "servicio"-service o localhost MALTIDA SEA
            String urlEndpoint = "http://api-user:8082/api/user/" + idUsuario + "/exist";
            Boolean existe = rt.getForObject(urlEndpoint, Boolean.class);
            return existe;

        } catch (Exception e) {
            throw new IllegalArgumentException("Error al verificar si existe el usuario");
        }
    }

    /**
     * ACA SE HACE LA VALIDACIÓN DEL ID DEL USUARIO Y EN BASE A ESO SI ES VALIDO
     * RETORNA LA LISTA DE VEHICULOS A SU NOMBRE
     *
     * @param idUsuario
     * @return
     */
    public List<VehiculoFullEntity> obtenerVehiculosPorIDService(Integer idUsuario) {
        // VALIDACIÓN DEL ID DEL USUARIO
        if (idUsuario == null || idUsuario <= 0) {
            // SI EL ID ES NULL,  MENOR O IGUAL QUE 0 MANDA UNA EXCEPCIÓN.
            throw new IllegalArgumentException("Ese ID no existe");
        }
        if (!usuarioExiste(idUsuario)) {
            throw new IllegalArgumentException("Ese usuario no existe o esta inactivo");
        }

        return vr.obtenerVehiculosPorIDRepository(idUsuario);
    }

    /**
     * VALIDACIÓN PREVIA AL REGISTRO DE UN NUEVO VEHICULO
     *
     * 1. Primero se checa si el ID del Usuario no sea null o que sea mayor a 0.
     * 2. Se valida que los demás valores no vengan vacios. 3. Se valida que no
     * hayan vehiculos con la misma placa 4. Se valida que el mismo usuario solo
     * tenga 4 vehiculos activos. 5. Si todo sale bien se crea el nuevo vehiculo
     * con un idVehiculo creado automaticamente y un estatus activo.
     *
     * @param vehiculo
     */
    public void registrarNuevoVehiculoService(VehiculoEntity vehiculo) {
        if (vehiculo.getIdUsuario() == null || vehiculo.getIdUsuario() <= 0) {
            throw new IllegalArgumentException("Ese ID no es valido");
        }
        if (!usuarioExiste(vehiculo.getIdUsuario())) {
            throw new IllegalArgumentException("Ese usuario no existe o esta inactivo");
        }
        if (vehiculo.getClaveVehiculo() == null || vehiculo.getClaveVehiculo().isEmpty()) {
            throw new IllegalArgumentException("Campo de clave vacio, ingresa un valor");
        }
        if (vehiculo.getClaveVehiculo().length() > 10) {
            throw new IllegalArgumentException("La clave de vehiculo solo debe tener 10 caracteres maximo");
        }
        if (vehiculo.getIdModelo() == null || vehiculo.getIdModelo() <= 0 || vehiculo.getIdModelo() > 39) {
            throw new IllegalArgumentException("Error con el campo de idModelo, ingrese un numero valido (1-39)");
        }
        if (vehiculo.getPlaca() == null || vehiculo.getPlaca().isEmpty()) {
            throw new IllegalArgumentException("Campo de placa vacio, ingrese un valor");
        }
        if (vehiculo.getPlaca().length() > 7) {
            throw new IllegalArgumentException("La placa del vehiculo debe tener 7 caracteres maximo");
        }
        if (vehiculo.getColor() == null || vehiculo.getColor().isEmpty()) {
            throw new IllegalArgumentException("Campo de color, ingresa un valor");
        }
        if (vehiculo.getAnio() == null || vehiculo.getAnio() < 1980 || vehiculo.getAnio() > 2026) {
            throw new IllegalArgumentException("Año invalido, ingresa un año valido (1980-2026)");
        }
        if (vehiculo.getDescripcion() == null || vehiculo.getDescripcion().isEmpty()) {
            throw new IllegalArgumentException("Descripción vacia, ingresa una descripción");
        }
        if (!validacionClave(vehiculo.getClaveVehiculo())) {
            throw new IllegalArgumentException("Esa clave ya esta registrada, utilice otra");
        }
        if (!validacionPlaca(vehiculo.getPlaca())) {
            throw new IllegalArgumentException("Esa placa ya esta registrada, utilice otra");
        }
        if (!usuarioTiene4Vehiculos(vehiculo.getIdUsuario())) {
            throw new IllegalArgumentException("Este usuario ya tiene 4 vehiculos activos, desactiva uno");
        }
        // DESPUES DE TODA ESA VALIDACIÓN SI NO HAY PROBLEMA YA HACE EL INSERT PASANDOLE EL OBJETO
        vr.registrarNuevoVehiculoRepository(vehiculo);
    }

    /**
     * VALIDACIÓN DEL NUMERO DE VEHICULOS ACTIVOS
     *
     * @param idUsuario
     * @return true (Si es menor que 4) / false (Si es igual a 4)
     */
    public boolean usuarioTiene4Vehiculos(Integer idUsuario) {
        Integer nVehiculos = vr.usuarioTiene4VehiculosRepository(idUsuario);
        if (nVehiculos >= 4) {
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
    public boolean validacionPlaca(String placa) {
        Integer nVehiculosPlaca = vr.validacionPlacaRepository(placa);
        if (nVehiculosPlaca > 0) {
            return false;
        }
        return true;
    }

    /**
     * VALIDACIÓN DE CLAVES REPETIDAS
     *
     * @param claveVehiculo
     * @return
     */
    public boolean validacionClave(String claveVehiculo) {
        Integer nVehiculosClave = vr.validacionClaveRepository(claveVehiculo);
        if (nVehiculosClave > 0) {
            return false;
        }
        return true;
    }

    /**
     * VALIDACIÓN DE PLACA VACIA PARA RETORNAR SU ID RESPECTIVO
     *
     * @param placa
     * @return
     */
    public String obtenerIdVehiculoDePlacaService(String placa) {
        if (placa != null && !placa.trim().isEmpty()) {
            if (placa.length() <= 7) {
                String idVehiculo = vr.obtenerIdVehiculoDePlacaRepository(placa);
                if (idVehiculo != null) {
                    return idVehiculo;
                }
                throw new IllegalArgumentException("No se encontró un ID asignado a esa placa");
            }
            throw new IllegalArgumentException("La placa debe contar con maximo 7 caracteres");
        }
        throw new IllegalArgumentException("Ingresa una placa valida");
    }

    /**
     * VALIDACIÓN DE ID Y DE OBJETO VEHICULO PARA RETORNARLO AL CONTROLLER
     *
     * @param idVehiculo
     * @return
     */
    public VehiculoFullEntity obtenerVehiculoDeIDService(Integer idVehiculo) {
        if (idVehiculo == null || idVehiculo <= 0) {
            throw new IllegalArgumentException("Ingresa un ID valido");
        }
        VehiculoFullEntity vehiculo = vr.obtenerVehiculoDeId2Repository(idVehiculo);
        if (vehiculo == null) {
            throw new IllegalArgumentException("Ese ID no cuenta con un Vehiculo asignado.");
        }
        return vehiculo;
    }

    /**
     * ACTUALIZACIÓN DE UN VEHICULO EXISTENTE
     *
     * Se crea un registro del vehiculo original, y se guardan los cambios
     * hechos dentro del JSON Si hay algun campo que esta vacio o que no cumple
     * con el formato establecido se omite Si un campo esta bien, se reemplaza
     * el campo original por el nuevo.
     *
     * @param idVehiculo
     * @param vehiculoNuevo
     */
    public void actualizarVehiculoService(Integer idVehiculo, VehiculoEntity vehiculoNuevo) {
        VehiculoEntity original = vr.obtenerVehiculoDeIdRepository(idVehiculo);
        if (original == null) {
            throw new IllegalArgumentException("No existe un vehiculo con ese ID");
        }
        if (vehiculoNuevo.getClaveVehiculo() != null && !vehiculoNuevo.getClaveVehiculo().isEmpty()) {
            if (vehiculoNuevo.getClaveVehiculo().length() > 10) {
                throw new IllegalArgumentException("La clave de vehiculo solo debe tener 10 caracteres maximo");
            }
            if (!vehiculoNuevo.getClaveVehiculo().equals(original.getClaveVehiculo())) {
                if (!validacionClave(vehiculoNuevo.getClaveVehiculo())) {
                    throw new IllegalArgumentException("Esa clave ya esta registrada, utilice otra");
                }
                original.setClaveVehiculo(vehiculoNuevo.getClaveVehiculo());
            }
        }
        if (vehiculoNuevo.getIdModelo() != null) {
            if(vehiculoNuevo.getIdModelo() <= 0 && vehiculoNuevo.getIdModelo() > 39){
                 throw new IllegalArgumentException("Error con el campo de idModelo, ingrese un numero valido (1-39)");
            }
             original.setIdModelo(vehiculoNuevo.getIdModelo());
        }
        if (vehiculoNuevo.getPlaca() != null && !vehiculoNuevo.getPlaca().isEmpty()) {
            if (vehiculoNuevo.getPlaca().length() > 7) {
                throw new IllegalArgumentException("La placa del vehiculo debe tener 7 caracteres maximo");
            }
            if (!vehiculoNuevo.getPlaca().equals(original.getPlaca())) {
                if (!validacionPlaca(vehiculoNuevo.getPlaca())) {
                    throw new IllegalArgumentException("Esa placa ya esta registrada, utilice otra");
                }
                original.setPlaca(vehiculoNuevo.getPlaca());
            }
        }
        if (vehiculoNuevo.getColor() != null && !vehiculoNuevo.getColor().isEmpty()) {
            original.setColor(vehiculoNuevo.getColor());
        }
        if (vehiculoNuevo.getAnio() != null) {
            if(vehiculoNuevo.getAnio() < 1980 && vehiculoNuevo.getAnio() > 2026){
                throw new IllegalArgumentException("Año invalido, ingresa un año valido (1980-2026)");
            }
            original.setAnio(vehiculoNuevo.getAnio());
        }
        if (vehiculoNuevo.getDescripcion() != null && !vehiculoNuevo.getDescripcion().isEmpty()) {
            original.setDescripcion(vehiculoNuevo.getDescripcion());
        }

        // CON TODOS LOS DATOS VALIDADOS, SE HACE LA CONSULTA
        vr.actualizarVehiculoRepository(original);
    }

    /**
     * ACTUALIZACIÓN DEL ESTADO DE UN VEHICULO
     *
     * Primero se checa si el idVehiculo que se manda corresponde a un vehiculo
     * que no sea null Luego se compara que el estado existente y el nuevo no
     * sean los mismos Y se actualiza
     *
     * @param idVehiculo
     * @param estatusNuevo
     */
    public void actualizarEstatusVehiculoService(Integer idVehiculo, Boolean estatusNuevo) {
        VehiculoEntity original = vr.obtenerVehiculoDeIdRepository(idVehiculo);
        if (original == null) {
            throw new IllegalArgumentException("No existe un vehiculo con ese ID");
        }

        if (estatusNuevo == original.getEstatus()) {
            throw new IllegalArgumentException("El vehiculo ya cuenta con ese estado");
        }

        if (estatusNuevo && !usuarioTiene4Vehiculos(original.getIdUsuario())) {
            throw new IllegalArgumentException("Este usuario ya tiene 4 vehiculos activos, desactiva otro para activar este");
        }

        vr.actualizarEstatusVehiculoRepository(idVehiculo, estatusNuevo);

        // CON TODOS LOS DATOS VALIDADOS, SE HACE LA CONSULTA
        vr.actualizarVehiculoRepository(original);
    }
}
