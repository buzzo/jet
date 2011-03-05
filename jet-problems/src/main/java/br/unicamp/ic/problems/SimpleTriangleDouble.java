package br.unicamp.ic.problems;


public class SimpleTriangleDouble {

    public int run(final double x, final double y, final double z) {
        TriangleType type;

        if (x + y > z && y + z > x && z + x > y) {
            if (x == y || y == z || z == x) {
                if (x == y && y == z) {
                    type = TriangleType.EQUILATERO;
                } else {
                    type = TriangleType.ISOSCELES;
                }
            } else {
                type = TriangleType.ESCALENO;
            }
        } else {
            type = TriangleType.NO_TRIANGLE;
        }
        return type.getValue();
    }
}
