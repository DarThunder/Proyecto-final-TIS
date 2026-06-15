package mx.edu.uv.parking_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.edu.uv.parking_service.dto.EntradaRequestDTO;
import mx.edu.uv.parking_service.dto.EntradaResponseDTO;
import mx.edu.uv.parking_service.dto.EspacioResponseDTO;
import mx.edu.uv.parking_service.dto.SalidaRequestDTO;
import mx.edu.uv.parking_service.dto.SalidaResponseDTO;
import mx.edu.uv.parking_service.service.ParkingService;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {
    
    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping("/entrada")
    public ResponseEntity<EntradaResponseDTO> registrarEntrada(@RequestBody EntradaRequestDTO request) {
        EntradaResponseDTO response = parkingService.registrarEntrada(request);

        if (response.getMensaje() != null && response.getMensaje().startsWith("Error")) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/salida")
    public ResponseEntity<SalidaResponseDTO> registrarSalida(@RequestBody SalidaRequestDTO salidaRequest) {
            SalidaResponseDTO response = parkingService.registrarSalida(salidaRequest);

            if (response.getMensaje() != null && response.getMensaje().startsWith("Error")) {
                return ResponseEntity.badRequest().body(response);
            }
            return ResponseEntity.ok(response);
    }

    @GetMapping("/espacios")
    public ResponseEntity<List<EspacioResponseDTO>> obtenerEspaciosDisponibles() {

            return ResponseEntity.ok(parkingService.obtenerEspaciosDisponibles());
    }
}
