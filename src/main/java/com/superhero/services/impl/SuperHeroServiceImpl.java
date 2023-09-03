package com.superhero.services.impl;

import com.superhero.constants.ExceptionConstants;
import com.superhero.exception.SuperHeroNotFoundException;
import com.superhero.model.SuperHero;
import com.superhero.repository.SuperHeroRepository;
import com.superhero.services.ISuperHeroService;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class SuperHeroServiceImpl implements ISuperHeroService {

    private final SuperHeroRepository superHeroRepository;

    @Autowired
    public SuperHeroServiceImpl(SuperHeroRepository superHeroRepository) {
        this.superHeroRepository = superHeroRepository;
    }


    @Cacheable("getAllSuperHeroesCache")
    @Override
    public List<SuperHero> getAllSuperHeroes() {
        List<SuperHero> superHeroes = superHeroRepository.findAll();
        return validateAndGetSuperHeroes(superHeroes, ExceptionConstants.SUPER_HEROES_NOT_FOUND);
    }

    @Cacheable(value = "superHeroByIdCache", key = "#id")
    @Override
    public SuperHero getSuperHeroById(Long id) {
        return findById(id);
    }

    @Override
    @Cacheable(value = "searchSuperHeroesByTextCache", key = "#name")
    public List<SuperHero> searchSuperHeroesByName(String name) {
        List<SuperHero> superHeroes = superHeroRepository.findByNameContainingIgnoreCase(name);

        return validateAndGetSuperHeroes(superHeroes, String.format(
            ExceptionConstants.SUPER_HEROES_WITH_NAME_NOT_FOUND, name) );
    }

    @CacheEvict(value = {"getAllSuperHeroesCache", "searchSuperHeroesByTextCache"}, allEntries = true)
    @Override
    public SuperHero createSuperHero(SuperHero superHero) {
        return superHeroRepository.save(superHero);
    }

    @CacheEvict(value = {"superHeroByIdCache", "getAllSuperHeroesCache", "searchSuperHeroesByTextCache"}, key = "#id")
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

    @CacheEvict(value = {"superHeroByIdCache", "getAllSuperHeroesCache", "searchSuperHeroesByTextCache"}, key = "#id")
    @Override
    public void deleteSuperHero(long id) {
        superHeroRepository.deleteById(findById(id).getId());
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
