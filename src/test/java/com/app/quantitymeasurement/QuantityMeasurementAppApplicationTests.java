package com.app.quantitymeasurement;

import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityInputDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuantityMeasurementAppApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/api/v1/quantities";
    }

    private QuantityInputDTO createInput(double v1, String u1, String mt1,
                                          double v2, String u2, String mt2) {
        QuantityDTO thisDTO = new QuantityDTO(v1, u1, mt1);
        QuantityDTO thatDTO = new QuantityDTO(v2, u2, mt2);
        return new QuantityInputDTO(thisDTO, thatDTO);
    }

    // ==================== Context Test ====================
    
    @org.junit.jupiter.api.BeforeEach
    void setUpAuth() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("user", "password")
                .postForEntity("http://localhost:" + port + "/api/auth/login", null, String.class);
        String token = response.getBody();
        restTemplate.getRestTemplate().getInterceptors().clear();
        restTemplate.getRestTemplate().getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + token);
            return execution.execute(request, body);
        });
    }

    @Test
    void testSpringBootApplicationStarts() {
        // If this test runs, the context loaded successfully
        assertNotNull(restTemplate);
    }

    // ==================== Compare Tests ====================

    @Test
    void testCompare_FeetEqualsInches() {
        QuantityInputDTO input = createInput(1.0, "FEET", "LengthUnit",
                12.0, "INCH", "LengthUnit");

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/compare", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("compare", response.getBody().getOperation());
        assertEquals("true", response.getBody().getResultString());
        assertFalse(response.getBody().isError());
    }

    @Test
    void testCompare_FeetNotEqualsInches() {
        QuantityInputDTO input = createInput(1.0, "FEET", "LengthUnit",
                5.0, "INCH", "LengthUnit");

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/compare", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("false", response.getBody().getResultString());
    }

    @Test
    void testCompare_KgEqualsGram() {
        QuantityInputDTO input = createInput(1.0, "KILOGRAM", "WeightUnit",
                1000.0, "GRAM", "WeightUnit");

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/compare", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("true", response.getBody().getResultString());
    }

    // ==================== Convert Tests ====================

    @Test
    void testConvert_FeetToInches() {
        QuantityInputDTO input = createInput(1.0, "FEET", "LengthUnit",
                0.0, "INCH", "LengthUnit");

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/convert", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("convert", response.getBody().getOperation());
        assertEquals(12.0, response.getBody().getResultValue(), 1e-6);
    }

    @Test
    void testConvert_KgToGram() {
        QuantityInputDTO input = createInput(1.0, "KILOGRAM", "WeightUnit",
                0.0, "GRAM", "WeightUnit");

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/convert", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1000.0, response.getBody().getResultValue(), 1e-4);
    }

    // ==================== Add Tests ====================

    @Test
    void testAdd_FeetPlusInches() {
        QuantityInputDTO input = createInput(1.0, "FEET", "LengthUnit",
                12.0, "INCH", "LengthUnit");

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/add", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("add", response.getBody().getOperation());
        assertEquals(2.0, response.getBody().getResultValue(), 1e-6);
        assertEquals("FEET", response.getBody().getResultUnit());
        assertEquals("LengthUnit", response.getBody().getResultMeasurementType());
    }

    @Test
    void testAdd_KgPlusGram() {
        QuantityInputDTO input = createInput(1.0, "KILOGRAM", "WeightUnit",
                1000.0, "GRAM", "WeightUnit");

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/add", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2.0, response.getBody().getResultValue(), 1e-4);
    }

    // ==================== Subtract Tests ====================

    @Test
    void testSubtract_FeetMinusInches() {
        QuantityInputDTO input = createInput(2.0, "FEET", "LengthUnit",
                12.0, "INCH", "LengthUnit");

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/subtract", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("subtract", response.getBody().getOperation());
        assertEquals(1.0, response.getBody().getResultValue(), 1e-6);
    }

    // ==================== History Tests ====================

    @Test
    void testGetOperationHistory_Compare() {
        // First perform a compare operation
        QuantityInputDTO input = createInput(1.0, "FEET", "LengthUnit",
                12.0, "INCH", "LengthUnit");
        restTemplate.postForEntity(baseUrl() + "/compare", input, QuantityMeasurementDTO.class);

        // Then get history
        ResponseEntity<QuantityMeasurementDTO[]> response = restTemplate.getForEntity(
                baseUrl() + "/history/operation/compare", QuantityMeasurementDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    void testGetMeasurementsByType() {
        // First perform an operation
        QuantityInputDTO input = createInput(1.0, "FEET", "LengthUnit",
                12.0, "INCH", "LengthUnit");
        restTemplate.postForEntity(baseUrl() + "/compare", input, QuantityMeasurementDTO.class);

        // Then get by type
        ResponseEntity<QuantityMeasurementDTO[]> response = restTemplate.getForEntity(
                baseUrl() + "/history/type/LengthUnit", QuantityMeasurementDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    void testGetOperationCount() {
        // Perform an operation first
        QuantityInputDTO input = createInput(1.0, "FEET", "LengthUnit",
                12.0, "INCH", "LengthUnit");
        restTemplate.postForEntity(baseUrl() + "/add", input, QuantityMeasurementDTO.class);

        // Then get count
        ResponseEntity<Long> response = restTemplate.getForEntity(
                baseUrl() + "/count/add", Long.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() >= 1);
    }

    @Test
    void testGetErrorHistory() {
        ResponseEntity<QuantityMeasurementDTO[]> response = restTemplate.getForEntity(
                baseUrl() + "/history/errored", QuantityMeasurementDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // ==================== Error Handling Tests ====================

    @Test
    void testAdd_IncompatibleTypes_Returns400() {
        QuantityInputDTO input = createInput(1.0, "FEET", "LengthUnit",
                1.0, "KILOGRAM", "WeightUnit");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl() + "/add", input, Map.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get("message").toString().contains("different measurement categories"));
    }

    @Test
    void testDivide_ByZero_Returns500() {
        QuantityInputDTO input = createInput(1.0, "FEET", "LengthUnit",
                0.0, "INCH", "LengthUnit");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl() + "/divide", input, Map.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get("message").toString().contains("Divide by zero"));
    }

    @Test
    void testInvalidUnit_Returns400() {
        QuantityInputDTO input = createInput(1.0, "FOOT", "LengthUnit",
                12.0, "INCHE", "LengthUnit");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl() + "/compare", input, Map.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // ==================== Actuator Tests ====================

    @Test
    void testActuatorHealth() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/actuator/health", Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UP", response.getBody().get("status"));
    }

    // ==================== Volume Tests ====================

    @Test
    void testCompare_LitreEqualsMillilitre() {
        QuantityInputDTO input = createInput(1.0, "LITRE", "VolumeUnit",
                1000.0, "MILLILITRE", "VolumeUnit");

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/compare", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("true", response.getBody().getResultString());
    }

    @Test
    void testAdd_LitrePlusMillilitre() {
        QuantityInputDTO input = createInput(1.0, "LITRE", "VolumeUnit",
                1000.0, "MILLILITRE", "VolumeUnit");

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
                baseUrl() + "/add", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2.0, response.getBody().getResultValue(), 1e-4);
    }
}
