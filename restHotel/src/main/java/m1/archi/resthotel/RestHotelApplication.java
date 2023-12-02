package m1.archi.resthotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"m1.archi.resthotel.models"})
@EnableJpaRepositories(basePackages = {"m1.archi.resthotel.repositories"})

@SpringBootApplication(scanBasePackages = {
        "m1.archi.resthotel.data",
        "m1.archi.resthotel.controllers",
        "m1.archi.resthotel.exceptions"})
public class RestHotelApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestHotelApplication.class, args);
    }

}
