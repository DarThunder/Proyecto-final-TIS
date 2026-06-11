package mx.edu.uv.user_service.repo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import mx.edu.uv.user_service.entity.UserEntity;

@Mapper
public interface UserRepository {
    @Select("SELECT EXISTS(SELECT 1 FROM usuario WHERE username = #{username} OR email = #{correo})")
    boolean existeUsuarioPorCorreoOUsername(@Param("username") String username, @Param("correo") String correo);

    @Select("SELECT estatus FROM usuario WHERE \"claveUsuario\" = #{claveUsuario}")
    Boolean estatusUsuario(String claveUsuario);

    @Insert("INSERT INTO usuario (\"idRol\", \"idTipoUsuario\", \"idProgramaEducativo\", \"claveUsuario\", username, password, email, nombre, \"apellidoPaterno\", \"apellidoMaterno\", estatus, \"tiempoCreacion\") VALUES (#{idRol}, #{idTipoUsuario}, #{idProgramaEducativo}, #{claveUsuario}, #{username}, #{password}, #{correo}, #{nombre}, #{apellidoPaterno}, #{apellidoMaterno}, CAST(#{estatus} AS bit), #{tiempoCreacion})")
    void insertarUsuario(UserEntity usuario);
}