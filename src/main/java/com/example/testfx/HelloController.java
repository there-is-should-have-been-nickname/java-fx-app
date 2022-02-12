package com.example.testfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class HelloController {
    //Required variables for input and output data
    @FXML
    private TableView<Row> tableView;

    @FXML
    private TableColumn<Row, String> columnNum;

    @FXML
    private TableColumn<Row, String> columnContent;

    @FXML
    private TableColumn<Row, ImageView> columnImageView;
    //Required array for storage data
    @FXML
    private ObservableList<Row> dataRows = FXCollections.observableArrayList();
    //Method for downloading file and output data to table
    @FXML
    protected void openFile() {
        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage) tableView.getScene().getWindow();

        int count = 0;

        try {

            File file = fileChooser.showOpenDialog(stage);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            //Clearing table if table isn't empty
            clearTable();

            String line = br.readLine();
            ++count;
            //Reading file lines and adding instance of class to data array
            while (line != null) {
                String content = line.split(",")[0];
                String filePath = line.split(",")[1];
                ImageView imageView = new ImageView(new Image("C:\\Users\\ACER\\Desktop\\Projects\\java-fx-app\\src\\main\\files\\"+ filePath + ".jpg"));

                dataRows.add(new Row(Integer.toString(count), content, imageView));

                line = br.readLine();

                ++count;
            }
            //Binding columns to class property and setting array data to table
            columnNum.setCellValueFactory(new PropertyValueFactory<Row, String>("number"));
            columnContent.setCellValueFactory(new PropertyValueFactory<Row, String>("content"));
            columnImageView.setCellValueFactory(new PropertyValueFactory<Row, ImageView>("imageView"));
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