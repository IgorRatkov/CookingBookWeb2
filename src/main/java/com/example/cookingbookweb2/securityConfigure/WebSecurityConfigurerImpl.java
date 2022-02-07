package com.example.cookingbookweb2.securityConfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@EnableWebSecurity
public class WebSecurityConfigurerImpl extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.POST,"/api/recipe/new").hasAnyRole("USER")
                .mvcMatchers(HttpMethod.PUT,"/api/recipe/{id}").hasAnyRole("USER")
                .mvcMatchers(HttpMethod.GET,"/api/recipe/{id}").hasAnyRole("USER")
                .mvcMatchers(HttpMethod.GET,"/api/recipe/search/").hasAnyRole("USER")
                .mvcMatchers(HttpMethod.GET,"/api/recipe/{id}").hasAnyRole("USER")
                //.mvcMatchers(HttpMethod.GET,"/api/recipe/search/").hasAnyRole("USER")

                .mvcMatchers(HttpMethod.POST,"/api/register").permitAll()
                .mvcMatchers(HttpMethod.POST,"/actuator/shutdown").permitAll() //for testing should be available without authentication.
            /*    .mvcMatchers("/test").hasAnyRole("USER")
                .mvcMatchers(HttpMethod.POST,"/api/recipe/new").hasAnyRole("USER")
                .mvcMatchers(HttpMethod.GET,"/api/recipe/id/{id}").hasAnyRole("USER")
                .mvcMatchers(HttpMethod.POST,"/api/register").permitAll()// make remaining endpoints public (including POST /api/register)
                .mvcMatchers(HttpMethod.POST,"/actuator/shutdown").permitAll() //for testing should be available without authentication.
*/

                .anyRequest().authenticated()
                .and()
                .csrf().disable() // disabling CSRF will allow sending POST request using Postman
                .httpBasic(); // enables basic auth.
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService) // user store
                .passwordEncoder(getEncoder());


    }
}
