package com.app.quantitymeasurement.model;

public enum WeightUnit implements IMeasurable {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double conversionFactorToKilogram;

    WeightUnit(double conversionFactorToKilogram) {
        this.conversionFactorToKilogram = conversionFactorToKilogram;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactorToKilogram;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * conversionFactorToKilogram;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactorToKilogram;
    }

    @Override
    public String getUnitName() {
        return name();
    }
}
