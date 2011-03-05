package br.unicamp.ic.problems;


public class ComplexTriangle {

    public int run(final int x, final int y, final int z) {
        TriangleType type;

        if (x + y > z && y + z > x && z + x > y) {
            if (x == y || y == z || z == x) {
                if (x == y && y == z) {
                    type = TriangleType.EQUILATERO;
                } else {
                    type = TriangleType.ISOSCELES;
                }
            } else {
                final int aa = x * x;
                final int bb = y * y;
                final int cc = z * z;
                final int d = bb + cc;
                if (aa == d) {
                    type = TriangleType.RETO;
                } else {
                    if (aa < d) {
                        type = TriangleType.ACUTANGULO;
                    } else {
                        type = TriangleType.OBTUSANGULO;
                    }
                }
            }
        } else {
            type = TriangleType.NO_TRIANGLE;
        }
        return type.getValue();
    }

    // path.addStep('a');
    // data.addStep('a');
    // if ((a.compareTo(b.add(c)) > 0) || (b.compareTo(c.add(a)) > 0)
    // || (c.compareTo(a.add(b)) > 0)) {
    // path.addStep('b');
    // data.addStep('b', new BigDecimal(Math.min(Math.min(b.add(c)
    // .subtract(a).doubleValue(), c.add(a).subtract(b)
    // .doubleValue()), a.add(b).subtract(c).doubleValue())));
    // type = TriangleType.NO_TRIANGLE;
    // } else {
    // path.addStep('c');
    // data.addStep('c', new BigDecimal(Math.min(Math.min(b.add(c)
    // .subtract(a).doubleValue(), c.add(a).subtract(b)
    // .doubleValue()), a.add(b).subtract(c).doubleValue())));
    // if ((a.compareTo(b) == 0) || (b.compareTo(c) == 0)
    // || (a.compareTo(c) == 0)) {
    // path.addStep('d');
    // data.addStep('d', new BigDecimal(Math.min(Math.min(a
    // .subtract(b).abs().doubleValue(), b.subtract(c).abs()
    // .doubleValue()), a.subtract(c).abs().doubleValue()))
    // .add(K));
    // if ((a.compareTo(b) == 0) && (b.compareTo(c) == 0)) {
    // path.addStep('e');
    // data.addStep('e', a.subtract(b).abs().add(
    // b.subtract(c).abs()).add(K));
    // type = TriangleType.EQUILATERO;
    // } else {
    // path.addStep('f');
    // data.addStep('f', a.subtract(b).abs().add(
    // b.subtract(c).abs()).add(K));
    // type = TriangleType.ISOSCELES;
    // }
    // } else {
    // path.addStep('g');
    // data.addStep('g', new BigDecimal(Math.min(Math.min(a
    // .subtract(b).abs().doubleValue(), b.subtract(c).abs()
    // .doubleValue()), a.subtract(c).abs().doubleValue()))
    // .add(K));
    // final BigDecimal aa = a.multiply(a);
    // final BigDecimal bb = b.multiply(b);
    // final BigDecimal cc = c.multiply(c);
    // d = bb.add(cc);
    // if (aa.compareTo(d) == 0) {
    // path.addStep('h');
    // data.addStep('h', aa.subtract(d).abs().add(K));
    // type = TriangleType.RETO;
    // } else {
    // path.addStep('i');
    // data.addStep('i', aa.subtract(d).abs().add(K));
    // if (aa.compareTo(d) < 0) {
    // path.addStep('j');
    // data.addStep('j', aa.subtract(d));
    // type = TriangleType.ACUTANGULO;
    // } else {
    // path.addStep('k');
    // data.addStep('k', aa.subtract(d));
    // type = TriangleType.OBTUSANGULO;
    // }
    // }
    // }
    // }
    // return type;

}
