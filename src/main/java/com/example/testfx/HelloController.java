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

import javafx.util.Callback;
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

            switch (extension) {
                case ("txt") -> {
                    String content = getTxtContent(br);
                    Text text = new Text(content);
                    text.setWrappingWidth(410);
                    addColumn(extension, size, text);
                }
                case ("docx") -> {
                    String content2 = getDocxContent(file.getAbsolutePath());
                    Text text2 = new Text(content2);
                    text2.setWrappingWidth(410);
                    addColumn(extension, size, text2);
                }
                case ("xlsx") -> {
                    String content3 = getXlsxContent(file.getAbsolutePath());
                    Text text3 = new Text(content3);
                    text3.setWrappingWidth(410);
                    addColumn(extension, size, text3);
                }
                case ("pptx") -> {
                    String content4 = getPptxContent(file.getAbsolutePath());
                    Text text4 = new Text(content4);
                    text4.setWrappingWidth(410);
                    addColumn(extension, size, text4);
                }
                case ("jpg"), ("png"), ("bmp") -> {
                    ImageView imageView = new ImageView(new Image(file.getAbsolutePath()));
                    addColumn(extension, size, imageView);
                }
                default -> {
                    Text text = new Text("Нет такого расширения");
                    text.setWrappingWidth(410);
                    addColumn(extension, size, text);
                }
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
    protected <T> void addColumn(String extension, String size, T data) {
        dataRows.add(new Row(extension, size, data));

        TableColumn<Row, T> tableColumn;
        tableColumn = getLastColumn();
        tableColumn.setCellValueFactory(new PropertyValueFactory<Row, T>("content"));

        if (tableView.getColumns().size() == 2) {
            tableView.getColumns().add(tableColumn);
        }
    }

    @FXML
    protected <T> TableColumn<Row, T> getLastColumn() {
        if (tableView.getColumns().size() == 2) {
            TableColumn<Row, T> tableColumn = new TableColumn<Row, T>();
            tableColumn.setText("Содержимое");
            tableColumn.setPrefWidth(413);
            return tableColumn;
        } else {
            return (TableColumn<Row, T>)tableView.getColumns().get(2);
        }
    }


    @FXML
    protected String getTxtContent(BufferedReader br) {
        String text = "";

        try {
            String line = br.readLine();

            while (line != null) {
                text += line + '\n';
                line = br.readLine();
            }
        } catch (Exception err) {
            System.out.println("Cant read the file");
        }

        return text;
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