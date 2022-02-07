package com.example.cookingbookweb2.presentation;



import com.example.cookingbookweb2.businesslayer.User;
import com.example.cookingbookweb2.persistance.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
@Slf4j
@RestController
public class RegistrationController {
@Autowired
UserRepository userRepository;
@Autowired
PasswordEncoder encoder;
    @PostMapping("/api/register")
    public long saveUser(@RequestBody User userInfo) {
        log.info("Registration="+userInfo.toString());
//Both fields are required and must be valid: email should contain @ and . symbols, password should contain at least 8 characters and shouldn't be blank
        if (userInfo.getEmail().matches(".+@.+\\..+")&& userRepository.findUserByEmail(userInfo.getEmail())==null) {
            if (StringUtils.hasText(userInfo.getPassword())&& userInfo.getPassword().length() >= 8) {

            userInfo.setRole("ROLE_USER");
            userInfo.setPassword(encoder.encode(userInfo.getPassword()));
            userRepository.save(userInfo);
            return userInfo.getId();
//@Pattern(regexp = ".+@.+\\..+")
        }
            else {throw new ResponseStatusException(HttpStatus.BAD_REQUEST);}
        }
        else {throw new ResponseStatusException(HttpStatus.BAD_REQUEST);}
    }

}
//@PostMapping("/items")
//    public void addItem(@AuthenticationPrincipal UserDetails details, @RequestParam String item) {
//        String username = details.getUsername();
//
//        if (items.containsKey(username)) {
//            items.get(username).add(item);
//        } else {
//            items.put(username, new HashSet<>(Set.of(item)));
//        }
//    }