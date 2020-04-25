package common;

import java.util.function.Supplier;

public class IntCodeComputer {
    String[] memory;
    Supplier<String> input;

    int currentPosition = 0;

    public IntCodeComputer(String memory, Supplier<String> input) {
        this.memory = memory.split(",");
        this.input = input;
    }

    public void modify(String noun, String verb) {
        this.memory[1] = noun;
        this.memory[2] = verb;
    }

    public Supplier<String> getInput() {
        return input;
    }

    private String read() {
        String value = this.memory[this.currentPosition];
        this.currentPosition = this.currentPosition + 1;
        return value;
    }

    private int readInt() {
        return Integer.parseInt(read());
    }

    /**
     * Runs until outputs or halts
     * @return result (output or halt)
     */
    public Result run() {
        while (true) {
            Instruction instruction = new Instruction(read());
            if (instruction.isEnd()) {//99
                return end(memory[0]);
            } else if (instruction.isAddition()) {//1
                int argOne = instruction.getFirstParamValue(readInt(), memory);
                int argTwo = instruction.getSecondParamValue(readInt(), memory);
                memory[readInt()] = String.valueOf(argOne + argTwo);
            } else if (instruction.isMultiplication()) {//2
                int argOne = instruction.getFirstParamValue(readInt(), memory);
                int argTwo = instruction.getSecondParamValue(readInt(), memory);
                memory[readInt()] = String.valueOf(argOne * argTwo);
            } else if (instruction.isInput()) {//3
                memory[readInt()] = input.get();
            } else if (instruction.isOutput()) {//4
                if (instruction.firstParam == ParameterMode.IMMEDIATE) {
                    return output(read());
                } else {
                    return output(memory[readInt()]);
                }
            } else if (instruction.isJumpIfTrue()) {//5
                int argOne = instruction.getFirstParamValue(readInt(), memory);
                int argTwo = instruction.getSecondParamValue(readInt(), memory);
                if (argOne != 0) {
                    currentPosition = argTwo;
                }
            } else if (instruction.isJumpIfFalse()) {//6
                int argOne = instruction.getFirstParamValue(readInt(), memory);
                int argTwo = instruction.getSecondParamValue(readInt(), memory);
                if (argOne == 0) {
                    currentPosition = argTwo;
                }
            } else if (instruction.isLessThan()) {//7
                int argOne = instruction.getFirstParamValue(readInt(), memory);
                int argTwo = instruction.getSecondParamValue(readInt(), memory);
                memory[readInt()] = argOne < argTwo? "1" : "0";
            } else if (instruction.isEquals()) {//8
                int argOne = instruction.getFirstParamValue(readInt(), memory);
                int argTwo = instruction.getSecondParamValue(readInt(), memory);
                memory[readInt()] = argOne == argTwo? "1" : "0";
            } else {
                throw new IllegalStateException("Unexpected operation: " + instruction.opCode);
            }
        }
    }

    private static Result end(String output) {
        Result result = new Result();
        result.output = output;
        result.end = true;
        return result;
    }

    private static Result output(String output) {
        Result result = new Result();
        result.output = output;
        result.end = false;
        return result;
    }

    public static class Result {
        String output;
        boolean end;

        public String get() {
            return output;
        }

        public boolean isEnd() {
            return end;
        }
    }

    static class Instruction {
        int opCode;
        ParameterMode firstParam = ParameterMode.POSITION;
        ParameterMode secondParam = ParameterMode.POSITION;

        Instruction(String source) {
            String[] parts = source.split("");
            if (parts.length <= 2) {
                opCode = Integer.parseInt(source);
            } else {
                opCode = Integer.parseInt(parts[parts.length - 2] + parts[parts.length - 1]);
            }
            if (parts.length >= 3) {
                firstParam = ParameterMode.from(Integer.parseInt(parts[parts.length - 3]));
            }
            if (parts.length >= 4) {
                secondParam = ParameterMode.from(Integer.parseInt(parts[parts.length - 4]));
            }
        }

        int getFirstParamValue(int source, String[] memory) {
            if (firstParam == ParameterMode.IMMEDIATE) {
                return source;
            }
            return Integer.parseInt(memory[source]);
        }

        int getSecondParamValue(int source, String[] memory) {
            if (secondParam == ParameterMode.IMMEDIATE) {
                return source;
            }
            return Integer.parseInt(memory[source]);
        }

        boolean isAddition() {
            return opCode == 1;
        }

        boolean isMultiplication() {
            return opCode == 2;
        }

        boolean isInput() {
            return opCode == 3;
        }

        boolean isOutput() {
            return opCode == 4;
        }

        boolean isJumpIfTrue() {
            return opCode == 5;
        }

        boolean isJumpIfFalse() {
            return opCode == 6;
        }

        boolean isLessThan() {
            return opCode == 7;
        }

        boolean isEquals() {
            return opCode == 8;
        }

        boolean isEnd() {
            return opCode == 99;
        }
    }

    enum ParameterMode {
        POSITION,
        IMMEDIATE;

        static ParameterMode from(int mode) {
            if (mode == 0) {
                return POSITION;
            }
            return IMMEDIATE;
        }
    }
}
