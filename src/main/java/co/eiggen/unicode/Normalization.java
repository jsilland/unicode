package co.eiggen.unicode;

import com.ibm.icu.text.Normalizer2;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Normalization {
    private static class ComparisonUnit {
        private final String left;
        private final String right;

        ComparisonUnit(String left, String right) {
            this.left = left;
            this.right = right;
        }

        public boolean areNormalizedFormsEqual(Normalizer2 normalizer) {
            String normalizedLeft = normalizer.normalize(this.left);
            String normalizedRight = normalizer.normalize(this.right);
            return normalizedLeft.equals(normalizedRight);
        }
    }

    public static void main(String... args) {
        Normalizer2 nfkdNormalizer = Normalizer2.getNFKDInstance();
        Normalizer2 nfkcNormalizer = Normalizer2.getNFKCInstance();

        List<Normalizer2> normalizers = new ArrayList<Normalizer2>();
        normalizers.add(Normalizer2.getNFDInstance());
        normalizers.add(Normalizer2.getNFCInstance());
        normalizers.add(nfkdNormalizer);
        normalizers.add(nfkcNormalizer);

        List<ComparisonUnit> units = new ArrayList<ComparisonUnit>();
        units.add(new ComparisonUnit("foo", "bar"));
        units.add(new ComparisonUnit("ﬁ", "fi"));
        units.add(new ComparisonUnit("Å", "Å"));
        units.add(new ComparisonUnit("ｶ", "カ"));
        units.add(new ComparisonUnit("⑦", "7"));
        units.add(new ComparisonUnit("8", "⁸"));
        units.add(new ComparisonUnit("ⅷ", "viii"));
        units.add(new ComparisonUnit(
            "ñ",
            new String(new byte[]{(byte)0x6E, (byte)0xCC, (byte)0x83}, Charset.forName("UTF-8"))));
        units.add(new ComparisonUnit(
            "ế",
            new String(new byte[]{(byte)0x65, (byte)0xCC, (byte)0x82, (byte)0xCC, (byte)0x81}, Charset.forName("UTF-8"))));
        units.add(new ComparisonUnit(
            "ế",
            new String(new byte[]{(byte)0x65, (byte)0xCC, (byte)0x81, (byte)0xCC, (byte)0x82}, Charset.forName("UTF-8"))));
        units.add(new ComparisonUnit(
            "ế",
            new String(new byte[]{(byte)0xC3, (byte)0xAA, (byte)0xCC, (byte)0x81}, Charset.forName("UTF-8"))));
        units.add(new ComparisonUnit(
            new String(new byte[]{(byte)0x65, (byte)0xCC, (byte)0x82, (byte)0xCC, (byte)0x81}, Charset.forName("UTF-8")),
            new String(new byte[]{(byte)0x65, (byte)0xCC, (byte)0x81, (byte)0xCC, (byte)0x82}, Charset.forName("UTF-8"))));

        System.out.println("Normalization forms comparison");
        Utils.Table table = Utils.Table.newTableWithHeaders(
            "Left String", "Right String", "Left codepoints", "Right codepoints",
            "NFD", "NFC", "NFKD", "NFKC");
        for (ComparisonUnit unit : units) {
            List<String> row = new ArrayList<>();
            row.add(unit.left);
            row.add(Utils.bmpCodePointsInfo(unit.left));
            row.add(unit.right);
            row.add(Utils.bmpCodePointsInfo(unit.right));
            for (Normalizer2 normalizer : normalizers) {
                row.add(String.format("%b", unit.areNormalizedFormsEqual(normalizer)));
            }
            table.addRow(row);
        }

        System.out.println(table.toMarkDown());

        List<String> inputs = new ArrayList<String>();
        inputs.add("foo");
        inputs.add("ﬁ");
        inputs.add("ñ");
        inputs.add("Å");
        inputs.add("Å");
        inputs.add("ế");

        System.out.println("Composed v. Decomposed normlization");
        table = Utils.Table.newTableWithHeaders("Input", "Codepoints", "NFKD =? NFKC");
        for (String input : inputs) {
            table.addRow(
                input,
                Utils.bmpCodePointsInfo(input),
                String.format("%b", nfkcNormalizer.normalize(input).equals(nfkdNormalizer.normalize(input)))
            );
        }
        System.out.println(table.toMarkDown());
    }
}