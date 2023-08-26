package com.superhero.services.impl;

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
        return null;
    }

    @Override
    public SuperHero getSuperHeroById(Long id) {
        return null;
    }

    @Override
    public List<SuperHero> searchSuperHeroesByName(String name) {
        return null;
    }
}
