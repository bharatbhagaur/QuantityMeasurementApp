package UtilityClasses;

import UtilityClasses.QuantityWeight;
import UtilityClasses.WeightUnit;
import UtilityClasses.QuantityLength;
import UtilityClasses.LengthUnit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuantityWeightTest {

    private static final double EPS = 1e-4;

    // ---------------- EQUALITY TESTS ----------------

    @Test
    void testEquality_KgToKg() {
        QuantityWeight w1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight w2 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        assertEquals(w1, w2);
    }

    @Test
    void testEquality_KgToGram() {
        QuantityWeight kg = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight g = new QuantityWeight(1000.0, WeightUnit.GRAM);

        assertEquals(kg, g);
    }

    @Test
    void testEquality_KgToPound() {
        QuantityWeight kg = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight lb = new QuantityWeight(2.20462, WeightUnit.POUND);

        assertEquals(kg, lb);
    }

    @Test
    void testEquality_NullComparison() {
        QuantityWeight w = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        assertNotEquals(w, null);
    }

    @Test
    void testEquality_SameReference() {
        QuantityWeight w = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        assertEquals(w, w);
    }

    @Test
    void testEquality_WeightVsLength() {
        QuantityWeight weight = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityLength length = new QuantityLength(1.0, LengthUnit.FEET);

        assertNotEquals(weight, length);
    }

    // ---------------- CONVERSION TESTS ----------------

    @Test
    void testConversion_KgToGram() {
        QuantityWeight kg = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight g = kg.convertTo(WeightUnit.GRAM);

        assertEquals(1000.0, g.getValue(), EPS);
        assertEquals(WeightUnit.GRAM, g.getUnit());
    }

    @Test
    void testConversion_PoundToKg() {
        QuantityWeight lb = new QuantityWeight(2.20462, WeightUnit.POUND);
        QuantityWeight kg = lb.convertTo(WeightUnit.KILOGRAM);

        assertEquals(1.0, kg.getValue(), EPS);
        assertEquals(WeightUnit.KILOGRAM, kg.getUnit());
    }

    @Test
    void testConversion_RoundTrip() {
        QuantityWeight original = new QuantityWeight(1.5, WeightUnit.KILOGRAM);

        QuantityWeight converted =
                original.convertTo(WeightUnit.GRAM)
                        .convertTo(WeightUnit.KILOGRAM);

        assertEquals(original.getValue(), converted.getValue(), EPS);
    }

    @Test
    void testConversion_ZeroValue() {
        QuantityWeight w = new QuantityWeight(0.0, WeightUnit.KILOGRAM);
        QuantityWeight g = w.convertTo(WeightUnit.GRAM);

        assertEquals(0.0, g.getValue(), EPS);
    }

    @Test
    void testConversion_NegativeValue() {
        QuantityWeight w = new QuantityWeight(-1.0, WeightUnit.KILOGRAM);
        QuantityWeight g = w.convertTo(WeightUnit.GRAM);

        assertEquals(-1000.0, g.getValue(), EPS);
    }

    // ---------------- ADDITION (IMPLICIT TARGET) ----------------

    @Test
    void testAddition_SameUnit() {
        QuantityWeight w1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight w2 = new QuantityWeight(2.0, WeightUnit.KILOGRAM);

        QuantityWeight result = w1.add(w2);

        assertEquals(3.0, result.getValue(), EPS);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    @Test
    void testAddition_KgPlusGram() {
        QuantityWeight kg = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight g = new QuantityWeight(1000.0, WeightUnit.GRAM);

        QuantityWeight result = kg.add(g);

        assertEquals(2.0, result.getValue(), EPS);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    @Test
    void testAddition_Commutativity() {
        QuantityWeight w1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight w2 = new QuantityWeight(1000.0, WeightUnit.GRAM);

        assertEquals(w1.add(w2), w2.add(w1));
    }

    @Test
    void testAddition_WithZero() {
        QuantityWeight w = new QuantityWeight(5.0, WeightUnit.KILOGRAM);
        QuantityWeight zero = new QuantityWeight(0.0, WeightUnit.GRAM);

        QuantityWeight result = w.add(zero);

        assertEquals(5.0, result.getValue(), EPS);
    }

    @Test
    void testAddition_Negative() {
        QuantityWeight w1 = new QuantityWeight(5.0, WeightUnit.KILOGRAM);
        QuantityWeight w2 = new QuantityWeight(-2000.0, WeightUnit.GRAM);

        QuantityWeight result = w1.add(w2);

        assertEquals(3.0, result.getValue(), EPS);
    }

    // ---------------- ADDITION (EXPLICIT TARGET) ----------------

    @Test
    void testAddition_ExplicitTarget_Gram() {
        QuantityWeight w1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight w2 = new QuantityWeight(1000.0, WeightUnit.GRAM);

        QuantityWeight result = w1.add(w2, WeightUnit.GRAM);

        assertEquals(2000.0, result.getValue(), EPS);
        assertEquals(WeightUnit.GRAM, result.getUnit());
    }

    @Test
    void testAddition_ExplicitTarget_Pound() {
        QuantityWeight w1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight w2 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        QuantityWeight result = w1.add(w2, WeightUnit.POUND);

        assertEquals(4.40924, result.getValue(), EPS);
        assertEquals(WeightUnit.POUND, result.getUnit());
    }

    // ---------------- VALIDATION TESTS ----------------

    @Test
    void testNullUnitConstructor() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityWeight(1.0, null));
    }

    @Test
    void testInvalidValueConstructor() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityWeight(Double.NaN, WeightUnit.KILOGRAM));
    }

    @Test
    void testAddNullOperand() {
        QuantityWeight w = new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        assertThrows(IllegalArgumentException.class,
                () -> w.add(null));
    }
}