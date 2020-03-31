package com.example.infinispansample;

import com.example.infinispansample.entity.Country;
import com.example.infinispansample.repository.CountryRepository;
import org.junit.Ignore;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;


@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class CountryRepositoryTest {
    @Autowired
    private CountryRepository repository;

    public void setUp() {
        /*Country c1 = new Country(1L, "846", "samara");
        Country c2 = new Country(2L, "495", "moscow");
        repository.save(c1);
        repository.save(c2);*/
    }

    @Test
    public void fetch_test() {
        Iterable<Country> countries = repository.findAll();
        assertNotNull(countries);
        System.out.println(countries);
        for (int i = 0; i < 20; i++) {
            countries = repository.findAll();
        }
    }
}
