package id.co.pcsindonesia.ia.ekyc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@OpenAPIDefinition
@SecurityScheme(
        name = "apikey",
        type = SecuritySchemeType.APIKEY,
        paramName = "token",
        in = SecuritySchemeIn.HEADER
)
@EnableWebMvc
@ConfigurationPropertiesScan
@SpringBootApplication
public class EKycApplication
//        extends SpringBootServletInitializer
{

    public static void main(String[] args) {
        SpringApplication.run(EKycApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
