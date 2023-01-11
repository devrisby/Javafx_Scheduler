package org.devrisby.c195.data;

import java.util.List;

/** Interface for database read-only operations */
public interface ReadRepository<T> {
    /** Returns all instances of entity type. */
    public List<T> findAll();

    /** Retrieves an entity by its id. */
    public T findById(int ID);

    /** Retrieves an entity by its id. */
    public T findByField(String field);
}
