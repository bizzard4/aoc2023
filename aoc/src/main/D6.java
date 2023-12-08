import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class D6 {
    public static void main(String[] args) {
        record TimeDistance(Long time, Long recordDistance) {}

        List<TimeDistance> recordsDebug = Arrays.asList(
                new TimeDistance(71530l, 940200l)
        );

        List<TimeDistance> records = Arrays.asList(
                new TimeDistance(56977875l, 546192711311139l)
        );

        List<Integer> winningCounts = new ArrayList<>();
        for (TimeDistance r: records) {
            List<Long> better = new ArrayList<>();
            for (long hold = 0; hold <= r.time; hold++) {
                var speed = hold;
                var remain = r.time - hold;
                var travelTime = remain;
                var distance = speed * travelTime;
                if (distance > r.recordDistance) {
                    better.add(distance);
                }
            }
            winningCounts.add(better.size());
        }

        Long product = 1l;
        for (Integer c: winningCounts) {
            product = product * c;
        }
        System.out.println(product);
    }
}
