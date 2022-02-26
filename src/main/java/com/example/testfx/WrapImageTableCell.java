package com.example.testfx;

import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class WrapImageTableCell<S> extends TableCell<S, ImageView> {

    private ImageView image;

    @Override
    protected void updateItem(ImageView item, boolean empty) {
        super.updateItem(item, empty);
        if (image != null) {
            image.imageProperty().unbind();
        }
        if (empty || item == null) {
            setGraphic(null);
        } else {
            if (image == null) {
                image = new ImageView();
//                text.wrappingWidthProperty().bind(getTableColumn().widthProperty());
            }
            image.imageProperty().bind(item.imageProperty());
            setGraphic(image);
        }
    }

}
