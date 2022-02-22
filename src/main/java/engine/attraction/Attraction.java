package engine.attraction;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;
import common.AttractionType;
import common.Geometry;
import common.OpeningHours;
import common.DayOpeningHours;
import googleAPI.JsonAttraction;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

public class Attraction {

    private String name;
    private String address;
    private String phoneNumber;
    private String website;
    private Geometry geometry;
    private String placeID;
    private ArrayList<AttractionType> types = new ArrayList<>();
    private ArrayList<DayOpeningHours> OpeningHoursArr = new ArrayList<>();
    private String imageUrl;
    private String description;
    private int duration = 0;

    public Attraction(String name, String address, String phoneNumber, String website, Geometry geometry, String placeID,
                      ArrayList<AttractionType> types, ArrayList<DayOpeningHours> openingHoursArr) {
        setName(name);
        setAddress(address);
        setPhoneNumber(phoneNumber);
        setWebsite(website);
        setPlaceID(placeID);
        setTypes(types);
        setDuration(types);
        setGeometry(geometry);
        setOpeningHoursArr(openingHoursArr);
    }

    public Attraction(ResultSet resultSet) throws SQLException {
        this.website = resultSet.getString("Website");
        this.placeID = resultSet.getString("attractionAPI_ID");  //ID
        this.name = resultSet.getString("Name");     // Name
        this.address = resultSet.getString("Address");  // Address
        this.phoneNumber = resultSet.getString("PhoneNumber"); //phone number
        String geometryStr = resultSet.getString("Geometry");
        String[] typesArray = resultSet.getString("types").split(",");
        for (String type: typesArray)
        {
            try{
            AttractionType value = AttractionType.valueOf(type);
                this.types.add(value);
            } catch (IllegalArgumentException | NullPointerException e) {}

        }
        String[] geometryArr = geometryStr.split(",");
        this.geometry = new Geometry(geometryArr[0], geometryArr[1]);
        String[] daysColumns = {null,"Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday","Sunday"};
        for(int i =0; i < 7;++i)
        {
            this.OpeningHoursArr.add(null);
        }
        for (int i=1;i<=7;++i) {
            String day = daysColumns[i]; // get the day opening time
            String DayOpeningHours = resultSet.getString(day);
            if(!DayOpeningHours.equals("All Day Long"))
            {
                DayOpeningHours currentDayOpeningHours = new DayOpeningHours(false, i);
                if(!DayOpeningHours.equals("Closed"))
                {
                    currentDayOpeningHours.setOpen(true);
                    String[] dayOperationTimesArray = DayOpeningHours.split(",");
                    for (String operationTime: dayOperationTimesArray) {
                        String[] openingAndClosingTimes = operationTime.split("-");
                        currentDayOpeningHours.addOpening(openingAndClosingTimes[0]);
                        currentDayOpeningHours.addClosing(openingAndClosingTimes[1]);
                    }
                }
                this.OpeningHoursArr.set(i%7,currentDayOpeningHours);
            }
            else
            {
                DayOpeningHours currentDayOpeningHours = new DayOpeningHours(true,true, i);
                this.OpeningHoursArr.set(i%7,currentDayOpeningHours);
            }
        }
        setDuration(this.types);
        if(!types.contains(AttractionType.lodging)) {
            this.description = resultSet.getString("Description");
            this.imageUrl = resultSet.getString("Image");
        }
    }

    public Attraction(Attraction other)
    {
        setName(other.name);
        setAddress(other.address);
        setPhoneNumber(other.phoneNumber);
        setWebsite(other.website);
        setPlaceID(other.placeID);
        setDuration(other.getTypes());
        setImageUrl(other.imageUrl);
        setDescription(other.description);
        setGeometry(other.geometry);
        setTypes(other.types);
        setOpeningHoursArr(other.OpeningHoursArr);
    }


    public Attraction(JsonAttraction jsonAttraction)
    {
        JsonAttraction.JsonResult attractionData =  jsonAttraction.getResult();
        this.website = attractionData.getWebsite();
        this.address = attractionData.getFormatted_address();
        this.name = attractionData.getName();
        this.phoneNumber = attractionData.getFormatted_phone_number();
        this.placeID = attractionData.getPlace_id();
        this.types = attractionData.getTypes();
        Geometry.GeometryAPI.Location location = attractionData.getGeometryAPI().getLocation();
        this.geometry = new Geometry(location.getLat(),location.getLng());
        //initialize the array, create 7 cells (one for each weekday)
        if(attractionData.getOpening_hours() != null)
        {
            if (!types.contains(AttractionType.lodging)) {
                for (int i = 0; i < 7; ++i) {
                    this.OpeningHoursArr.add(null);
                }
                for(int i = 1; i < 8; ++i)
                {
                    this.getOpeningHoursArr().set(i%7, new DayOpeningHours(i));
                }
                for (OpeningHours.DayOpeningHoursJson period : attractionData.getOpening_hours().getPeriods()) {
                    this.OpeningHoursArr.get(period.getClose().getDay()).setOpen(true);
                    String openingTime = period.getOpen().getTime();
                    String closingTime = period.getClose().getTime();
                    openingTime = openingTime.substring(0, 2) + ":" + openingTime.substring(2);
                    closingTime = closingTime.substring(0, 2) + ":" + closingTime.substring(2);
                    this.OpeningHoursArr.get(period.getOpen().getDay()).addOpening(openingTime);
                    this.OpeningHoursArr.get(period.getClose().getDay()).addClosing(closingTime);

                }
            }
        }
        else
        {
            for (int i = 1; i < 8; ++i) {
                this.OpeningHoursArr.add(new DayOpeningHours(true, true, i));
            }
        }
        this.setDuration(this.types);
    }

