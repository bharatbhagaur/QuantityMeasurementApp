package UtilityClasses;




public class QuantityMeasurementApp {

    public static void demonstrateLengthConversion(
            double value,
            LengthUnit from,
            LengthUnit to) {

        double result =
                QuantityLength.convert(value, from, to);

        System.out.println("Convert " + value + " " + from +
                " to " + to + ":" + result + " " + to);
    }

    public static void demonstrateLengthConversion(
            QuantityLength length,
            LengthUnit target) {

        QuantityLength converted =
                length.convertTo(target);

        System.out.println("Convert " + length +
                " to " + target + ":" + converted);
    }

    public static void demonstrateLengthEquality(
            QuantityLength q1,
            QuantityLength q2) {

        System.out.println("Compare " + q1 +
                " and " + q2 +
                " Equal: " + q1.equals(q2));
    }

    public static void main(String[] args) {

        demonstrateLengthConversion(1.0,
                LengthUnit.FEET,
                LengthUnit.INCH);

        demonstrateLengthConversion(3.0,
                LengthUnit.YARDS,
                LengthUnit.FEET);

        demonstrateLengthConversion(36.0,
                LengthUnit.INCH,
                LengthUnit.YARDS);

        demonstrateLengthConversion(2.54,
                LengthUnit.CENTIMETERS,
                LengthUnit.INCH);


        QuantityLength length =
                new QuantityLength(1.0, LengthUnit.YARDS);

        demonstrateLengthConversion(length,
                LengthUnit.INCH);


        QuantityLength q1 =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength q2 =
                new QuantityLength(12.0, LengthUnit.INCH);

        demonstrateLengthEquality(q1, q2);
    }
}