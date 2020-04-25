import common.IntCodeComputer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

/**
 * Part two of
 * https://adventofcode.com/2019/day/7
 *
 * @author Nerijus
 */
public class Day07_2 extends Day07_1 {
    public static void main(String[] args) {
        System.out.println("Highest signal that can be sent to the thrusters with feedback: " + new Day07_2().getResult());
    }

    private int getResult() {
        List<Integer> results = new ArrayList<>();
        for (int a = 5; a <= 9; a++) {
            for (int b = 5; b <= 9; b++) {
                for (int c = 5; c <= 9; c++) {
                    for (int d = 5; d <= 9; d++) {
                        for (int e = 5; e <= 9; e++) {
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
        IntCodeComputer compA = initComputer(phaseAmpA);
        IntCodeComputer compB = initComputer(phaseAmpB);
        IntCodeComputer compC = initComputer(phaseAmpC);
        IntCodeComputer compD = initComputer(phaseAmpD);
        IntCodeComputer compE = initComputer(phaseAmpE);

        String input = "0"; // initial input
        while (true) {
            ((AmplifierSupplier)compA.getInput()).updateWith(input);
            ((AmplifierSupplier)compB.getInput()).updateWith(compA.run().get());
            ((AmplifierSupplier)compC.getInput()).updateWith(compB.run().get());
            ((AmplifierSupplier)compD.getInput()).updateWith(compC.run().get());
            ((AmplifierSupplier)compE.getInput()).updateWith(compD.run().get());
            IntCodeComputer.Result eOutput = compE.run();
            if (eOutput.isEnd()) {
                // e halted, return last output
                resultCollector.accept(Integer.parseInt(input));
                return;
            } else {
                input = eOutput.get();
            }
        }
    }

    private IntCodeComputer initComputer(String phase) {
        return new IntCodeComputer(getPuzzleInput(), new AmplifierSupplier(phase, null));
    }
}
