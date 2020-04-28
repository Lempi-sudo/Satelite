package FXUtils;


import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Loader {

    public Stage initStage(String title, Parent parent, boolean isResizable){
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setResizable(isResizable);
        Scene scene = new Scene(parent);
        scene.getStylesheets().add((Loader.class.getResource("/thick-chart.css")).toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        return stage;

    }
}
