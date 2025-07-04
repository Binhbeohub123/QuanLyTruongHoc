package randomcodemavuan;

import java.util.UUID;
import javax.swing.JTextField;

public class CaseUtils {


    public static String generateCaseCode() {
        return UUID.randomUUID()
                   .toString()
                   .replace("-", "")
                   .toUpperCase();
    }

    public static void assignCaseCodeToField(JTextField field) {
        field.setText(generateCaseCode());
    }
}
