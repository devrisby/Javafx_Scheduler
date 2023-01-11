package org.devrisby.c195.modules.division;

import org.devrisby.c195.data.AppDataSource;

import java.util.List;

/** Class for Division business logic */
public class DivisionService {
    private final DivisionRepository divisionRepository;

    public DivisionService() {
        this.divisionRepository = new DivisionRepository(AppDataSource.getConnection());
    }

    public List<Division> findAll() {
        return this.divisionRepository.findAll();
    }

    public List<Division> findByCountryId(int id) {
        return this.divisionRepository.findByCountryId(id);
    }
}
