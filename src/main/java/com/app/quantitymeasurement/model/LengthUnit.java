package com.app.quantitymeasurement.model;

public enum LengthUnit implements IMeasurable {

    FEET(1.0),
    INCH(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double conversionFactorToFeet;

    LengthUnit(double conversionFactorToFeet) {
        this.conversionFactorToFeet = conversionFactorToFeet;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactorToFeet;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * conversionFactorToFeet;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactorToFeet;
    }

    @Override
    public String getUnitName() {
        return name();
    }
}
