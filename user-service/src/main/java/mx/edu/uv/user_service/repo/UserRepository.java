package mx.edu.uv.user_service.repo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import mx.edu.uv.user_service.dto.UserResponse;
import mx.edu.uv.user_service.entity.UserEntity;

@Mapper
public interface UserRepository {
    @Select("SELECT EXISTS(SELECT 1 FROM usuario WHERE \"idUsuario\" = #{idUsuario})")
    boolean existeUsuario(int idUsuario);
    //Incluye username Y correo porque ambos deben ser únicos en el sistema
    @Select("SELECT EXISTS(SELECT 1 FROM usuario WHERE username = #{username} OR email = #{correo})")
    boolean existeUsuarioPorCorreoOUsername(@Param("username") String username, @Param("correo") String correo);

    //Excluye al mismo usuario para permitir que conserve su correo actual al editar
    @Select("SELECT EXISTS(SELECT 1 FROM usuario WHERE email = #{correo} AND \"idUsuario\" != #{idUsuario})")
    boolean correoUsado(@Param("correo") String correo, @Param("idUsuario") int idUsuario);

    //Retorna null si la clave no existe, por eso el tipo es Boolean y no boolean
    @Select("SELECT estatus FROM usuario WHERE \"claveUsuario\" = #{claveUsuario}")
    Boolean estatusUsuario(String claveUsuario);

    //apellidoMaterno es opcional, COALESCE lo reemplaza con cadena vacía si es null
    @Select("SELECT rol AS \"Rol\", TRIM(nombre || ' ' || \"apellidoPaterno\" || ' ' || COALESCE(\"apellidoMaterno\", '')) AS \"nombreCompleto\", \"tipoUsuario\", \"programaEducativo\", username, email AS correo, telefono, estatus = B'1' AS status, \"claveUsuario\", \"tiempoCreacion\", \"tempoActualizacion\" AS \"tiempoActualizacion\" FROM \"usuarioFullInfo\" WHERE \"idUsuario\" = #{idUsuario}")
    UserResponse perfilUsuario(int idUsuario);

    //El estatus se inserta como bit porque así lo maneja PostgreSQL en esta tabla
    @Insert("INSERT INTO usuario (\"idRol\", \"idTipoUsuario\", \"idProgramaEducativo\", \"claveUsuario\", username, password, email, nombre, \"apellidoPaterno\", \"apellidoMaterno\", estatus, \"tiempoCreacion\") VALUES (#{idRol}, #{idTipoUsuario}, #{idProgramaEducativo}, #{claveUsuario}, #{username}, #{password}, #{correo}, #{nombre}, #{apellidoPaterno}, #{apellidoMaterno}, CAST(#{estatus} AS bit), #{tiempoCreacion})")
    void insertarUsuario(UserEntity usuario);

    @Update("UPDATE usuario SET \"idRol\" = #{idRol}, \"idTipoUsuario\" = #{idTipoUsuario}, \"idProgramaEducativo\" = #{idProgramaEducativo}, nombre = #{nombre}, \"apellidoPaterno\" = #{apellidoPaterno}, \"apellidoMaterno\" = #{apellidoMaterno}, email = #{correo}, telefono = #{telefono}, \"tempoActualizacion\" = #{tiempoActualizacion} WHERE \"idUsuario\" = #{idUsuario}")
    void actualizarUsuario(UserEntity usuario);

    //El toggle se hace directo en SQL para evitar una consulta extra de lectura previa
    @Update("UPDATE usuario SET estatus = CASE WHEN estatus = B'1' THEN B'0' ELSE B'1' END WHERE \"idUsuario\" = #{idUsuario}")
    void cambiarEstatusUsuario(int idUsuario);
}