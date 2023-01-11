package org.devrisby.c195.modules.country;

import org.devrisby.c195.data.AppDataSource;

import java.util.List;

/** Class for Country business logic */
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService() {
        this.countryRepository = new CountryRepository(AppDataSource.getConnection());
    }

    public List<Country> findAll() {
        return this.countryRepository.findAll();
    }
}
