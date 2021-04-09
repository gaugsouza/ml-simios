package com.ml.simios.controller;

import com.ml.simios.controller.representation.DnaRepresentation;
import com.ml.simios.controller.representation.StatsRepresentation;
import com.ml.simios.service.BeingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.ml.simios.utils.ValidationUtils.validateRequest;

@RestController
public class BeingController {
    @Autowired
    private BeingService beingService;
    private ModelMapper modelMapper = new ModelMapper();

    @PostMapping("/simian")
    public HttpStatus checkBeing(@Valid @RequestBody DnaRepresentation request){
        validateRequest(request);

        var response = beingService.checkBeing(request);

        return response.getIsSimian() ? HttpStatus.OK : HttpStatus.FORBIDDEN;
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsRepresentation> getStats(){
        return ResponseEntity.ok(beingService.getStats());
    }
}
