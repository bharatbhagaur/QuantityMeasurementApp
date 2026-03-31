package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.model.*;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);

    @Autowired
    private QuantityMeasurementRepository repository;

    // ==================== Operation Methods ====================

    @Override
    public QuantityMeasurementDTO compareQuantities(QuantityDTO thisDTO, QuantityDTO thatDTO) {
        QuantityMeasurementEntity entity = buildEntity(thisDTO, thatDTO, "compare");
        try {
            IMeasurable thisUnit = resolveUnit(thisDTO.getMeasurementType(), thisDTO.getUnit());
            IMeasurable thatUnit = resolveUnit(thatDTO.getMeasurementType(), thatDTO.getUnit());
            Quantity<IMeasurable> q1 = new Quantity<>(thisDTO.getValue(), thisUnit);
            Quantity<IMeasurable> q2 = new Quantity<>(thatDTO.getValue(), thatUnit);
            boolean result = q1.equals(q2);
            entity.setResultString(String.valueOf(result));
            entity.setError(false);
            logger.info("Compare {} and {} -> {}", q1, q2, result);
        } catch (Exception e) {
            handleError(entity, "compare", e);
        }
        repository.save(entity);
        return QuantityMeasurementDTO.fromEntity(entity);
    }

    @Override
    public QuantityMeasurementDTO convertQuantity(QuantityDTO thisDTO, QuantityDTO thatDTO) {
        QuantityMeasurementEntity entity = buildEntity(thisDTO, thatDTO, "convert");
        try {
            validateSameMeasurementType(thisDTO, thatDTO, "convert");
            IMeasurable thisUnit = resolveUnit(thisDTO.getMeasurementType(), thisDTO.getUnit());
            IMeasurable thatUnit = resolveUnit(thatDTO.getMeasurementType(), thatDTO.getUnit());
            Quantity<IMeasurable> q1 = new Quantity<>(thisDTO.getValue(), thisUnit);
            Quantity<IMeasurable> result = q1.convertTo(thatUnit);
            entity.setResultValue(result.getValue());
            entity.setError(false);
            logger.info("Convert {} to {} -> {}", q1, thatDTO.getUnit(), result.getValue());
        } catch (Exception e) {
            handleError(entity, "convert", e);
        }
        repository.save(entity);
        return QuantityMeasurementDTO.fromEntity(entity);
    }

    @Override
    public QuantityMeasurementDTO addQuantities(QuantityDTO thisDTO, QuantityDTO thatDTO) {
        QuantityMeasurementEntity entity = buildEntity(thisDTO, thatDTO, "add");
        try {
            validateSameMeasurementType(thisDTO, thatDTO, "add");
            IMeasurable thisUnit = resolveUnit(thisDTO.getMeasurementType(), thisDTO.getUnit());
            IMeasurable thatUnit = resolveUnit(thatDTO.getMeasurementType(), thatDTO.getUnit());
            Quantity<IMeasurable> q1 = new Quantity<>(thisDTO.getValue(), thisUnit);
            Quantity<IMeasurable> q2 = new Quantity<>(thatDTO.getValue(), thatUnit);
            Quantity<IMeasurable> result = q1.add(q2, thisUnit);
            entity.setResultValue(result.getValue());
            entity.setResultUnit(thisUnit.getUnitName());
            entity.setResultMeasurementType(thisDTO.getMeasurementType());
            entity.setError(false);
            logger.info("Add {} and {} -> {}", q1, q2, result);
        } catch (Exception e) {
            handleError(entity, "add", e);
        }
        repository.save(entity);
        return QuantityMeasurementDTO.fromEntity(entity);
    }

    @Override
    public QuantityMeasurementDTO subtractQuantities(QuantityDTO thisDTO, QuantityDTO thatDTO) {
        QuantityMeasurementEntity entity = buildEntity(thisDTO, thatDTO, "subtract");
        try {
            validateSameMeasurementType(thisDTO, thatDTO, "subtract");
            IMeasurable thisUnit = resolveUnit(thisDTO.getMeasurementType(), thisDTO.getUnit());
            IMeasurable thatUnit = resolveUnit(thatDTO.getMeasurementType(), thatDTO.getUnit());
            Quantity<IMeasurable> q1 = new Quantity<>(thisDTO.getValue(), thisUnit);
            Quantity<IMeasurable> q2Negated = new Quantity<>(-thatDTO.getValue(), thatUnit);
            Quantity<IMeasurable> result = q1.add(q2Negated, thisUnit);
            entity.setResultValue(result.getValue());
            entity.setResultUnit(thisUnit.getUnitName());
            entity.setResultMeasurementType(thisDTO.getMeasurementType());
            entity.setError(false);
            logger.info("Subtract {} from {} -> {}", thatDTO.getValue(), q1, result);
        } catch (Exception e) {
            handleError(entity, "subtract", e);
        }
        repository.save(entity);
        return QuantityMeasurementDTO.fromEntity(entity);
    }

    @Override
    public QuantityMeasurementDTO divideQuantities(QuantityDTO thisDTO, QuantityDTO thatDTO) {
        QuantityMeasurementEntity entity = buildEntity(thisDTO, thatDTO, "divide");
        try {
            validateSameMeasurementType(thisDTO, thatDTO, "divide");
            IMeasurable thisUnit = resolveUnit(thisDTO.getMeasurementType(), thisDTO.getUnit());
            IMeasurable thatUnit = resolveUnit(thatDTO.getMeasurementType(), thatDTO.getUnit());
            double thisBase = thisUnit.convertToBaseUnit(thisDTO.getValue());
            double thatBase = thatUnit.convertToBaseUnit(thatDTO.getValue());
            if (Math.abs(thatBase) < 1e-9) {
                throw new ArithmeticException("Divide by zero");
            }
            double resultValue = thisBase / thatBase;
            entity.setResultValue(resultValue);
            entity.setError(false);
            logger.info("Divide {} by {} -> {}", thisDTO.getValue(), thatDTO.getValue(), resultValue);
        } catch (Exception e) {
            handleError(entity, "divide", e);
        }
        repository.save(entity);
        return QuantityMeasurementDTO.fromEntity(entity);
    }

    // ==================== History & Count Methods ====================

    @Override
    public List<QuantityMeasurementDTO> getOperationHistory(String operation) {
        List<QuantityMeasurementEntity> entities = repository.findByOperation(operation.toLowerCase());
        return QuantityMeasurementDTO.fromEntityList(entities);
    }

    @Override
    public List<QuantityMeasurementDTO> getMeasurementsByType(String measurementType) {
        List<QuantityMeasurementEntity> entities = repository.findByThisMeasurementType(measurementType);
        return QuantityMeasurementDTO.fromEntityList(entities);
    }

    @Override
    public long getOperationCount(String operation) {
        return repository.countByOperationAndIsErrorFalse(operation.toLowerCase());
    }

    @Override
    public List<QuantityMeasurementDTO> getErrorHistory() {
        List<QuantityMeasurementEntity> entities = repository.findByIsErrorTrue();
        return QuantityMeasurementDTO.fromEntityList(entities);
    }

    // ==================== Helper Methods ====================

    private QuantityMeasurementEntity buildEntity(QuantityDTO thisDTO, QuantityDTO thatDTO, String operation) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(thisDTO.getValue());
        entity.setThisUnit(thisDTO.getUnit());
        entity.setThisMeasurementType(thisDTO.getMeasurementType());
        entity.setThatValue(thatDTO.getValue());
        entity.setThatUnit(thatDTO.getUnit());
        entity.setThatMeasurementType(thatDTO.getMeasurementType());
        entity.setOperation(operation);
        return entity;
    }

    private void handleError(QuantityMeasurementEntity entity, String operation, Exception e) {
        entity.setError(true);
        entity.setErrorMessage(e.getMessage());
        repository.save(entity);
        logger.error("{} Error: {}", operation, e.getMessage());
        if (e instanceof ArithmeticException) {
            throw (ArithmeticException) e;
        }
        throw new QuantityMeasurementException(operation + " Error: " + e.getMessage(), e);
    }

    private void validateSameMeasurementType(QuantityDTO thisDTO, QuantityDTO thatDTO, String operation) {
        if (!thisDTO.getMeasurementType().equals(thatDTO.getMeasurementType())) {
            throw new QuantityMeasurementException(
                    "Cannot perform arithmetic between different measurement categories: "
                            + thisDTO.getMeasurementType() + " and " + thatDTO.getMeasurementType());
        }
    }

    private IMeasurable resolveUnit(String measurementType, String unitName) {
        try {
            switch (measurementType) {
                case "LengthUnit":
                    return LengthUnit.valueOf(unitName);
                case "VolumeUnit":
                    return VolumeUnit.valueOf(unitName);
                case "WeightUnit":
                    return WeightUnit.valueOf(unitName);
                default:
                    throw new QuantityMeasurementException("Invalid measurement type: " + measurementType);
            }
        } catch (IllegalArgumentException e) {
            throw new QuantityMeasurementException(
                    "Invalid unit '" + unitName + "' for measurement type: " + measurementType);
        }
    }
}
