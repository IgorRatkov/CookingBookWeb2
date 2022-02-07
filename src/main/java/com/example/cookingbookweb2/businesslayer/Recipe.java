package com.example.cookingbookweb2.businesslayer;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "recipes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String category;
    private LocalDateTime date;
    @ElementCollection
    @CollectionTable(name="recipes_ingredients",joinColumns = @JoinColumn(name = "id"))
    @Column(name = "ingredients")
    private List<String> ingredients;
    @ElementCollection
    @CollectionTable(name="recipes_directions",joinColumns = @JoinColumn(name = "id"))
    @Column(name = "directions")
    private List<String> directions;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Recipe(Long id, String name, String description, String category, LocalDateTime date, List<String> ingredients, List<String> directions) {
    }

    // public Recipes(Long id, String name, String description, String category, LocalDateTime date, List<String> ingredients, List<String> directions) {
    // }

    //public void setDate(LocalDateTime date) {
    //    this.date = date;
    // }

    @Override
    public String toString() {
        return "Recipes{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ingredients=" + ingredients +
                ", directions=" + directions +
                '}';
    }
    //@JsonIgnore

}

