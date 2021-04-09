package com.ml.simios.controller.representation;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DnaRepresentation {
    @NotEmpty(message = "Uma sequência de DNA deve ser informada")
    private String[] dna;
}
