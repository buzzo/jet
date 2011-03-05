package br.unicamp.ic.problems;

public class SimpleTriangle {

    public int run(final int x, final int y, final int z) {
        TriangleType type;
        // 0
        if (x + y > z && y + z > x && z + x > y) {
            // 1 //2 //3
            if (x == y || y == z || z == x) {
                // 4 //5 ??
                if (x == y && y == z) {
                    // 6 // 7 // 8 ??
                    type = TriangleType.EQUILATERO; // (2)
                } else {
                    // 9
                    type = TriangleType.ISOSCELES; // (3)
                }
            } else {
                // 10
                type = TriangleType.ESCALENO; // (4)
            }
        } else {
            // 11
            type = TriangleType.NO_TRIANGLE; // (1)
        }
        // 12
        return type.getValue();
    }

    // if ((x.equals(y) && !y.equals(z))
    // || (y.equals(z) && !z.equals(x))
    // || (z.equals(x) && !x.equals(y))) {
    // path.addStep('e');
    //                  
    // final double pp1 = x
    // .subtract(y).abs().add(
    // BigDecimal.ONE.divide(y.subtract(z).abs()
    // .add(K), 5, RoundingMode.HALF_UP))
    // .doubleValue();
    //                  
    // final double pp2 = y.subtract(z).abs().add(
    // BigDecimal.ONE.divide(z.subtract(x).abs().add(K),
    // 5, RoundingMode.HALF_UP)).doubleValue();
    //                  
    // final double pp3 = z
    // .subtract(x).abs().add(
    // BigDecimal.ONE.divide(x.subtract(y).abs()
    // .add(K), 5, RoundingMode.HALF_UP))
    // .doubleValue();
    //                  
    // data.addStep('e', new BigDecimal(Math.min(Math.min(pp1,
    // pp2),pp3)));
    // type = TriangleType.ISOSCELES;
    // } else {
    // path.addStep('f');
    // data.addStep('f', new BigDecimal(Math.min(Math.min(x
    // .subtract(y).abs().add(
    // BigDecimal.ONE.divide(y.subtract(z).abs()
    // .add(K), 5, RoundingMode.HALF_UP))
    // .doubleValue(), y.subtract(z).abs().add(
    // BigDecimal.ONE.divide(z.subtract(x).abs().add(K),
    // 5, RoundingMode.HALF_UP)).doubleValue()), z
    // .subtract(x).abs().add(
    // BigDecimal.ONE.divide(x.subtract(y).abs()
    // .add(K), 5, RoundingMode.HALF_UP))
    // .doubleValue())));
    // type = TriangleType.EQUILATERO;
    // }

}
