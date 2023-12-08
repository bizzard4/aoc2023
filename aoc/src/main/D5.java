import com.sun.jdi.Value;
import jdk.jshell.execution.Util;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Array;
import java.time.temporal.ValueRange;
import java.util.*;

public class D5 {

    public static SpecialMap buildMap(BufferedReader reader) throws IOException {
        SpecialMap finalMap = new SpecialMap();
        for (String line; (line = reader.readLine()) != null;) {
            var split = Arrays.stream(line.split(" ")).map(Long::valueOf).toList();
            var range = split.get(2);

            var beginSource = split.get(1);
            var beginDestination = split.get(0);
            finalMap.addRange(beginSource, beginDestination, range);
        }
        return finalMap;
    }

    public static void main(String[] args) throws IOException {
        // Toggle for debug
        var debug = false;
        var prefix = "d5/" + (debug ? "debug/" : "");

        // ******
        // Construction of maps
        // ******

        // Seed
        var seedReader = Utils.quickReader(prefix + "0-seeds.txt");
        List<Long> seedsEntry = Arrays.stream(seedReader.readLine().split(" ")).map(Long::valueOf).toList();
        System.out.println(seedsEntry);

        // 1 - Seed to soil
        var seedToSoilReader = Utils.quickReader(prefix + "1-seed-to-soil.txt");
        Map<Long, Long> seedToSoilMap = buildMap(seedToSoilReader);
        System.out.println(seedToSoilMap);

        // 2 - Soil to fertilizer
        var soilToFertilizerReader = Utils.quickReader(prefix + "2-soil-to-fertilizer.txt");
        Map<Long, Long> soilToFertilizerMap = buildMap(soilToFertilizerReader);
        System.out.println(soilToFertilizerMap);

        // 2 - Fertilizer to water
        var fertilizerToWaterReader = Utils.quickReader(prefix + "3-fertilizer-to-water.txt");
        Map<Long, Long> fertilizerToWaterMap = buildMap(fertilizerToWaterReader);
        System.out.println(fertilizerToWaterMap);

        // 4 - Water to light
        var waterToLightReader = Utils.quickReader(prefix + "4-water-to-light.txt");
        Map<Long, Long> waterToLightMap = buildMap(waterToLightReader);
        System.out.println(waterToLightMap);

        // 5 - Light to temperature
        var lightToTempratureReader = Utils.quickReader(prefix + "5-light-to-temperature.txt");
        Map<Long, Long> lightToTempratureMap = buildMap(lightToTempratureReader);
        System.out.println(lightToTempratureMap);

        // 6 - Temperature to humidity
        var temperatureToHumidityReader = Utils.quickReader(prefix + "6-temperature-to-humidity.txt");
        Map<Long, Long> temperatureToHumidityMap = buildMap(temperatureToHumidityReader);
        System.out.println(temperatureToHumidityMap);

        // 7 - Humidity to location
        var humidityToLocationReader = Utils.quickReader(prefix + "7-humidity-to-location.txt");
        Map<Long, Long> humidityToLocationMap = buildMap(humidityToLocationReader);
        System.out.println(humidityToLocationMap);

        // ******
        // Seed to location computing
        // ******
        Long minLocation = Long.MAX_VALUE;

        for (int i = 0; i < seedsEntry.size(); i+=2) {
            System.out.println("i:"+i);
            var range = seedsEntry.get(i+1);
            var initialValue = seedsEntry.get(i);
            for (int j = 0; j < range; j++) {
                var seed = initialValue + j;
                var soil = seedToSoilMap.get(seed);
                var fertilizer = soilToFertilizerMap.get(soil);
                var water = fertilizerToWaterMap.get(fertilizer);
                var light = waterToLightMap.get(water);
                var temperature = lightToTempratureMap.get(light);
                var humidity = temperatureToHumidityMap.get(temperature);
                var location = humidityToLocationMap.get(humidity);
                if (location < minLocation) {
                    minLocation = location;
                }
            }
        }

        // Final output
        System.out.println(minLocation);
    }
}

class SpecialMap extends HashMap<Long, Long> {


    record RangePair(ValueRange source, ValueRange destination) { }
    List<RangePair> ranges = new ArrayList<>();

    public void addRange(Long sourceStart, Long destinationStart, Long range) {
        var newRange = ValueRange.of(sourceStart, sourceStart+range);
        var destinationRange = ValueRange.of(destinationStart, destinationStart+range);

        ranges.add(new RangePair(newRange, destinationRange));
    }

    private Long specialGet(Long key) {
        // if key within any of the range, return corresponding destination, add to map.
        RangePair withinRangeOf = null;
        for (RangePair pair: ranges) {
            if (key >= pair.source.getMinimum() && key < pair.source.getMaximum()) {
                withinRangeOf = pair;
                break;
            }
        }

        if (withinRangeOf == null) {
            return key;
        } else {
            var diff = key - withinRangeOf.source.getMinimum();
            var destination = withinRangeOf.destination.getMinimum() + diff;
            return destination;
        }
    }

    @Override
    public Long get(Object key) {
        if (this.containsKey(key)) {
            return this.get(key);
        }
        else {
            var specialGetValue = specialGet((Long)key);
            return specialGetValue;
        }
    }

    @Override
    public String toString() {
        return "Special map of " + ranges.size() + " starting with " + ranges.get(0);
    }
}
