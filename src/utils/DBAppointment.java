package utils;
import model.Appointment;
import javafx.collections.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class DBAppointment {
        private static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        private static ZoneId utcZoneId = ZoneId.of("UTC");
        private static ZoneId localZoneId = ZoneId.systemDefault();
        public static ObservableList<Appointment> getAllAppointments(int customerId) {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try {
//            String sql = "SELECT * FROM appointments WHERE Customer_ID=" + customerId;
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

                Appointment appointment = new Appointment(customerId, userId, contactId, contact, appointmentId, title, description, location, type, start, end);
                appointmentList.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentList;
    }
    public static ObservableList<Timestamp> getAllUserAppointments(int userId) {
        ObservableList<Timestamp> appointmentStartTimesList = FXCollections.observableArrayList();
        try {
//            String sql = "SELECT * FROM appointments WHERE Customer_ID=" + customerId;
            String sql = "SELECT * FROM appointments WHERE User_ID=" + userId;
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Timestamp appointmentStartTimestamp = rs.getTimestamp("Start");
//                LocalTime appointmentStartTime = appointmentStartTimestamp.toLocalDateTime().toLocalTime();
                appointmentStartTimesList.add(appointmentStartTimestamp);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentStartTimesList;
    }
}
