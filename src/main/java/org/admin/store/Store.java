package org.admin.store;

/**
 * Represents a store entity with its ID and name.
 */
class Store {

    private int id;
    private String name;

    /**
     * Constructor for creating a new Store object.
     *
     * @param id   The ID of the store.
     * @param name The name of the store.
     */
    public Store(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the ID of the store.
     *
     * @return The ID of the store.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the store.
     *
     * @return The name of the store.
     */
    public String getName() {
        return name;
    }
}
