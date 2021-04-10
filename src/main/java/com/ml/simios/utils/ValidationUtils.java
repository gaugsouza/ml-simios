package com.ml.simios.utils;

import com.ml.simios.controller.representation.DnaRepresentation;
import com.ml.simios.exception.BadRequestException;

import java.util.Arrays;
import java.util.regex.Pattern;

public class ValidationUtils {
    public static void validateRequest(DnaRepresentation request){
        var dna = Arrays.asList(request.getDna());
        var dnaSize = dna.size();
        /*If the row contains less than 4 items, means that the square is at maximum a 3x3*/
        if(dnaSize < 4){
            throw new BadRequestException("The number of rows/items has to be greater than four.");
        }

        /*Checks for possible rectangles and letters that doesn't fit the pattern A T G C*/
        var regex = Pattern.compile("(?!A|T|G|C).");
        var isDnaInvalid = dna.stream()
                .anyMatch(sequence -> regex.matcher(sequence).find() ||
                        sequence.length() != dnaSize);

        if(isDnaInvalid) {
            throw new BadRequestException(
                    "Each char has to be like A,T,G or C and the number of columns and rows has to be the same.");
        }
    }
}
