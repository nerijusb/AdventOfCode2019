import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Part two of
 * https://adventofcode.com/2019/day/8
 *
 * @author Nerijus
 */
public class Day08_2 extends Day08_1 {
    public static void main(String[] args) {
        new Day08_2().getResult();
    }

    private void getResult() {
        Image image = readImage();
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                System.out.print(getPixelColor(image, x, y));
            }
            System.out.println();
        }
    }

    // 0 is black
    // 1 is white
    // 2 is transparent
    private String getPixelColor(Image image, int x, int y) {
        int position = WIDTH * y + x;
        List<Integer> pixelsAtPosition = image.layers.stream().map(l -> l.pixels.get(position)).collect(toList());
        for (Integer px : pixelsAtPosition) {
            if (px == 0 || px == 1) {
                if (px == 1) {
                    return "1";// print only white
                }
                return " ";
            }
        }
        return " ";// transparent
    }
}
