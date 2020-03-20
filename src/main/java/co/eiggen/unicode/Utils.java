package co.eiggen.unicode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static String uNumber(int codepoint) {
        return String.format("U+%s", Integer.toHexString(codepoint).toUpperCase());
    }

    public static String hex(byte b) {
        int castByte = (int) b;
        castByte = castByte < 0 ? castByte + 256 : castByte;
        String hexByte = Integer.toString(castByte, 16).toUpperCase();
        hexByte = hexByte.length() == 1 ? "0" + hexByte : hexByte;
        return String.format("0x%s", hexByte);
    }

    public static String bmpCodePointsInfo(String string) {
        return string.codePoints()
            .mapToObj(i -> bmpCodePointInfo(i))
            .collect(Collectors.joining("<br>"));
    }

    public static String bmpCodePointInfo(int codePoint) {
        String name = Character.getName(codePoint);
        char character = Character.toChars(codePoint)[0];
        String uNumber = uNumber(codePoint);

        return String.format("%s `%s`", uNumber, name, character);
    }

    public static class Table {
        private final List<String> headers;
        private final List<List<String>> rows;

        private Table(List<String> headers) {
            this.headers = new ArrayList<>(headers);
            this.rows = new ArrayList<>();
        }

        public static Table newTableWithHeaders(List<String> headers) {
            return new Table(headers);
        }

        public static Table newTableWithHeaders(String... headers) {
            return new Table(Arrays.asList(headers));
        }

        public void addRow(List<String> row) {
            if (row.size() > headers.size()) {
                throw new IllegalStateException();
            }
            final List<String> copiedRow = new ArrayList<>(row); 
            if (copiedRow.size() < headers.size()) {
                while (copiedRow.size() < 4) {
                    copiedRow.add("");
                } 
            }
            rows.add(copiedRow);
        }

        public void addRow(String... elements) {
            addRow(Arrays.asList(elements));
        }

        public String toMarkDown() {
            StringBuilder result = new StringBuilder();
            result.append(headers.stream().collect(Collectors.joining(" | ", "| ", " |")));
            result.append("\n");
            result.append(headers.stream().map(s -> "---").collect(Collectors.joining("|", "|", "|")));
            result.append("\n");
            for (List<String> row : rows) {
                result.append(row.stream().collect(Collectors.joining(" | ", "| ", " |")));
                result.append("\n");
            }
            return result.toString();
        }
    }
}