package mx.edu.uv.parking_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Duration;
import mx.edu.uv.parking_service.repository.MovimientoRepository;
import mx.edu.uv.parking_service.repository.EspacioEstacionamientoRepository;
import mx.edu.uv.parking_service.dto.EntradaRequestDTO;
import mx.edu.uv.parking_service.dto.EntradaResponseDTO;
import mx.edu.uv.parking_service.dto.EspacioResponseDTO;
import mx.edu.uv.parking_service.dto.SalidaRequestDTO;
import mx.edu.uv.parking_service.dto.SalidaResponseDTO;
import mx.edu.uv.parking_service.entity.EspacioEstacionamiento;
import mx.edu.uv.parking_service.entity.Movimiento;

/**
 * Servicio encargado de la lógica de negocio del estacionamiento.
 */
@Service
public class ParkingService {
    
    private final MovimientoRepository movimientoRepository;
    private final EspacioEstacionamientoRepository espacioEstacionamientoRepository;
    private final RestTemplate restTemplate;

    /**
     * Constructor para la inyección de dependencias.
     */
    public ParkingService(MovimientoRepository movimientoRepository, EspacioEstacionamientoRepository espacioEstacionamientoRepository, RestTemplate restTemplate) {
        this.movimientoRepository = movimientoRepository;
        this.espacioEstacionamientoRepository = espacioEstacionamientoRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * Registra la entrada de un vehículo al estacionamiento validando previamente
     * la existencia del usuario, del vehículo y la regla de negocio de cupo máximo por usuario.
     */
    public EntradaResponseDTO registrarEntrada(EntradaRequestDTO request) {
        try {
            // 1.- Validar usuario
            String urlUsuario = "http://api-user:8082/api/user/" + request.getClaveUsuario() + "/status";
            Boolean isUserActive = restTemplate.getForObject(urlUsuario, Boolean.class);

            if(Boolean.FALSE.equals(isUserActive) || isUserActive == null) {
                return new EntradaResponseDTO(0, null, 0, null, "Error: El usuario no está activo o no existe.");
            }

            // 2.- Obtiene el ID del vehículo usando la placa en vehicle-service
            String urlVehiculo = "http://api-vehicle:8084/api/vehiculos/placa/" + request.getPlaca() + "/id-vehiculo";
            String idVehiculoStr = restTemplate.getForObject(urlVehiculo, String.class);

            if (idVehiculoStr == null || idVehiculoStr.isEmpty()){
                return new EntradaResponseDTO(0, null, 0, null, "Error: El vehículo no está activo o no existe.");
            }

            int idVehiculo = Integer.parseInt(idVehiculoStr);

            // 3.- Verifica que el vehículo en específico no esté adentro (Evita clonación de auto)
            int esteAutoAdentro = movimientoRepository.contarVehiculosEstacionados(idVehiculo);
            if (esteAutoAdentro >= 1) {
                return new EntradaResponseDTO(0, null, 0, null, "Error: Este vehículo ya se encuentra dentro del estacionamiento.");
            }

            // 4.- Validar máximo 2 vehículos por usuario"
            //Obtiene a qué usuario pertenece el vehículo
            String urlDetalleVehiculo = "http://api-vehicle:8084/api/vehiculos/obtener-vehiculo/" + idVehiculo;
            java.util.Map<String, Object> vehiculoInfo = restTemplate.getForObject(urlDetalleVehiculo, java.util.Map.class);
            Integer idUsuario = (Integer) vehiculoInfo.get("idUsuario");

            // B)Obtiene todos los vehículos de ese usuario
            String urlVehiculosUsuario = "http://api-vehicle:8084/api/vehiculos/usuario-vehiculos/" + idUsuario;
            List<java.util.Map<String, Object>> listaVehiculos = restTemplate.getForObject(urlVehiculosUsuario, List.class);

            List<Integer> idsVehiculosUsuario = new ArrayList<>();
            if (listaVehiculos != null) {
                for (java.util.Map<String, Object> v : listaVehiculos) {
                    idsVehiculosUsuario.add(((Number) v.get("idVehiculo")).intValue());
                }
            }

            //Contamos cuántos de esos autos en total están adentro
            if (!idsVehiculosUsuario.isEmpty()) {
                int vehiculosAdentroUsuario = movimientoRepository.contarVehiculosEstacionadosPorUsuario(idsVehiculosUsuario);
                if (vehiculosAdentroUsuario >= 2) {
                    return new EntradaResponseDTO(0, null, 0, null, "Error: El usuario ya alcanzó el límite de 2 vehículos dentro.");
                }
            }

            // 5.- Crea y guarda el nuevo registro de movimiento
            Movimiento movimiento = new Movimiento();
            movimiento.setIdVehiculo(idVehiculo);
            movimiento.setIdEspacio(request.getIdEspacio());
            movimiento.setTiempoEntrada(request.getTiempoEntrada());
            movimiento.setTiempoCreacion(request.getTiempoEntrada());
            movimiento.setTarifaHora(request.getTarifaHora());

            movimientoRepository.registrarMovimiento(movimiento);

            // 6.- Cambia el estatus del cajón de estacionamiento a "Ocupado"
            espacioEstacionamientoRepository.ocuparEspacio(request.getIdEspacio());

            // 7.- Respuesta Exitosa
            return new EntradaResponseDTO(
                0, // idMovimiento (podría mapearse si la BD retorna el ID generado)
                request.getTiempoEntrada(),
                request.getIdEspacio(),
                request.getTarifaHora(),
                "Entrada registrada exitosamente"
            );

        } catch (Exception e) {
            // Manejo de errores de red (si algún microservicio está caído) o de base de datos
            return new EntradaResponseDTO(0, null, 0, null, "Error: de comunicación con servicios. " + e.getMessage());
        }   
    }

    /**
     * Registra la salida de un vehículo calculando el tiempo que pasó dentro
     * y el costo total a cobrar según la tarifa establecida.
     */
    public SalidaResponseDTO registrarSalida(SalidaRequestDTO salidaRequest) {
        try {
            // 1.- Validar usuario
            String urlUsuario = "http://api-user:8082/api/user/" + salidaRequest.getClaveUsuario() + "/status";
            Boolean isUserActive = restTemplate.getForObject(urlUsuario, Boolean.class);

            if(Boolean.FALSE.equals(isUserActive) || isUserActive == null) {
                return new SalidaResponseDTO(0, null, null, 0, null, null, 0, "Error: El usuario no está activo o no existe.");
            }

            // 2.- Obtiene el ID del vehículo usando la placa en vehicle-service
            String urlVehiculo = "http://api-vehicle:8084/api/vehiculos/placa/" + salidaRequest.getPlaca() + "/id-vehiculo";
            String idVehiculoStr = restTemplate.getForObject(urlVehiculo, String.class);

            if (idVehiculoStr == null || idVehiculoStr.isEmpty()){
                return new SalidaResponseDTO(0, null, null, 0, null, null, 0, "Error: El vehículo no está activo o no existe.");
            }

            // 3.- Obtiene el movimiento de entrada que todavía no tiene salida
            Movimiento movimiento = movimientoRepository.obtenerMovimientoActivo(Integer.parseInt(idVehiculoStr));

            if (movimiento == null) {
                return new SalidaResponseDTO(0, null, null, 0, null, null, 0, "Error: No se encontró un movimiento activo para este vehículo.");
            }

            // 4.- Cálculo de tiempo y costo
            Duration duration = Duration.between(movimiento.getTiempoEntrada(), salidaRequest.getTiempoSalida());
            long minutosEstacionado = duration.toMinutes();

            // Redondeo hacia arriba
            double horasCalculadas = Math.ceil(minutosEstacionado / 60.0);
            int horasCobradas = (int) horasCalculadas;

            // Multiplicación de horas por tarifa
            BigDecimal costoTotal = movimiento.getTarifaHora().multiply(new BigDecimal(horasCobradas));

            // 5.- Actualización del objeto Movimiento con los resultados del cálculo
            movimiento.setTiempoSalida(salidaRequest.getTiempoSalida());
            movimiento.setMinutosEstacionado((int) minutosEstacionado);
            movimiento.setHorasCobradas(horasCobradas);
            movimiento.setCostoTotal(costoTotal);
            movimiento.setTiempoActualizacion(salidaRequest.getTiempoSalida());

            // 6.- Guarda la salida y libera el cajón de estacionamiento
            movimientoRepository.actualizarMovimiento(movimiento);
            espacioEstacionamientoRepository.liberarEspacio(movimiento.getIdEspacio());

            // 7.- Respuesta Exitosa con el recibo de cobro
            return new SalidaResponseDTO(
                movimiento.getIdMovimiento(),
                movimiento.getTiempoEntrada(),
                movimiento.getTiempoSalida(),
                movimiento.getIdEspacio(),
                movimiento.getTarifaHora(),
                movimiento.getCostoTotal(),
                movimiento.getHorasCobradas(),
                "Salida registrada exitosamente"
            );

        } catch (Exception e) {
            return new SalidaResponseDTO(0, null, null, 0, null, null, 0, "Error: de comunicación con servicios. " + e.getMessage());
        } 
    }

    /**
     * Consulta la base de datos para pedir una lista de todos los espacios
     * que se encuentran desocupados.
     */
    public List<EspacioResponseDTO> obtenerEspaciosDisponibles() {
        List<EspacioResponseDTO> espaciosDisponibles = new ArrayList<>();
        
        // Solicita a la base de datos únicamente los espacios vacíos
        List<EspacioEstacionamiento> espacios = espacioEstacionamientoRepository.encontrarEspaciosVacios();
        
        // Mapea la Entidad de Base de Datos al DTO para no exponer información sensible
        for (EspacioEstacionamiento espacio : espacios) {
            espaciosDisponibles.add(new EspacioResponseDTO(
                espacio.getIdEspacio(), 
                espacio.getClaveEspacio(), 
                espacio.getTipo()
            ));
        }
        
        return espaciosDisponibles;
    }
}