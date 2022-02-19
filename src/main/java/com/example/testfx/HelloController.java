package com.example.testfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

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

            columnExtension.setCellValueFactory(new PropertyValueFactory<Row, String>("extension"));
            columnSize.setCellValueFactory(new PropertyValueFactory<Row, String>("size"));

            deleteLastColumn();


            if (Objects.equals(extension, "txt")) {
                String line = br.readLine();
                String text = line;

                while (line != null) {
                    line = br.readLine();
                    text += line + '\n';
                }
                addColumn(extension, size, text);
            } else if (Objects.equals(extension, "docx")) {
                String text = "";
                FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
                XWPFDocument document = new XWPFDocument(fileInputStream);

                var paragraphs = document.getParagraphs();
                for (org.apache.poi.xwpf.usermodel.XWPFParagraph par : paragraphs) {
                    text += par.getText() + '\n';
                }
                fileInputStream.close();

                addColumn(extension, size, text);
            }

            else if (Objects.equals(extension, "jpg")) {
                ImageView imageView = new ImageView(new Image(file.getAbsolutePath()));
                addColumn(extension, size, imageView);
            }


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
    @FXML
    protected void deleteLastColumn() {
        if (tableView.getColumns().size() == 3) {
            tableView.getColumns().remove(2);
        }
    }
    @FXML
    protected <T> void addColumn(String extension, String size, T data) {
        dataRows.add(new Row(extension, size, data));
        TableColumn<Row, T> tableColumn = new TableColumn<>();
        tableColumn.setPrefWidth(400);
        tableColumn.setText("Содержимое");
        tableColumn.setCellValueFactory(new PropertyValueFactory<Row, T>("content"));

        tableView.getColumns().add(tableColumn);
    }

}