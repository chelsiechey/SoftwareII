package utils;
import model.Appointment;
import model.Report;
import javafx.collections.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * This class is used to get appointment information from the database
 */
public class DBAppointment {
    private static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static ZoneId utcZoneId = ZoneId.of("UTC");
    private static ZoneId localZoneId = ZoneId.systemDefault();

    /**
     * This method gets all appointments from the DB with the matching customer ID
     * @param customerId The customer ID used to search for appointments
     * @return Returns an observable list of appointments matching the customer ID
     */
    public static ObservableList<Appointment> getAllAppointments(int customerId) {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT appointments.*, contacts.Contact_Name FROM appointments, contacts WHERE appointments.Contact_ID=contacts.Contact_ID AND Customer_ID=" + customerId;
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String contact = rs.getString("Contact_Name");
                String startUtc = rs.getString("Start").substring(0,19);
                String endUtc = rs.getString("End").substring(0,19);
                LocalDateTime startUtcLdt = LocalDateTime.parse(startUtc, dateTimeFormat);
                LocalDateTime endUtcLdt = LocalDateTime.parse(endUtc, dateTimeFormat);
                ZonedDateTime startLocalZdt = startUtcLdt.atZone(utcZoneId).withZoneSameInstant(localZoneId);
                ZonedDateTime endLocalZdt = endUtcLdt.atZone(utcZoneId).withZoneSameInstant(localZoneId);
                String start = startLocalZdt.format(dateTimeFormat);
                String end = endLocalZdt.format(dateTimeFormat);
                String startTime = startLocalZdt.format(timeFormat);
                String endTime = endLocalZdt.format(timeFormat);
                String startDate = startLocalZdt.format(dateFormat);
                String endDate = endLocalZdt.format(dateFormat);

                Appointment appointment = new Appointment(customerId, userId, contactId, contact, appointmentId, title, description, location, type, start, end, startTime, endTime, startDate, endDate);
                appointmentList.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentList;
    }
    /**
     * This method gets all appointment start times from the DB with the matching user ID
     * @param userId The user ID used to search for appointments
     * @return Returns an observable list of appointment start timestamps
     */
    public static ObservableList<Timestamp> getAllUserAppointmentStartTimes(int userId) {
        ObservableList<Timestamp> appointmentStartTimesList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM appointments WHERE User_ID=" + userId;
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Timestamp appointmentStartTimestamp = rs.getTimestamp("Start");
                appointmentStartTimesList.add(appointmentStartTimestamp);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentStartTimesList;
    }

    /**
     * This method gets all appointment end times from the DB with the matching user ID
     * @param userId The user ID used to search for appointments
     * @return Returns an observable list of appointment end timestamps
     */
    public static ObservableList<Timestamp> getAllUserAppointmentEndTimes(int userId) {
        ObservableList<Timestamp> appointmentEndTimesList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM appointments WHERE User_ID=" + userId;
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Timestamp appointmentEndTimestamp = rs.getTimestamp("End");
                appointmentEndTimesList.add(appointmentEndTimestamp);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentEndTimesList;
    }

