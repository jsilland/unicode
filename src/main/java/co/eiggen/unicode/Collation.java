package co.eiggen.unicode;

import com.ibm.icu.text.CollationKey;
import com.ibm.icu.text.Collator;
import com.ibm.icu.util.ULocale;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Collation {
  public static void main(String... args) {
    ULocale germanPhoneBookLocale = ULocale.GERMAN.setKeywordValue("collation", "phonebook");
    Collator germanPhoneBookCollator = Collator.getInstance(germanPhoneBookLocale);
    
    List<String> words = new ArrayList<String>();
    words.add("Affekt");
    words.add("Äquivalent");

    for (String word : words) {
      byte[] collationKey = germanPhoneBookCollator.getCollationKey(word).toByteArray();
      BigInteger collationKeyAsInteger = new BigInteger(1, collationKey);
      System.out.printf("Collation key for %s is %s\n", word,  collationKeyAsInteger.toString(16));
    }

    System.out.printf("Initial order: %s\n", words.toString());

    Collections.sort(words, germanPhoneBookCollator);

    System.out.printf("Sorted order: %s\n", words.toString());
  }
}
