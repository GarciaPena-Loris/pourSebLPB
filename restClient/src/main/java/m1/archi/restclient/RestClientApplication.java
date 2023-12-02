package m1.archi.restclient;

import m1.archi.restclient.clientInterface.ComparateurRestClientInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.swing.*;

@SpringBootApplication(scanBasePackages = {
		"m1.archi.restclient.clientInterface",
		"m1.archi.restclient.models"})
@EnableWebMvc
public class RestClientApplication  {

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");
		SpringApplication.run(RestClientApplication.class, args);
		new ComparateurRestClientInterface();
	}
}
