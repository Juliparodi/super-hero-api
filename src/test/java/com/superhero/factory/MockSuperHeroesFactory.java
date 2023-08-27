package com.superhero.factory;

import com.superhero.model.SuperHero;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MockSuperHeroesFactory {

    public static List<SuperHero> getAllSuperHeroes() {
        return List.of(
            new SuperHero(1, "Superman", "The Man of Steel", LocalDate.parse("1938-04-18"), LocalDate.parse("2023-08-30"), LocalDate.parse("2023-08-30")),
            new SuperHero(2, "Spider-Man", "Your friendly neighborhood superhero", LocalDate.parse("1962-08-10"), LocalDate.parse("2023-08-30"), LocalDate.parse("2023-08-30")),
            new SuperHero(3, "Wonder Woman", "Amazonian princess with super strength", LocalDate.parse("1941-12-01"), LocalDate.parse("2023-08-30"), LocalDate.parse("2023-08-30")),
            new SuperHero(4, "Batman", "The Dark Knight of Gotham City", LocalDate.parse("1939-03-30"), LocalDate.parse("2023-08-30"), LocalDate.parse("2023-08-30"))
        );
    }

    public static Optional<SuperHero> getSuperHero(long heroId) {
        return getAllSuperHeroes().stream()
            .filter(superHero -> heroId == superHero.getId())
            .findFirst();
    }

    public static List<SuperHero> getSuperHeroContains(String searchName) {
        return getAllSuperHeroes().stream()
            .filter(superHero -> superHero.getName().toLowerCase().contains(searchName))
            .collect(Collectors.toList());
    }
}
