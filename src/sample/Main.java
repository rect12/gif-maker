package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage pStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        setPrimaryStage(primaryStage);
        primaryStage.setTitle("git maker");
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(410);
        Scene mainScene = new Scene(root, 500, 420);
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }

    public static Stage getPrimaryStage() {
        return pStage;
    }

    private void setPrimaryStage(Stage pStage) {
        Main.pStage = pStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
