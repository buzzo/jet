package br.unicamp.ic.problems;


public enum TriangleType {
    NO_TRIANGLE(1), EQUILATERO(2), ISOSCELES(3), ESCALENO(4), RETO(5), ACUTANGULO(6), OBTUSANGULO(7);
    private final int value;

    private TriangleType(final int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
