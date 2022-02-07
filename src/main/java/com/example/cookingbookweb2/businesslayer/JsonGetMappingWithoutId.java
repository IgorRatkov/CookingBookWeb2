package com.example.cookingbookweb2.businesslayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonGetMappingWithoutId {
    private String name;
    private String description;
    private String category;
    private LocalDateTime date;
    private List<String> ingredients;
    private List<String> directions;
public JsonGetMappingWithoutId(Recipe recipe){
    name = recipe.getName();
    description = recipe.getDescription();
    category = recipe.getCategory();
    date = recipe.getDate();
    ingredients = recipe.getIngredients();
    directions = recipe.getDirections();
}
}
