package com.superhero.services;

import com.superhero.model.SuperHero;
import java.util.List;

public interface ISuperHeroService {

    List<SuperHero> getAllSuperHeroes();

    SuperHero getSuperHeroById(Long id);

    List<SuperHero> searchSuperHeroesByName(String name);
}
