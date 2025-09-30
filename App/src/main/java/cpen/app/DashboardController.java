package cpen.app;

import cpen.network.Network;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;

public class DashboardController {
    @FXML
    private Button createAccountButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button existingSessionBtn;
    @FXML
    private Button createSessionBtn;
    @FXML
    private Button bookRoomBtn;

    /**
     * When the "Find Study Session" button is clicked, scene changed to find existing sessions interface
     *
     * @param actionEvent the ActionEvent triggered by clicking the "Find Study Session" button.
     */
    @FXML
    protected void onFindSessionClick(javafx.event.ActionEvent actionEvent) {
        // debug
        System.out.println("Clicked Find Study Session");
        SceneControllerUtil.loadScene("existing-session.fxml", (Stage) existingSessionBtn.getScene().getWindow());
    }

    /**
     * When the "Book Study Room" button is clicked, changes the scene to the study room booking interface.
     *
     * @param actionEvent the ActionEvent triggered by clicking the "Book Study Room" button.
     */
    @FXML
    protected void onBookStudyRoomClick(javafx.event.ActionEvent actionEvent) {
        SceneControllerUtil.loadScene("search-room.fxml", (Stage) bookRoomBtn.getScene().getWindow());
    }

    /**
     * When the "Create Study Session" button is clicked changes the scene to the new session creation interface.
     *
     * @param actionEvent the ActionEvent triggered by clicking the "Create Study Session" button.
     */
    @FXML
    protected void onCreateSessionClick(javafx.event.ActionEvent actionEvent) {
        // debug
        System.out.println("Clicked Create Study Session");
        SceneControllerUtil.loadScene("new-session.fxml", (Stage) createSessionBtn.getScene().getWindow());
    }

    /**
     * When the "Login" button is clicked changes the scene to the login interface.
     *
     * @param actionEvent the ActionEvent triggered by clicking the "Login" button.
     */
    @FXML
    protected void onLoginButtonClick(javafx.event.ActionEvent actionEvent) {
        // debug
        System.out.println("Clicked Login");
        SceneControllerUtil.loadScene("login.fxml", (Stage) loginButton.getScene().getWindow());
    }

    /**
     * When the "Create Account" button is clicked by changing the scene to the user account creation interface.
     *
     * @param actionEvent the ActionEvent triggered by clicking the "Create Account" button.
     */
    @FXML
    protected void onCreateAccountClick(javafx.event.ActionEvent actionEvent) {
        // debug
        System.out.println("Clicked Create Account");
        SceneControllerUtil.loadScene("create-user.fxml", (Stage) createAccountButton.getScene().getWindow());
    }
}
