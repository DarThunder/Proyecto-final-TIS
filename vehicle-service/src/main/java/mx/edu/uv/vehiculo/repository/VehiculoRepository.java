package mx.edu.uv.vehiculo.repository;

import java.util.List;
import mx.edu.uv.vehiculo.entity.Marca;
import mx.edu.uv.vehiculo.entity.VehiculoEntity;
import mx.edu.uv.vehiculo.entity.VehiculoFullEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * ACA SE HACEN TODAS LAS CONSULTAS A LA BD DE MYSQL
 * 
 * @author Gustavo López
 */
@Mapper
public interface VehiculoRepository {
    
    /**
     * PRUEBA PARA VER QUE POSTMAN HICIERA LAS PETICIONES
     * REGRESA TODA LA LISTA DE MARCAS DE VEHICULOS
     * 
     * @return 
     */
    @Select("SELECT * FROM marca")
    List<Marca> obtenerMarcasRepository();
    
    /**
     * HACE LA CONSULTA A LA BASE UTILIZANDO EL ID DEL USUARIO Y RETORNA LOS 
     * VEHICULOS RESPECTIVOS A ESTE.
     * 
     * @param idUsuario
     * @return 
     */
    @Select("SELECT * FROM vehiculofullinfo WHERE idUsuario = #{idUsuario}")
    List<VehiculoFullEntity> obtenerVehiculosPorIDRepository(Integer idUsuario);
    
    /**
     * INSERTA EL NUEVO VEHICULO DENTRO DE LA BD
     * 
     * No se pone idVehiculo ya que en la BD se tiene como AUTO_INCREMENT
     * Tampoco se pone el estatus ya que igual se establece que su valor default es "1"
     * 
     * @param vehiculo 
     */
    @Insert("INSERT INTO vehiculo (idUsuario, claveVehiculo, idModelo, placa, color, anio, descripcion) "
            + "VALUES (#{vehiculo.getIdUsuario}, #{vehiculo.getClaveVehiculo}, #{vehiculo.getIdModelo}, #{vehiculo.getPlaca}, "
            + "#{vehiculo.getColor}, #{vehiculo.getAnio}, #{vehiculo.getDescripcion})")
    void registrarNuevoVehiculoRepository(VehiculoEntity vehiculo);
    
    /**
     * RETORNA EL NUMERO DE VEHICULOS ACTIVOS QUE TIENE UN USUARIO
     * 
     * Este nuemero se retorna en el Service y se hace la validación para ver si es mayor o menor que 4
     * 
     * @param idUsuario
     * @return # De vehiculos activos correspondientes a ese id de Usuario
     */
    @Select("SELECT COUNT(*) FROM vehiculo WHERE estatus = 1 AND idUsuario = #{idUsuario}")
    Integer usuarioTiene4VehiculosRepository(Integer idUsuario);
    
    /**
     * RETORNA EL NUMERO DE VEHICULOS CON UNA PLACA
     * 
     * @param placa
     * @return # De vehiculos con esa misma placa
     */
    @Select("SELECT COUNT(*) FROM vehiculo WHERE placa = #{placa}")
    Integer validacionPlacaRepository(String placa);
}
