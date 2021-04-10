package com.ml.simios.service;

import com.ml.simios.controller.representation.DnaRepresentation;
import com.ml.simios.controller.representation.StatsRepresentation;
import com.ml.simios.domain.Being;
import com.ml.simios.repository.BeingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

@Service
public class BeingService {
    private final String EMPTY_STRING = "";

    @Autowired
    private BeingRepository beingRepository;

    public Being checkBeing(DnaRepresentation request){
        var being = new Being();

        being.setIsSimian(isSimian(request.getDna()));
        being.setDna(String.join(EMPTY_STRING, request.getDna()));
        /*In case of repetead dna, the API will return the right code but won't save it again*/
        try {
            being = beingRepository.save(being);
        } catch (DataIntegrityViolationException e) {}

        return being;
    }

    public StatsRepresentation getStats(){
        var beings = beingRepository.findAll();
        var stats = new StatsRepresentation();
        var totalBeings = beings.size();
        var countMutantDna = beings.stream()
                .filter(Being::getIsSimian)
                .count();

        stats.setCountMutantDna(countMutantDna);
        stats.setCountHumanDna(totalBeings - countMutantDna);
        stats.setRatio(totalBeings > 0 ? (float) countMutantDna / totalBeings : 0);

        return stats;
    }

    private Boolean isSimian(String[] dna){
        var regex = Pattern.compile("\\b(AAAA|TTTT|GGGG|CCCC)");
        var rowLenght = dna[0].length();

        /*If any item in dna's array has four repeated A, T, G or C the being is a simian*/
        var horizontalMatches = Arrays.stream(dna)
                .anyMatch(row -> regex.matcher(row).find());
        if(horizontalMatches){
            return Boolean.TRUE;
        }

        /*Turns the dna array into a splited version, where each letter has a position*/
        var dnaSequence = new ArrayList<String>();
        Arrays.stream(dna)
                .forEach(row -> dnaSequence.addAll(Arrays.asList(row.split(""))));

        var dnaSequenceLength = dnaSequence.size();

        /*For to iterate over rows*/
        for(var i = 0; i < rowLenght; i++){
            var column = EMPTY_STRING;
            var upperDiagonal = EMPTY_STRING;
            var lowerDiagonal = EMPTY_STRING;
            var inverseUpperDiagonal = EMPTY_STRING;
            var inverseLowerDiagonal = EMPTY_STRING;

            /*Calculations to find the position of every letter in the "matrix"*/
            var upperDiagonalIndex = i;
            var lowerDiagonalIndex = rowLenght * (i + 1);
            var inverseUpperDiagonalIndex = rowLenght - (i + 1);
            var inverseLowerDiagonalIndex = ((rowLenght * 2) - 1) + (rowLenght * i);

            var upperDiagonalChecked = Boolean.FALSE;
            var inverseUpperDiagonalChecked = Boolean.FALSE;

            /*Inner for to "iterate over columns"*/
            for(var j = i; j < dnaSequenceLength; j += rowLenght) {
                column = column.concat(dnaSequence.get(j));

                /*Checks if the position in the array is valid and then goes to the next position*/
                if (isValidDiagonalIndex(rowLenght, i, j, upperDiagonalIndex) && ! upperDiagonalChecked) {
                    upperDiagonal = upperDiagonal.concat(dnaSequence.get(upperDiagonalIndex));
                    upperDiagonalChecked = (upperDiagonalIndex + 1) % rowLenght == 0;
                    upperDiagonalIndex += rowLenght + 1;
                }

                /*Checks if the position in the array is valid and then goes to the next position*/
                if(isValidDiagonalIndex(rowLenght, i, j, lowerDiagonalIndex)){
                    lowerDiagonal = lowerDiagonal.concat(dnaSequence.get(lowerDiagonalIndex));
                    lowerDiagonalIndex += rowLenght + 1;
                }

                /*Checks if the position in the array is valid and then goes to the next position*/
                if (isValidDiagonalIndex(rowLenght, i, j, inverseUpperDiagonalIndex) && ! inverseUpperDiagonalChecked) {
                    inverseUpperDiagonal = inverseUpperDiagonal.concat(dnaSequence.get(inverseUpperDiagonalIndex));
                    inverseUpperDiagonalChecked = inverseUpperDiagonalIndex % rowLenght == 0;
                    inverseUpperDiagonalIndex += rowLenght - 1;
                }

                /*Checks if the position in the array is valid and then goes to the next position*/
                if (isValidDiagonalIndex(rowLenght, i, j, inverseLowerDiagonalIndex)) {
                    inverseLowerDiagonal = inverseLowerDiagonal.concat(dnaSequence.get(inverseLowerDiagonalIndex));
                    inverseLowerDiagonalIndex += rowLenght - 1;
                }
            }

            /*Check every row produced by the inner for*/
            return checkDna(regex, column) ||
                    checkDna(regex, upperDiagonal) ||
                    checkDna(regex, lowerDiagonal) ||
                    checkDna(regex, inverseUpperDiagonal) ||
                    checkDna(regex, inverseLowerDiagonal);
        }

        return Boolean.FALSE;
    }

    /*Checks if the position is valid in the array, only for diagonal positions*/
    private Boolean isValidDiagonalIndex(Integer rowLenght, Integer outerOuterIndex, Integer outerIndex, Integer innerIndex){
        return (outerIndex + rowLenght) - outerOuterIndex > innerIndex;
    }

    /*Check if the DNA matches with the pattern*/
    private Boolean checkDna(Pattern regex, String sequence){
        return regex.matcher(sequence).find();
    }
}
