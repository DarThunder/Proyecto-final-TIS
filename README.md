**PROYECTO DE TÉCNOLOGIAS PARA LA INTEGRACIÓN**

*EJECUTAR LOS CONTENEDORES*  
>docker compose down -v && docker-compose up --build -d

*ABRIR BASES DE DATOS:*  
>docker exec -it BD_Usuarios psql -U UserPapu -d sicaeUsuario  
>docker exec -it BD_Vehiculo mysql -u UserPapu -pPapulin -D sicaeVehiculo  
>docker exec -it BD_Estacionamiento mysql -u UserPapu -pPapulin -D sicaeEstacionamiento
