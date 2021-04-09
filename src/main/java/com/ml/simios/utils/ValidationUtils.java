package com.ml.simios.utils;

import com.ml.simios.controller.representation.DnaRepresentation;
import com.ml.simios.exception.BadRequestException;

import java.util.Arrays;
import java.util.regex.Pattern;

public class ValidationUtils {
    public static void validateRequest(DnaRepresentation request){
        var dna = Arrays.asList(request.getDna());
        var dnaSize = dna.size();

        if(dnaSize < 4){
            throw new BadRequestException("O número de itens/linhas deve ser maior ou igual a quatro.");
        }

        var regex = Pattern.compile("(?!A|T|G|C).");
        var isDnaInvalid = dna.stream()
                .anyMatch(sequence -> regex.matcher(sequence).find() ||
                        sequence.length() != dnaSize);

        if(isDnaInvalid) {
            throw new BadRequestException(
                    "Todos os caractéres devem ser iguais a A, T, G ou C e o número de colunas deve ser igual ao número de linhas.");
        }
    }
}
