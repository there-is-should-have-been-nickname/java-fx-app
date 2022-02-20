package com.example.testfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.*;
import java.util.List;
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
            String size = String.format("%.3f", (double) file.length() / 1024) + " Кб";

            columnExtension.setCellValueFactory(new PropertyValueFactory<Row, String>("extension"));
            columnSize.setCellValueFactory(new PropertyValueFactory<Row, String>("size"));

            deleteLastColumn();


            if (Objects.equals(extension, "txt")) {
                String line = br.readLine();
                String text = line;

                while (line != null) {
                    text += line + '\n';
                    line = br.readLine();
                }
                addColumn(extension, size, text);
            } else if (Objects.equals(extension, "docx")) {
                String content = getDocxContent(file.getAbsolutePath());
                addColumn(extension, size, content);
            } else if (Objects.equals(extension, "xlsx")) {
                String content = getXlsxContent(file.getAbsolutePath());
                addColumn(extension, size, content);
            } else if (Objects.equals(extension, "pptx")) {
                String content = getPptxContent(file.getAbsolutePath());
                addColumn(extension, size, content);
            } else if (Objects.equals(extension, "jpg")
                    || Objects.equals(extension, "png")
                    || Objects.equals(extension, "bmp")) {
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

//        tableColumn.setCellFactory(param -> {
//            TableCell<Row, T> cell = new TableCell<>();
//            cell.setWrapText(true);
//            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
//            return cell;
//        });
        tableColumn.setCellValueFactory(new PropertyValueFactory<Row, T>("content"));

        tableView.getColumns().add(tableColumn);
    }

    @FXML
    protected String getDocxContent(String path) {
        String text = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            XWPFDocument document = new XWPFDocument(fileInputStream);

            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (org.apache.poi.xwpf.usermodel.XWPFParagraph par : paragraphs) {
                text += par.getText() + '\n';
            }
            fileInputStream.close();
        } catch (Exception err) {
            System.out.println("Cant read the file");
        }
        return text;
    }

    @FXML
    protected String getXlsxContent(String path) {
        String text = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            XSSFWorkbook document = new XSSFWorkbook(fileInputStream);

            XSSFRow rows = document.getSheetAt(0).getRow(1);
            for (Cell row : rows) {
                text += row.getStringCellValue() + '\n';
            }
            fileInputStream.close();
        } catch (Exception err) {
            System.out.println("Cant read the file");
        }
        return text;
    }

    @FXML
    protected String getPptxContent(String path) {
        String text = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            XMLSlideShow document = new XMLSlideShow(fileInputStream);

            List<XSLFSlide> slides = document.getSlides();
            for (XSLFSlide slide : slides) {
                text += slide.getTitle() + '\n';
            }
            fileInputStream.close();
        } catch (Exception err) {
            System.out.println("Cant read the file");
        }
        return text;
    }
}