package com.example.cookingbookweb2.businesslayer;



import com.example.cookingbookweb2.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private  final UserRepository UserRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.UserRepository = userRepository;
    }
    public User findUserById(Long id) {
        return UserRepository.findUserById(id);
    }

    public User findUserByEmail(String email) {
        return UserRepository.findUserByEmail(email);
    }

    public User saveUser(User toSave) {
        return UserRepository.save(toSave);
    }
}
