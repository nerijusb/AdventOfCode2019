import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Part one of
 * https://adventofcode.com/2019/day/14
 *
 * @author Nerijus
 */
public class Day14_1 {

    public static final Pattern INGREDIENT_PATTERN = Pattern.compile("(([\\d]+) ([A-Z]+))");

    public static void main(String[] args) {
        System.out.printf("Minimum amount of ORE required to produce 1 FUEL: %.0f\n", new Day14_1().getResult());
    }

    private double getResult() {
        Nanofactory nanofactory = buildNanofactory("Day14");
        nanofactory.produce("FUEL", 1);
        return nanofactory.getOreConsumed();
    }

    protected Nanofactory buildNanofactory(String input) {
        // list of formulas
        List<Formula> formulas = Inputs.readStrings(input)
                .stream()
                .map(this::toFormula)
                .collect(Collectors.toList());

        Nanofactory nanofactory = new Nanofactory();
        formulas.forEach(f -> nanofactory.materialProducers.put(f.output.type,
                f.isInputIsOre() ?
                        new BaseMaterialProducer(f, nanofactory) :
                        new CompositeMaterialProducer(f, nanofactory)));
        return nanofactory;
    }

    protected static class Nanofactory {
        Map<String, MaterialProducer> materialProducers = new HashMap<>();

        void produce(String type, int amount) {
            materialProducers.get(type).produce(amount);
        }

        double getOreConsumed() {
            return materialProducers.values().stream().mapToDouble(mp -> {
                if (mp instanceof BaseMaterialProducer) {
                    return ((BaseMaterialProducer)mp).oreConsumed;
                }
                return 0;
            }).sum();
        }
    }

    /**
     * Base class for material producer
     */
    protected abstract static class MaterialProducer {
        Nanofactory factory;
        String type;
        Formula formula;
        int stock = 0;

        public MaterialProducer(Formula formula, Nanofactory factory) {
            this.formula = formula;
            this.type = formula.output.type;
            this.factory = factory;
        }

        public void produce(int amount) {
            if (amount > stock) {
                // produce until we have enough
                while(amount > stock) {
                    react();
                }
            }
            stock = stock - amount;
        }

        protected abstract void react();
    }

    /**
     * Produces material from non-base materials
     */
    protected static class CompositeMaterialProducer extends MaterialProducer {

        public CompositeMaterialProducer(Formula formula, Nanofactory factory) {
            super(formula, factory);
        }

        @Override
        public void react() {
            formula.input.forEach(m -> factory.materialProducers.get(m.type).produce(m.amount));
            this.stock = stock + formula.output.amount;
        }
    }

    /**
     * Produces base material (just from ORE)
     */
    protected static class BaseMaterialProducer extends MaterialProducer {
        double oreConsumed = 0D;

        public BaseMaterialProducer(Formula formula, Nanofactory factory) {
            super(formula, factory);
        }

        @Override
        public void react() {
            // consume ore
            this.oreConsumed = oreConsumed + formula.input.get(0).amount;
            // produce material
            this.stock = stock + formula.output.amount;
        }
    }

    protected static class Formula {
        List<Material> input;
        Material output;

        public Formula(List<Material> input, Material output) {
            this.input = input;
            this.output = output;
        }

        public boolean isInputIsOre() {
            return input.stream().allMatch(Material::isOre);
        }

        @Override
        public String toString() {
            return input
                    .stream()
                    .map(Material::toString)
                    .collect(Collectors.joining(","))
                    + " => "
                    + output;
        }
    }

    protected static class Material {
        String type;
        int amount;

        public Material(String type, int amount) {
            this.type = type;
            this.amount = amount;
        }

        public boolean isOre() {
            return "ORE".equals(type);
        }

        @Override
        public String toString() {
            return type + '=' + amount;
        }
    }

    private Formula toFormula(String source) {
        String[] parts = source.split("=>");
        List<Material> inputMaterials = toMaterials(parts[0]);
        Material outputMaterial = toMaterials(parts[1]).get(0);
        return new Formula(inputMaterials, outputMaterial);
    }

    private List<Material> toMaterials(String source) {
        Matcher matcher = INGREDIENT_PATTERN.matcher(source);
        List<Material> materials = new ArrayList<>();
        while (matcher.find()) {
            materials.add(new Material(matcher.group(3), Integer.parseInt(matcher.group(2))));
        }
        return materials;
    }
}
