package com.ml.simios.integration;

import com.ml.simios.controller.BeingController;
import com.ml.simios.controller.representation.DnaRepresentation;
import com.ml.simios.controller.representation.StatsRepresentation;
import com.ml.simios.domain.Being;
import com.ml.simios.domain.Dna;
import com.ml.simios.repository.BeingRepository;
import com.ml.simios.service.BeingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SimiosIntegrationTest {
    @Autowired
    private BeingController beingController;
    @LocalServerPort
    private int port;

    private RestTemplate restTemplate = new RestTemplate();
    private String urlSimian;
    private String urlStats;

    private Being being = new Being();
    private Dna dna = new Dna();
    private List<Being> beings = new ArrayList<>();
    private StatsRepresentation statsRepresentation = new StatsRepresentation();

    @BeforeEach
    public void init(){
        urlSimian = "http://localhost:" + port + "/simian";
        urlStats = "http://localhost:" + port + "/stats";
    }

    @Test
    public void verify_being_and_assert_as_simian(){
        var dnaRepresentation = new DnaRepresentation();
        dnaRepresentation.setDna(new String[]{"CTGAGA","CTGAGC","CATTGT","CGAGGG","CCCCTA","TCACTG"});

        var responseEntity = restTemplate.postForEntity(urlSimian, dnaRepresentation, null);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void quando_chamar_stats_deve_retornar_com_sucesso(){
        beingController.getStats();
    }
}
