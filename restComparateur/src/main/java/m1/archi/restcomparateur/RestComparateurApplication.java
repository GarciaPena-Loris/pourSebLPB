package m1.archi.restcomparateur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"m1.archi.restcomparateur.models"})
@EnableJpaRepositories(basePackages = {"m1.archi.restcomparateur.repositories"})
@SpringBootApplication(scanBasePackages = {
        "m1.archi.restcomparateur.data",
        "m1.archi.restcomparateur.controllers",
        "m1.archi.restcomparateur.client",
        "m1.archi.restcomparateur.exceptions"})
public class RestComparateurApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestComparateurApplication.class, args);
    }

}
