package com.ml.simios.service;

import com.ml.simios.controller.representation.BeingRepresentation;
import com.ml.simios.domain.Being;
import com.ml.simios.repository.BeingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeingService {
    @Autowired
    private BeingRepository beingRepository;

    public Being checkSimian(BeingRepresentation request){
        var being = new Being();

        being.setIsSimian(isSimian(request.getDna()));
        being.setDna(request);

        return beingRepository.save(being);
    }

    private Boolean isSimian(String[] dna){
        return Boolean.TRUE;
    }
}
