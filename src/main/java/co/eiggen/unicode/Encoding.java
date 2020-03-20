package co.eiggen.unicode;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Encoding {
    public static void main(String... args) {
        String[] inputs = new String[] {"a", "➤", "𓃦", "😍"};
        String[] encodings = new String[] {"UTF-32", "UTF-16", "UTF-8"};
        for (String encoding : encodings) {
            Charset javaEncoding = Charset.forName(encoding);
            System.out.println(encoding);

            Utils.Table table = Utils.Table.newTableWithHeaders(
                "Codepoint", "Name", "Glyph", "Byte 1", "Byte 2", "Byte 3", "Byte 4");

            for (String input : inputs) {
                int codePoint = input.codePointAt(0);
                
                List<String> row = new ArrayList<>();
                row.add(Utils.uNumber(codePoint));
                row.add(Character.getName(codePoint));
                row.add(input);


                byte[] bytes = input.getBytes(javaEncoding);

                // Special-case UTF-16, as the encoder will always prefix the string
                // with a BOM, which is undesirable for the examples
                if ("UTF-16".equals(javaEncoding.name())) {
                    bytes = Arrays.copyOfRange(bytes, 2, bytes.length);
                }
                for (byte b : bytes) {
                    row.add(Utils.hex(b));
                }

                table.addRow(row);
            }

            System.out.println(table.toMarkDown());
        }
    }
}