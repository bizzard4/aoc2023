import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class D3 {
    public static void main(String[] args) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("input/d3b.txt");
        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);

        // Pre-read all line
        List<String> lines = new ArrayList<String>();
        for (String line; (line = reader.readLine()) != null;) {
            lines.add(line);
        }

        // Build a grid of EntryPointer allowing to map where symbol and number are for every position.
        // Number share reference for every position.
        EntryPointer[][] grid = new EntryPointer[lines.size()][lines.get(0).length()];
        int lineNumber = 0;
        for (String line: lines) {
            // Extract all number on a given line
            var listOfNumber = Arrays.stream(line.split("[.*\\-$%+&/@#=]")).map((str) -> {
                Optional<NumberPointer> extract = Optional.empty();
                try {
                    var trim = str.replaceAll("[^0-9]", "");
                    extract = Optional.of(new NumberPointer(Integer.valueOf(trim)));
                } catch (Exception e) { }
                return extract;
            }).flatMap(Optional::stream).collect(Collectors.toList());

            // Build grid line
            var gridLine = grid[lineNumber];
            for (int i = 0; i < line.length(); i++) {
                var ch = line.charAt(i);
                if (!Character.isDigit(ch) && !Character.isLetter(ch) && !Character.isSpace(ch)) {
                    if (ch != '.') gridLine[i] = new SymbolPointer(ch);
                }
            }
            var tempLine = line;
            for (NumberPointer num: listOfNumber) {
                var numStr = num.number.toString();
                var numSize = numStr.length();
                var start = tempLine.indexOf(numStr);
                for (int k = 0; k < numSize; k++) {
                    gridLine[start+k] = num;
                }
                tempLine = tempLine.replaceFirst(numStr, new String(new char[numSize]).replace('\0', '.'));
            }
            lineNumber++;

            // Debug output
            System.out.println(line);
        }

        // Backread grid, lookup symbol and read number around
        Long ratioSum = 0l;
        for (int i = 0; i < grid.length; i++) { // line
            for (int j = 0; j < grid[i].length; j++) { // column
                if ((grid[i][j] instanceof SymbolPointer) && (((SymbolPointer)grid[i][j]).symbol() == '*')) {
                    // Lookup for number around
                    // a b c (i - 1)
                    // d x e
                    // f g h (i + 1)
                    Set<NumberPointer> adjacentPart = new HashSet<>();

                   var a = safeTryGetNumber(grid, i-1, j-1);
                   if (a != null) adjacentPart.add(a);

                    var b = safeTryGetNumber(grid, i-1, j);
                    if (b != null) adjacentPart.add(b);

                    var c = safeTryGetNumber(grid, i-1, j+1);
                    if (c != null) adjacentPart.add(c);

                    var d = safeTryGetNumber(grid, i, j-1);
                    if (d != null) adjacentPart.add(d);

                    var e = safeTryGetNumber(grid, i, j+1);
                    if (e != null) adjacentPart.add(e);

                    var f = safeTryGetNumber(grid, i+1, j-1);
                    if (f != null) adjacentPart.add(f);

                    var g = safeTryGetNumber(grid, i+1, j);
                    if (g != null) adjacentPart.add(g);

                    var h = safeTryGetNumber(grid, i+1, j+1);
                    if (h != null) adjacentPart.add(h);

                    if (adjacentPart.size() == 2) {
                        var temp = new ArrayList<>(adjacentPart);
                        var ratio = temp.get(0).number * temp.get(1).number;
                        ratioSum += ratio;
                    }
                }
            }
        }

        // Final sum
        System.out.println(ratioSum);
    }

    public static NumberPointer safeTryGetNumber(EntryPointer[][] grid, int di, int dj) {
        NumberPointer returnValue = null;
        try {
            if (grid[di][dj] instanceof NumberPointer) returnValue = (NumberPointer)grid[di][dj];
        } catch (Exception ex) { }
        return returnValue;
    }
}

interface EntryPointer { }
record SymbolPointer(Character symbol) implements EntryPointer { }
class NumberPointer implements EntryPointer {
    Integer number;
    Boolean isPartNumber;
    public NumberPointer(Integer iNumber) {
        number = iNumber;
        isPartNumber = false;
    }
}
