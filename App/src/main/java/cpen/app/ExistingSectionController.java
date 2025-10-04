package cpen.app;

import cpen.ADT.Course;
import cpen.ADT.DateTime;
import cpen.ADT.DateTimeFormat;
import cpen.ADT.Room;
import cpen.ADT.Session;
import cpen.ADT.User;
import cpen.network.Network;
import cpen.network.Requests.GetSessionsRequest;
import cpen.network.Requests.JoinSessionRequest;
import cpen.network.Responses.GetSessionsResponse;
import cpen.network.Responses.JoinSessionResponse;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExistingSectionController {
    // existing session parameters
    private String location = "";
    // If user does not specify start or end date, assume they filter for empty rooms on that day
    private DateTime beginDate = new DateTime(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth(), 0);
    private DateTime endDate = new DateTime(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth(), 23);
    private String course = "";

    private Network connection = DataSingleton.getInstance().getConnection();

    List<Session> listOfSessions = new ArrayList<>();

    @FXML
    private Text emptyFieldWarningText;
    @FXML
    private Spinner<Integer> startTimeSpinner;
    @FXML
    private Spinner<Integer> endTimeSpinner;
    @FXML
    private DatePicker beginDateSelector;
    @FXML
    private DatePicker endDateSelector;
    @FXML
    private Button allAvailableButton;
    @FXML
    private ChoiceBox<String> locationListChoiceBox;
    @FXML
    private ChoiceBox<String> courseListChoiceBox;
    @FXML
    private Label beginDateLabel;
    @FXML
    private Label endDateLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label startDateLabel;
    @FXML
    private TableView<Session> existingTableView;
    @FXML
    private TableColumn<Session, String> hostCol;
    @FXML
    private TableColumn<Session, String> courseCodeCol;
    @FXML
    private TableColumn<Session, String> roomCol;
    @FXML
    private TableColumn<Session, String> timeCol;
    @FXML
    private TableColumn<Session, Void> requestCol;


    /**
     * Initializes the user interface components for the application.
     */
    @FXML
    protected void initialize() {
        // disables direct text editing of the date selector, forces user to use the GUI based date selector
        beginDateSelector.setEditable(false);
        endDateSelector.setEditable(false);

        // initialize spinner
        startTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        endTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));

        // sets the list of options for the choice boxes
        locationListChoiceBox.getItems().addAll(RoomCourseUtil.locationList);
        courseListChoiceBox.getItems().addAll(RoomCourseUtil.courseList);

        locationListChoiceBox.setOnAction(this::getLocation);
        courseListChoiceBox.setOnAction(this::getCourse);

        // Date Selector
        LocalDate today = LocalDate.now();
        // Disable past dates for beginDateSelector
        beginDateSelector.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(today));
            }
        });

        // Disable past dates for endDateSelector
        endDateSelector.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(today));
            }
        });
    }

    /**
     * Converts a list of Session objects into an observable list
     *
     * @param listOfSession the list of Session objects to be converted
     * @return an ObservableList containing the specific Session objects
     */
    @FXML
    ObservableList<Session> getSessionData(List<Session> listOfSession) {
        return FXCollections.observableArrayList(listOfSession);
    }

    /**
     * Handles the action event triggered when the begin date is changed.
     * This updates the endDateSelector's cell factory to disable dates before the selected begin date
     *
     * @param actionEvent the ActionEvent triggered by changing the begin date
     */
    @FXML
    protected void beginDateChanged(javafx.event.ActionEvent actionEvent) {
        // gets the date that is selected
        LocalDate date = beginDateSelector.getValue();

        endDateSelector.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate localDate, boolean b) {
            super.updateItem(localDate, b);
            LocalDate today = LocalDate.now();
            // disables any date before the selected date in the end date selector or a day in the past
            if (localDate.isBefore(date) || localDate.compareTo(today) < 0) {
                setDisable(true);
            }
            }
        });
    }

    /**
     * Handles the action event triggered when the end date is changed.
     * This updates the beginDateSelector's cell factory to disable dates after the selected end date
     *
     * @param actionEvent the ActionEvent triggered by changing the end date
     */
    @FXML
    protected void endDateChanged(javafx.event.ActionEvent actionEvent) {
        // gets the date that is selected
        LocalDate date = endDateSelector.getValue();

        beginDateSelector.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate localDate, boolean b) {
                super.updateItem(localDate, b);
                LocalDate today = LocalDate.now();
                // disables any date after the selected date in the begin date selector or a day in the past
                if (localDate.isAfter(date) || localDate.compareTo(today) < 0) {
                    setDisable(true);
                }
            }
        });
    }

    /**
     * Handles the action event triggered when the "All Available" button is clicked.
     *
     * @param actionEvent the ActionEvent triggered by the button click
     */
    @FXML
    protected void onAllAvailableClick(javafx.event.ActionEvent actionEvent) {
        // debug
        System.out.println("All Available Click");

        if (locationListChoiceBox.getValue() != null) {
            location = locationListChoiceBox.getValue();

            //debug
            System.out.println(location);
        } else {
            System.out.println("No location");
        }

        if (beginDateSelector.getValue() != null && startTimeSpinner.getValue() != null) {
            beginDate = new DateTime(beginDateSelector.getValue().getYear(),
                beginDateSelector.getValue().getMonthValue(),
                beginDateSelector.getValue().getDayOfMonth(),
                startTimeSpinner.getValue());

            //debug
            System.out.println(beginDateSelector.toString());
        } else {
            System.out.println("No begin");
        }

        if (endDateSelector.getValue() != null && endTimeSpinner.getValue() != null) {
            endDate = new DateTime(endDateSelector.getValue().getYear(),
                endDateSelector.getValue().getMonthValue(),
                endDateSelector.getValue().getDayOfMonth(),
                endTimeSpinner.getValue());

            //debug
            System.out.println(endDate.toString());
        } else {
            System.out.println("No end");
            emptyFieldWarningText.setText("Empty Fields!");
        }

        if (courseListChoiceBox.getValue() != null) {
            course = courseListChoiceBox.getValue();
            //debug
            System.out.println(course);
        } else {
            System.out.println("No course");
        }

        emptyFieldWarningText.setText("Searching...");

        Course _course = new Course(course, "", 0); // course only provide courseCode for now
        connection.sendRequest(new GetSessionsRequest("", "", beginDate, endDate));

        // attempts to get a connection response, will break from attempt if connection takes longer than 10s
        boolean connectionSuccess = true;
        long startTime = System.currentTimeMillis();
        while (!connection.responseAvailable()) {
            long presentTime = System.currentTimeMillis();
            if (presentTime - startTime >= 10000) {
                connectionSuccess = false;
                break;
            }
        }
        // if connection is successful, update the list of available study sessions
        if (connectionSuccess) {
            String str = connection.receiveResponse();
            GetSessionsResponse response = new GetSessionsResponse(str);
            listOfSessions.addAll(response.getSessionsList());
        }

        // Table View
        requestCol.setCellFactory(col -> new TableCell<Session, Void>() {
            private final Button button = new Button("Request");
            // Set the buttons for each cell such that chen clicked, leads to the corresponding study session
            {
                button.setOnAction(evt -> {
                    Session thisSession = getTableRow().getItem(); // session the user clicked to request
                    //debug
                    if (thisSession != null) {
                        System.out.println("You requested to join: " + thisSession.getHost() + "'s study session.");


                        User requester = DataSingleton.getInstance().getUser();
                        connection.sendRequest(new JoinSessionRequest(requester, thisSession));
                        boolean connectionSuccess = true;
                        long startTime = System.currentTimeMillis();

                        while (!connection.responseAvailable()) {
                            long presentTime = System.currentTimeMillis();
                            if (presentTime - startTime >= 10000) {
                                connectionSuccess = false;
                                break;
                            }
                        }
                        if (connectionSuccess) {
                            String str = connection.receiveResponse();
                            JoinSessionResponse response = new JoinSessionResponse(str);
                            if (response.isJoinProcessed()) {
                                emptyFieldWarningText.setText("Request success");
                            } else {
                                emptyFieldWarningText.setText("Request failed");
                            }
                        } else {
                            emptyFieldWarningText.setText("Connection failed");
                        }

                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null); // Don't display anything if the row is empty
                } else {
                    setGraphic(button); // Set the button in the cell
                }
            }
        });

        hostCol.setCellValueFactory(
            cellData -> new SimpleStringProperty(cellData.getValue().getHost().getUsername()));
        courseCodeCol.setCellValueFactory(
            cellData -> new SimpleStringProperty(cellData.getValue().getCourse()));
        roomCol.setCellValueFactory(
            cellData -> new SimpleStringProperty(cellData.getValue().getRoom() + " " + cellData.getValue().getRoom()));
        timeCol.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getStartTime().getDateString(DateTimeFormat.YYYY_MM_DD) + " ~ "
                + cellData.getValue().getStartTime().getHourString() + ":00 - " + cellData.getValue().getEndTime().getHourString()+ ":00"));

            existingTableView.setItems(getSessionData(listOfSessions));
    }

    /**
     * Handles the action event triggered when a course is selected from the course list.
     *
     * @param actionEvent the ActionEvent triggered by selecting a course from the ChoiceBox
     */
    @FXML
    protected void getCourse(javafx.event.ActionEvent actionEvent) {
        String courseSelected = courseListChoiceBox.getValue();
        // debug
        System.out.println("Select: " + courseSelected);
    }

    /**
     * Handles the action event triggered when a location is selected from the location list ChoiceBox.
     *
     * @param actionEvent the ActionEvent triggered by selecting a location from the ChoiceBox
     */
    @FXML
    protected void getLocation(javafx.event.ActionEvent actionEvent) {
        String locationSelected = locationListChoiceBox.getValue();
        // debug
        System.out.println("Select: " + locationSelected);
    }
}