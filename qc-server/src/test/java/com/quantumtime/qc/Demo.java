import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** The type Demo. */
public class Demo {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        int i = 10 / 3;
        int i1 = (50 * 2 + 20 * 3 + 30 * 4) / (2 + 3 + 4);
        System.err.println(i);
        System.err.println(i1);
//        new Demo().getResult(1,100);
    }

    private void getResult(int i, int j) {
        if (i < 1 || j < 1 || i > 100000 || j > 100000) return;
        IntStream.rangeClosed(i, j)
                .filter(this::isResult)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new))
                .forEach(System.out::println);
    }

    /**
     * Created on 18:09 2019/11/20 Author: Tablo.
     *
     * <p>Description:[判断是否完数]
     *
     * @param a 参数
     * @return boolean
     */
    private boolean isResult(int a) {
        ArrayList<Integer> nums =
                IntStream.range(1, a).filter(i -> a % i == 0).boxed().collect(Collectors.toCollection(ArrayList::new));
        return nums.size() != 0 && a == nums.stream().mapToInt(num -> num).sum();
    }
}
