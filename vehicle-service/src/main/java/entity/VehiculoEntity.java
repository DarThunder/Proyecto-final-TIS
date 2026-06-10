package entity;

public class VehiculoEntity {
    
    // DEFINICIONES
    private Integer idVehiculo;
    private Integer idUsuario;
    private String claveVehiculo;
    private Integer idModelo;
    private String placa;
    private String color;
    private Integer anio;
    private String descripcion;

    // CONSTRUCTOR VACIO
    public VehiculoEntity() {
    }

    // CONSTRUCTOR
    public VehiculoEntity(Integer idVehiculo, Integer idUsuario, String claveVehiculo, Integer idModelo, String placa, 
            String color, Integer anio, String descripcion) {
        this.idVehiculo = idVehiculo;
        this.idUsuario = idUsuario;
        this.claveVehiculo = claveVehiculo;
        this.idModelo = idModelo;
        this.placa = placa;
        this.color = color;
        this.anio = anio;
        this.descripcion = descripcion;
    }

    // GETTERS Y SETTERS
    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getClaveVehiculo() {
        return claveVehiculo;
    }

    public void setClaveVehiculo(String claveVehiculo) {
        this.claveVehiculo = claveVehiculo;
    }

    public Integer getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(Integer idModelo) {
        this.idModelo = idModelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    } 
}
