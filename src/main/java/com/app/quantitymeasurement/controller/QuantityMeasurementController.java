package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.model.QuantityInputDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quantities")
@Tag(name = "Quantity Measurements", description = "REST API for quantity measurement operations")
public class QuantityMeasurementController {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementController.class);

    @Autowired
    private IQuantityMeasurementService service;

    // ==================== POST Endpoints ====================

    @PostMapping("/compare")
    @Operation(summary = "Compare two quantities", description = "Compares two quantities and returns whether they are equal")
    public ResponseEntity<QuantityMeasurementDTO> compareQuantities(
            @Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /compare - Comparing quantities");
        QuantityMeasurementDTO result = service.compareQuantities(
                input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert a quantity", description = "Converts a quantity from one unit to another within the same measurement type")
    public ResponseEntity<QuantityMeasurementDTO> convertQuantity(
            @Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /convert - Converting quantity");
        QuantityMeasurementDTO result = service.convertQuantity(
                input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add")
    @Operation(summary = "Add two quantities", description = "Adds two quantities and returns the result in the first quantity's unit")
    public ResponseEntity<QuantityMeasurementDTO> addQuantities(
            @Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /add - Adding quantities");
        QuantityMeasurementDTO result = service.addQuantities(
                input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/subtract")
    @Operation(summary = "Subtract two quantities", description = "Subtracts the second quantity from the first and returns the result")
    public ResponseEntity<QuantityMeasurementDTO> subtractQuantities(
            @Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /subtract - Subtracting quantities");
        QuantityMeasurementDTO result = service.subtractQuantities(
                input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/divide")
    @Operation(summary = "Divide two quantities", description = "Divides the first quantity by the second and returns the ratio")
    public ResponseEntity<QuantityMeasurementDTO> divideQuantities(
            @Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /divide - Dividing quantities");
        QuantityMeasurementDTO result = service.divideQuantities(
                input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    // ==================== GET Endpoints ====================

    @GetMapping("/history/operation/{operation}")
    @Operation(summary = "Get operation history", description = "Retrieves all measurement records for a specific operation type")
    public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistory(
            @PathVariable String operation) {
        logger.info("GET /history/operation/{}", operation);
        List<QuantityMeasurementDTO> history = service.getOperationHistory(operation);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/history/type/{measurementType}")
    @Operation(summary = "Get measurements by type", description = "Retrieves all measurement records for a specific measurement type")
    public ResponseEntity<List<QuantityMeasurementDTO>> getMeasurementsByType(
            @PathVariable String measurementType) {
        logger.info("GET /history/type/{}", measurementType);
        List<QuantityMeasurementDTO> measurements = service.getMeasurementsByType(measurementType);
        return ResponseEntity.ok(measurements);
    }

    @GetMapping("/count/{operation}")
    @Operation(summary = "Get operation count", description = "Returns the count of successful operations for a specific operation type")
    public ResponseEntity<Long> getOperationCount(
            @PathVariable String operation) {
        logger.info("GET /count/{}", operation);
        long count = service.getOperationCount(operation);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/history/errored")
    @Operation(summary = "Get error history", description = "Retrieves all measurement records that resulted in errors")
    public ResponseEntity<List<QuantityMeasurementDTO>> getErrorHistory() {
        logger.info("GET /history/errored");
        List<QuantityMeasurementDTO> errors = service.getErrorHistory();
        return ResponseEntity.ok(errors);
    }
}
