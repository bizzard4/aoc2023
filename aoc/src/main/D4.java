import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class D4 {
    public static Integer score(String gamePart) {
        var gameSplit = gamePart.split("\\|");
        List<Integer> winningNumbers = Arrays.stream(gameSplit[0].trim().split("\\s+")).map(Integer::valueOf).toList();
        List<Integer> cardNumbers = Arrays.stream(gameSplit[1].trim().split("\\s+")).map(Integer::valueOf).toList();
        var validNumbers = cardNumbers.stream().filter(winningNumbers::contains).toList();
        return validNumbers.size();
    }
    public static void main(String[] args) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("input/d4.txt");
        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);

        ArrayList<ScoredCard> cards = new ArrayList<>();
        for (String line; (line = reader.readLine()) != null;) {
            var topSplit = line.split(":");
            var cardNumber = Integer.valueOf(topSplit[0].split("\\s+")[1]);
            var score = score(topSplit[1]);

            cards.add(new ScoredCard(cardNumber, score));
        }

        Integer totalScore = 0;
        for (int i = 0; i < cards.size(); i++) {
            var score = cards.get(i).score;
            totalScore += cards.get(i).count;
            for (int j = 1; j <= score; j++) {
                var updateCount = cards.get(i+j);
                updateCount.count += cards.get(i).count;
            }
        }

        System.out.println(totalScore);
    }

    public static Integer score(Integer current, Integer iteration, Integer maxIteration) {
        if (iteration >= maxIteration) {
            return current;
        } else {
            return score(current * 2, iteration + 1, maxIteration);
        }
    }
}

class ScoredCard {
    Integer number;
    Integer score;
    Integer count;

    public ScoredCard(Integer nb, Integer s) {
        number = nb;
        score = s;
        count = 1;
    }
}
