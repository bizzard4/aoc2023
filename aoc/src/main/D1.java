import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class D1 {
    public static void main(String[] args) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("input/d1b.txt");
        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);

        BufferedReader reader = new BufferedReader(streamReader);
        Integer sum = 0;
        for (String line; (line = reader.readLine()) != null;) {
            String trimmed = line;

            // Special case
            trimmed = trimmed.replace("eightwo", "82");
            trimmed = trimmed.replace("twone", "21");
            trimmed = trimmed.replace("oneight", "18");


            trimmed = trimmed.replace("nine", "9");
            trimmed = trimmed.replace("eight", "8");
            trimmed = trimmed.replace("seven", "7");
            trimmed = trimmed.replace("six", "6");
            trimmed = trimmed.replace("five", "5");
            trimmed = trimmed.replace("four", "4");
            trimmed = trimmed.replace("three", "3");
            trimmed = trimmed.replace("two", "2");
            trimmed = trimmed.replace("one", "1");
            trimmed = trimmed.replaceAll("[^0-9]", "");
            System.out.println(line + " " + trimmed);

            Integer toAdd = 0;
            if (!trimmed.isEmpty()) {
                List<Character> listed = trimmed.chars().mapToObj(c -> (char) c).toList();
                var concat = listed.get(0).toString() + listed.get(listed.size() - 1).toString();
                toAdd = Integer.valueOf(concat);
            }
            sum = sum + toAdd;
        }
        System.out.println("Total " + sum);
    }
}
