package common;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static java.lang.Integer.parseInt;

public class IntCodeComputer {
    Map<Integer, String> memory;
    Supplier<String> input;
    int relativeBase = 0;
    int pointerPosition = 0;

    public IntCodeComputer(String initialMemory, Supplier<String> input) {
        this.memory = new HashMap<>();
        // fill memory with initial values
        Arrays.stream(initialMemory.split(",")).forEach(i -> memory.put(memory.size(), i));
        this.input = input;
    }

    public void modify(String noun, String verb) {
        memory.put(1, noun);
        memory.put(2, verb);
    }

    public Supplier<String> getInput() {
        return input;
    }

    private String read(int position) {
        String value = memory.get(position);
        return value != null? value : "0";
    }

    private String readNext() {
        String value = read(pointerPosition);
        pointerPosition = pointerPosition + 1;
        return value;
    }

    /**
     * Runs until outputs or halts
     * @return result (output or halt)
     */
    public Result run() {
        while (true) {
            Instruction instruction = new Instruction(readNext());
            if (instruction.isEnd()) {//99
                return end(read(0));
            } else if (instruction.isAddition()) {//1
                BigInteger argOne = new BigInteger(getFirstParamValue(instruction));
                BigInteger argTwo = new BigInteger(getSecondParamValue(instruction));
                memory.put(getPositionValue(instruction.getThirdParamMode()), argOne.add(argTwo).toString());
            } else if (instruction.isMultiplication()) {//2
                BigInteger argOne = new BigInteger(getFirstParamValue(instruction));
                BigInteger argTwo = new BigInteger(getSecondParamValue(instruction));
                memory.put(getPositionValue(instruction.getThirdParamMode()), argOne.multiply(argTwo).toString());
            } else if (instruction.isInput()) {//3
                memory.put(getPositionValue(instruction.getFirstParamMode()), input.get());
            } else if (instruction.isOutput()) {//4
                return output(getFirstParamValue(instruction));
            } else if (instruction.isJumpIfTrue()) {//5
                int argOne = parseInt(getFirstParamValue(instruction));
                int argTwo = parseInt(getSecondParamValue(instruction));
                if (argOne != 0) {
                    pointerPosition = argTwo;
                }
            } else if (instruction.isJumpIfFalse()) {//6
                int argOne = parseInt(getFirstParamValue(instruction));
                int argTwo = parseInt(getSecondParamValue(instruction));
                if (argOne == 0) {
                    pointerPosition = argTwo;
                }
            } else if (instruction.isLessThan()) {//7
                BigInteger argOne = new BigInteger(getFirstParamValue(instruction));
                BigInteger argTwo = new BigInteger(getSecondParamValue(instruction));
                memory.put(getPositionValue(instruction.getThirdParamMode()), argOne.compareTo(argTwo) < 0 ? "1" : "0");
            } else if (instruction.isEquals()) {//8
                BigInteger argOne = new BigInteger(getFirstParamValue(instruction));
                BigInteger argTwo = new BigInteger(getSecondParamValue(instruction));
                memory.put(getPositionValue(instruction.getThirdParamMode()), argOne.compareTo(argTwo) == 0? "1" : "0");
            } else if (instruction.isAdjustRelativeBase()) {//9
                int argOne = parseInt(getFirstParamValue(instruction));
                relativeBase = relativeBase + argOne;
            } else {
                throw new IllegalStateException("Unexpected operation: " + instruction.opCode);
            }
        }
    }

    String getFirstParamValue(Instruction instruction) {
        return getParamValue(instruction.getFirstParamMode());
    }

    String getSecondParamValue(Instruction instruction) {
        return getParamValue(instruction.getSecondParamMode());
    }

    String getParamValue(ParameterMode paramMode) {
        String source = readNext();
        if (paramMode == ParameterMode.IMMEDIATE) {
            return source;
        } else if (paramMode == ParameterMode.POSITION) {
            return read(parseInt(source));
        } else if (paramMode == ParameterMode.RELATIVE) {
            return read(relativeBase + parseInt(source));
        } else {
            throw new IllegalStateException("Unknown parameter mode type: " + paramMode);
        }
    }

    int getPositionValue(ParameterMode paramMode) {
        String source = readNext();
        if (paramMode == ParameterMode.IMMEDIATE || paramMode == ParameterMode.POSITION) {
            return parseInt(source);
        } else if (paramMode == ParameterMode.RELATIVE) {
            return relativeBase + parseInt(source);
        } else {
            throw new IllegalStateException("Unknown parameter mode type: " + paramMode);
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
        String source;
        int opCode;
        ParameterMode[] paramModes = new ParameterMode[] {
            ParameterMode.POSITION, // first
            ParameterMode.POSITION, // second
            ParameterMode.POSITION, // third
        };

        Instruction(String source) {
            this.source = source;
            String[] parts = source.split("");
            if (parts.length <= 2) {
                opCode = parseInt(source);
            } else {
                opCode = parseInt(parts[parts.length - 2] + parts[parts.length - 1]);
            }
            if (parts.length >= 3) {
                paramModes[0] = ParameterMode.from(parseInt(parts[parts.length - 3]));
            }
            if (parts.length >= 4) {
                paramModes[1] = ParameterMode.from(parseInt(parts[parts.length - 4]));
            }
            if (parts.length >= 5) {
                paramModes[2] = ParameterMode.from(parseInt(parts[parts.length - 5]));
            }
        }

        ParameterMode getFirstParamMode() {
            return paramModes[0];
        }

        ParameterMode getSecondParamMode() {
            return paramModes[1];
        }

        ParameterMode getThirdParamMode() {
            return paramModes[2];
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

        boolean isAdjustRelativeBase() {
            return opCode == 9;
        }

        boolean isEnd() {
            return opCode == 99;
        }
    }

    enum ParameterMode {
        POSITION,
        IMMEDIATE,
        RELATIVE;

        static ParameterMode from(int mode) {
            if (mode == 0) {
                return POSITION;
            } else if (mode == 1) {
                return IMMEDIATE;
            } else if (mode == 2) {
                return RELATIVE;
            } else {
                throw new IllegalStateException("Unknown parameter mode: " + mode);
            }
        }
    }
}
