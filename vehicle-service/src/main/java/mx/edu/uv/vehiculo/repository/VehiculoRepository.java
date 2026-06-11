package mx.edu.uv.vehiculo.repository;

import java.util.List;
import mx.edu.uv.vehiculo.entity.Marca;
import mx.edu.uv.vehiculo.entity.VehiculoEntity;
import mx.edu.uv.vehiculo.entity.VehiculoFullEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
     * SE OBTIENE TODO UN VEHICULO TAN SOLO DE SU ID
     * 
     * @param idVehiculo
     */
    @Select("SELECT * FROM vehiculo WHERE idVehiculo = #{idVehiculo}")
    VehiculoEntity obtenerVehiculoDeIdRepository(Integer idVehiculo);
    
    /**
     * LO MISMO QUE LA DE ARRIBA PERO AHORA NOS REGRESA EL VEHICULO FULL
     * EN VEZ DEL NORMALITO
     * 
     * @param idVehiculo
     */
    @Select("SELECT * FROM vehiculofullinfo WHERE idVehiculo = #{idVehiculo}")
    VehiculoFullEntity obtenerVehiculoDeId2Repository(Integer idVehiculo);
    
    /**
     * REGRESA EL ID UN VEHICULO PASANDO SU PLACA
     * 
     * @param placa
     */
    @Select("SELECT idVehiculo FROM vehiculofullinfo WHERE placa = #{placa}")
    String obtenerIdVehiculoDePlacaRepository(String placa);
    
    /**
     * HACE LA CONSULTA A LA BASE UTILIZANDO EL ID DEL USUARIO Y RETORNA LOS 
     * VEHICULOS RESPECTIVOS A ESTE.
     * 
     * @param idUsuario
     */
    @Select("SELECT * FROM vehiculofullinfo WHERE idUsuario = #{idUsuario}")
    List<VehiculoFullEntity> obtenerVehiculosPorIDRepository(Integer idUsuario);
    
    /**
     * INSERTA EL NUEVO VEHICULO DENTRO DE LA BD
     * 
     * No se pone idVehiculo ya que en la BD se tiene como AUTO_INCREMENT
     * Tampoco se pone el estatus ya que igual se establece que su valor default es "1"
     * 
     * Al parecer MyBatis puede checar los atributos de un objeto por si solo, evitando poner
     * vehiculo.get...() por cada valor ingresado en el query
     * 
     * @param vehiculo 
     */
    @Insert("INSERT INTO vehiculo (idUsuario, claveVehiculo, idModelo, placa, color, anio, descripcion) "
            + "VALUES (#{idUsuario}, #{claveVehiculo}, #{idModelo}, #{placa}, "
            + "#{color}, #{anio}, #{descripcion})")
    void registrarNuevoVehiculoRepository(VehiculoEntity vehiculo);
    
    /**
     * RETORNA EL NUMERO DE VEHICULOS ACTIVOS QUE TIENE UN USUARIO
     * 
     * Este numero se retorna en el Service y se hace la validación para ver si es mayor o menor que 4
     * 
     * @param idUsuario
     */
    @Select("SELECT COUNT(*) FROM vehiculofullinfo WHERE estatus = 1 AND idUsuario = #{idUsuario}")
    Integer usuarioTiene4VehiculosRepository(Integer idUsuario);
    
    /**
     * RETORNA EL NUMERO DE VEHICULOS CON UNA PLACA
     * 
     * @param placa
     */
    @Select("SELECT COUNT(*) FROM vehiculofullinfo WHERE placa = #{placa}")
    Integer validacionPlacaRepository(String placa);
    
    /**
     * ACTUALIZA LOS VALORES DE UN VEHICULO EXISTENTE
     * 
     * @param vehiculo 
     */
    @Update("UPDATE vehiculo SET claveVehiculo = #{claveVehiculo}, idModelo = #{idModelo}, "
            + "placa = #{placa}, color = #{color}, anio = #{anio}, descripcion = #{descripcion} WHERE idVehiculo = #{idVehiculo}")
    void actualizarVehiculoRepository(VehiculoEntity vehiculo);
    
    /**
     * ACTUALIZA EL ESTADO DE UN VEHICULO EXISTENTE
     * 
     * Se usan @Param para evitar confusiones con los nombres de las variables en el MyBatis
     * 
     * @param idVehiculo
     * @param estatus 
     */
    @Update("UPDATE vehiculo SET estatus = #{estatus} WHERE idVehiculo = #{idVehiculo}")
    void actualizarEstatusVehiculoRepository(@Param("idVehiculo") Integer idVehiculo, @Param("estatus") Boolean estatus);
}
