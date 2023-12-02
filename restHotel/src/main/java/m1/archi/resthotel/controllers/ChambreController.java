package m1.archi.resthotel.controllers;

import m1.archi.resthotel.exceptions.ChambreNotFoundException;
import m1.archi.resthotel.exceptions.HotelNotFoundException;
import m1.archi.resthotel.models.Chambre;
import m1.archi.resthotel.repositories.ChambreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChambreController {
    /* Attributs */
    @Autowired
    private ChambreRepository repository;

    /* Méthodes */
    @GetMapping("${base-uri}/chambres")
    public List<Chambre> getAllChambres() {
        return repository.findAll();
    }

    @GetMapping("${base-uri}/chambres/{id}")
    public Chambre getChambreById(@PathVariable long id) throws ChambreNotFoundException {
        return repository.findById(id).orElseThrow(() -> new ChambreNotFoundException("Chambre not found with id " + id));
    }

    @GetMapping("${base-uri}/chambres/{id}/image")
    public String getImageHotel(@PathVariable long id) throws ChambreNotFoundException {
        return repository.findById(id).orElseThrow(() -> new ChambreNotFoundException("Chambre not found with id " + id)).getImageChambre();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("${base-uri}/chambres")
    public Chambre createChambre(@RequestBody Chambre chambre) {
        return repository.save(chambre);
    }

    @PutMapping("${base-uri}/chambres/{id}")
    public Chambre updateChambre(@RequestBody Chambre newChambre, @PathVariable long id) throws ChambreNotFoundException {
        return repository.findById(id).map(chambre -> {
            chambre.setNumero(newChambre.getNumero());
            chambre.setPrix(newChambre.getPrix());
            // Autres propriétés à mettre à jour si nécessaire
            return repository.save(chambre);
        }).orElseGet(() -> repository.save(newChambre));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("${base-uri}/chambres/{id}")
    public void deleteChambre(@PathVariable long id) throws ChambreNotFoundException {
        Chambre chambre = repository.findById(id).orElseThrow(() -> new ChambreNotFoundException("Chambre not found with id " + id));
        repository.delete(chambre);
    }
}
