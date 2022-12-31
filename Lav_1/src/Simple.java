public class Simple {
    public static void main(String[] args) {
        int range = 100; // кол-во чисел, которые будм искать

        for (int i = 2; i < range; i++) { // проходимся по каждому числу
            if (isSimple(i)) {
                System.out.println(i);
            }
        }


    }

    public static boolean isSimple(int n) {
        boolean isPrime = false;

        if (n % 7 == 0)
        {
            isPrime = true;
        }

        return isPrime;
    }

}
