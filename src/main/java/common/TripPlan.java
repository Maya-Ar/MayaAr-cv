package common;

import engine.trip.DayPlan;

import java.util.ArrayList;

public class TripPlan {
    private String tripName;
    private ArrayList<DayPlan> plans = new ArrayList<>();
    private String destination;
    private int tripId;

    public TripPlan(String tripName, ArrayList<DayPlan> planForDays, String destination, int tripId) {
        setTripName(tripName);
        setPlans(planForDays);
        setDestination(destination);
        setTripId(tripId);

    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getTripName() {
        return tripName;
    }
    public void setTripName(String tripName) {
        this.tripName = tripName;
    }
    public ArrayList<DayPlan> getPlans() {
        return plans;
    }
    public void setPlans(ArrayList<DayPlan> plans) {
        this.plans = plans;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return "TripPlan{" +
                "tripName='" + tripName + '\'' +
                ", plans=" + plans +
                '}';
    }
}