    /**
     * This method gets all unique appointment types and their count from the DB
     * @return Returns an observable list of reports
     */
    public static ObservableList<Report> getUniqueAppointmentTypesAndCount() {
        ObservableList<Report> appointmentTypesAndCount = FXCollections.observableArrayList();
        try {
            String sql = "SELECT DISTINCT Type FROM appointments";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String type = rs.getString("Type");
                try {
                    String sqlCount = "SELECT COUNT(Type) AS NumberOfType FROM appointments WHERE Type='" + type + "'";
                    PreparedStatement psCount = DBConnection.getConnection().prepareStatement(sqlCount);
                    ResultSet rsCount = psCount.executeQuery();
                    while (rsCount.next()) {
                        String count = String.valueOf(rsCount.getInt("NumberOfType"));
                        Report report = new Report(type, count);
                        appointmentTypesAndCount.add(report);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentTypesAndCount;
    }

    /**
     * This method gets all unique appointment locations and their count from the DB
     * @return Returns an observable list of reports
     */
    public static ObservableList<Report> getAppointmentLocationAndCount() {
        ObservableList<Report> appointmentLocationAndCount = FXCollections.observableArrayList();
        try {
            String sql = "SELECT DISTINCT Location FROM appointments";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String location = rs.getString("Location");
                try {
                    String sqlCount = "SELECT COUNT(Location) AS NumberOfLocation FROM appointments WHERE Location='" + location + "'";
                    PreparedStatement psCount = DBConnection.getConnection().prepareStatement(sqlCount);
                    ResultSet rsCount = psCount.executeQuery();
                    while (rsCount.next()) {
                        String count = String.valueOf(rsCount.getInt("NumberOfLocation"));
                        Report report = new Report(location, count);
                        appointmentLocationAndCount.add(report);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentLocationAndCount;
    }

    /**
     * This method gets all appointments and the contact name associated with the appointment from the DB
     * @return Returns an observable list of appointments
     */
    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT appointments.*, contacts.Contact_Name FROM appointments, contacts WHERE appointments.Contact_ID=contacts.Contact_ID";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("User_ID");
                int customerId = rs.getInt("Customer_ID");
                int contactId = rs.getInt("Contact_ID");
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String contact = rs.getString("Contact_Name");
                String startUtc = rs.getString("Start").substring(0,19);
                String endUtc = rs.getString("End").substring(0,19);
                LocalDateTime startUtcLdt = LocalDateTime.parse(startUtc, dateTimeFormat);
                LocalDateTime endUtcLdt = LocalDateTime.parse(endUtc, dateTimeFormat);
                ZonedDateTime startLocalZdt = startUtcLdt.atZone(utcZoneId).withZoneSameInstant(localZoneId);
                ZonedDateTime endLocalZdt = endUtcLdt.atZone(utcZoneId).withZoneSameInstant(localZoneId);
                String start = startLocalZdt.format(dateTimeFormat);
                String end = endLocalZdt.format(dateTimeFormat);
                String startTime = startLocalZdt.format(timeFormat);
                String endTime = endLocalZdt.format(timeFormat);
                String startDate = startLocalZdt.format(dateFormat);
                String endDate = endLocalZdt.format(dateFormat);

                Appointment appointment = new Appointment(customerId, userId, contactId, contact, appointmentId, title, description, location, type, start, end, startTime, endTime, startDate, endDate);
                appointmentList.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentList;
    }

    /**
     * This method gets each month an appointment is scheduled an how many appointments are in that month from the DB
     * @return Returns an observable list of reports
     */
    public static ObservableList<Report> getUniqueAppointmentMonthsAndCount() {
        ObservableList<Report> appointmentMonthsAndCount = FXCollections.observableArrayList();
        var monthMap = new HashMap<YearMonth, Integer>();
        getAllAppointments().forEach((appointment) -> {
            String start = appointment.getStart().substring(0, 7);
            System.out.println(start);
            YearMonth startAsYearMonth = YearMonth.parse(start);
            if (monthMap.get(startAsYearMonth) == null) {
                monthMap.put(startAsYearMonth, 1);
            } else {
                monthMap.merge(startAsYearMonth, 1, Integer::sum);
            }
        });
        System.out.println(monthMap);
        monthMap.forEach((month, count) -> {
            String monthToString = String.valueOf(month);
            String countToString = String.valueOf(count);
            Report report = new Report(monthToString, countToString);
            appointmentMonthsAndCount.add(report);
        });
        return appointmentMonthsAndCount;
    }

    /**
     * This method gets all appointments for a given contact from the DB
     * @param contactId The contact ID used to search for appointments
     * @return Returns an observable list of appointments
     */
    public static ObservableList<Appointment> getAllAppointmentsForContact(int contactId) {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM appointments WHERE Contact_ID=" + contactId;
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int customerId = rs.getInt("Customer_ID");
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String type = rs.getString("Type");
                String startUtc = rs.getString("Start").substring(0,19);
                String endUtc = rs.getString("End").substring(0,19);
                LocalDateTime startUtcLdt = LocalDateTime.parse(startUtc, dateTimeFormat);
                LocalDateTime endUtcLdt = LocalDateTime.parse(endUtc, dateTimeFormat);
                ZonedDateTime startLocalZdt = startUtcLdt.atZone(utcZoneId).withZoneSameInstant(localZoneId);
                ZonedDateTime endLocalZdt = endUtcLdt.atZone(utcZoneId).withZoneSameInstant(localZoneId);
                String start = startLocalZdt.format(dateTimeFormat);
                String end = endLocalZdt.format(dateTimeFormat);
                String startTime = startLocalZdt.format(timeFormat);
                String endTime = endLocalZdt.format(timeFormat);
                String startDate = startLocalZdt.format(dateFormat);
                String endDate = endLocalZdt.format(dateFormat);

                Appointment appointment = new Appointment(customerId, contactId, appointmentId, title, description, type, start, end, startTime, endTime, startDate, endDate);
                appointmentList.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentList;
    }
}
