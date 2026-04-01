package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;

import java.util.List;

public interface IQuantityMeasurementService {

    QuantityMeasurementDTO compareQuantities(QuantityDTO thisDTO, QuantityDTO thatDTO);

    QuantityMeasurementDTO convertQuantity(QuantityDTO thisDTO, QuantityDTO thatDTO);

    QuantityMeasurementDTO addQuantities(QuantityDTO thisDTO, QuantityDTO thatDTO);

    QuantityMeasurementDTO subtractQuantities(QuantityDTO thisDTO, QuantityDTO thatDTO);

    QuantityMeasurementDTO divideQuantities(QuantityDTO thisDTO, QuantityDTO thatDTO);

    List<QuantityMeasurementDTO> getOperationHistory(String operation);

    List<QuantityMeasurementDTO> getMeasurementsByType(String measurementType);

    long getOperationCount(String operation);

    List<QuantityMeasurementDTO> getErrorHistory();
}
