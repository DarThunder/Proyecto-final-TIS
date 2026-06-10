package mx.edu.uv.auth_service.repo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import mx.edu.uv.auth_service.entity.UserEntity;

@Mapper
public interface AuthRepository {
    @Select("SELECT * FROM \"usuarioFullInfo\" WHERE username = #{username} AND estatus = '1'")
    UserEntity buscarPorUsername(String username);
}
