package com.example.github_search;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class GithubRepository {
    Integer id;
    String name;
    String description;
    String url;


    public GithubRepository(Integer id, String name, String description) {

        this.id = id;
        this.name = name;
        this.description = description;
    }
}
