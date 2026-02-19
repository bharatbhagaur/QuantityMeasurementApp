package UtilityClasses;

import java.util.Scanner;

public class FeetMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter first feet value: ");
        String input1 = sc.nextLine();

        System.out.print("Enter second feet value: ");
        String input2 = sc.nextLine();

        if (!isNumeric(input1) || !isNumeric(input2)) {
            System.out.println("Invalid input. Please enter numeric values.");
            return;
        }

        FeetEquality.Feet f1 = new FeetEquality.Feet(Double.parseDouble(input1));
        FeetEquality.Feet f2 = new FeetEquality.Feet(Double.parseDouble(input2));

        boolean result = f1.equals(f2);

        System.out.println("Equal (" + result + ")");
    }

    private static boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}