package com.example.cookingbookweb2.presentation;
import com.example.cookingbookweb2.businesslayer.*;
import com.example.cookingbookweb2.persistance.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RestController
public class RecipesExchange {
    @Autowired
    RecipesService recipesService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/api/recipe/newqq")
    public void createRecipesW(@AuthenticationPrincipal User userDetails,
                              @RequestBody Recipe recipe) {

        recipe.setUser(userDetails);
        recipesService.save(recipe);

    }
//POST /api/recipe/new receives a recipe as a JSON object and returns a JSON object with one id field;
    @PostMapping("/api/recipe/new")
    public JsonIdReturn createRecipes(@AuthenticationPrincipal UserDetails details,
                              @RequestBody Recipe recipe) {
      //проверяем рецепт на соответствие входным параметрам
        if(!StringUtils.hasText(recipe.getName())||!StringUtils.hasText(recipe.getDescription())||
                recipe.getDirections()==null|| recipe.getDirections().size()==0||
                recipe.getIngredients()==null|| recipe.getIngredients().size()==0||
                recipe.getId()!=null||!StringUtils.hasText(recipe.getCategory())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        else {
            if (recipe.getIngredients().isEmpty() || recipe.getDirections().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else {
                if (recipe.getName().isEmpty() || recipe.getDescription().isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                } else {
                    User user = userRepository.findUserByEmail(details.getUsername());
                    recipe.setUser(user);//привязываем к таблице User который размещает рецепт таблицу Recipe
                    recipe.setDate(LocalDateTime.now());//устанавливаем время создания рецепта
                    recipesService.save(recipe);
                    //возвращаем ответ в формате Json  с полем id рецепта
                    JsonIdReturn jsonIdReturn = new JsonIdReturn();
                    jsonIdReturn.setId(recipe.getId());
                    return jsonIdReturn;
                }
            }
        }
    }





    @GetMapping("/posts/{user_id}")
    public User showUser(@PathVariable (value = "user_id") Long userId
                              ) {

        return userRepository.findUserById(userId);

    }

    @PutMapping("/api/recipe/{id}")
    public void updateRecipes(@AuthenticationPrincipal UserDetails details,@PathVariable long id, @RequestBody Recipe recipe) {
        User user = userRepository.findUserByEmail(details.getUsername());
        Optional<Recipe> recipeOptional = recipesService.findByIdAndUser(id, user);


        if (!recipeOptional.isEmpty()) {


            Optional<Recipe> checkNull = Optional.ofNullable(recipesService.findRecipesById(id));
            if (checkNull.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            } else {// return checkNull;
                if (!StringUtils.hasText(recipe.getName()) || !StringUtils.hasText(recipe.getDescription()) ||
                        recipe.getDirections() == null || recipe.getDirections().size() == 0 ||
                        recipe.getIngredients() == null || recipe.getIngredients().size() == 0 ||
                        recipe.getId() != null || !StringUtils.hasText(recipe.getCategory())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                } else {
                    if (recipe.getIngredients().isEmpty() || recipe.getDirections().isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                    } else {
                        if (recipe.getName().isEmpty() || recipe.getDescription().isEmpty()) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                        } else {


                            Recipe updateId = recipesService.findRecipesById(id);

                            updateId.setDate(LocalDateTime.now());
                            updateId.setCategory(recipe.getCategory());
                            updateId.setDescription(recipe.getDescription());
                            updateId.setDirections(recipe.getDirections());
                            updateId.setName(recipe.getName());
                            updateId.setIngredients(recipe.getIngredients());

                            //User user = userRepository.findUserByEmail(details.getUsername());//уже определено в начале метода
                            updateId.setUser(user);
                            recipesService.save(updateId);
                            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
                        }

                    }
                }
            }
        }
        else {throw new ResponseStatusException(HttpStatus.FORBIDDEN);}

    }

    // Если нужно получить запрос в формате Json вместе с полем Id
    @GetMapping("/api/recipe/id/{id}")
    public Optional<Recipe> getRecipesWithId(@PathVariable long id) {
        Optional<Recipe> checkNull=Optional.ofNullable(recipesService.findRecipesById(id));
        if (checkNull.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else { return checkNull;}

    }
    //GET /api/recipe/{id} returns a recipe with a specified id as a JSON object;
    //выводим исключая поле Id через буферный класс JsonGetMappingWithoutId:
    @GetMapping("/api/recipe/{id}")
    public JsonGetMappingWithoutId getRecipes(@PathVariable long id) {
        Optional<Recipe> checkNull=Optional.ofNullable(recipesService.findRecipesById(id));
             if (checkNull.isEmpty()){
             throw new ResponseStatusException(HttpStatus.NOT_FOUND);
  }
             //выводим исключая поле Id через буферный класс JsonGetMappingWithoutId:

   else { //Recipes withoutId=recipesService.findRecipesById(id);
     //  JsonGetMappingWithoutId withoutIdJson=new JsonGetMappingWithoutId(withoutId.getName(),
     //          withoutId.getDescription(), withoutId.getCategory(),withoutId.getDate(),
     //          withoutId.getIngredients(),withoutId.getDirections());
                 JsonGetMappingWithoutId withoutIdJson = new JsonGetMappingWithoutId(recipesService.findRecipesById(id));

       return withoutIdJson;
   }
   }


    //выдача запроса из базы по полю name //выдача запроса из базы по полю category
    @GetMapping("/api/recipe/search/")
    public List<JsonGetMappingWithoutId> getRecipesName(@RequestParam Map<String, String> allQueryParams ) {
        Iterable<Recipe> serchRec = Collections.emptySet();

        /*If no recipes are found, the program should return an empty JSON array. If 0 parameters were passed,
        or more than 1, the server should return 400 (Bad Request).
        The same response should follow if the specified parameters are not valid.
        If everything is correct, it should return 200 (Ok).
         */
        if (allQueryParams.containsKey("name")) {

            if (allQueryParams.get("name").isEmpty()||(allQueryParams.get("name").split(" ")).length>1){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            else {
                serchRec = recipesService.findByNameIgnoreCaseContainsOrderByDateDesc(allQueryParams.get("name"));

                }

        }
        else if(allQueryParams.containsKey("category")){
            if(allQueryParams.get("category").isEmpty()||(allQueryParams.get("category").split(" ")).length>1){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            else {
                serchRec = recipesService.findByCategoryIgnoreCaseOrderByDateDesc(allQueryParams.get("category"));
            }
        }
        else if (allQueryParams.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        else {throw new ResponseStatusException(HttpStatus.BAD_REQUEST);}
        List<Recipe> recipe2 = new ArrayList<Recipe>();
        List<JsonGetMappingWithoutId> recipes2New = new ArrayList<JsonGetMappingWithoutId>();
        for (Recipe recipe1 : serchRec) {

                    recipe2.add(recipe1) ;
             }
        for (int i = 0; i< recipe2.size(); i++){

            recipes2New.add(new JsonGetMappingWithoutId(recipe2.get(i)));
        }

        return recipes2New;
    }
//выдача all elements
@GetMapping("/api/recipe/getAll/")
public Iterable<Recipe> getAllRecipies(){
        return recipesService.findRecipesAll();
}


//DELETE /api/recipe/{id} deletes a recipe with a specified id.
    @DeleteMapping("/api/recipe/{id}")
    public void delRecipes(@AuthenticationPrincipal UserDetails details,@PathVariable long id) {
        User user = userRepository.findUserByEmail(details.getUsername());
        Optional<Recipe> recipeOptional = recipesService.findByIdAndUser(id,user);


            if (!recipeOptional.isEmpty()) {

                Optional<Recipe> checkNull = Optional.ofNullable(recipesService.findRecipesById(id));

                if (checkNull.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                } else {
                    recipesService.deleteRecipesById(id);
                    throw new ResponseStatusException(HttpStatus.NO_CONTENT);
                }

            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

        }

    @DeleteMapping("/api/recipe/all/")
    public void delRecipes() {
        recipesService.deleteAllRecipes();
    }
}



