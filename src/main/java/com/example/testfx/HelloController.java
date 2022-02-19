package com.example.testfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;

public class HelloController {
    //Required variables for input and output data
    @FXML
    private TableView<Row> tableView;

    @FXML
    private TableColumn<Row, String> columnExtension;

    @FXML
    private TableColumn<Row, String> columnSize;

    @FXML
    private TableColumn<Row, String> columnContent;
    //Required array for storage data
    @FXML
    private ObservableList<Row> dataRows = FXCollections.observableArrayList();
    //Method for downloading file and output data to table
    @FXML
    protected void openFile() {
        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage) tableView.getScene().getWindow();

        try {
            File file = fileChooser.showOpenDialog(stage);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);


            String extension = file.getName().split("\\.")[1];
            String size = String.format("%.3f", (double)file.length() / 1024) + " Кб";

            //Binding columns to class property and setting array data to table
            columnExtension.setCellValueFactory(new PropertyValueFactory<Row, String>("extension"));
            columnSize.setCellValueFactory(new PropertyValueFactory<Row, String>("size"));


            if (Objects.equals(extension, "txt")) {
                String content = br.readLine();

                String line = br.readLine();

                //Reading file lines and adding instance of class to data array
                while (line != null) {
//                ImageView imageView = new ImageView(new Image("C:\\Users\\ACER\\Desktop\\Projects\\java-fx-app\\src\\main\\files\\"+ "img2" + ".jpg"));
                    line = br.readLine();
                    content += line + '\n';
                }
                dataRows.add(new Row(extension, size, content));
                //Binding columns to class property and setting array data to table
                TableColumn<Row, String> tableColumn = new TableColumn<>();
                tableColumn.setPrefWidth(400);
                tableColumn.setText("Содержимое");
                tableColumn.setCellValueFactory(new PropertyValueFactory<Row, String>("content"));

                tableView.getColumns().add(tableColumn);

            }
//            } else {
//                //Binding columns to class property and setting array data to table
//                columnContent.setCellValueFactory(new PropertyValueFactory<Row, ImageView>("content"));
//            }



            tableView.setItems(dataRows);
        } catch (IOException e) {
            System.out.println("Cant read the file");
        }
    }
    //Method for clearing data from array
    @FXML
    protected void clearTable() {
        dataRows.clear();
    }
}