package id.co.pcsindonesia.ia.ekyc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@OpenAPIDefinition
@EnableWebMvc
@SpringBootApplication
public class EKycApplication {

    public static void main(String[] args) {
        SpringApplication.run(EKycApplication.class, args);
    }

}
