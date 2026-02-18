package uc1;

public class FeetMeasurementEquility {

    // Inner class
    public static class Feet {
        private final double value; // immutable

        public Feet(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {

            // 1. Same reference check
            if (this == obj) return true;

            // 2. Null & type check
            if (obj == null || getClass() != obj.getClass()) return false;

            // 3. Type cast
            Feet other = (Feet) obj;

            // 4. Compare double safely
            return Double.compare(this.value, other.value) == 0;
        }
    }

    // Main method
  
}
