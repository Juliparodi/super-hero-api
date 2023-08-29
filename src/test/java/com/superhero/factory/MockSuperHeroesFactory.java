package com.superhero.factory;

import com.superhero.model.SuperHero;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MockSuperHeroesFactory {

    public static List<SuperHero> getAllSuperHeroesWithIdAndDates() {
        return List.of(
            new SuperHero(1, "Superman", "The Man of Steel", LocalDate.parse("1938-04-18"), LocalDate.parse("2023-08-30"), LocalDate.parse("2023-08-30")),
            new SuperHero(2, "Spider-Man", "Your friendly neighborhood superhero", LocalDate.parse("1962-08-10"), LocalDate.parse("2023-08-30"), LocalDate.parse("2023-08-30")),
            new SuperHero(3, "Wonder Woman", "Amazonian princess with super strength", LocalDate.parse("1941-12-01"), LocalDate.parse("2023-08-30"), LocalDate.parse("2023-08-30")),
            new SuperHero(4, "Green Lantern", "Guardian of Sector 2814 with a power ring", LocalDate.parse("1939-03-30"), LocalDate.parse("2023-08-30"), LocalDate.parse("2023-08-30"))
        );
    }

    public static List<SuperHero> getAllSuperHeroes() {
        return List.of(
            SuperHero.builder()
                .id(1)
                .name("Superman")
                .description("The Man of Steel")
                .birthDate(LocalDate.parse("1938-04-18"))
                .build(),
            SuperHero.builder()
                .id(2)
                .name("Spider-Man")
                .description("Your friendly neighborhood superhero")
                .birthDate(LocalDate.parse("1962-08-10"))
                .build(),
            SuperHero.builder()
                .id(3)
                .name("Wonder Woman")
                .description("Amazonian princess with super strength")
                .birthDate(LocalDate.parse("1941-12-01"))
                .build(),
            SuperHero.builder()
                .id(4)
                .name("Green Lantern")
                .description("Guardian of Sector 2814 with a power ring")
                .birthDate(LocalDate.parse("1939-03-30"))
                .build()
        );
    }

    public static Optional<SuperHero> getSuperHero(long heroId) {
        return getAllSuperHeroesWithIdAndDates().stream()
            .filter(superHero -> heroId == superHero.getId())
            .findFirst();
    }

    public static List<SuperHero> getSuperHeroContains(String searchName) {
        return getAllSuperHeroesWithIdAndDates().stream()
            .filter(superHero -> superHero.getName().toLowerCase().contains(searchName))
            .collect(Collectors.toList());
    }

    public static SuperHero newSuperHero() {
        return SuperHero.builder()
            .name("Aqua Dynamo")
            .description("Master of the Abyss")
            .birthDate(LocalDate.parse("1938-04-18"))
            .build();
    }

    public static SuperHero inputUpdatedSuperHero() {
        return SuperHero.builder()
            .id(3)
            .name("Wonder Woman")
            .description("Wonder Woman, also known as Diana Prince, is an iconic superheroine and a founding member of the Justice League")
            .birthDate(LocalDate.parse("1941-12-01"))
            .build();
    }

    public static SuperHero createNewSuperHeroe(SuperHero superHero) {
        return SuperHero.builder()
            .name(superHero.getName())
            .description(superHero.getDescription())
            .birthDate(superHero.getBirthDate())
            .creationDate(LocalDate.now())
            .modifiedDate(LocalDate.now())
            .build();
    }
}
