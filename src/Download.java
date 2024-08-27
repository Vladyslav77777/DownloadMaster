import javafx.beans.property.*;

public class Download {
    private final StringProperty fileName;
    private final StringProperty url;
    private final StringProperty status;
    private final DoubleProperty progress;

    public Download(String fileName, String url) {
        this.fileName = new SimpleStringProperty(fileName);
        this.url = new SimpleStringProperty(url);
        this.status = new SimpleStringProperty("Ожидание");
        this.progress = new SimpleDoubleProperty(0);
    }

    public String getFileName() {
        return fileName.get();
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }

    public String getUrl() {
        return url.get();
    }

    public StringProperty urlProperty() {
        return url;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }

    public double getProgress() {
        return progress.get();
    }

    public void setProgress(double progress) {
        this.progress.set(progress);
    }

    public DoubleProperty progressProperty() {
        return progress;
    }
}
