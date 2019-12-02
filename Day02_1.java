import java.util.Arrays;

/**
 * Part one of
 * https://adventofcode.com/2019/day/2
 *
 * @author Nerijus
 */
public class Day02_1 {
    public static void main(String[] args) {
        System.out.println("Program result (at position 0): " + new Day02_1().getResult());
    }

    private int getResult() {
        return new Program(12, 2).run();
    }

    static class Program {
        int[] memory;

        Program(int noun, int verb) {
            this.memory = init();
            this.memory[1] = noun;
            this.memory[2] = verb;
        }

        private int[] init() {
            return Arrays.stream(Inputs.readString("Day02").split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }

        int run() {
            for (int i = 0; i < this.memory.length; i+=4) {
                int opCode = this.memory[i];
                if (opCode == 99) {
                    // end of the program
                    return this.memory[0];
                }
                int argOne = this.memory[this.memory[i + 1]];
                int argTwo = this.memory[this.memory[i + 2]];
                int destination = this.memory[i + 3];

                if (opCode == 1) {
                    this.memory[destination] = argOne + argTwo;
                } else if (opCode == 2) {
                    this.memory[destination] = argOne * argTwo;
                } else {
                    throw new IllegalStateException("Unexpected operation code: " + opCode);
                }
            }

            throw new IllegalStateException("Exit code not found");
        }
    }

}
