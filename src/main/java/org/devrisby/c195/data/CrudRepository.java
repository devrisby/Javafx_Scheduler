package org.devrisby.c195.data;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    // Returns number of entities available
    public long count();

    // Deletes a given entity
    public void delete(T entity);

    // Deletes the entity with the given ID
    public void deleteById(int ID);

    // Deletes all entities
    public void deleteAll();

    // Returns whether an entity with the given ID exists
    public boolean existsById(int ID);

    // Returns all instances of entity type
    public List<T> findAll();

    // Retrieves an entity by its id
    public T findById(int ID);

    // Create new entity or update existing entity
    public T save(T entity);

}
