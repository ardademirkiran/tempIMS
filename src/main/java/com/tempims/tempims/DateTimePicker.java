package com.tempims.tempims;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class DateTimePicker extends DatePicker {
    private final SimpleObjectProperty<LocalDateTime> dateTimeValue;
    private final DateTimeFormatter formatter;

    public DateTimePicker(DateTimeFormatter formatter, LocalDateTime localDateTime) {
        super();
        this.formatter = formatter;
        this.dateTimeValue = new SimpleObjectProperty<>(localDateTime);
        this.setConverter(new StringConverter<LocalDate>() {
            public String toString(LocalDate value) {
                return DateTimePicker.this.dateTimeValue.get() != null
                        ? DateTimePicker.this.dateTimeValue.get().format(DateTimePicker.this.getFormatter())
                        : "";
            }

            public LocalDate fromString(String value) {
                if (value == null) {
                    DateTimePicker.this.dateTimeValue.set(null);
                    return null;
                } else {
                    DateTimePicker.this.dateTimeValue.set(LocalDateTime.parse(value, DateTimePicker.this.getFormatter()));
                    return DateTimePicker.this.dateTimeValue.get().toLocalDate();
                }
            }
        });

        this.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                DateTimePicker.this.dateTimeValue.set(null);
            } else if (DateTimePicker.this.dateTimeValue.get() == null) {
                DateTimePicker.this.dateTimeValue.set(LocalDateTime.of(newValue, LocalTime.now()));
            } else {
                LocalTime time = DateTimePicker.this.dateTimeValue.get().toLocalTime();
                DateTimePicker.this.dateTimeValue.set(LocalDateTime.of(newValue, time));
            }
        });

        this.dateTimeValue.addListener((observable, oldValue, newValue) -> {
            DateTimePicker.this.valueProperty().set(newValue != null ? newValue.toLocalDate() : null);
        });

        this.getEditor().textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                simulateEnterPressed();
            }
        });
    }

    private void simulateEnterPressed() {
        this.getEditor().fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER, false, false, false, false));
    }

    public SimpleObjectProperty<LocalDateTime> dateTimeValueProperty() {
        return this.dateTimeValue;
    }

    public DateTimeFormatter getFormatter() {
        return this.formatter;
    }
}
