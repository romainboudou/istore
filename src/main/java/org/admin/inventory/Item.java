package org.admin.inventory;

/**
 * Represents an item in the inventory.
 */
public class Item {
    private String name;
    private double price;
    private int quantity;

    /**
     * Constructs a new instance of Item with the specified name, price, and quantity.
     *
     * @param name     The name of the item.
     * @param price    The price of the item.
     * @param quantity The quantity of the item.
     */
    public Item(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Gets the name of the item.
     *
     * @return The name of the item.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the item.
     *
     * @param name The new name of the item.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the price of the item.
     *
     * @return The price of the item.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the item.
     *
     * @param price The new price of the item.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the quantity of the item.
     *
     * @return The quantity of the item.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the item.
     *
     * @param quantity The new quantity of the item.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
