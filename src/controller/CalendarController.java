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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Filter;

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

    private ToggleGroup monthOrWeekToggleGroup;

    // stage and scene
    private Stage stage;
    private Parent scene;

    // determines if sorting by month or week
    boolean sortByMonth;

    // date and month variables
//    Month displayMonth;
//    int displayYear;
    LocalDate currentDate = LocalDate.now();
//    Month currentMonth = currentDate.getMonth();
//    int currentYear = currentDate.getYear();
    LocalDate firstOfMonth;
    LocalDate lastOfMonth;
    YearMonth month;
    LocalDate sunday;
    LocalDate saturday;

    // formats
    private static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sortByMonth = true;
        setOriginalDisplayMonth();
        monthOrWeekToggleGroup = new ToggleGroup();
        monthRadioButton.setToggleGroup(monthOrWeekToggleGroup);
        weekRadioButton.setToggleGroup(monthOrWeekToggleGroup);
        monthRadioButton.setSelected(true);
        weekRadioButton.setSelected(false);
        filterByMonth();
    }

    public void setOriginalDisplayMonth() {
//        displayMonth = currentMonth;
//        displayYear = currentYear;
//        dateRangeLabel.setText(displayMonth.toString() + " " + displayYear);
        month = YearMonth.now();
        firstOfMonth = month.atDay(1);
        lastOfMonth = month.atEndOfMonth();
        dateRangeLabel.setText(month.toString());
    }

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
        dateRangeLabel.setText(sunday.toString() + " - " + saturday.toString());
    }
    
    @FXML
    void goBack(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Customer.fxml"));
        scene.getStylesheets().add("/stylesheet.css");
        stage.setTitle("Customer View");
        stage.setScene(new Scene(scene));
        stage.show();
    }


    public void getPreviousDates(ActionEvent actionEvent) {
        if (sortByMonth) {
            month = month.minusMonths(1);
            firstOfMonth = month.atDay(1);
            lastOfMonth = month.atEndOfMonth();
            dateRangeLabel.setText(month.toString());
            filterByMonth();
//            if (displayMonth == Month.JANUARY) {
//                displayYear = displayYear - 1;
//            }
//            displayMonth = displayMonth.minus(1);
//            dateRangeLabel.setText(displayMonth.toString() + " " + displayYear);
        } else {
            sunday = sunday.minusDays(7);
            saturday = saturday.minusDays(7);
            dateRangeLabel.setText(sunday.toString() + " - " + saturday.toString());
            filterByWeek();
        }
    }

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
           dateRangeLabel.setText(sunday.toString() + " - " + saturday.toString());
           filterByWeek();
        }
    }

    public void sortByMonth(ActionEvent actionEvent) {
        sortByMonth = true;
        setOriginalDisplayMonth();
        filterByMonth();
    }

    public void filterByMonth() {
        FilteredList<Appointment> filteredAppointments = new FilteredList<>(DBAppointment.getAllAppointments());
        filteredAppointments.setPredicate(row -> {
            LocalDate appointmentDate = LocalDate.parse(row.getStart(), dateTimeFormat);
            return (appointmentDate.isAfter(firstOfMonth) && appointmentDate.isBefore(lastOfMonth)) ||
                   (appointmentDate.isEqual(firstOfMonth) && appointmentDate.isBefore(lastOfMonth)) ||
                   (appointmentDate.isAfter(firstOfMonth) && appointmentDate.isEqual(lastOfMonth)) ||
                   (appointmentDate.isEqual(firstOfMonth) && appointmentDate.isEqual(lastOfMonth));
        });
        setAppointmentTable(filteredAppointments);
    }

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

    public void sortByWeek(ActionEvent actionEvent) {
        sortByMonth = false;
        setOriginalDisplayWeek();
        filterByWeek();
    }
}
