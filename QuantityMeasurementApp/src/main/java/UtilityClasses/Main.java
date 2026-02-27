package UtilityClasses;


import java.util.Scanner;
  class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter first value:");
        double value1 = sc.nextDouble();
        String unit1 = sc.next();

        System.out.println("Enter second value:");
        double value2 = sc.nextDouble();
        String unit2 = sc.next();

        try {

            QuantityLength q1 =
                    new QuantityLength(value1, parseUnit(unit1));

            QuantityLength q2 =
                    new QuantityLength(value2, parseUnit(unit2));

            System.out.println("Equal (" + q1.equals(q2) + ")");

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid unit entered.");
        }
    }

    private static LengthUnit parseUnit(String unit) {

        if (unit.equalsIgnoreCase("feet") ||
                unit.equalsIgnoreCase("foot")) {
            return LengthUnit.FEET;
        }

        if (unit.equalsIgnoreCase("inch") ||
                unit.equalsIgnoreCase("inches")) {
            return LengthUnit.INCH;
        }

        throw new IllegalArgumentException("Unsupported unit");
    }
}