package date;

import date.DatePickerComponent;
import java.awt.Component;
import java.awt.Rectangle;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DatePickerUtils {


    public static void autoReplaceDateFields(JPanel panel, DateFormat dateFormat) {
        Component[] components = panel.getComponents();

        for (Component comp : components) {
            if (comp instanceof JTextField && !(comp instanceof DatePickerComponent)) {
                JTextField tf = (JTextField) comp;
                String value = tf.getText().trim();

                // Xác định đây có phải là field nhập ngày không
                if (value.matches("\\d{1,2}/\\d{1,2}/\\d{4}") || value.equalsIgnoreCase("dd/MM/yyyy")) {
                    Rectangle bounds = tf.getBounds();
                    panel.remove(tf);

                    DatePickerComponent datePicker = new DatePickerComponent(dateFormat);
                    datePicker.setBounds(bounds);

                    // Nếu text có sẵn là ngày hợp lệ thì đặt luôn vào component mới
                    if (!value.equalsIgnoreCase("dd/MM/yyyy")) {
                        try {
                            Date parsed = dateFormat.parse(value);
                            datePicker.setSelectedDate(parsed);
                        } catch (ParseException ignored) {}
                    }

                    panel.add(datePicker);
                }
            }
        }

        panel.revalidate();
        panel.repaint();
    }
}
