package engine.traveler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class Traveler {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    private int travelerId;

    public Traveler(String firstName, String lastName, String emailAddress, String password) throws IllegalValueException {
        setFirstName(firstName);
        setLastName(lastName);
        setEmailAddress(emailAddress);
        setPassword(password);

    }

    public Traveler(Traveler other) throws IllegalValueException {
        setFirstName(other.firstName);
        setLastName(other.lastName);
        setEmailAddress(other.emailAddress);
        setPassword(other.password);
    }

    public Traveler(ResultSet resultSet) throws SQLException, IllegalValueException {
        setLastName(resultSet.getString("LastName"));
        setFirstName(resultSet.getString("FirstName"));
        setPassword(resultSet.getString("Password"));
        setEmailAddress(resultSet.getString("Email"));
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmailAddress() {
        return emailAddress;
    }
    public String getPassword() {
        return password;
    }

    public int getTravelerId() {
        return travelerId;
    }

    public void setTravelerId(int travelerId) {
        this.travelerId = travelerId;
    }

    public void setFirstName(String firstName) throws IllegalValueException {
        if (firstName.trim().isEmpty())
            throw new IllegalValueException("Name cannot be empty");
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) throws IllegalValueException {
        if (lastName.trim().isEmpty())
            throw new IllegalValueException("Name cannot be empty");
        this.lastName = lastName.trim();
    }

    public void setEmailAddress(String emailAddress) throws IllegalValueException {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(emailAddress).matches())
            throw new IllegalValueException("Email address is not valid");
        this.emailAddress = emailAddress;
    }

    public void setPassword(String password) throws IllegalValueException {
        if (password == null || password.trim().isEmpty())
            throw new IllegalValueException("Password cannot be empty");
        if(password.length() < 6 || password.length() > 20)
            throw new IllegalValueException("Password length illegal");
        this.password = password;
    }


    public static class IllegalValueException extends Exception{
        public IllegalValueException(String message) {
            super(message);
        }
    }

    public static class AlreadyExistsException extends Exception {
        public AlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class NotFoundException extends Exception {
        public NotFoundException(String message) {super(message);}
    }


    public static class HasNoTripsException extends Exception {
        public HasNoTripsException(String message) { super(message); }
    }

    @Override
    public String toString() {
        return "Traveler{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


}
