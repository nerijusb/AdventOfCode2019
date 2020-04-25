import common.IntCodeComputer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Part one of
 * https://adventofcode.com/2019/day/7
 *
 * @author Nerijus
 */
public class Day07_1 {
    public static void main(String[] args) {
        System.out.println("Highest signal that can be sent to the thrusters: " + new Day07_1().getResult());
    }

    private int getResult() {
        List<Integer> results = new ArrayList<>();
        for (int a = 0; a <= 4; a++) {
            for (int b = 0; b <= 4; b++) {
                for (int c = 0; c <= 4; c++) {
                    for (int d = 0; d <= 4; d++) {
                        for (int e = 0; e <= 4; e++) {
                            if (new HashSet<>(List.of(a, b, c, d, e)).size() == 5) {
                                calculateOutput(String.valueOf(a), String.valueOf(b), String.valueOf(c), String.valueOf(d), String.valueOf(e), results::add);
                            }
                        }
                    }
                }
            }
        }

        return results.stream().mapToInt(r -> r).max().orElseThrow(() -> new IllegalStateException("Could not find max output value"));
    }

    private void calculateOutput(String phaseAmpA, String phaseAmpB, String phaseAmpC, String phaseAmpD, String phaseAmpE, Consumer<Integer> resultCollector) {
        new IntCodeComputer(new AmplifierSupplier(phaseAmpA, "0"), (aResult) -> {
            new IntCodeComputer(new AmplifierSupplier(phaseAmpB, aResult), (bResult) -> {
                new IntCodeComputer(new AmplifierSupplier(phaseAmpC, bResult), (cResult) -> {
                    new IntCodeComputer(new AmplifierSupplier(phaseAmpD, cResult), (dResult) -> {
                        new IntCodeComputer(new AmplifierSupplier(phaseAmpE, dResult), (eResult) -> {
                            resultCollector.accept(Integer.parseInt(eResult));
                        }, getInput()).run();
                    }, getInput()).run();
                }, getInput()).run();
            }, getInput()).run();
        }, getInput()).run();
    }

    private String getInput() {
        return Inputs.readString("Day07_demo");
    }

    static class AmplifierSupplier implements Supplier<String> {
        private String phase;
        private String input;

        public AmplifierSupplier(String phase, String input) {
            this.phase = phase;
            this.input = input;
        }

        @Override
        public String get() {
            if (this.phase != null) {
                String output = this.phase;
                this.phase = null;
                return output;
            }
            return this.input;
        }
    }
}
