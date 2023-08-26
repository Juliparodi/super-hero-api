package com.superhero.factory;

import com.superhero.repository.SuperHeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class InitHeroes implements ApplicationListener<ContextRefreshedEvent> {

    private final SuperHeroRepository superHeroRepository;

    @Autowired
    public InitHeroes(SuperHeroRepository superHeroRepository) {
        this.superHeroRepository = superHeroRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }
}
