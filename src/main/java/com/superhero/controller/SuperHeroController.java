package com.superhero.controller;

import com.superhero.model.SuperHero;
import com.superhero.services.ISuperHeroService;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<SuperHero> getSuperHeroById(@PathVariable Long id) {
        SuperHero superHero = superHeroService.getSuperHeroById(id);
        return ResponseEntity.ok(superHero);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SuperHero>> searchSuperHeroesByName(@RequestParam @NotEmpty String name) {
        List<SuperHero> matchingSuperHeroes = superHeroService.searchSuperHeroesByName(name);
        return ResponseEntity.ok(matchingSuperHeroes);
    }

}
