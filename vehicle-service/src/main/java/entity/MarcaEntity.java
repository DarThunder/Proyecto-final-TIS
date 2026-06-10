package entity;

public class MarcaEntity {
    
    // DEFINICIONES
    private Integer idMarca;
    private String marca;
    private Boolean estatus;
    
    // CONSTRUCTOR VACIO
    public MarcaEntity() {
    }

    // CONSTRUCTOR
    public MarcaEntity(Integer idMarca, String marca, Boolean estatus) {
        this.idMarca = idMarca;
        this.marca = marca;
        this.estatus = estatus;
    }
    
    // GETTERS Y SETTERS
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

    public Boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }
}