    // calculate the distance between two attractions
    public double calcDistanceBetweenAttractions(Attraction other)
    {
        double xLat = Double.parseDouble(this.geometry.getLat());
        double xLng = Double.parseDouble(this.geometry.getLng());
        double yLat = Double.parseDouble(other.geometry.getLat());
        double yLng = Double.parseDouble(other.geometry.getLng());

        xLat = Math.toRadians(xLat);
        xLng = Math.toRadians(xLng);
        yLat = Math.toRadians(yLat);
        yLng = Math.toRadians(yLng);

        double dlon = yLng - xLng;
        double dlat = yLat - xLat;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(xLat) * Math.cos(yLat)
                * Math.pow(Math.sin(dlon / 2),2);
        double c = 2 * Math.asin(Math.sqrt(a));
        // Radius of earth in kilometers
        double r = 6371;
        return (c*r);
    }

    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public Geometry getGeometry() {
        return geometry;
    }
    public String getPlaceID() {
        return placeID;
    }
    public String getWebsite() {return website;}
    public int getDuration() {return duration;}
    public ArrayList<AttractionType> getTypes() {
        return types;
    }
    public ArrayList<DayOpeningHours> getOpeningHoursArr() {
        return OpeningHoursArr;
    }
    public DayOpeningHours getOpeningHoursByDay(DayOfWeek day){
        return OpeningHoursArr.get(day.getValue() % 7);
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getDescription() {
        return description;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    public void setName(String name) {this.name = name;}
    public void setAddress(String address) {this.address = address;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setWebsite(String website) {this.website = website;}
    public void setGeometry(Geometry geometry) {this.geometry = geometry;}
    public void setPlaceID(String placeID) {this.placeID = placeID;}
    public void setTypes(ArrayList<AttractionType> types) {this.types = types;}
    public void setOpeningHoursArr(ArrayList<DayOpeningHours> openingHoursArr) {OpeningHoursArr = openingHoursArr;}
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    // according to attraction type we define it's average duration
    public void setDuration(ArrayList<AttractionType> types)
    {
        if (types.contains(AttractionType.amusement_park) || types.contains(AttractionType.aquarium)
                || types.contains(AttractionType.bowling_alley) || types.contains(AttractionType.casino)
                || types.contains(AttractionType.point_of_interest) || types.contains(AttractionType.park)
                || types.contains(AttractionType.shopping_mall) || types.contains(AttractionType.spa)
                || types.contains(AttractionType.tourist_attraction) || types.contains(AttractionType.zoo)) {
            duration = 3;
        }else if (types.contains(AttractionType.airport) || types.contains(AttractionType.embassy)
                || types.contains(AttractionType.establishment) || types.contains(AttractionType.cemetery)
                || types.contains(AttractionType.church) || types.contains(AttractionType.hindu_temple)
                || types.contains(AttractionType.mosque) || types.contains(AttractionType.synagogue)) {
            duration = 2;
        } else if (types.contains(AttractionType.art_gallery) || types.contains(AttractionType.museum)
                || types.contains(AttractionType.movie_theater) || types.contains(AttractionType.stadium)) {
            duration = 2;
        } else if (types.contains(AttractionType.bakery)) {
            duration = 1;
        } else if (types.contains(AttractionType.bar) || types.contains(AttractionType.cafe)
                || types.contains(AttractionType.night_club) || types.contains(AttractionType.restaurant)) {
            duration = 2;
        } else if (types.contains(AttractionType.lodging)) {
            duration = 2;
        }else
            duration = 3;
    }

    @Override
    public String toString() {
        return "Attraction{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", website='" + website + '\'' +
                ", geometry=" + geometry +
                ", placeID='" + placeID + '\'' +
                ", types=" + types +
                ", OpeningHoursArr=" + OpeningHoursArr +
                ", duration=" + duration +
                '}';
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attraction that = (Attraction) o;
        return  Objects.equals(placeID, that.placeID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeID);
    }

    public static class NotFoundException extends Exception {
        public NotFoundException(String message) {
            super(message);
        }
    }

    public static class NoHotelsOnDestination extends Exception {
        public NoHotelsOnDestination(String message) { super(message); }
    }
}

