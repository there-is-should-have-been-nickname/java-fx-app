package com.example.testfx;

import javafx.scene.control.TableCell;
import javafx.scene.text.Text;

public class WrapTextTableCell<S> extends TableCell<S, String> {

    private Text text;

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (text != null) {
            text.textProperty().unbind();
        }
        if (empty || item == null) {
            setGraphic(null);
        } else {
            if (text == null) {
                text = new Text();
                text.wrappingWidthProperty().bind(getTableColumn().widthProperty());
            }
            text.textProperty().bind(itemProperty());
            setGraphic(text);
        }
    }
}
