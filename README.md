**PROYECTO DE TÉCNOLOGIAS PARA LA INTEGRACIÓN**

*EJECUTAR LOS CONTENEDORES*  
>docker compose down -v && docker-compose up --build -d

*ABRIR BASES DE DATOS:*  
>docker exec -it BD_Usuarios psql -U UserPapu -d sicaeUsuario  
>docker exec -it BD_Vehiculo mysql -u UserPapu -pPapulin -D sicaeVehiculo  
>docker exec -it BD_Estacionamiento mysql -u UserPapu -pPapulin -D sicaeEstacionamiento

*FUENTES REVISADAS*
>https://www.baeldung.com/rest-template
>https://www-baeldung-com.translate.goog/mybatis?_x_tr_sl=en&_x_tr_tl=es&_x_tr_hl=es&_x_tr_pto=tc  
>https://www.geeksforgeeks.org/springboot/spring-boot-3-0-jwt-authentication-with-spring-security-using-mysql-database/
>https://www.geeksforgeeks.org/springboot/spring-boot-rest-example/
