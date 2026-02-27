package UtilityClasses;




public enum LengthUnit {

    FEET(1.0),
    INCH(1.0 / 12.0);

    private final double conversionFactorToFeet;

    LengthUnit(double factor) {
        this.conversionFactorToFeet = factor;
    }

    public double toFeet(double value) {
        return value * conversionFactorToFeet;
    }
}