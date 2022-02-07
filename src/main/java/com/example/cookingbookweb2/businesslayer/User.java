package com.example.cookingbookweb2.businesslayer;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;




    @Entity
    @Table(name = "user")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull
        @Column(unique = true)
        private String email;
        @NotNull
        private String password;
        @NotNull
        private String role;
        @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
        private List<Recipe> recipes;
    }


