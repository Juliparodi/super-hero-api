package com.superhero.services.impl;

import com.superhero.constants.ExceptionConstants;
import com.superhero.exception.SuperHeroNotFoundException;
import com.superhero.model.SuperHero;
import com.superhero.repository.SuperHeroRepository;
import com.superhero.services.ISuperHeroService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuperHeroServiceImpl implements ISuperHeroService {

    private final SuperHeroRepository superHeroRepository;

    @Autowired
    public SuperHeroServiceImpl(SuperHeroRepository superHeroRepository) {
        this.superHeroRepository = superHeroRepository;
    }

    @Override
    public List<SuperHero> getAllSuperHeroes() {
        List<SuperHero> superHeroes = superHeroRepository.findAll();

        return validateAndGetSuperHeroes(superHeroes, ExceptionConstants.SUPER_HEROES_NOT_FOUND);
    }

    @Override
    public SuperHero getSuperHeroById(Long id) {
        Optional<SuperHero> superHero = superHeroRepository.findById(id);
        return superHero.orElseThrow(() -> new SuperHeroNotFoundException(String.format(
            ExceptionConstants.SUPER_HEROE_WITH_ID_NOT_FOUND, id)));
    }

    @Override
    public List<SuperHero> searchSuperHeroesByName(String name) {
        List<SuperHero> superHeroes = superHeroRepository.findByNameContainingIgnoreCase(name);

        return validateAndGetSuperHeroes(superHeroes, String.format(
            ExceptionConstants.SUPER_HEROES_WITH_NAME_NOT_FOUND, name) );
    }

    private List<SuperHero> validateAndGetSuperHeroes(List<SuperHero> superHeroes, String message) {
        if (superHeroes.isEmpty()) {
            throw new SuperHeroNotFoundException(message);
        }

        return superHeroes;
    }
}
