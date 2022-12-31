import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {
    // Максимальное количество итераций
    public static final int MAX_ITERATIONS = 2000;

    public void getInitialRange(Rectangle2D.Double range) {
        // Значения для фрактала Мандельброта
        range.x = -2;
        range.y = -1.5;
        range.height = 3;
        range.width = 3;
    }

    /*
     * returns the number of iterations for the corresponding coordinate.
     */
    public int numIterations(double x, double y) {
        // Функция для фрактала Мандельброта
        // Zn= (Zn-1)^2 + c
        // Ограничения для функции
        // |Z| > 2
        // или пока меньше MAX_ITERATIONS
        int iteration = 0; // Текущая итерация
        double zReal = 0.0; // Действительная
        double zComplex = 0.0; // и комплексная часть

        while (iteration < MAX_ITERATIONS && zReal * zReal + zComplex * zComplex < 4) {
            // Z0 = 0, Z1 = Z0 * Z0 + c = x + y*i, Z2 = ...
            // Xn+1 = Xn^2 + Yn^2 + x; Yn+1 = 2*Xn*Yn + y
            // from wikipedia)
            double zRealNew = zReal * zReal - zComplex * zComplex + x;
            double zComplexNew = 2 * zReal * zComplex + y;
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
        return "Mandelbrot";
    }
}

