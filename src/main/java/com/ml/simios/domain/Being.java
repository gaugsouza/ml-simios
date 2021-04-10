package com.ml.simios.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Being {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String dna;
    private Boolean isSimian;
}
