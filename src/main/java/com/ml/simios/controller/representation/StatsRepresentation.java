package com.ml.simios.controller.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StatsRepresentation {
    @JsonProperty(value = "count_mutant_dna")
    private Long countMutantDna;
    @JsonProperty(value = "count_human_dna")
    private Long countHumanDna;
    private Float ratio;
}
