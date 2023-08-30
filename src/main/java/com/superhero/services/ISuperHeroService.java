package com.superhero.services;

import com.superhero.model.SuperHero;
import java.util.List;

public interface ISuperHeroService {

    List<SuperHero> getAllSuperHeroes();

    SuperHero getSuperHeroById(Long id);

    List<SuperHero> searchSuperHeroesByName(String name);

    SuperHero createSuperHero(SuperHero superHero);

    SuperHero updateSuperHero(SuperHero superHero, long parseLong);

    void deleteSuperHero(long parseLong);

}
