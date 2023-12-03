import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class D2 {
    record Round(Integer red, Integer green, Integer blue) { }
    public static void main(String[] args) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("input/d2b.txt");
        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);

        Integer sumOfValidRound = 0;
        for (String line; (line = reader.readLine()) != null;) {
            // Extraction
            var topSplit = Arrays.stream(line.split(": ")).toList();
            Integer gameNumber = extractGameNumber(topSplit.get(0));
            var rounds = extractRoundDetails(topSplit.get(1));

            // Minimum needed
            Integer maxSeenRed = 0;
            Integer maxSeenGreen = 0;
            Integer maxSeenBlue = 0;
            for (Round r: rounds) {
                maxSeenRed = Math.max(r.red, maxSeenRed);
                maxSeenGreen = Math.max(r.green, maxSeenGreen);
                maxSeenBlue = Math.max(r.blue, maxSeenBlue);
            }

            var power = maxSeenRed * maxSeenGreen * maxSeenBlue;
            sumOfValidRound += power;

            // Debug string
            System.out.println(gameNumber + " -> " + power);
        }

        System.out.println("Sum of valid round : " + sumOfValidRound);
    }

    public static List<Round> extractRoundDetails(String detailStr) {
        // Example: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        var rounds = Arrays
                .stream(detailStr.split("; "))
                .map((roundLine) -> {
                    // Example: 3 blue
                    // Example  1 red, 2 green, 6 blue
                    String[] splitted = roundLine.split(", ");
                    Integer red = 0;
                    Integer green = 0;
                    Integer blue = 0;
                    for (String part: splitted) {
                        if (part.contains("red")) {
                            red = Integer.valueOf(part.split(" ")[0]);
                        } else if (part.contains("green")) {
                            green = Integer.valueOf(part.split(" ")[0]);
                        } else if (part.contains("blue")) {
                            blue = Integer.valueOf(part.split(" ")[0]);
                        }
                    }
                    return new Round(red, green, blue);
                })
                .toList();
        return rounds;
    }

    public static Integer extractGameNumber(String gameStr) {
        // Example: Game 1
        return Integer.valueOf(gameStr.split(" ")[1]);
    }
}
