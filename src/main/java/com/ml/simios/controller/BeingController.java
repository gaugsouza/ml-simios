package com.ml.simios.controller;

import com.ml.simios.controller.representation.BeingRepresentation;
import com.ml.simios.service.BeingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BeingController {
    @Autowired
    private BeingService beingService;

    @PostMapping("/simian")
    public HttpStatus checkSimian(@RequestBody BeingRepresentation request){
        var response = beingService.checkSimian(request);

        return response.getIsSimian() ? HttpStatus.OK : HttpStatus.FORBIDDEN;
    }
}
