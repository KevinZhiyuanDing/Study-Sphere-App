package cpen.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import cpen.ADT.LocalStorage;
import cpen.ADT.User;

public class UserLoginController {

    @FXML
    private Button createBtn;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Text wrongLoginText;
    @FXML
    private Button returnToDashboardButton;

    /**
     * When the user presses login button, initiates the
     * login process
     *
     * @param actionEvent the event triggered by the user's interaction with button
     * @throws IOException if an I/O error occurs during the login process
     */
    @FXML
    public void userLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        checkLogin();
    }

    @FXML
    private void checkLogin() {
        String email = emailTextField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            wrongLoginText.setText("Empty field!");
        } else {
            LocalStorage storage = LocalStorage.load();
            User user = storage.users.get(email);
            if (user != null && user.getPassword().equals(password)) {
                wrongLoginText.setText("Login success");
                DataSingleton.getInstance().setUser(user);
            } else {
                wrongLoginText.setText("Wrong username or password");
            }
        }
    }

    /**
     * When user clicks create account button, initiates creation of new account
     *
     * @param actionEvent the event triggered by the user's interaction with the create account button
     */
    @FXML
    protected void createAccount(ActionEvent actionEvent) {
        String email = emailTextField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            wrongLoginText.setText("Empty field!");
        } else {
            LocalStorage storage = LocalStorage.load();
            if (storage.users.containsKey(email)) {
                wrongLoginText.setText("User already exists!");
            } else {
                User user = new User(email, password);
                storage.users.put(email, user);
                storage.save();
                wrongLoginText.setText("Successfully created account");
                DataSingleton.getInstance().setUser(user);
            }
        }
    }
}
