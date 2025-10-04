package cpen.app;

import cpen.ADT.User;
import cpen.network.Network;
import cpen.network.Requests.CreateUserRequest;
import cpen.network.Responses.CreateUserResponse;
import cpen.network.Responses.GetSessionsResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateUserController {
    @FXML
    private Button createAccountButton;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text confirmCreateUserText;
    @FXML
    private Button returnToDashboardButton;

    /**
     * When create button is clicked, calls create account.
     *
     * @param actionEvent The event triggered by clicking the create account button.
     * @throws IOException If an I/O error occurs during the attempt to create a user account.
     */
    @FXML
    public void createAccountClick(javafx.event.ActionEvent actionEvent) throws IOException {
        createAccount();
    }

    /**
     * Handles the creation of a new user account.
     *
     * @throws IOException if an I/O error occurs during the account creation process.
     */
    @FXML
    private void createAccount() throws IOException {
        UserLogin m = new UserLogin();
        // debug
        System.out.println("Create User click");

        String email = emailTextField.getText();
        String password = passwordField.getText();
        // debug
        if (email.isEmpty() || password.isEmpty()) {
            confirmCreateUserText.setText("Empty field!");
        } else {
            Network connection = DataSingleton.getInstance().getConnection();
            // Attempt to send a CreateUserRequest in network
            connection.sendRequest(new CreateUserRequest(new User(email, password)));
            boolean connectionSuccess = true;
            long startTime = System.currentTimeMillis();
            while (!connection.responseAvailable()) {
                // if the connection response is not available in 10s, break and try again
                long presentTime = System.currentTimeMillis();
                if (presentTime - startTime >= 10000) {
                    connectionSuccess = false;
                    break;
                }
            }
            if (connectionSuccess) {
                String str = connection.receiveResponse();
                CreateUserResponse response = new CreateUserResponse(str);
                if (response.isUserAccepted()) {
                    confirmCreateUserText.setText("Account Created");
                } else {
                    confirmCreateUserText.setText("Account creation failed");
                }
            } else {
                confirmCreateUserText.setText("Account creation failed");
            }
        }
    }

    /**
     * When the dashboard button is clicked, changes the dashboard scene and sets it as the new root of the current stage.
     *
     * @param actionEvent The event triggered by clicking the return to dashboard button.
     */
    @FXML
    protected void onReturnToDashboardClick(javafx.event.ActionEvent actionEvent) {
        // debug
        System.out.println("Dashboard click");

        try {
            //Load the dashboard scene
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            Parent newSceneParent = fxmlLoader.load();

            //Get the current stage
            Stage stage = (Stage) returnToDashboardButton.getScene().getWindow();

            //Update the root of the current scene
            stage.getScene().setRoot(newSceneParent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
