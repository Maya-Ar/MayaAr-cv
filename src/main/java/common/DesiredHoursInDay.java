package common;

import engine.traveler.Traveler;

import java.time.LocalDate;

import java.time.LocalTime;

public class DesiredHoursInDay {
    private String date;
    private String startTime;
    private String endTime;

    public  DesiredHoursInDay(){}

    public DesiredHoursInDay(String s) {
        setDate(s);
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "DesiredHoursInDay{" +
                "date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }

    // check the user input, make sure the range of hours is valid
    public void checkHour() throws Traveler.IllegalValueException {
        LocalDate date = common.converter.convertStringToLocalDate(this.date);
        if(LocalDate.now().isAfter(date))
            throw new Traveler.IllegalValueException("The traveler time is after now time");

        LocalTime startTime  = common.converter.convertStringToLocalTime(this.startTime);
        LocalTime endTime  = common.converter.convertStringToLocalTime(this.endTime);
        if(startTime.isAfter(endTime))
            throw new Traveler.IllegalValueException("Start time of the day is after end of the day");
         if (startTime.equals(endTime))
             throw new Traveler.IllegalValueException("Start time of the day is equal to end of the day");
        if(startTime.plusMinutes(10).isBefore(LocalTime.now()) && LocalDate.now().equals(date))
            throw new Traveler.IllegalValueException("Start time of the day doesn't suitable to current time");
    }
}
