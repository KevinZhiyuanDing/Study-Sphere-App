package cpen.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Utility class for handling scene changes.
 */
public class SceneControllerUtil {
    /**
     * Loads and sets a new scene onto a stage determined by the FXML file
     * @param fxmlFile the path to the FXML file that defines the new scene.
     * @param stage the primary stage where the new scene will be set.
     */
    public static void loadScene(String fxmlFile, Stage stage) {
        try {
            // Load the new scene
            FXMLLoader fxmlLoader = new FXMLLoader(SceneControllerUtil.class.getResource(fxmlFile));
            Parent newSceneParent = fxmlLoader.load();

            // Update the root of the current scene
            stage.getScene().setRoot(newSceneParent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
