package cpen.app;

import cpen.ADT.DateTime;
import cpen.ADT.Session;
import cpen.ADT.User;
import cpen.network.Network;
import cpen.network.Requests.CreateSessionRequest;
import cpen.network.Requests.GetRoomsRequest;
import cpen.network.Responses.GetRoomsResponse;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;


public class NewSessionController {
    private final String[] courseList = {"APSC", "MATH", "PHYS", "CHEM", "MECH", "ELEC", "CPEN"};

    @FXML
    private Spinner<Integer> startTimeSpinner;
    @FXML
    private Spinner<Integer> endTimeSpinner;
    @FXML
    private TextField roomTextField;
    @FXML
    private DatePicker dateSelector;
    @FXML
    private ChoiceBox<String> locationListChoiceBox;
    @FXML
    private ChoiceBox<String> courseListChoiceBox;
    @FXML
    private TextField courseCodeTextField;
    @FXML
    private TextField studySessionDescriptionTextField;
    @FXML
    private Button createButton;

    /**
     * Initializes the user interface components for the application.
     */
    @FXML
    protected void initialize() {
        // disables direct text editing of the date selector, forces user to use the GUI based date selector
        dateSelector.setEditable(false);
        dateSelector.setValue(LocalDate.of(2024, 11, 11));
        locationListChoiceBox.getItems().addAll(RoomCourseUtil.locationList);

        // Course list is selectable for now
        courseListChoiceBox.getItems().addAll(courseList);
        courseListChoiceBox.setOnAction(this::getCourse);
    }

    /**
     * When create button is click action, attempts to create a study session
     *
     * @param actionEvent the event triggered by clicking the "Create" button
     */
    @FXML
    protected void onCreateClick(javafx.event.ActionEvent actionEvent) {
        Network connection = DataSingleton.getInstance().getConnection();
        User user = DataSingleton.getInstance().getUser();

        DateTime startTime = null;
        DateTime endTime = null;

        if (dateSelector.getValue() != null) {
            startTime = new DateTime(dateSelector.getValue().getYear(),
                     dateSelector.getValue().getMonthValue(),
                    dateSelector.getValue().getDayOfMonth(), 0);
            endTime = new DateTime(dateSelector.getValue().getYear(),
                    dateSelector.getValue().getMonthValue(),
                    dateSelector.getValue().getDayOfMonth(), 0);
        }

        Session session = new Session(user, locationListChoiceBox.getValue(), courseCodeTextField.getText(),
                roomTextField.getText(), startTime, endTime, studySessionDescriptionTextField.getText());


        if (connection.isConnected()) {
            CreateSessionRequest req = new CreateSessionRequest(session);
        }
    }

    /**
     * When the course list choice box is clicked, course selected parameter is updated.
     *
     * @param actionEvent the actionEvent triggered by changing course Choice box
     */
    @FXML
    protected void getCourse(javafx.event.ActionEvent actionEvent) {
        String courseSelected = courseListChoiceBox.getValue();
    }

    /**
     * Gets the description text from the user that describes the study session details
     *
     * @param actionEvent the actionEvent triggered by changing description field
     */
    @FXML
    private void onSetDescription(javafx.event.ActionEvent actionEvent) {
        String description = studySessionDescriptionTextField.getText();
    }

    /**
     * When the course code is entered, updates the course code filter parameter.
     *
     * @param actionEvent the actionEvent triggered by changing courseCodeTextField
     */
    @FXML
    private void onCourseCode(javafx.event.ActionEvent actionEvent) {
        String courseCode = courseCodeTextField.getText();
    }

}
