package mx.edu.uv.parking_service.dto;

import java.time.LocalDateTime;

public class SalidaRequestDTO {

    private String claveUsuario;
    private String placa;
    private LocalDateTime tiempoSalida;

    public SalidaRequestDTO() {
    }

    public SalidaRequestDTO(String claveUsuario, String placa, LocalDateTime tiempoSalida) {
        this.claveUsuario = claveUsuario;
        this.placa = placa;
        this.tiempoSalida = tiempoSalida;
    }

    public String getClaveUsuario() {
        return claveUsuario;
    }

    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public LocalDateTime getTiempoSalida() {
        return tiempoSalida;
    }

    public void setTiempoSalida(LocalDateTime tiempoSalida) {
        this.tiempoSalida = tiempoSalida;
    }
}