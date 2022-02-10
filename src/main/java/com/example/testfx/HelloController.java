package com.example.testfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;

public class HelloController {
    //Required variables for input and output data
    @FXML
    private TextField inputFileName;

    @FXML
    private TableView<Row> tableView;

    @FXML
    private TableColumn<Row, String> columnNum;

    @FXML
    private TableColumn<Row, String> columnContent;
    //Required array for storage data
    @FXML
    private ObservableList<Row> dataRows = FXCollections.observableArrayList();
    //Method for downloading file and output data to table
    @FXML
    protected void downloadFile() {
        String fileName = inputFileName.getText();
        int count = 0;

        try {
            File file = new File( "./src/main/files/" + fileName + ".txt");

            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            //Clearing table if table isn't empty
            clearTable();

            String line = br.readLine();
            ++count;
            //Reading file lines and adding instance of class to data array
            while (line != null) {
                dataRows.add(new Row(Integer.toString(count), line));

                line = br.readLine();
                ++count;
            }
            //Binding columns to class property and setting array data to table
            columnNum.setCellValueFactory(new PropertyValueFactory<Row, String>("number"));
            columnContent.setCellValueFactory(new PropertyValueFactory<Row, String>("content"));
            tableView.setItems(dataRows);
        } catch (FileNotFoundException e) {
            System.out.println("Cant find the file");
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