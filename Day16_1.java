import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

/**
 * Part one of
 * https://adventofcode.com/2019/day/16
 *
 * @author Nerijus
 */
public class Day16_1 {

    private static final List<Integer> INITIAL_PATTERN = Arrays.asList(0, 1, 0, -1);

    public static void main(String[] args) {
        System.out.printf("After 100 phases of FFT, final output: %s\n",
                new Day16_1().getResult());
    }

    String getResult() {
        Integer[] signal = Arrays.stream(Inputs.readString("Day16").split(""))
                .map(Integer::parseInt)
                .toArray(Integer[]::new);

        // apply 100 phases
        int PHASES = 100;
        signal = applyPhases(signal, PHASES);

        // after phases
        return Arrays.stream(signal).limit(8).map(n -> Integer.toString(n)).collect(joining(""));
    }

    static Integer[] applyPhases(Integer[] signal, int count) {
        for (int i = 1; i <= count; i++) {
            signal = applyPhase(signal);
        }
        return signal;
    }

    private static Integer[] applyPhase(Integer[] signal) {
        Integer[] signalAfterPhase = new Integer[signal.length];
        IntStream.rangeClosed(1, signal.length).parallel().forEach(position -> {
            Pattern pattern = generatePatternFor(position);
            int result = 0;
            for (Integer number : signal) {
                result = result + number * pattern.get();
            }
            signalAfterPhase[position - 1] = lastDigitOf(result);
        });
        return signalAfterPhase;
    }

    private static Integer lastDigitOf(int result) {
        String[] parts = ("" + result).split("");
        return Integer.valueOf(parts[parts.length - 1]);
    }

    static Pattern generatePatternFor(int position) {
        List<Integer> generatedPattern = new ArrayList<>();
        for (Integer i : INITIAL_PATTERN) {
            for (int j = 0; j < position; j++) {
                generatedPattern.add(i);
            }
        }

        // return rolling pattern class
        return new Pattern(generatedPattern);
    }

    static class Pattern {
        LinkedList<Integer> values;
        boolean offset = true;

        public Pattern(List<Integer> repeatingPattern) {
            this.values = new LinkedList<>(repeatingPattern);
        }

        Integer get() {
            if (offset) {
                values.addLast(values.pop());
                offset = false;
            }

            Integer value = values.pop();
            values.addLast(value);
            return value;
        }
    }
}
