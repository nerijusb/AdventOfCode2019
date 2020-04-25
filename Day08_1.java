import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Part one of
 * https://adventofcode.com/2019/day/8
 *
 * @author Nerijus
 */
public class Day08_1 {

    static final int WIDTH = 25;
    static final int HEIGHT = 6;

    public static void main(String[] args) {
        System.out.println("In layer with fewest 0, number of 1 digits multiplied by the number of 2 digits: "
                + new Day08_1().getResult());
    }

    private long getResult() {
        Image image = readImage();
        Layer layer = getLayerWithFewestZeros(image);
        return layer.oneCount() * layer.twoCount();
    }

    private Layer getLayerWithFewestZeros(Image image) {
        return image.layers
                .stream()
                .min(Comparator.comparingLong(Layer::zeroCount))
                .orElseThrow(() -> new IllegalStateException("Could not find layer with fewest zeros"));
    }

    Image readImage() {
        List<Integer> puzzleInput = getPuzzleInput();

        int layerArea = WIDTH * HEIGHT;
        int layerCount = puzzleInput.size() / layerArea;

        Image image = new Image();
        for (int l = 0; l < layerCount; l++) {
            int layerStart = l * layerArea;
            int layerEnd = layerStart + layerArea;
            Layer layer = new Layer();
            for (int px = layerStart; px < layerEnd; px++) {
                layer.pixels.add(puzzleInput.get(px));
            }
            image.layers.add(layer);
        }

        return image;
    }

    List<Integer> getPuzzleInput() {
        return Arrays.stream(Inputs.readString("Day08").split(""))
                .map(Integer::parseInt)
                .collect(toList());
    }

    static class Image {
        List<Layer> layers = new ArrayList<>();
    }

    static class Layer {
        List<Integer> pixels = new ArrayList<>();

        long zeroCount() {
            return count(0);
        }
        long oneCount() {
            return count(1);
        }
        long twoCount() {
            return count(2);
        }
        long count(int digit) {
            return pixels.stream().filter(px -> px == digit).count();
        }
    }
}
