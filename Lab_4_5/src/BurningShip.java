import java.awt.geom.Rectangle2D.Double;

public class BurningShip extends FractalGenerator {
    public static final int MAX_ITERATIONS = 2000;

    @Override
    public void getInitialRange(Double range) {

        range.x = -2;
        range.y = -2.5;
        range.height = 3;
        range.width = 3;

    }

    @Override
    public int numIterations(double x, double y) {
        // Функция для фрактала BurningShip
        // Используется комплексное сопряжение
        // zn= (|Re(zn-1)| + i|Im(zn-1)|)2+ c.
        // Ограничения для функции
        // |Z| > 2
        // или пока меньше MAX_ITERATIONS
        int iteration = 0; // Текущая итерация
        double zReal = 0.0; // Действительная
        double zComplex = 0.0; // и комплексная часть
        while (iteration < MAX_ITERATIONS && zReal * zReal + zComplex * zComplex < 4) {
            // Xn+1 = Xn^2 + Yn^2 + x; Yn+1 = 2*Xn*Yn + y
            // from wikipedia)
            double zRealNew = zReal * zReal - zComplex * zComplex + x;
            // -2 вместо 2
            double zComplexNew = Math.abs(2 * zReal * zComplex) + y;
            zReal = zRealNew;
            zComplex = zComplexNew;
            iteration++;
        }
        if (iteration == MAX_ITERATIONS) {
            return -1;
        }
        return iteration;
    }

    @Override
    public String toString() {
        return "Burning Ship";
    }

}
