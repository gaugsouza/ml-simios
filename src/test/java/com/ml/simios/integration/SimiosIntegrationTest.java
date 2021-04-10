package com.ml.simios.integration;

import com.ml.simios.controller.BeingController;
import com.ml.simios.controller.representation.DnaRepresentation;
import com.ml.simios.controller.representation.StatsRepresentation;
import com.ml.simios.domain.Being;
import com.ml.simios.domain.Dna;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    private List<Being> beings = new ArrayList<>();
    private StatsRepresentation statsRepresentation = new StatsRepresentation();

    @BeforeEach
    public void init(){
        urlSimian = "http://localhost:" + port + "/simian";
        urlStats = "http://localhost:" + port + "/stats";
    }

    @Test
    @Order(1)
    public void verify_being_and_assert_as_simian_horizontal_match(){
        var dnaRepresentation = new DnaRepresentation();
        dnaRepresentation.setDna(new String[]{"CTGAGA","CTGAGC","CATTGT","CGAGGG","CCCCTA","TCACTG"});

        var response = restTemplate.postForObject(urlSimian, dnaRepresentation, HttpStatus.class);

        assertEquals(HttpStatus.OK, response);
    }

    @Test
    @Order(2)
    public void verify_being_and_assert_as_simian_vertical_match(){
        var dnaRepresentation = new DnaRepresentation();
        dnaRepresentation.setDna(new String[]{"CTGAGA","CTGAGC","CATTGT","CGAGGG","CCCGTA","TCACTG"});

        var response = restTemplate.postForObject(urlSimian, dnaRepresentation, HttpStatus.class);

        assertEquals(HttpStatus.OK, response);
    }

    @Test
    @Order(3)
    public void verify_being_and_assert_as_simian_diagonal_match(){
        var dnaRepresentation = new DnaRepresentation();
        dnaRepresentation.setDna(new String[]{"CTGAGA","CCGAGC","CACTGT","TGACGG","TCCGTA","TCACTG"});

        var response = restTemplate.postForObject(urlSimian, dnaRepresentation, HttpStatus.class);

        assertEquals(HttpStatus.OK, response);
    }

    @Test
    @Order(4)
    public void verify_being_and_assert_as_human(){
        var dnaRepresentation = new DnaRepresentation();
        dnaRepresentation.setDna(new String[]{"AAGAGA","CCGAGC","CACTGT","TGACGG","TCCGTA","TCACTG"});

        var response = restTemplate.postForObject(urlSimian, dnaRepresentation, HttpStatus.class);

        assertEquals(HttpStatus.FORBIDDEN, response);
    }

    @Test
    @Order(5)
    public void get_stats_from_database_success(){
        var responseEntity = restTemplate.getForEntity(urlStats, StatsRepresentation.class);
        var stats = new StatsRepresentation();

        stats.setCountMutantDna(3L);
        stats.setCountHumanDna(1L);
        stats.setRatio(0.75F);

        assertEquals(stats, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(6)
    public void verify_being_dna_with_size_less_than_four(){
        var dnaRepresentation = new DnaRepresentation();
        dnaRepresentation.setDna(new String[]{"CTG","CTG","CAT"});

        assertThrows(HttpClientErrorException.class,
                () -> restTemplate.postForEntity(urlSimian, dnaRepresentation, null));
    }

    @Test
    @Order(7)
    public void verify_being_dna_with_invalid_char(){
        var dnaRepresentation = new DnaRepresentation();
        dnaRepresentation.setDna(new String[]{"CTGA","CTGA","CATT", "CATS"});

        assertThrows(HttpClientErrorException.class,
                () -> restTemplate.postForEntity(urlSimian, dnaRepresentation, null));
    }

    @Test
    @Order(8)
    public void verify_being_dna_with_different_columns_and_rows_size(){
        var dnaRepresentation = new DnaRepresentation();
        dnaRepresentation.setDna(new String[]{"CTGAGA","CTGAGC","CATTGT", "CATTGT"});

        assertThrows(HttpClientErrorException.class,
                () -> restTemplate.postForEntity(urlSimian, dnaRepresentation, null));
    }
}
