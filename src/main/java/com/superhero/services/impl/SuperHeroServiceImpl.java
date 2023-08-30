package com.superhero.services.impl;

import com.superhero.constants.ExceptionConstants;
import com.superhero.exception.SuperHeroNotFoundException;
import com.superhero.model.SuperHero;
import com.superhero.repository.SuperHeroRepository;
import com.superhero.services.ISuperHeroService;
import java.util.List;
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
        return findById(id);
    }

    @Override
    public List<SuperHero> searchSuperHeroesByName(String name) {
        List<SuperHero> superHeroes = superHeroRepository.findByNameContainingIgnoreCase(name);

        return validateAndGetSuperHeroes(superHeroes, String.format(
            ExceptionConstants.SUPER_HEROES_WITH_NAME_NOT_FOUND, name) );
    }

    @Override
    public SuperHero createSuperHero(SuperHero superHero) {
        return superHeroRepository.save(superHero);
    }

    @Override
    public SuperHero updateSuperHero(SuperHero inputSuperHero, long id) {

        SuperHero superHeroFound = findById(id);

        SuperHero superHeroUpdated = SuperHero.builder()
            .id(superHeroFound.getId())
            .name(inputSuperHero.getName())
            .description(inputSuperHero.getDescription())
            .birthDate(inputSuperHero.getBirthDate())
            .creationDate(superHeroFound.getCreationDate())
            .build();

        return superHeroRepository.save(superHeroUpdated);
    }

    @Override
    public void deleteSuperHero(long id) {
        SuperHero superHeroFound = findById(id);

        superHeroRepository.deleteById(id);
    }

    private SuperHero findById(long id) {
        return superHeroRepository.findById(id)
            .orElseThrow(() -> new SuperHeroNotFoundException(
                String.format(ExceptionConstants.SUPER_HEROE_WITH_ID_NOT_FOUND, id)));
    }

    private List<SuperHero> validateAndGetSuperHeroes(List<SuperHero> superHeroes, String message) {
        if (superHeroes.isEmpty()) {
            throw new SuperHeroNotFoundException(message);
        }

        return superHeroes;
    }
}
