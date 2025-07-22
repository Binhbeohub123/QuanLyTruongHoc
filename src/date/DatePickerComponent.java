package date;

import com.toedter.calendar.JDateChooser;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;
import javax.swing.JPanel;

public class DatePickerComponent extends JDateChooser {
    private Consumer<Date> dateChangeListener;

    public DatePickerComponent() {
        this(new SimpleDateFormat("dd/MM/yyyy"));
    }

    public DatePickerComponent(DateFormat format) {
        setDateFormatString(((SimpleDateFormat) format).toPattern());
        setDate(new Date());
        addPropertyChangeListener("date", e -> {
            if (dateChangeListener != null) {
                dateChangeListener.accept(getDate());
            }
        });
    }

    public void setDateChangeListener(Consumer<Date> listener) {
        this.dateChangeListener = listener;
    }

    public Date getSelectedDate() {
        return getDate();
    }

    public void setSelectedDate(Date date) {
        setDate(date);
    }
}
