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

@Service
public class ParkingService {
    
    private final MovimientoRepository movimientoRepository;
    private final EspacioEstacionamientoRepository espacioEstacionamientoRepository;
    private final RestTemplate restTemplate;

    public ParkingService(MovimientoRepository movimientoRepository, EspacioEstacionamientoRepository espacioEstacionamientoRepository, RestTemplate restTemplate) {
        this.movimientoRepository = movimientoRepository;
        this.espacioEstacionamientoRepository = espacioEstacionamientoRepository;
        this.restTemplate = restTemplate;
    }

    public EntradaResponseDTO registrarEntrada(EntradaRequestDTO request) {
        try{
        String urlUsuario = "http://api-user:8082/api/user/" + request.getClaveUsuario() + "/status";
        Boolean isUserActive = restTemplate.getForObject(urlUsuario, Boolean.class);

        if(Boolean.FALSE.equals(isUserActive) || isUserActive == null) {
            return new EntradaResponseDTO(0, null, 0, null, "Error: El usuario no está activo o no existe.");
        }

        String urlVehiculo = "http://api-vehicle:8084/api/vehiculos/placa/" + request.getPlaca() + "/id-vehiculo";
        String idVehiculoStr = restTemplate.getForObject(urlVehiculo, String.class);

        if (idVehiculoStr == null || idVehiculoStr.isEmpty()){
            return new EntradaResponseDTO(0, null, 0, null, "Error: El vehículo no está activo o no existe.");
        }

        int idVehiculo = Integer.parseInt(idVehiculoStr);

        int vehiculosAdentro = movimientoRepository.contarVehiculosEstacionados(idVehiculo);

        if (vehiculosAdentro >=2) {
            return new EntradaResponseDTO(0, null, 0, null, "Error: El usuario ya alcanzó el límite de 2 vehículos dentro.");
        }

        Movimiento movimiento = new Movimiento();
        movimiento.setIdVehiculo(idVehiculo);
        movimiento.setIdEspacio(request.getIdEspacio());
        movimiento.setTiempoEntrada(request.getTiempoEntrada());
        movimiento.setTiempoCreacion(request.getTiempoEntrada());
        movimiento.setTarifaHora(request.getTarifaHora());

        movimientoRepository.registrarMovimiento(movimiento);

        espacioEstacionamientoRepository.ocuparEspacio(request.getIdEspacio());

        return new EntradaResponseDTO(
            0,
            request.getTiempoEntrada(),
            request.getIdEspacio(),
            request.getTarifaHora(),
            "Entrada registrada exitosamente"
        );


        } catch (Exception e) {
            return new EntradaResponseDTO(0, null, 0, null, "Error: de comunicación con servicios." + e.getMessage());
        }   
    }

    public SalidaResponseDTO registrarSalida(SalidaRequestDTO salidaRequest) {
    try{
        String urlUsuario = "http://api-user:8082/api/user/" + salidaRequest.getClaveUsuario() + "/status";
        Boolean isUserActive = restTemplate.getForObject(urlUsuario, Boolean.class);

        if(Boolean.FALSE.equals(isUserActive) || isUserActive == null) {
            return new SalidaResponseDTO(0, null, null, 0, null, null, 0, "Error: El usuario no está activo o no existe.");
        }

        String urlVehiculo = "http://api-vehicle:8084/api/vehiculos/placa/" + salidaRequest.getPlaca() + "/id-vehiculo";
        String idVehiculoStr = restTemplate.getForObject(urlVehiculo, String.class);

        if (idVehiculoStr == null || idVehiculoStr.isEmpty()){
            return new SalidaResponseDTO(0, null, null, 0, null, null, 0, "Error: El vehículo no está activo o no existe.");
        }

        Movimiento movimiento = movimientoRepository.obtenerMovimientoActivo(Integer.parseInt(idVehiculoStr));

        if (movimiento == null) {
            return new SalidaResponseDTO(0, null, null, 0, null, null, 0, "Error: No se encontró un movimiento activo para este vehículo.");
        }

        Duration duration = Duration.between(movimiento.getTiempoEntrada(), salidaRequest.getTiempoSalida());
        long minutosEstacionado = duration.toMinutes();

        double horasCalculadas = Math.ceil(minutosEstacionado / 60.0);
        int horasCobradas = (int) horasCalculadas;

        BigDecimal costoTotal = movimiento.getTarifaHora().multiply(new BigDecimal(horasCobradas));

        movimiento.setTiempoSalida(salidaRequest.getTiempoSalida());
        movimiento.setMinutosEstacionado((int) minutosEstacionado);
        movimiento.setHorasCobradas(horasCobradas);
        movimiento.setCostoTotal(costoTotal);
        movimiento.setTiempoActualizacion(salidaRequest.getTiempoSalida());

        movimientoRepository.actualizarMovimiento(movimiento);
        espacioEstacionamientoRepository.liberarEspacio(movimiento.getIdEspacio());

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
            return new SalidaResponseDTO(0, null, null, 0, null, null, 0, "Error: de comunicación con servicios." + e.getMessage());
        } 
    }

    public List<EspacioResponseDTO> obtenerEspaciosDisponibles() {
        List<EspacioResponseDTO> espaciosDisponibles = new ArrayList<>();
        List<EspacioEstacionamiento> espacios = espacioEstacionamientoRepository.encontrarEspaciosVacios();
        for (EspacioEstacionamiento espacio : espacios) {
            espaciosDisponibles.add(new EspacioResponseDTO(espacio.getIdEspacio(), espacio.getClaveEspacio(), espacio.getTipo()));
        }
        return espaciosDisponibles;
    }

}

