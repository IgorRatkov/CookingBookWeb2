package com.example.cookingbookweb2.persistance;



import com.example.cookingbookweb2.businesslayer.Recipe;
import com.example.cookingbookweb2.businesslayer.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RecipesRepository extends CrudRepository<Recipe,Long> {
    Recipe findRecipesById(Long id);
    Iterable<Recipe> findByNameIgnoreCaseContainsOrderByDateDesc(String name);
    Iterable<Recipe> findByCategoryIgnoreCaseOrderByDateDesc(String category);


    Optional<Recipe> findByIdAndUser(Long id, User user);
    // Iterable<Recipes> findRecipiesByName(String name);
    //findByCategoryIgnoreCaseOrderByDateDesc(String category)
}

