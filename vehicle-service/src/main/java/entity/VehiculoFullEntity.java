package entity;

public class VehiculoFullEntity {
    
    // DEFINICIONES
    private Integer idVehiculo;
    private Integer idUsuario;
    private String claveVehiculo;
    private Integer idMarca;
    private String marca;
    private Integer idModelo;
    private String modelo;
    private String placa;
    private String color;
    private Integer anio;
    private String descripcion;

    // CONSTRUCTOR VACIO
    public VehiculoFullEntity() {
    }

    // CONSTRUCTOR
    public VehiculoFullEntity(Integer idVehiculo, Integer idUsuario, String claveVehiculo, Integer idMarca, 
            String marca, Integer idModelo, String modelo, String placa, String color, Integer anio, String descripcion) {
        this.idVehiculo = idVehiculo;
        this.idUsuario = idUsuario;
        this.claveVehiculo = claveVehiculo;
        this.idMarca = idMarca;
        this.marca = marca;
        this.idModelo = idModelo;
        this.modelo = modelo;
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

    public Integer getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Integer idMarca) {
        this.idMarca = idMarca;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(Integer idModelo) {
        this.idModelo = idModelo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
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
