package com.ml.simios.service;

import com.ml.simios.controller.representation.DnaRepresentation;
import com.ml.simios.controller.representation.StatsRepresentation;
import com.ml.simios.domain.Being;
import com.ml.simios.domain.Dna;
import com.ml.simios.repository.BeingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

@Service
public class BeingService {
    private final String EMPTY_STRING = "";
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private BeingRepository beingRepository;

    public Being checkBeing(DnaRepresentation request){
        var being = new Being();

        being.setIsSimian(isSimian(request.getDna()));
        being.setDna(modelMapper.map(request.getDna(), Dna.class));

        return beingRepository.save(being);
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
        stats.setRatio((float) countMutantDna / totalBeings);

        return stats;
    }

    private Boolean isSimian(String[] dna){
        var regex = Pattern.compile("\\b(AAAA|TTTT|GGGG|CCCC)");
        var rowLenght = dna[0].length();

        var horizontalMatches = Arrays.stream(dna)
                .anyMatch(row -> regex.matcher(row).find());
        if(horizontalMatches){
            return Boolean.TRUE;
        }

        var dnaSequence = new ArrayList<String>();
        Arrays.stream(dna)
                .forEach(row -> dnaSequence.addAll(Arrays.asList(row.split(""))));

        var dnaSequenceLength = dnaSequence.size();

        for(var i = 0; i < rowLenght; i++){
            var column = EMPTY_STRING;
            var upperDiagonal = EMPTY_STRING;
            var lowerDiagonal = EMPTY_STRING;
            var inverseUpperDiagonal = EMPTY_STRING;
            var inverseLowerDiagonal = EMPTY_STRING;

            var upperDiagonalIndex = i;
            var lowerDiagonalIndex = rowLenght * (i + 1);
            var inverseUpperDiagonalIndex = rowLenght - (i + 1);
            var inverseLowerDiagonalIndex = ((rowLenght * 2) - 1) + (rowLenght * i);

            var upperDiagonalChecked = Boolean.FALSE;
            var inverseUpperDiagonalChecked = Boolean.FALSE;

            for(var j = i; j < dnaSequenceLength; j += rowLenght) {
                column = column.concat(dnaSequence.get(j));

                if (isValidDiagonalIndex(rowLenght, i, j, upperDiagonalIndex) && ! upperDiagonalChecked) {
                    upperDiagonal = upperDiagonal.concat(dnaSequence.get(upperDiagonalIndex));
                    upperDiagonalChecked = (upperDiagonalIndex + 1) % rowLenght == 0;
                    upperDiagonalIndex += rowLenght + 1;
                }

                if(isValidDiagonalIndex(rowLenght, i, j, lowerDiagonalIndex)){
                    lowerDiagonal = lowerDiagonal.concat(dnaSequence.get(lowerDiagonalIndex));
                    lowerDiagonalIndex += rowLenght + 1;
                }

                if (isValidDiagonalIndex(rowLenght, i, j, inverseUpperDiagonalIndex) && ! inverseUpperDiagonalChecked) {
                    inverseUpperDiagonal = inverseUpperDiagonal.concat(dnaSequence.get(inverseUpperDiagonalIndex));
                    inverseUpperDiagonalChecked = inverseUpperDiagonalIndex % rowLenght == 0;
                    inverseUpperDiagonalIndex += rowLenght - 1;
                }

                if (isValidDiagonalIndex(rowLenght, i, j, inverseLowerDiagonalIndex)) {
                    inverseLowerDiagonal = inverseLowerDiagonal.concat(dnaSequence.get(inverseLowerDiagonalIndex));
                    inverseLowerDiagonalIndex += rowLenght - 1;
                }
            }

            return checkDna(regex, column) ||
                    checkDna(regex, upperDiagonal) ||
                    checkDna(regex, lowerDiagonal) ||
                    checkDna(regex, inverseUpperDiagonal) ||
                    checkDna(regex, inverseLowerDiagonal);
        }

        return Boolean.FALSE;
    }

    private Boolean isValidDiagonalIndex(Integer rowLenght, Integer outerOuterIndex, Integer outerIndex, Integer innerIndex){
        return (outerIndex + rowLenght) - outerOuterIndex > innerIndex;
    }

    private Boolean checkDna(Pattern regex, String sequence){
        return regex.matcher(sequence).find();
    }
}
