import javafx.concurrent.Task;
import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;

public class DownloadTask extends Task<Void> {
    private final Download download;

    public DownloadTask(Download download, Settings settings) {
        this.download = download;
    }

    @Override
    protected Void call() throws Exception {
        try {
            URL url = new URL(download.getUrl());
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            long completeFileSize = httpConnection.getContentLength();

            download.setStatus("Идет загрузка");

            try (InputStream input = httpConnection.getInputStream();
                 FileOutputStream output = new FileOutputStream(download.getFileName())) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                long downloadedSize = 0;

                while ((bytesRead = input.read(buffer, 0, 1024)) != -1) {
                    if (isCancelled()) {
                        updateMessage("Загрузка приостановлена");
                        break;
                    }
                    output.write(buffer, 0, bytesRead);
                    downloadedSize += bytesRead;
                    updateProgress(downloadedSize, completeFileSize);
                    download.setProgress(downloadedSize / (double) completeFileSize);
                }

                if (isCancelled()) {
                    download.setStatus("Пауза");
                } else {
                    download.setStatus("Завершено");
                    updateMessage("Загрузка завершена");
                }
            }

        } catch (IOException e) {
            updateMessage("Ошибка");
            download.setStatus("Ошибка");
        }
        return null;
    }
}
