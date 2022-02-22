package engine;

import common.DesiredHoursInDay;
import common.Destinations;
import common.TripPlan;
import database.DBManager;
import engine.attraction.Attraction;
import engine.trip.DayPlan;
import engine.trip.RouteTrip;
import engine.traveler.Traveler;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collection;

// this class is the mediator between the servlets and data base
// each url on servlet is action here, this class call the function on DB
public class Engine {

    String currentTravelerID = null;

    public String getCurrentTravelerID() {
        return currentTravelerID;
    }
    public void setCurrentTravelerID(String currentTravelerID) {
        this.currentTravelerID = currentTravelerID;
    }

    //here we need to implements all the action we wand the app will have
    public Traveler login(String emailAddress, String password) throws Traveler.NotFoundException, SQLException, Traveler.IllegalValueException {
        DBManager db = new DBManager();
        return db.Login(emailAddress,password);
    }

    public String Register(Traveler traveler) throws SQLException, Traveler.AlreadyExistsException {
        DBManager db = new DBManager();
        return db.Register(traveler);
    }

    public Collection<Attraction> getAttractions(String destination) throws SQLException {
        DBManager db = new DBManager();
        return db.getAllAttractionsByDestination(destination);
    }

    public ArrayList<Attraction> getFavoriteAttractions() throws SQLException {
        DBManager db = new DBManager();
        return db.getFavoriteAttractions(currentTravelerID); //or currentId? not sure
    }

    public void deleteFromFavoriteAttractions(ArrayList<String> favoriteAttractionsList) throws SQLException,Attraction.NotFoundException {
        DBManager db = new DBManager();
        for(String favoriteAttraction : favoriteAttractionsList) {
            db.deleteUserOneFavoriteAttraction(favoriteAttraction, currentTravelerID);
        }
    }

    public void addToFavoriteAttractions(ArrayList<String> favoriteAttractionsList) throws SQLException, Traveler.NotFoundException {
        DBManager db = new DBManager();
        for(String favoriteAttraction : favoriteAttractionsList){
            db.addFavoriteAttraction(favoriteAttraction,currentTravelerID);
        }
    }

    public ArrayList<TripPlan> getUserTrips() throws SQLException, Traveler.HasNoTripsException {
        DBManager db = new DBManager();
        return db.getTripsFromDbByTravelerId(currentTravelerID);
    }

    public void deleteTripFromUserTrips(ArrayList<String> tripsListToDelete) throws SQLException, Traveler.HasNoTripsException {
        DBManager db = new DBManager();
        for(String tripId : tripsListToDelete)
            db.deleteTripFromUserTripsInDB(tripId,currentTravelerID);
    }

    public ArrayList<DayPlan> createTripForUser(String destination, String stringHotelID,
             ArrayList<String> mustSeenAttractionsID, ArrayList<DesiredHoursInDay> desiredHoursInDays) throws SQLException, RouteTrip.AlreadyExistException, Traveler.IllegalValueException {
        checkIfHoursAreValid(desiredHoursInDays);
        DBManager db = new DBManager();
        // update all necessary values to create trip
        Attraction hotel  = db.getHotelFromDBByID(stringHotelID, Destinations.valueOf(destination));

        ArrayList<Attraction> mustSeenAttractions = createArrayListOfMustSeenAttractions(mustSeenAttractionsID, db, destination);
        RouteTrip routeTrip = new RouteTrip(destination,hotel, mustSeenAttractions, desiredHoursInDays);
        ArrayList<Attraction> attractionsAvailable = createListOfRestAttractionAvailableInDestination(db,destination,mustSeenAttractions);

        routeTrip.planRouteTrip(attractionsAvailable);
        return routeTrip.getPlanForDays();

    }
    private void checkIfHoursAreValid(ArrayList<DesiredHoursInDay> desiredHoursInDays) throws Traveler.IllegalValueException {
        for(DesiredHoursInDay hour : desiredHoursInDays){
            hour.checkHour();
        }
        if(desiredHoursInDays.size() > 7 || desiredHoursInDays.isEmpty())
            throw new Traveler.IllegalValueException("too many days of trip");
    }

    // create list of available attractions on destination without must seen attractions
    private ArrayList<Attraction> createListOfRestAttractionAvailableInDestination(DBManager db,String destination, ArrayList<Attraction> mustSeenAttractions) throws SQLException {
        ArrayList<Attraction> allPossibleAttractions = db.getAllAttractionsByDestination(destination);
        ArrayList<Attraction> attractionsAvailable = new ArrayList<>();
        for (Attraction attraction : allPossibleAttractions){
            if(!mustSeenAttractions.contains(attraction))
                attractionsAvailable.add(attraction);
        }
        return attractionsAvailable;
    }

    private ArrayList<Attraction> createArrayListOfMustSeenAttractions(ArrayList<String> mustSeenAttractionsID,DBManager db,String destination) throws SQLException {
        ArrayList<Attraction> mustSeenAttractions = new ArrayList<>();
        for (String attractionID: mustSeenAttractionsID)
            mustSeenAttractions.add(db.getAttractionFromDBByID(attractionID, Destinations.valueOf(destination)));
        return mustSeenAttractions;
    }

    public ArrayList<String> getDestinations() {
        ArrayList<String> destinations = new ArrayList<>();
        Destinations[] destinationsList = Destinations.values();
        for (Destinations des: destinationsList)
            destinations.add(des.name());
        return destinations;
    }

    public void updateTravelerDetails(Traveler newTraveler) throws SQLException, Traveler.AlreadyExistsException, Traveler.IllegalValueException {
        DBManager db = new DBManager();
        Boolean isSameEmail = newTraveler.getEmailAddress().equals(db.getTravelerFromDBByID(currentTravelerID).getEmailAddress());
        db.updateTravelerDetailsOnDB(newTraveler,currentTravelerID, isSameEmail);
    }

    public int saveUserTripOnDB(TripPlan tripPlan) throws SQLException, RouteTrip.AlreadyExistException, RouteTrip.NotFoundException {
        DBManager db = new DBManager();
       return db.insertTripToDB(tripPlan.getTripName(), tripPlan.getPlans(), currentTravelerID, tripPlan.getDestination());
    }

    public ArrayList<Attraction> getHotelsByDestination(String destination) throws SQLException, Attraction.NoHotelsOnDestination {
        DBManager db = new DBManager();
        return db.getAllHotelsFromDB(destination);

    }
    public void checkIfTravelerExistsInDB(String stringId) throws SQLException, Traveler.NotFoundException {
        DBManager db = new DBManager();
        db.isTravelerExistInDB(stringId);
    }
}
