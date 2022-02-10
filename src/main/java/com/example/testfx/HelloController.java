package com.example.testfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;

public class HelloController {
    @FXML
    private TextField inputFileName;

    @FXML
    private TableView<Row> tableView;

    @FXML
    private TableColumn<Row, String> columnNum;

    @FXML
    private TableColumn<Row, String> columnContent;

    @FXML
    private ObservableList<Row> dataRows = FXCollections.observableArrayList();


    @FXML
    protected void downloadFile() {
        String fileName = inputFileName.getText();
        int count = 0;

        try {
            File file = new File( "./src/main/files/" + fileName + ".txt");

            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);


            clearTable();

            String line = br.readLine();
            ++count;

            while (line != null) {
                dataRows.add(new Row(Integer.toString(count), line));

                line = br.readLine();
                ++count;
            }

            columnNum.setCellValueFactory(new PropertyValueFactory<Row, String>("number"));
            columnContent.setCellValueFactory(new PropertyValueFactory<Row, String>("content"));


            tableView.setItems(dataRows);
        } catch (FileNotFoundException e) {
            System.out.println("Cant find the file");
        } catch (IOException e) {
            System.out.println("Cant read the file");
        }
    }

    @FXML
    protected void clearTable() {
        dataRows.clear();
    }
}