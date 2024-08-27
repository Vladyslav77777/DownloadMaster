import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class SettingsDialog extends Stage {
    private final TextField downloadDirField;
    private final TextField proxyHostField;
    private final TextField proxyPortField;
    private final Settings settings;

    public SettingsDialog(Settings settings) {
        this.settings = settings;

        setTitle("Настройки");
        initModality(Modality.APPLICATION_MODAL);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);

        Label downloadDirLabel = new Label("Папка для загрузки:");
        downloadDirField = new TextField(settings.getDownloadDirectory());
        Button browseButton = new Button("Обзор...");
        browseButton.setOnAction(e -> chooseDirectory());

        Label proxyHostLabel = new Label("Прокси-сервер:");
        proxyHostField = new TextField(settings.getProxyHost());

        Label proxyPortLabel = new Label("Порт прокси:");
        proxyPortField = new TextField(String.valueOf(settings.getProxyPort()));

        Button saveButton = new Button("Сохранить");
        saveButton.setOnAction(e -> saveSettings());

        grid.add(downloadDirLabel, 0, 0);
        grid.add(downloadDirField, 1, 0);
        grid.add(browseButton, 2, 0);
        grid.add(proxyHostLabel, 0, 1);
        grid.add(proxyHostField, 1, 1);
        grid.add(proxyPortLabel, 0, 2);
        grid.add(proxyPortField, 1, 2);
        grid.add(saveButton, 1, 3);

        Scene scene = new Scene(grid, 400, 200);
        setScene(scene);
    }

    private void chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(this);
        if (selectedDirectory != null) {
            downloadDirField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private void saveSettings() {
        settings.setDownloadDirectory(downloadDirField.getText());
        settings.setProxyHost(proxyHostField.getText());
        settings.setProxyPort(Integer.parseInt(proxyPortField.getText()));
        close();
    }
}
