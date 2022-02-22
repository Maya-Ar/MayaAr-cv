package engine.trip;

import com.google.gson.Gson;
import common.*;
import engine.attraction.Attraction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


public class RouteTrip {
    ArrayList<DayPlan> planForDays;
    int tripDuration;
    Destinations destination;    //an Enum that saves destinations(include Paris, London)
    Attraction hotel;
    LocalDate arrivingDate;
    LocalDate leavingDate;
    ArrayList<Attraction> mustSeenAttractions;
    int tripId;

    public RouteTrip(){}

    public RouteTrip(String destination, Attraction hotel, ArrayList<Attraction> mustSeenAttractions, ArrayList<DesiredHoursInDay> desiredHoursInDays) {
        setArrivingDate(LocalDate.parse(((desiredHoursInDays.get(0)).getDate())));
        String lastDay = desiredHoursInDays.get(desiredHoursInDays.size()-1).getDate();
        setLeavingDate(LocalDate.parse(lastDay));
        setTripDuration(leavingDate.getDayOfMonth() - arrivingDate.getDayOfMonth() + 1);
        setMustSeenAttractions(mustSeenAttractions);
        setDestination(Destinations.valueOf(destination));
        setHotel(hotel);
        setPlanForDays(desiredHoursInDays,this.hotel);

    }

    public RouteTrip(ArrayList<DayPlan> tripPlan, String destination) {
        planForDays = new ArrayList<>(tripPlan);
        setDestination(Destinations.valueOf(destination));
    }

    public RouteTrip createRouteTripFromJson(ResultSet results) throws SQLException {
        Gson gson = new Gson();
        String str = results.getString("trip");
         return  gson.fromJson(str, RouteTrip.class);
    }

    public Destinations getDestination() {
        return destination;
    }
    public ArrayList<DayPlan> getPlanForDays() {
        return planForDays;
    }
    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }
    public void setDestination(Destinations destination) {
        this.destination = destination;
    }
    public void setHotel(Attraction hotel) {
        this.hotel = hotel;
    }
    public void setArrivingDate(LocalDate arrivingDate) {
        this.arrivingDate = arrivingDate;
    }
    public void setLeavingDate(LocalDate leavingDate) {
        this.leavingDate = leavingDate;
    }
    public void setTripId(int tripId) {
        this.tripId = tripId;
    }


    public void setMustSeenAttractions(ArrayList<Attraction> mustSeenAttractions) {
        this.mustSeenAttractions = new ArrayList<>();
        if(mustSeenAttractions != null) {
            this.mustSeenAttractions.addAll(mustSeenAttractions);
        }
    }

    //this function create the structure of the route, defines dates and hours desired by user
    public void setPlanForDays(ArrayList<DesiredHoursInDay> planForDays, Attraction hotel) {
        this.planForDays = new ArrayList<DayPlan>();
        for(DesiredHoursInDay day: planForDays){
            DayPlan dayPlan = new DayPlan(LocalDate.parse(day.getDate()),
                    LocalTime.parse(day.getStartTime()),
                            LocalTime.parse(day.getEndTime()),hotel);
            this.planForDays.add(dayPlan);
        }
    }

    //this function plan the route
    public void planRouteTrip(ArrayList<Attraction> attractionsAvailable){
        divideMustSeenAttractionsForEachDay();

        ArrayList<Attraction> mustSeenAttractionsLocal = new ArrayList<>();
        for(DayPlan dayPlan: this.planForDays){
            dayPlan.updateMustSeenAttractions(mustSeenAttractionsLocal);
            dayPlan.calculateDayPlanWithMustSeenAttractions();
            mustSeenAttractionsLocal = dayPlan.getMustSeenAttractionsForDay();
            dayPlan.findTheBestRouteOutOfFive(attractionsAvailable, false, dayPlan.getFinishTime());
        }
    }

    public void divideMustSeenAttractionsForEachDay() {
        int numberOfIterations = 1;

        while (mustSeenAttractions.size() != 0 && numberOfIterations != 200) { /// limit number of iterations
            for (DayPlan dayPlan : this.planForDays) {
                dayPlan.setMustSeenAttractionsForDay(mustSeenAttractions);
            }
            numberOfIterations += 1;
        }
        this.planForDays.forEach(dayPlan -> dayPlan.setDurationDay(0));    }

    @Override
    public String toString() {
        return "RouteTrip{" +
                "planForDays=" + planForDays +
                //", attractionsForTheTrip=" + attractionsForTheTrip +
                ", tripDuration=" + tripDuration +
                ", destination='" + destination + '\'' +
                ", hotel=" + hotel +
                ", arrivingDate=" + arrivingDate +
                ", leavingDate=" + leavingDate +
                '}';
    }

    public static class NotFoundException extends Exception {
        public NotFoundException(String message) {
            super(message);
        }
    }

    public static class AlreadyExistException extends Exception {
        public AlreadyExistException(String message) {
            super(message);
        }

    }
}
