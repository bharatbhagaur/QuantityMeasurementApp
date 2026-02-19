package UtilityClasses;

import java.util.Scanner;

public class FeetEquality {

    public static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;                     // Reflexive

            if (obj == null || getClass() != obj.getClass())
                return false;                                 // Null & Type check

            Feet other = (Feet) obj;

            return Double.compare(this.value, other.value) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(value); // Maintain equals-hashCode contract
        }
    }


}
