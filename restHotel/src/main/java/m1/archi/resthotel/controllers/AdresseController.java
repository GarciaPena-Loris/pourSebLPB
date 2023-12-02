package m1.archi.resthotel.controllers;

import m1.archi.resthotel.exceptions.AdresseNotFoundException;
import m1.archi.resthotel.models.Adresse;
import m1.archi.resthotel.repositories.AdresseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdresseController {
    /* Attributs */
    @Autowired
    private AdresseRepository repository;

    /* Méthodes */
    @GetMapping("${base-uri}/adresses")
    public List<Adresse> getAllAdresses() {
        return repository.findAll();
    }

    @GetMapping("${base-uri}/adresses/{id}")
    public Adresse getAdresseById(@PathVariable long id) throws AdresseNotFoundException {
        return repository.findById(id).orElseThrow(() -> new AdresseNotFoundException("Adresse not found with id " + id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("${base-uri}/adresses")
    public Adresse createAdresse(@RequestBody Adresse adresse) {
        return repository.save(adresse);
    }

    @PutMapping("${base-uri}/adresses/{id}")
    public Adresse updateAdresse(@RequestBody Adresse newAdresse, @PathVariable long id) throws AdresseNotFoundException {
        return repository.findById(id).map(adresse -> {
            adresse.setPays(newAdresse.getPays());
            adresse.setVille(newAdresse.getVille());
            adresse.setRue(newAdresse.getRue());
            adresse.setNumero(newAdresse.getNumero());
            adresse.setPosition(newAdresse.getPosition());
            return repository.save(adresse);
        }).orElseGet(() -> repository.save(newAdresse));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("${base-uri}/adresses/{id}")
    public void deleteAdresse(@PathVariable long id) throws AdresseNotFoundException {
        Adresse adresse = repository.findById(id).orElseThrow(() -> new AdresseNotFoundException("Adresse not found with id " + id));
        repository.delete(adresse);
    }
}
