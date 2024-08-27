import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public class DownloadTableController {
    private final TableView<Download> tableView;
    private final ObservableList<Download> downloads;
    private final List<DownloadTask> downloadTasks;
    private final Settings settings;

    public DownloadTableController(TableView<Download> tableView, Settings settings) {
        this.tableView = tableView;
        this.settings = settings;
        this.downloads = FXCollections.observableArrayList();
        this.downloadTasks = new ArrayList<>();
        this.tableView.setItems(downloads);
    }

    public void addDownload(String url) {
        String fileName = extractFileName(url);
        Download download = new Download(fileName, url);
        downloads.add(download);

        DownloadTask downloadTask = new DownloadTask(download, settings);
        downloadTasks.add(downloadTask);

        Thread thread = new Thread(downloadTask);
        thread.setDaemon(true);
        thread.start();
    }

    public void addDownload(Download download) {
        downloads.add(download);
        DownloadTask downloadTask = new DownloadTask(download, settings);
        downloadTasks.add(downloadTask);

        Thread thread = new Thread(downloadTask);
        thread.setDaemon(true);
        thread.start();
    }

    public void pauseDownload(int index) {
        DownloadTask task = downloadTasks.get(index);
        if (task != null && !task.isCancelled() && task.isRunning()) {
            task.cancel();
            downloads.get(index).setStatus("Пауза");
        }
    }

    public void resumeDownload(int index) {
        Download download = downloads.get(index);
        DownloadTask task = new DownloadTask(download, settings);
        downloadTasks.set(index, task);

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void deleteDownload(int index) {
        downloadTasks.get(index).cancel();
        downloads.remove(index);
        downloadTasks.remove(index);
    }

    public List<Download> getDownloads() {
        return downloads;
    }

    private String extractFileName(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }
}
