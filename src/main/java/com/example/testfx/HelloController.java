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

import org.apache.poi.sl.image.ImageHeaderBitmap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.lang.Object;

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
    private void openFile() {

        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage) tableView.getScene().getWindow();

        try {
            AddNewColumnAndData(fileChooser, stage);
        } catch (IOException e) {
            System.out.println("Cant read the file");
        }
    }

    private void AddNewColumnAndData(FileChooser fileChooser, Stage stage) throws FileNotFoundException {
        File file = fileChooser.showOpenDialog(stage);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String extension = file.getName().split("\\.")[1];
        String size = String.format("%.3f", (double) file.length() / 1024) + " Кб";

        columnExtension.setCellValueFactory(new PropertyValueFactory<Row, String>("extension"));
        columnSize.setCellValueFactory(new PropertyValueFactory<Row, String>("size"));

        AddColumnByExtensions(file, br, extension, size, (int)file.length());

        tableView.setItems(dataRows);
    }

    private void AddColumnByExtensions(File file, BufferedReader br, String extension, String size, Integer fullSize) {
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
            case ("jpg"), ("png") -> {
                Image image = new Image(file.getAbsolutePath());
                Integer width = (int)image.getWidth();
                Integer height = (int)image.getHeight();

                size += "\n" + width + "x" + height;
                ImageView imageView = new ImageView(image);
                addColumn(extension, size, imageView);
            }
            case ("bmp") -> {
                Image image = new Image(file.getAbsolutePath());
                //14 первых байт - BITMAPFILEHEADER
                //4 след байта - размер структуры в байтах, по размеру определяется версия
                //4 след байта - ширина растра
                //4 след байта - высота растра
                //2 след байта - значение 1, для значков в винде
                //2 след байта - кол-во бит на пиксель
                byte[] arrBytes = getBinaryFile(file.getAbsolutePath());
                Integer deep = arrBytes[28] * (int)Math.pow(256, 0) + arrBytes[29] * (int)Math.pow(256, 1);
                escapeNegativeNumbers(arrBytes, 18, 4);
                Integer width = arrBytes[18] * (int)Math.pow(256, 0) + arrBytes[19] * (int)Math.pow(256, 1) +
                        arrBytes[20] * (int)Math.pow(256, 2) + arrBytes[21] * (int)Math.pow(256, 3);
                escapeNegativeNumbers(arrBytes, 22, 4);
                Integer height = arrBytes[22] * (int)Math.pow(256, 0) + arrBytes[23] * (int)Math.pow(256, 1) +
                        arrBytes[24] * (int)Math.pow(256, 2) + arrBytes[25] * (int)Math.pow(256, 3);

                Integer typeStorage = arrBytes[30] * (int)Math.pow(256, 0) + arrBytes[31] * (int)Math.pow(256, 1) +
                        arrBytes[32] * (int)Math.pow(256, 2) + arrBytes[33] * (int)Math.pow(256, 3);

                size += "\n" + width + "x" + height + "\n"
                        + deep + " бит на пиксель" + "\n"
                        + getTypeStorageDesc(typeStorage) + " способ хранения" + "\n";

                Integer versionSize = arrBytes[14] * (int)Math.pow(256, 0) + arrBytes[15] * (int)Math.pow(256, 1) +
                        arrBytes[16] * (int)Math.pow(256, 2) + arrBytes[17] * (int)Math.pow(256, 3);

                if (versionSize == 108) {
                    Integer typeColorSpace = arrBytes[74] * (int)Math.pow(256, 0) + arrBytes[75] * (int)Math.pow(256, 1) +
                            arrBytes[76] * (int)Math.pow(256, 2) + arrBytes[77] * (int)Math.pow(256, 3);
                    size += "\n" + typeColorSpace + " вид цветового пространства";
                } else if (versionSize == 124) {
                    Integer typeColorSpace = arrBytes[74] * (int)Math.pow(256, 0) + arrBytes[75] * (int)Math.pow(256, 1) +
                            arrBytes[76] * (int)Math.pow(256, 2) + arrBytes[77] * (int)Math.pow(256, 3);
                    Integer renderPrefer = arrBytes[126] * (int)Math.pow(256, 0) + arrBytes[127] * (int)Math.pow(256, 1) +
                            arrBytes[128] * (int)Math.pow(256, 2) + arrBytes[129] * (int)Math.pow(256, 3);

                    size += "\n" + typeColorSpace + " вид цветового пространства" +
                            "\n" + renderPrefer + " предпочтение при рендеринге растра";
                }

                ImageView imageView = new ImageView(image);
                addColumn(extension, size, imageView);
            }
            default -> {
                Text text = new Text("Нет такого расширения");
                text.setWrappingWidth(410);
                addColumn(extension, size, text);
            }
        }
    }
    private void escapeNegativeNumbers(byte[] arr, Integer start, Integer count) {
        for (Integer i = start; i < start + count; ++i) {
            arr[i] = (arr[i] > 0) ? arr[i] : (byte) (256 + arr[i]);
        }
    }

    private String getTypeStorageDesc(Integer type) {
        switch (type) {
            case (0):
                return "двумерный массив";
            case (1), (2):
                return "RLE кодирование";
            case (3):
                return "двумерный массив с масками цветовых каналов";
            case (4):
                return "встроенный JPEG файл";
            case (5):
                return "встроенный PNG файл";
            case (6):
                return "двумерный массив с масками цветовых каналов и альфа-каналов";
        }
        return "";
    }

    private byte[] getBinaryFile(String path) {
        File bFile = new File(path);
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(bFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            try {
                //byte[] content = Files.readAllBytes(Paths.get(bfile.getAbsolutePath()));
                byte[] data = new byte[bufferedInputStream.available()];
                bufferedInputStream.read(data);
                return data;
            } finally {
                bufferedInputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void clearTable() {
        dataRows.clear();
    }

    protected <T> void addColumn(String extension, String size, T data) {
        dataRows.add(new Row(extension, size, data));

        TableColumn<Row, T> tableColumn;
        tableColumn = getLastColumn();
        tableColumn.setCellValueFactory(new PropertyValueFactory<Row, T>("content"));

        if (tableView.getColumns().size() == 2) {
            tableView.getColumns().add(tableColumn);
        }
    }

    private  <T> TableColumn<Row, T> getLastColumn() {
        if (tableView.getColumns().size() == 2) {
            TableColumn<Row, T> tableColumn = new TableColumn<Row, T>();
            tableColumn.setText("Содержимое");
            tableColumn.setPrefWidth(413);
            return tableColumn;
        } else {
            return (TableColumn<Row, T>)tableView.getColumns().get(2);
        }
    }


    private String getTxtContent(BufferedReader br) {
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

    private String getDocxContent(String path) {
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

    private String getXlsxContent(String path) {
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

    private String getPptxContent(String path) {
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