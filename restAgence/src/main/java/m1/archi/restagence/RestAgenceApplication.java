package m1.archi.restagence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"m1.archi.restagence.models"})
@EnableJpaRepositories(basePackages = {"m1.archi.restagence.repositories"})
@SpringBootApplication(scanBasePackages = {
		"m1.archi.restagence.data",
		"m1.archi.restagence.controllers",
		"m1.archi.restagence.client",
		"m1.archi.restagence.exceptions"})
public class RestAgenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestAgenceApplication.class, args);
	}

}
