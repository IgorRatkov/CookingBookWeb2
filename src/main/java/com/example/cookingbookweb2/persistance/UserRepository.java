package com.example.cookingbookweb2.persistance;



import com.example.cookingbookweb2.businesslayer.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    User findUserById(Long id);

    User findUserByEmail(String username);
}
