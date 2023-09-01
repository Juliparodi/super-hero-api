package com.superhero.controller;

import static com.superhero.constants.OutputMessageConstants.CREATED;
import static com.superhero.constants.OutputMessageConstants.DELETED;
import static com.superhero.constants.OutputMessageConstants.MESSAGE_OUTPUT;
import static com.superhero.constants.OutputMessageConstants.UPDATED;
import static com.superhero.utils.ValidationUtils.validateString;
import static com.superhero.utils.ValidationUtils.validateStringConvertToLong;

import com.superhero.annotations.CommonApiResponses;
import com.superhero.annotations.ExecutionTimeLog;
import com.superhero.model.SuperHero;
import com.superhero.services.ISuperHeroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Operation(description = "Retrieve all super heroes")
    @ApiResponse(responseCode = "200", description = "Super Heroes found")
    @CommonApiResponses
    @ExecutionTimeLog
    @GetMapping
    public ResponseEntity<List<SuperHero>> getAllSuperHeroes() {
        List<SuperHero> allSuperHeroes = superHeroService.getAllSuperHeroes();
        return ResponseEntity.ok(allSuperHeroes);
    }

    @Operation(description = "Retrieve super hero by id")
    @ApiResponse(responseCode = "200", description = "Super Heroes with given id found")
    @CommonApiResponses
    @ExecutionTimeLog
    @GetMapping("/{id}")
    public ResponseEntity<SuperHero> getSuperHeroById(@PathVariable String id) {
        Long heroId = validateStringConvertToLong(id);
        SuperHero superHero = superHeroService.getSuperHeroById(heroId);
        return ResponseEntity.ok(superHero);
    }

    @Operation(description = "Retrieve all super heroes that matches given text")
    @ApiResponse(responseCode = "200", description = "Super Heroes with matching text shown")
    @CommonApiResponses
    @ExecutionTimeLog
    @GetMapping("/search")
    public ResponseEntity<List<SuperHero>> searchSuperHeroesByText(@RequestParam String name) {
        validateString(name);
        List<SuperHero> matchingSuperHeroes = superHeroService.searchSuperHeroesByName(name);
        return ResponseEntity.ok(matchingSuperHeroes);
    }

    @Operation(description = "Creates new super hero")
    @ApiResponse(responseCode = "200", description = "Super Heroes created")
    @CommonApiResponses
    @ExecutionTimeLog
    @PostMapping("/hero")
    public ResponseEntity<String> createSuperHero(@RequestBody @Valid SuperHero superHero) {
        SuperHero superHeroCreated = superHeroService.createSuperHero(superHero);
        return ResponseEntity.ok(String.format(MESSAGE_OUTPUT, superHeroCreated.getId(), CREATED));
    }

    @Operation(description = "Update user by ID")
    @ApiResponse(responseCode = "200", description = "Super Hero with given id successfully updated")
    @CommonApiResponses
    @ExecutionTimeLog
    @PutMapping("/hero/{id}")
    public ResponseEntity<String> updateSuperHero(@RequestBody @Valid SuperHero superHero, @PathVariable String id) {
        Long heroId = validateStringConvertToLong(id);
        SuperHero superHeroUpdated = superHeroService.updateSuperHero(superHero, heroId);
        return ResponseEntity.ok(String.format(MESSAGE_OUTPUT, superHeroUpdated.getId(), UPDATED));
    }

    @Operation(description = "Delete user by ID")
    @ApiResponse(responseCode = "200", description = "Super Hero with given id successfully deleted")
    @CommonApiResponses
    @ExecutionTimeLog
    @DeleteMapping("/hero/{id}")
    public ResponseEntity<String> deleteSuperHero(@PathVariable String id) {
        Long heroId = validateStringConvertToLong(id);
        superHeroService.deleteSuperHero(heroId);
        return ResponseEntity.ok(String.format(MESSAGE_OUTPUT, id, DELETED));
    }

}
