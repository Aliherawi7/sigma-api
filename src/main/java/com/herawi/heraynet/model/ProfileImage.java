package com.herawi.heraynet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProfileImage {
    @Id
    private long userId;
    @Lob
    private byte[] image;
}
