package com.ml.simios.service;

import com.ml.simios.controller.representation.BeingRepresentation;
import com.ml.simios.controller.representation.StatsRepresentation;
import com.ml.simios.domain.Being;
import com.ml.simios.repository.BeingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeingService {
    @Autowired
    private BeingRepository beingRepository;

    public Being checkBeing(BeingRepresentation request){
        var being = new Being();

        being.setIsSimian(isSimian(request.getDna()));
        being.setDna(request);

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
        stats.setRatio((float) (countMutantDna/totalBeings));

        return stats;
    }

    private Boolean isSimian(String[] dna){
        return Boolean.TRUE;
    }
}
