package UtilityClasses;

import UtilityClasses.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuantityTest {

    private static final double EPS = 1e-6;

    // ---------------- LENGTH TESTS ----------------

    @Test
    void testLengthEquality() {
        Quantity<LengthUnit> f =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> i =
                new Quantity<>(12.0, LengthUnit.INCH);

        assertEquals(f, i);
    }

    @Test
    void testLengthConversion() {
        Quantity<LengthUnit> f =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> result =
                f.convertTo(LengthUnit.INCH);

        assertEquals(12.0, result.getValue(), EPS);
    }

    @Test
    void testLengthAddition() {
        Quantity<LengthUnit> f =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> i =
                new Quantity<>(12.0, LengthUnit.INCH);

        Quantity<LengthUnit> result =
                f.add(i, LengthUnit.FEET);

        assertEquals(2.0, result.getValue(), EPS);
    }

    // ---------------- WEIGHT TESTS ----------------

    @Test
    void testWeightEquality() {
        Quantity<WeightUnit> kg =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> g =
                new Quantity<>(1000.0, WeightUnit.GRAM);

        assertEquals(kg, g);
    }

    @Test
    void testWeightConversion() {
        Quantity<WeightUnit> kg =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> result =
                kg.convertTo(WeightUnit.GRAM);

        assertEquals(1000.0, result.getValue(), EPS);
    }

    @Test
    void testWeightAddition() {
        Quantity<WeightUnit> kg =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> g =
                new Quantity<>(1000.0, WeightUnit.GRAM);

        Quantity<WeightUnit> result =
                kg.add(g, WeightUnit.KILOGRAM);

        assertEquals(2.0, result.getValue(), EPS);
    }

    // ---------------- CROSS CATEGORY SAFETY ----------------

    @Test
    void testCrossCategoryComparison() {

        Quantity<LengthUnit> length =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<WeightUnit> weight =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        assertNotEquals(length, weight);
    }

    // ---------------- VALIDATION ----------------

    @Test
    void testNullUnitConstructor() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(1.0, null));
    }

    @Test
    void testInvalidValueConstructor() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(Double.NaN, LengthUnit.FEET));
    }
}