package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.exception.GlobalExceptionHandler;
import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityInputDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class QuantityMeasurementControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private IQuantityMeasurementService service;

    @InjectMocks
    private QuantityMeasurementController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // ==================== Helper Methods ====================

    private QuantityInputDTO createLengthInput(double val1, String unit1, double val2, String unit2) {
        QuantityDTO thisDTO = new QuantityDTO(val1, unit1, "LengthUnit");
        QuantityDTO thatDTO = new QuantityDTO(val2, unit2, "LengthUnit");
        return new QuantityInputDTO(thisDTO, thatDTO);
    }

    private QuantityMeasurementDTO createCompareResult(boolean equal) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisValue(1.0);
        dto.setThisUnit("FEET");
        dto.setThisMeasurementType("LengthUnit");
        dto.setThatValue(12.0);
        dto.setThatUnit("INCH");
        dto.setThatMeasurementType("LengthUnit");
        dto.setOperation("compare");
        dto.setResultString(String.valueOf(equal));
        dto.setError(false);
        return dto;
    }

    private QuantityMeasurementDTO createAddResult(double result) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisValue(1.0);
        dto.setThisUnit("FEET");
        dto.setThisMeasurementType("LengthUnit");
        dto.setThatValue(12.0);
        dto.setThatUnit("INCH");
        dto.setThatMeasurementType("LengthUnit");
        dto.setOperation("add");
        dto.setResultValue(result);
        dto.setResultUnit("FEET");
        dto.setResultMeasurementType("LengthUnit");
        dto.setError(false);
        return dto;
    }

    // ==================== Compare Tests ====================

    @Test
    void testCompareQuantities_Equal() throws Exception {
        QuantityInputDTO input = createLengthInput(1.0, "FEET", 12.0, "INCH");
        QuantityMeasurementDTO expected = createCompareResult(true);

        Mockito.when(service.compareQuantities(any(), any())).thenReturn(expected);

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.operation").value("compare"))
                .andExpect(jsonPath("$.resultString").value("true"))
                .andExpect(jsonPath("$.error").value(false));
    }

    @Test
    void testCompareQuantities_NotEqual() throws Exception {
        QuantityInputDTO input = createLengthInput(1.0, "FEET", 5.0, "INCH");
        QuantityMeasurementDTO expected = createCompareResult(false);

        Mockito.when(service.compareQuantities(any(), any())).thenReturn(expected);

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultString").value("false"));
    }

    // ==================== Convert Tests ====================

    @Test
    void testConvertQuantity() throws Exception {
        QuantityInputDTO input = createLengthInput(1.0, "FEET", 0.0, "INCH");
        QuantityMeasurementDTO expected = new QuantityMeasurementDTO();
        expected.setOperation("convert");
        expected.setResultValue(12.0);
        expected.setError(false);

        Mockito.when(service.convertQuantity(any(), any())).thenReturn(expected);

        mockMvc.perform(post("/api/v1/quantities/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("convert"))
                .andExpect(jsonPath("$.resultValue").value(12.0));
    }

    // ==================== Add Tests ====================

    @Test
    void testAddQuantities() throws Exception {
        QuantityInputDTO input = createLengthInput(1.0, "FEET", 12.0, "INCH");
        QuantityMeasurementDTO expected = createAddResult(2.0);

        Mockito.when(service.addQuantities(any(), any())).thenReturn(expected);

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("add"))
                .andExpect(jsonPath("$.resultValue").value(2.0))
                .andExpect(jsonPath("$.resultUnit").value("FEET"));
    }

    // ==================== Subtract Tests ====================

    @Test
    void testSubtractQuantities() throws Exception {
        QuantityInputDTO input = createLengthInput(2.0, "FEET", 12.0, "INCH");
        QuantityMeasurementDTO expected = new QuantityMeasurementDTO();
        expected.setOperation("subtract");
        expected.setResultValue(1.0);
        expected.setResultUnit("FEET");
        expected.setError(false);

        Mockito.when(service.subtractQuantities(any(), any())).thenReturn(expected);

        mockMvc.perform(post("/api/v1/quantities/subtract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("subtract"))
                .andExpect(jsonPath("$.resultValue").value(1.0));
    }

    // ==================== Divide Tests ====================

    @Test
    void testDivideQuantities() throws Exception {
        QuantityInputDTO input = createLengthInput(1.0, "FEET", 1.0, "INCH");
        QuantityMeasurementDTO expected = new QuantityMeasurementDTO();
        expected.setOperation("divide");
        expected.setResultValue(12.0);
        expected.setError(false);

        Mockito.when(service.divideQuantities(any(), any())).thenReturn(expected);

        mockMvc.perform(post("/api/v1/quantities/divide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("divide"))
                .andExpect(jsonPath("$.resultValue").value(12.0));
    }

    // ==================== History Tests ====================

    @Test
    void testGetOperationHistory() throws Exception {
        QuantityMeasurementDTO dto = createCompareResult(true);
        List<QuantityMeasurementDTO> history = Arrays.asList(dto);

        Mockito.when(service.getOperationHistory("COMPARE")).thenReturn(history);

        mockMvc.perform(get("/api/v1/quantities/history/operation/COMPARE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].operation").value("compare"));
    }

    @Test
    void testGetMeasurementsByType() throws Exception {
        QuantityMeasurementDTO dto = createCompareResult(true);
        List<QuantityMeasurementDTO> measurements = Arrays.asList(dto);

        Mockito.when(service.getMeasurementsByType("LengthUnit")).thenReturn(measurements);

        mockMvc.perform(get("/api/v1/quantities/history/type/LengthUnit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].thisMeasurementType").value("LengthUnit"));
    }

    @Test
    void testGetOperationCount() throws Exception {
        Mockito.when(service.getOperationCount("COMPARE")).thenReturn(5L);

        mockMvc.perform(get("/api/v1/quantities/count/COMPARE"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void testGetErrorHistory() throws Exception {
        Mockito.when(service.getErrorHistory()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/quantities/history/errored"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ==================== Error Handling Tests ====================

    @Test
    void testCompare_ServiceThrowsException_Returns400() throws Exception {
        QuantityInputDTO input = createLengthInput(1.0, "FEET", 12.0, "INCH");

        Mockito.when(service.compareQuantities(any(), any()))
                .thenThrow(new com.app.quantitymeasurement.exception.QuantityMeasurementException(
                        "compare Error: Invalid unit"));

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Quantity Measurement Error"));
    }

    @Test
    void testDivide_ServiceThrowsArithmetic_Returns500() throws Exception {
        QuantityInputDTO input = createLengthInput(1.0, "FEET", 0.0, "INCH");

        Mockito.when(service.divideQuantities(any(), any()))
                .thenThrow(new ArithmeticException("Divide by zero"));

        mockMvc.perform(post("/api/v1/quantities/divide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Divide by zero"));
    }
}
