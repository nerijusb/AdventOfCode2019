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
        AmplifierSupplier aInput = null;
        IntCodeComputer compA = initComputer();

        AmplifierSupplier bInput = null;
        IntCodeComputer compB = initComputer();

        AmplifierSupplier cInput = null;
        IntCodeComputer compC = initComputer();

        AmplifierSupplier dInput = null;
        IntCodeComputer compD = initComputer();

        AmplifierSupplier eInput = null;
        IntCodeComputer compE = initComputer();

        String input = "0";
        while (true) {
            aInput = aInput != null? aInput.updateInput(input) : new AmplifierSupplier(phaseAmpA, input);
            bInput = bInput != null? bInput.updateInput(compA.run(aInput).get()) : new AmplifierSupplier(phaseAmpB, compA.run(aInput).get());
            cInput = cInput != null? cInput.updateInput(compB.run(bInput).get()) : new AmplifierSupplier(phaseAmpC, compB.run(bInput).get());
            dInput = dInput != null? dInput.updateInput(compC.run(cInput).get()) : new AmplifierSupplier(phaseAmpD, compC.run(cInput).get());
            eInput = eInput != null? eInput.updateInput(compD.run(dInput).get()) : new AmplifierSupplier(phaseAmpE, compD.run(dInput).get());
            IntCodeComputer.Result eOutput = compE.run(eInput);
            if (eOutput.isEnd()) {
                // e halted, return last output
                resultCollector.accept(Integer.parseInt(input));
                return;
            } else {
                input = eOutput.get();
            }
        }
    }

    private IntCodeComputer initComputer() {
        return new IntCodeComputer(getPuzzleInput());
    }
}
