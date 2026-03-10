package UtilityClasses;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityLengthTest {

    private static final double EPSILON = 1e-6;

    // ---------------- UC4 EQUALITY TESTS ----------------

    @Test
    void testEquality_YardToYard_SameValue() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.YARDS);

        assertEquals(q1, q2);
    }

    @Test
    void testEquality_YardToFeet_EquivalentValue() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength feet = new QuantityLength(3.0, LengthUnit.FEET);

        assertEquals(yard, feet);
    }

    @Test
    void testEquality_YardToInches_EquivalentValue() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength inches = new QuantityLength(36.0, LengthUnit.INCH);

        assertEquals(yard, inches);
    }

    @Test
    void testEquality_CentimeterToInches_EquivalentValue() {
        QuantityLength cm = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        QuantityLength inch = new QuantityLength(0.393701, LengthUnit.INCH);

        assertEquals(cm, inch);
    }

    @Test
    void testEquality_MultiUnit_TransitiveProperty() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength feet = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(36.0, LengthUnit.INCH);

        assertEquals(yard, feet);
        assertEquals(feet, inches);
        assertEquals(yard, inches);
    }

    @Test
    void testEquality_NullComparison() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);

        assertNotEquals(yard, null);
    }

    @Test
    void testEquality_NullUnit_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                new QuantityLength(1.0, null)
        );
    }

    // ---------------- UC5 CONVERSION TESTS ----------------

    @Test
    void testConversion_FeetToInches() {
        double result = QuantityLength.convert(
                1.0,
                LengthUnit.FEET,
                LengthUnit.INCH
        );

        assertEquals(12.0, result, EPSILON);
    }

    @Test
    void testConversion_InchesToFeet() {
        double result = QuantityLength.convert(
                24.0,
                LengthUnit.INCH,
                LengthUnit.FEET
        );

        assertEquals(2.0, result, EPSILON);
    }

    @Test
    void testConversion_YardsToInches() {
        double result = QuantityLength.convert(
                1.0,
                LengthUnit.YARDS,
                LengthUnit.INCH
        );

        assertEquals(36.0, result, EPSILON);
    }

    @Test
    void testConversion_CentimetersToInches() {
        double result = QuantityLength.convert(
                2.54,
                LengthUnit.CENTIMETERS,
                LengthUnit.INCH
        );

        assertEquals(1.0, result, EPSILON);
    }

    @Test
    void testConversion_ZeroValue() {
        double result = QuantityLength.convert(
                0.0,
                LengthUnit.FEET,
                LengthUnit.INCH
        );

        assertEquals(0.0, result, EPSILON);
    }

    @Test
    void testConversion_NegativeValue() {
        double result = QuantityLength.convert(
                -1.0,
                LengthUnit.FEET,
                LengthUnit.INCH
        );

        assertEquals(-12.0, result, EPSILON);
    }

    @Test
    void testConversion_RoundTrip_PreservesValue() {
        double original = 5.0;

        double inches = QuantityLength.convert(
                original,
                LengthUnit.FEET,
                LengthUnit.INCH
        );

        double backToFeet = QuantityLength.convert(
                inches,
                LengthUnit.INCH,
                LengthUnit.FEET
        );

        assertEquals(original, backToFeet, EPSILON);
    }

    @Test
    void testConversion_InvalidUnit_ShouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
                QuantityLength.convert(
                        1.0,
                        null,
                        LengthUnit.FEET
                )
        );
    }

    @Test
    void testConversion_NaN_ShouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
                QuantityLength.convert(
                        Double.NaN,
                        LengthUnit.FEET,
                        LengthUnit.INCH
                )
        );
    }
    @Test
    void testAddition_SameUnit_FeetPlusFeet() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(2.0, LengthUnit.FEET);

        QuantityLength result = q1.add(q2);

        assertEquals(new QuantityLength(3.0, LengthUnit.FEET), result);
    }

    @Test
    void testAddition_CrossUnit_FeetPlusInches() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCH);

        QuantityLength result = q1.add(q2);

        assertEquals(new QuantityLength(2.0, LengthUnit.FEET), result);
    }

    @Test
    void testAddition_CrossUnit_InchPlusFeet() {
        QuantityLength q1 = new QuantityLength(12.0, LengthUnit.INCH);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength result = q1.add(q2);

        assertEquals(new QuantityLength(24.0, LengthUnit.INCH), result);
    }

    @Test
    void testAddition_YardPlusFeet() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q2 = new QuantityLength(3.0, LengthUnit.FEET);

        QuantityLength result = q1.add(q2);

        assertEquals(new QuantityLength(2.0, LengthUnit.YARDS), result);
    }

    @Test
    void testAddition_CentimeterPlusInch() {
        QuantityLength q1 = new QuantityLength(2.54, LengthUnit.CENTIMETERS);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.INCH);

        QuantityLength result = q1.add(q2);

        assertEquals(new QuantityLength(5.08, LengthUnit.CENTIMETERS), result);
    }

    @Test
    void testAddition_WithZero() {
        QuantityLength q1 = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(0.0, LengthUnit.INCH);

        QuantityLength result = q1.add(q2);

        assertEquals(new QuantityLength(5.0, LengthUnit.FEET), result);
    }

    @Test
    void testAddition_NegativeValues() {
        QuantityLength q1 = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(-2.0, LengthUnit.FEET);

        QuantityLength result = q1.add(q2);

        assertEquals(new QuantityLength(3.0, LengthUnit.FEET), result);
    }

    @Test
    void testAddition_Commutativity() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCH);

        QuantityLength result1 = q1.add(q2);
        QuantityLength result2 = q2.add(q1);

        assertEquals(result1, result2);
    }

    @Test
    void testAddition_NullOperand_ShouldThrow() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class, () ->
                q1.add(null)
        );
    }
    @Test
void testAddition_ExplicitTargetUnit_Inches() {
    QuantityLength result =
            QuantityLength.add(
                    new QuantityLength(1.0, LengthUnit.FEET),
                    new QuantityLength(12.0, LengthUnit.INCH),
                    LengthUnit.INCH
            );

    assertEquals(
            new QuantityLength(24.0, LengthUnit.INCH),
            result
    );
}
@Test
void testAddition_ExplicitTargetUnit_Feet() {
    QuantityLength result =
            QuantityLength.add(
                    new QuantityLength(1.0, LengthUnit.FEET),
                    new QuantityLength(12.0, LengthUnit.INCH),
                    LengthUnit.FEET
            );

    assertEquals(
            new QuantityLength(2.0, LengthUnit.FEET),
            result
    );
}
@Test
void testAddition_ExplicitTargetUnit_Yards() {
    QuantityLength result =
            QuantityLength.add(
                    new QuantityLength(1.0, LengthUnit.FEET),
                    new QuantityLength(12.0, LengthUnit.INCH),
                    LengthUnit.YARDS
            );

    assertEquals(
            new QuantityLength(2.0 / 3.0, LengthUnit.YARDS),
            result
    );
}
@Test
void testAddition_NullTargetUnit() {
    assertThrows(
            IllegalArgumentException.class,
            () -> QuantityLength.add(
                    new QuantityLength(1.0, LengthUnit.FEET),
                    new QuantityLength(12.0, LengthUnit.INCH),
                    null
            )
    );
}
}