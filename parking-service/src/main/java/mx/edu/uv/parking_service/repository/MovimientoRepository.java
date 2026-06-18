package mx.edu.uv.parking_service.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import mx.edu.uv.parking_service.entity.Movimiento;

@Mapper
public interface MovimientoRepository {

    @Insert ("INSERT INTO movimiento (idEspacio, idVehiculo, tiempoEntrada, tiempoCreacion, tarifaHora) VALUES (#{idEspacio}, #{idVehiculo}, #{tiempoEntrada}, #{tiempoCreacion}, #{tarifaHora})")
    void registrarMovimiento(Movimiento movimiento);

    @Select ("SELECT * FROM movimiento WHERE idVehiculo = #{idVehiculo} AND tiempoSalida IS NULL")
    Movimiento obtenerMovimientoActivo(@Param("idVehiculo") int idVehiculo);

    @Update ("UPDATE movimiento SET tiempoSalida = #{tiempoSalida}, minutosEstacionado = #{minutosEstacionado}, horasCobradas = #{horasCobradas}, costoTotal = #{costoTotal}, tiempoActualizacion = #{tiempoActualizacion} WHERE idMovimiento = #{idMovimiento}")
    void actualizarMovimiento(Movimiento movimiento);

    @Select ("SELECT COUNT(*) FROM movimiento WHERE idVehiculo = #{idVehiculo} AND tiempoSalida IS NULL")
    int contarVehiculosEstacionados (@Param ("idVehiculo") int idVehiculo);

    @Select({
        "<script>",
        "SELECT COUNT(*) FROM movimiento WHERE tiempoSalida IS NULL AND idVehiculo IN ",
        "<foreach item='id' collection='idsVehiculos' open='(' separator=',' close=')'>",
        "#{id}",
        "</foreach>",
        "</script>"
    })
    int contarVehiculosEstacionadosPorUsuario(@Param("idsVehiculos") List<Integer> idsVehiculos);
}
