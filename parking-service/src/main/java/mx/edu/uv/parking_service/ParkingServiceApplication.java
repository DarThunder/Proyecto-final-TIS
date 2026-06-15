package mx.edu.uv.parking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@SpringBootApplication
public class ParkingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkingServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            //Busca la petición original que llegó desde Postman
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            
            if (attributes != null) {
                //extrae el Header "Authorization" (donde viene el Token)
                String token = attributes.getRequest().getHeader("Authorization");
                
                //Si trae token, se lo pasamos a la nueva llamada 
                if (token != null) {
                    request.getHeaders().add("Authorization", token);
                }
            }
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}