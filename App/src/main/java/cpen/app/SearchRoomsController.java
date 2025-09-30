package cpen.app;

import cpen.adt.DateTime;
import cpen.adt.DateTimeFormat;
import cpen.adt.Room;
import cpen.network.Network;
import cpen.network.Requests.GetRoomsRequest;
import cpen.network.Responses.GetRoomsResponse;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.awt.Desktop;
import java.util.List;

public class SearchRoomsController {
    @FXML
    private Spinner<Integer> startTimeSpinner;
    @FXML
    private Spinner<Integer> endTimeSpinner;
    @FXML
    private ChoiceBox<String> locationListChoiceBox;
    @FXML
    private DatePicker beginDateSelector;
    @FXML
    private DatePicker endDateSelector;
    @FXML
    private Label beginDateText;
    @FXML
    private Label endDateText;
    @FXML
    private Button allAvailableButton;
    @FXML
    private Text emptyFieldWarningText;
    @FXML
    private TableView<Room> existingTableView;
    @FXML
    private TableColumn<Room, String> locationCol;
    @FXML
    private TableColumn<Room, String> roomCol;
    @FXML
    private TableColumn<Room, String> dateCol;
    @FXML
    private TableColumn<Room, String> timeCol;
    @FXML
    private TableColumn<Room, Void> requestCol;
    @FXML
    private TableColumn<Room, Void> requestWithRoomCol;

    private final List<Room> roomList = new ArrayList<>();

    /**
     * Initializes the user interface components for selecting dates, times, and locations.
     *
     */
    @FXML
    protected void initialize() {
        // disables direct text editing of the date selector, forces user to use the GUI based date selector
        beginDateSelector.setEditable(false);
        endDateSelector.setEditable(false);

        // Initialize Course Choice box with selection
        locationListChoiceBox.getItems().addAll(RoomCourseUtil.locationList);
        locationListChoiceBox.setOnAction(this::getLocation);
        // initialize spinner
        startTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        endTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        endTimeSpinner.getValueFactory().setValue(23);


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

    @FXML
    ObservableList<Room> getLocationData(List<Room> listOfLocations) {
        return FXCollections.observableArrayList(listOfLocations);
    }

    /**
     * When the start date is changed, updates the endDateSelector to disable dates before the selected start date
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
                if (localDate.isBefore(date) || localDate.isBefore(today)) {
                    setDisable(true);
                }
            }
        });
    }

    /**
     * When the end date is changed, updates the beginDateSelector to disable dates after the selected end date
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
                if (localDate.isAfter(date) || localDate.isBefore(today)) {
                    setDisable(true);
                }
            }
        });
    }

    /**
     * When the allAvailable button is clicked, sends a request to the server to retrieve all available rooms
     * between the specified dates and times at the selected location.
     *
     * @param actionEvent the ActionEvent triggered by clicking the "All Available" button
     */
    @FXML
    protected void onAllAvailableClick(javafx.event.ActionEvent actionEvent) {
        Network connection = DataSingleton.getInstance().getConnection();

        // If user does not specify start or end date, assume they filter for empty rooms on that day
        DateTime beginDate =
                new DateTime(LocalDate.now().getYear(), LocalDate.now().getMonthValue(),
                        LocalDate.now().getDayOfMonth(), 0);
        DateTime endDate =
                new DateTime(LocalDate.now().getYear(), LocalDate.now().getMonthValue(),
                        LocalDate.now().getDayOfMonth(), 23);
        String location = "";

        // Read date if the start date is not empty
        if (beginDateSelector.getValue() != null) {
            beginDate.setYear(beginDateSelector.getValue().getYear());
            beginDate.setMonth(beginDateSelector.getValue().getMonthValue());
            beginDate.setDay(beginDateSelector.getValue().getDayOfMonth());
        }

        // Read date if the end date box is not empty
        if (endDateSelector.getValue() != null) {
            endDate.setYear(endDateSelector.getValue().getYear());
            endDate.setMonth(endDateSelector.getValue().getMonthValue());
            endDate.setDay(endDateSelector.getValue().getDayOfMonth());
        }

        if (startTimeSpinner.getValue() != null) {
            beginDate.setHour(startTimeSpinner.getValue());
        }

        if (endTimeSpinner.getValue() != null) {
            endDate.setHour(endTimeSpinner.getValue());
        }

        // Read location
        if (locationListChoiceBox.getValue() != null) {
            location = locationListChoiceBox.getValue();
        }

        // if connection is successful attempt to receive response from server
        if (connection.isConnected()) {
            connection.sendRequest(new GetRoomsRequest(location, beginDate, endDate));
            emptyFieldWarningText.setText("Searching...");

            boolean connectionSuccess = true;
            long startTime = System.currentTimeMillis();
            while (!connection.responseAvailable()) {
                long presentTime = System.currentTimeMillis();
                if (presentTime - startTime >= 5000) {
                    connectionSuccess = false;
                    break;
                }
            }
            if (connectionSuccess) {
                String str = connection.receiveResponse();
                GetRoomsResponse response = new GetRoomsResponse(str);
                roomList.clear();
                roomList.addAll(response.getEmptyRooms());
                updateTable();
                emptyFieldWarningText.setText("");
            }
        } else {
            emptyFieldWarningText.setText("Not connected to server");
        }
    }

    /**
     * Updates the UI table with the relevant data.
     */
    private void updateTable() {
        requestCol.setCellFactory(col -> new TableCell<Room, Void>() {
            private final Button button = new Button("Link to Study Site");

            {
                button.setOnAction(evt -> {
                    Room thisRoom = getTableRow().getItem();
                    //debug
                    if (thisRoom != null) {
                        try {
                            Desktop.getDesktop().browse(new URI(thisRoom.getUrl()));
                        } catch (IOException | URISyntaxException e) {
                            emptyFieldWarningText.setText("URL failed");
                        }
                        System.out.println(
                                "You are going to the website to book: " + thisRoom.getBuilding());
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

        // sets the button to each cell to link to create sessions
        requestWithRoomCol.setCellFactory(col -> new TableCell<Room, Void>() {
            private final Button button = new Button("Link + Create Session");

            {
                button.setOnAction(evt -> {
                    Room thisRoom = getTableRow().getItem();
                    //debug
                    if (thisRoom != null) {
                        System.out.println(
                                "You are going to the website to book: " + thisRoom.getBuilding() +
                                        " and you want to create a new study session.");
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

        locationCol.setCellValueFactory(new PropertyValueFactory<Room, String>("building"));
        roomCol.setCellValueFactory(new PropertyValueFactory<Room, String>(
                "roomNumber")); //roomNumber is the name of the field in the Room class corresponding to the room number of the room
        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getStartTime().getDateString(
                        DateTimeFormat.YYYY_MM_DD)));
        timeCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getStartTime().getHourString()));

        existingTableView.setItems(getLocationData(roomList));
    }

    /**
     * When a location is selected from the choice box, the user's selected location is processed.
     *
     * @param actionEvent the ActionEvent triggered by selecting a location from the ChoiceBox
     */
    @FXML
    protected void getLocation (javafx.event.ActionEvent actionEvent){
        String locationSelected = locationListChoiceBox.getValue();
        // debug
        System.out.println("Select: " + locationSelected);
    }
}