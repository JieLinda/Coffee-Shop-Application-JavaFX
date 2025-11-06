package com.example.pboproject.controllers;

import com.example.pboproject.dao.ReportDAO;
import com.example.pboproject.utils.ConnectionManager;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {
    @FXML
    private ChoiceBox<String> cbReportType;

    @FXML
    private Button btnSubmit;

    @FXML
    private ImageView btnExport;

    @FXML
    private TableView<ObservableList<String>> tblReport;

    @FXML
    private TableColumn<ObservableList<String>, String> colId;

    @FXML
    private TableColumn<ObservableList<String>, String> colProduct;

    @FXML
    private TableColumn<ObservableList<String>, String> colQuantitySold;

    @FXML
    private TableColumn<ObservableList<String>, String> colTotalSales;
    @FXML
    private DatePicker dpStartDate;

    @FXML
    private DatePicker dpEndDate;
    private ReportDAO reportDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reportDAO = new ReportDAO();
        System.out.println("ReportDAO initialized" + reportDAO != null);
        cbReportType.setItems(FXCollections.observableArrayList(
                "Top 5 Selling Drinks",
                "Sales by Sizes",
                "Topping Popularity",
                "Top 5 Payment Methods",
                "Sales Report by Period",
                "Top Selling Products by Period",
                "Most Active Members by Period",
                "Promo Usage by Period"
        ));

        btnSubmit.setOnAction(e -> generateReport());
//        btnPDF.setOnAction(e -> exportToPDF());
//        btnExcel.setOnAction(e -> exportToExcel());
        System.out.println("ChoiceBox initialized with items: " + cbReportType.getItems());
    }


    private void generateReport() {
        String reportType = cbReportType.getValue();
        if (reportType == null) {
            return;
        }

        LocalDate start = dpStartDate.getValue();
        LocalDate end = dpEndDate.getValue();
        Date startDate = Date.valueOf(start);
        Date endDate = Date.valueOf(end);

        System.out.println("Selected " + reportType);
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        List<String> columnNames = reportDAO.getColumnNames(reportType);

        switch (reportType) {
            case "Top 5 Selling Drinks":
                data = reportDAO.getTopSellingDrinks(startDate, endDate);
                break;
            case "Sales by Sizes":
                data = reportDAO.getSalesBySizes(startDate, endDate);
                break;
            case "Topping Popularity":
                data = reportDAO.getToppingPopularity(startDate, endDate);
                break;
            case "Top 5 Payment Methods":
                data = reportDAO.getTopPaymentMethods(startDate, endDate);
                break;
            case "Sales Report by Period":
                data = reportDAO.getSalesReportByPeriod(startDate, endDate);
                break;
            case "Top Selling Products by Period":
                data = reportDAO.getTopSellingProductsByPeriod(startDate, endDate);
                break;
            case "Most Active Members by Period":
                data = reportDAO.getMostActiveMembersByPeriod(startDate, endDate);
                break;
            case "Promo Usage by Period":
                data = reportDAO.getPromoUsageByPeriod(startDate, endDate);
                break;
        }
        setupTableColumns(FXCollections.observableArrayList(columnNames));
        tblReport.setItems(data);
        tblReport.refresh();
    }

    private void setupTableColumns(ObservableList<String> columnNames) {
//        tblReport.getColumns().clear();
//
//        for (int i = 0; i < columnNames.size(); i++) {
//            final int colIndex = i;
//            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames.get(i));
//            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(colIndex)));
//            tblReport.getColumns().add(column);
//        }
        tblReport.getColumns().clear();

        for (int i = 0; i < columnNames.size(); i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames.get(i));
            column.setCellValueFactory(cellData -> {
                if (colIndex < cellData.getValue().size()) {
                    return new SimpleStringProperty(cellData.getValue().get(colIndex));
                } else {
                    System.err.println("Index " + colIndex + " out of bounds for row data: " + cellData.getValue());
                    return new SimpleStringProperty("");
                }
            });
            tblReport.getColumns().add(column);
        }
    }

    @FXML
    public void export() {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter excelFilter = new FileChooser.ExtensionFilter("Microsoft Excel Spreadsheet (*.xlsx)", "*.xlsx");
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("Portable Document Format files (*.pdf)", "*.pdf");
        chooser.getExtensionFilters().add(pdfFilter);
        chooser.getExtensionFilters().add(excelFilter);

        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = chooser.showSaveDialog(tblReport.getScene().getWindow());
        FileChooser.ExtensionFilter selectedFilter = chooser.getSelectedExtensionFilter();

        if (file != null) {
            if (selectedFilter.getExtensions().get(0).equalsIgnoreCase("*.xlsx")) {
                exportToExcel(file);
            } else if (selectedFilter.getExtensions().get(0).equalsIgnoreCase("*.pdf")) {
                exportToPdf(file);
            }
        }
    }

    private void exportToPdf(File file) {
        try (PdfWriter writer = new PdfWriter(file);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("Report"));

            int numColumns = tblReport.getColumns().size();
            Table table = new Table(numColumns);

            for (TableColumn<ObservableList<String>, ?> column : tblReport.getColumns()) {
                table.addHeaderCell(new Cell().add(new Paragraph(column.getText())));
            }

            for (ObservableList<String> row : tblReport.getItems()) {
                for (String cell : row) {
                    table.addCell(new Cell().add(new Paragraph(cell)));
                }
            }

            document.add(table);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportToExcel(File file) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Report");

        FileOutputStream out = null;

        try {
            int rowid = 0;

            XSSFRow headerRow = spreadsheet.createRow(rowid++);
            for (int i = 0; i < tblReport.getColumns().size(); i++) {
                XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(tblReport.getColumns().get(i).getText());
            }

            for (int i = 0; i < tblReport.getItems().size(); i++) {
                XSSFRow row = spreadsheet.createRow(rowid++);
                ObservableList<String> rowData = tblReport.getItems().get(i);
                for (int j = 0; j < rowData.size(); j++) {
                    XSSFCell cell = row.createCell(j);
                    cell.setCellValue(rowData.get(j));
                }
            }

            out = new FileOutputStream(file);
            workbook.write(out);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}