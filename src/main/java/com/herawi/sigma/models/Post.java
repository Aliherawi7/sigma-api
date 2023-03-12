package com.herawi.sigma.models;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Collection;

public class Post {

    @Id
    private Long id;
    private String username;
    private LocalDateTime localDateTime;
    private Long reactions;
    private String textContent;
    @OneToMany(fetch = FetchType.EAGER)
    private Collection<String> images;

}
