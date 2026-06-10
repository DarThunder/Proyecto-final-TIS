package entity;

public class ModeloEntity {
    
    // DEFINICIONES
    private Integer idModelo;
    private Integer idMarca;
    private String modelo;
    private Boolean estatus;

    // CONSTRUCTOR VACIO
    public ModeloEntity() {
    }

    // CONSTRUCTOR
    public ModeloEntity(Integer idModelo, Integer idMarca, String modelo, Boolean estatus) {
        this.idModelo = idModelo;
        this.idMarca = idMarca;
        this.modelo = modelo;
        this.estatus = estatus;
    }

    // GETTERS Y SETTERS
    public Integer getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(Integer idModelo) {
        this.idModelo = idModelo;
    }

    public Integer getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Integer idMarca) {
        this.idMarca = idMarca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }
}
