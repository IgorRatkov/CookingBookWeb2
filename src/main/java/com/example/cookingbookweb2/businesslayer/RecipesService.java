package com.example.cookingbookweb2.businesslayer;




import com.example.cookingbookweb2.persistance.RecipesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipesService {
    private  final RecipesRepository recipesRepository;
    @Autowired
   public RecipesService(RecipesRepository recipesRepository) {
       this.recipesRepository = recipesRepository;
   }
    public Recipe findRecipesById(Long id) {
        return recipesRepository.findRecipesById(id);
            }
    public Iterable<Recipe> findByCategoryIgnoreCaseOrderByDateDesc(String category) {
        return recipesRepository.findByCategoryIgnoreCaseOrderByDateDesc(category); }
    public Optional<Recipe> findByIdAndUser(Long id, User user){
        return recipesRepository.findByIdAndUser(id, user);
    }
    public Iterable<Recipe> findByNameIgnoreCaseContainsOrderByDateDesc(String name) {
        return  recipesRepository.findByNameIgnoreCaseContainsOrderByDateDesc(name);
    }

   public Iterable<Recipe> findRecipesAll() {

        return recipesRepository.findAll();
    }

    public void deleteRecipesById(Long id) {
         recipesRepository.deleteById(id);
    }
    public  void  deleteAllRecipes(){
        recipesRepository.deleteAll();
    }
    public Recipe save(Recipe toSave) {
        return recipesRepository.save(toSave);
    }



}
