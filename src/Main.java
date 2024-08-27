import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        DownloadManager manager = new DownloadManager();
        Scene scene = new Scene(manager.getRoot(), 800, 600);

        primaryStage.setTitle("Download Master");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
