package com.mockcompany.webapp.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class ProductItem {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Integer cost;
    @Lob
    @Column(nullable = false)
    private String image;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Compares this ProductItem with another object for equality.
     * Two ProductItems are considered equal if their name, description, cost, and image are all equal.
     * This method is used by collections like List to check for object equality.
     *
     * @param o the object to compare with
     * @return true if this ProductItem is equal to the specified object; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductItem that = (ProductItem) o;
        return Objects.equals(getName(), that.getName())
                && Objects.equals(getDescription(), that.getDescription())
                && Objects.equals(getCost(), that.getCost())
                && Objects.equals(getImage(), that.getImage());
    }

    /**
     * Returns a hash code value for this ProductItem.
     * The hash code is computed based on the name, description, cost, and image.
     * This method is used by collections like Set to manage object uniqueness and quick lookups.
     *
     * @return a hash code value for this ProductItem
     */
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getCost(), getImage());
    }

    @Override
    public String toString() {
        return "ProductItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                ", image='" + image + '\'' +
                '}';
    }
}
