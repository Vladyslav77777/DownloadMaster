import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.scene.control.cell.ProgressBarTableCell;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadManager {

    private BorderPane root;
    private TextField urlField;
    private Button downloadButton;
    private TableView<Download> tableView;
    private DownloadTableController tableController;
    private Settings settings;

    public DownloadManager() {
        root = new BorderPane();
        settings = new Settings();

        // Верхняя панель с URL-полем и кнопкой загрузки
        HBox topPanel = new HBox(10);
        topPanel.setPadding(new Insets(10));

        urlField = new TextField();
        urlField.setPromptText("Введите URL файла");

        downloadButton = new Button("Загрузить");
        downloadButton.setOnAction(e -> startDownload());

        Button settingsButton = new Button("Настройки");
        settingsButton.setOnAction(e -> showSettingsDialog());

        Button exportButton = new Button("Экспорт");
        exportButton.setOnAction(e -> exportDownloads());

        Button importButton = new Button("Импорт");
        importButton.setOnAction(e -> importDownloads());

        topPanel.getChildren().addAll(urlField, downloadButton, settingsButton, exportButton, importButton);
        root.setTop(topPanel);

        // Таблица загрузок
        tableView = new TableView<>();
        tableController = new DownloadTableController(tableView, settings);
        root.setCenter(tableView);

        configureTable();
    }

    private void configureTable() {
        TableColumn<Download, String> fileNameColumn = new TableColumn<>("Имя файла");
        fileNameColumn.setCellValueFactory(cellData -> cellData.getValue().fileNameProperty());

        TableColumn<Download, String> urlColumn = new TableColumn<>("URL");
        urlColumn.setCellValueFactory(cellData -> cellData.getValue().urlProperty());

        TableColumn<Download, String> statusColumn = new TableColumn<>("Статус");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        TableColumn<Download, Double> progressColumn = new TableColumn<>("Прогресс");
        progressColumn.setCellValueFactory(cellData -> cellData.getValue().progressProperty().asObject());
        progressColumn.setCellFactory(ProgressBarTableCell.forTableColumn());

        TableColumn<Download, Void> actionColumn = new TableColumn<>("Действия");
        actionColumn.setCellFactory(createActionColumnCellFactory());

        tableView.getColumns().addAll(fileNameColumn, urlColumn, statusColumn, progressColumn, actionColumn);
    }

    private Callback<TableColumn<Download, Void>, TableCell<Download, Void>> createActionColumnCellFactory() {
        return param -> new TableCell<>() {
            private final Button pauseButton = new Button("Пауза");
            private final Button resumeButton = new Button("Возобновить");
            private final Button deleteButton = new Button("Удалить");

            {
                pauseButton.setOnAction(event -> tableController.pauseDownload(getIndex()));
                resumeButton.setOnAction(event -> tableController.resumeDownload(getIndex()));
                deleteButton.setOnAction(event -> tableController.deleteDownload(getIndex()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(5, pauseButton, resumeButton, deleteButton);
                    setGraphic(hbox);
                }
            }
        };
    }

    private void startDownload() {
        String url = urlField.getText();
        if (url != null && !url.trim().isEmpty()) {
            tableController.addDownload(url);
        }
    }

    private void showSettingsDialog() {
        SettingsDialog dialog = new SettingsDialog(settings);
        dialog.showAndWait();
    }

    private void exportDownloads() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить список загрузок");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON файлы", "*.json"));
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());

        if (file != null) {
            try {
                JSONArray jsonArray = new JSONArray();
                for (Download download : tableController.getDownloads()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("fileName", download.getFileName());
                    jsonObject.put("url", download.getUrl());
                    jsonObject.put("status", download.getStatus());
                    jsonObject.put("progress", download.getProgress());
                    jsonArray.put(jsonObject);
                }
                Files.write(Paths.get(file.toURI()), jsonArray.toString(4).getBytes());
            } catch (IOException e) {
                showErrorAlert("Ошибка экспорта", "Не удалось сохранить список загрузок.");
            }
        }
    }

    private void importDownloads() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Открыть список загрузок");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON файлы", "*.json"));
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());

        if (file != null) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
                JSONArray jsonArray = new JSONArray(content);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String fileName = jsonObject.getString("fileName");
                    String url = jsonObject.getString("url");
                    String status = jsonObject.getString("status");
                    double progress = jsonObject.getDouble("progress");

                    Download download = new Download(fileName, url);
                    download.setStatus(status);
                    download.setProgress(progress);

                    tableController.addDownload(download);
                }
            } catch (IOException e) {
                showErrorAlert("Ошибка импорта", "Не удалось загрузить список загрузок.");
            }
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public BorderPane getRoot() {
        return root;
    }
}
