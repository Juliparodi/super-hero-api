package com.superhero.controller;

import static com.superhero.constants.OutputMessageConstants.CREATED;
import static com.superhero.constants.OutputMessageConstants.DELETED;
import static com.superhero.constants.OutputMessageConstants.MESSAGE_OUTPUT;
import static com.superhero.constants.OutputMessageConstants.UPDATED;

import com.superhero.constants.OutputMessageConstants;
import com.superhero.model.SuperHero;
import com.superhero.services.ISuperHeroService;
import com.superhero.utils.ValidationUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/superheroes")
@Validated
public class SuperHeroController {

    private final ISuperHeroService superHeroService;


    @Autowired
    public SuperHeroController(ISuperHeroService superHeroService) {
        this.superHeroService = superHeroService;
    }

    @GetMapping
    public ResponseEntity<List<SuperHero>> getAllSuperHeroes() {
        List<SuperHero> allSuperHeroes = superHeroService.getAllSuperHeroes();
        return ResponseEntity.ok(allSuperHeroes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuperHero> getSuperHeroById(@PathVariable String id) {
        ValidationUtils.validateString(id);
        SuperHero superHero = superHeroService.getSuperHeroById(Long.parseLong(id));
        return ResponseEntity.ok(superHero);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SuperHero>> searchSuperHeroesByName(@RequestParam String name) {
        ValidationUtils.validateString(name);
        List<SuperHero> matchingSuperHeroes = superHeroService.searchSuperHeroesByName(name);
        return ResponseEntity.ok(matchingSuperHeroes);
    }

    @PostMapping
    public ResponseEntity<String> createSuperHero(@RequestBody SuperHero superHero) {
        SuperHero superHeroCreated = superHeroService.createSuperHero(superHero);
        String outputMessage = String.format(MESSAGE_OUTPUT, superHeroCreated.getName(),CREATED);
        return ResponseEntity.ok(outputMessage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSuperHero(@RequestBody SuperHero superHero, @PathVariable String id) {
        ValidationUtils.validateString(id);
        SuperHero superHeroUpdated = superHeroService.updateSuperHero(superHero, Long.parseLong(id));
        String outputMessage = String.format(MESSAGE_OUTPUT, superHeroUpdated.getName(), UPDATED);
        return ResponseEntity.ok(outputMessage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSuperHero(@RequestBody SuperHero superHero, @PathVariable String id) {
        ValidationUtils.validateString(id);
        SuperHero superHeroDeleted = superHeroService.deleteSuperHero(superHero, Long.parseLong(id));
        String outputMessage = String.format(MESSAGE_OUTPUT, superHeroDeleted.getName(),DELETED);
        return ResponseEntity.ok(outputMessage);
    }

}
