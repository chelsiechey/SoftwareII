package controller;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import utils.DBAppointment;
import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class creates the controller for viewing appointments by month and week
 */
public class CalendarController implements Initializable {
    @FXML
    private Label dateRangeLabel;
    @FXML
    private RadioButton monthRadioButton;
    @FXML
    private RadioButton weekRadioButton;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    @FXML
    private TableColumn<Appointment, String> locationColumn;
    @FXML
    private TableColumn<Appointment, String> contactColumn;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, String> startDateColumn;
    @FXML
    private TableColumn<Appointment, String> startColumn;
    @FXML
    private TableColumn<Appointment, String> endDateColumn;
    @FXML
    private TableColumn<Appointment, String> endColumn;
    @FXML
    private TableColumn<Appointment, Integer> appointmentCustomerIdColumn;

    // determines if sorting by month or week
    private boolean sortByMonth;

    // date and month variables
    private final LocalDate currentDate = LocalDate.now();
    private LocalDate firstOfMonth;
    private LocalDate lastOfMonth;
    private YearMonth month;
    private LocalDate sunday;
    private LocalDate saturday;

    // formats
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * This method initializes the calendar controller
     * by setting the default display to by month,
     * creating a toggle group for the month and week toggle buttons,
     * and by setting the appointments in the table
     * @param url Unused parameter for a URL
     * @param resourceBundle Unused parameter for a resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sortByMonth = true;
        setOriginalDisplayMonth();
        ToggleGroup monthOrWeekToggleGroup = new ToggleGroup();
        monthRadioButton.setToggleGroup(monthOrWeekToggleGroup);
        weekRadioButton.setToggleGroup(monthOrWeekToggleGroup);
        monthRadioButton.setSelected(true);
        weekRadioButton.setSelected(false);
        filterByMonth();
    }

    /**
     * This method gets the first and last day of the current month and sets the date range label
     */
    public void setOriginalDisplayMonth() {
        month = YearMonth.now();
        firstOfMonth = month.atDay(1);
        lastOfMonth = month.atEndOfMonth();
        dateRangeLabel.setText(month.toString());
    }

    /**
     * This method gets the Sunday and Saturday of the current week and sets the date range label
     */
    public void setOriginalDisplayWeek() {
        // Go backward to get Sunday
        sunday = currentDate;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY)
        {
            sunday = sunday.minusDays(1);
        }

        // Go forward to get Saturday
        saturday = currentDate;
        while (saturday.getDayOfWeek() != DayOfWeek.SATURDAY)
        {
            saturday = saturday.plusDays(1);
        }
        dateRangeLabel.setText(sunday + " - " + saturday);
    }

    /**
     * This method redirects the user to the customer page
     * @param actionEvent The ActionEvent object generated when the button 'Back to Customer Screen' is pressed
     * @throws IOException Throws an exception if the fxml file for the Customer page is not found
     */
    @FXML
    void goBack(ActionEvent actionEvent) throws IOException {
        // stage and scene
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Customer.fxml")));
        scene.getStylesheets().add("/stylesheet.css");
        stage.setTitle("Customer View");
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /**
     * This method gets the week or month before the week or month currently being displayed
     * and sets the appointments in the table to the new values
     * and updates the date range listed on the date range label
     * @param actionEvent The ActionEvent object generated when the Prev button is pressed
     */
    public void getPreviousDates(ActionEvent actionEvent) {
        if (sortByMonth) {
            month = month.minusMonths(1);
            firstOfMonth = month.atDay(1);
            lastOfMonth = month.atEndOfMonth();
            dateRangeLabel.setText(month.toString());
            filterByMonth();
        } else {
            sunday = sunday.minusDays(7);
            saturday = saturday.minusDays(7);
            dateRangeLabel.setText(sunday + " - " + saturday);
            filterByWeek();
        }
    }

    /**
     * This method gets the week or month following the week or month currently being displayed
     * and sets the appointments in the table to the new values
     * and updates the date range listed on the date range label
     * @param actionEvent The ActionEvent object generated when the Next button is pressed
     */
    public void getNextDates(ActionEvent actionEvent) {
        if (sortByMonth) {
            month = month.plusMonths(1);
            firstOfMonth = month.atDay(1);
            lastOfMonth = month.atEndOfMonth();
            dateRangeLabel.setText(month.toString());
            filterByMonth();
        } else {
           sunday = sunday.plusDays(7);
           saturday = saturday.plusDays(7);
           dateRangeLabel.setText(sunday + " - " + saturday);
           filterByWeek();
        }
    }

    /**
     * This method sets the appointments in the calendar to be displayed by month
     * @param actionEvent The ActionEvent object generated when the By Month radio button is selected
     */
    public void sortByMonth(ActionEvent actionEvent) {
        sortByMonth = true;
        setOriginalDisplayMonth();
        filterByMonth();
    }

    /**
     * This method uses a lambda expression to filter the appointment list to only those in the displayed month
     */
    public void filterByMonth() {
        FilteredList<Appointment> filteredAppointments = new FilteredList<>(DBAppointment.getAllAppointments());
        // lambda expression
        filteredAppointments.setPredicate(row -> {
            LocalDate appointmentDate = LocalDate.parse(row.getStart(), dateTimeFormat);
            return (appointmentDate.isAfter(firstOfMonth) && appointmentDate.isBefore(lastOfMonth)) ||
                   (appointmentDate.isEqual(firstOfMonth) && appointmentDate.isBefore(lastOfMonth)) ||
                   (appointmentDate.isAfter(firstOfMonth) && appointmentDate.isEqual(lastOfMonth)) ||
                   (appointmentDate.isEqual(firstOfMonth) && appointmentDate.isEqual(lastOfMonth));
        });
        setAppointmentTable(filteredAppointments);
    }

    /**
     * This method filters the appointment list to only those in the displayed week
     */
    public void filterByWeek() {
        FilteredList<Appointment> filteredAppointments = new FilteredList<>(DBAppointment.getAllAppointments());
        filteredAppointments.setPredicate(row -> {
            LocalDate appointmentDate = LocalDate.parse(row.getStart(), dateTimeFormat);
            return (appointmentDate.isAfter(sunday) && appointmentDate.isBefore(saturday)) ||
                   (appointmentDate.isEqual(sunday) && appointmentDate.isBefore(saturday)) ||
                   (appointmentDate.isAfter(sunday) && appointmentDate.isEqual(saturday)) ||
                   (appointmentDate.isEqual(sunday) && appointmentDate.isEqual(saturday));
        });
        setAppointmentTable(filteredAppointments);
    }

    /**
     * This method sets the appointments in the table to the filtered list
     * @param filteredAppointments The appointments within the displayed week or month
     */
    public void setAppointmentTable(FilteredList<Appointment> filteredAppointments) {
        appointmentTable.setItems(filteredAppointments);
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        appointmentCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    }

    /**
     * This method sets the appointments in the calendar to be displayed by week
     * @param actionEvent The ActionEvent object generated when the By Week radio button is selected
     */
    public void sortByWeek(ActionEvent actionEvent) {
        sortByMonth = false;
        setOriginalDisplayWeek();
        filterByWeek();
    }
}
