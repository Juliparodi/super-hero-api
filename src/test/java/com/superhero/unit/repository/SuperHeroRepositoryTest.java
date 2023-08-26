package com.superhero.unit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.superhero.model.SuperHero;
import com.superhero.repository.SuperHeroRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SuperHeroRepositoryTest {

    @Autowired
    private SuperHeroRepository superHeroRepository;

    @Test
    public void testFindByNameContainingIgnoreCase() {
        List<SuperHero> result = superHeroRepository.findByNameContainingIgnoreCase("man");

        assertEquals(4, result.size());
        assertEquals("Superman", result.get(0).getName());

    }

}
