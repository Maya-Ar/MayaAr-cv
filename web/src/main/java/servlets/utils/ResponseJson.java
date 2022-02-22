package servlets.utils;

import engine.traveler.Traveler;

public class ResponseJson{
    public String status; // "ok" or "error"
    public Object message;

    public ResponseJson(){
        status = "ok";
    }

    public static Traveler travelerFromJson(Traveler jsonTraveler) throws Traveler.IllegalValueException {
        Traveler newTraveler;
        newTraveler = new Traveler(jsonTraveler.getFirstName(), jsonTraveler.getLastName(), jsonTraveler.getEmailAddress(), jsonTraveler.getPassword());
        newTraveler.setFirstName(jsonTraveler.getFirstName());
        newTraveler.setLastName(jsonTraveler.getLastName());
        newTraveler.setEmailAddress(jsonTraveler.getEmailAddress());
        newTraveler.setPassword(jsonTraveler.getPassword());
        return newTraveler;

    }

}