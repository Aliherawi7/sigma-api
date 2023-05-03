package com.herawi.sigma.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

public class Post {

    @Id
    @SequenceGenerator(sequenceName = "post_sequence", name = "post_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_sequence")
    private Long id;
    private String username;
    private LocalDateTime localDateTime;
    private Long reactions;
    private String textContent;
    @OneToMany(fetch = FetchType.EAGER)
    private Collection<String> images;

}
