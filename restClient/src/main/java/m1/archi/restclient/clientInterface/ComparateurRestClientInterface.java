package m1.archi.restclient.clientInterface;

import m1.archi.restclient.clientInterface.swingInterface.Interface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ComparateurRestClientInterface implements CommandLineRunner {
    @Autowired
    private RestTemplate proxy;

    @Override
    public void run(String... args) {
        try {
            new Interface(proxy);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
