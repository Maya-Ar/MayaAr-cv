package engine.trip;

import engine.attraction.Attraction;

import java.time.LocalTime;


public class OnePlan {
    Attraction attraction;
    LocalTime startTime;
    LocalTime finishTime;
    Boolean isFavoriteAttraction = false;
    String attractionId;

    public OnePlan(Attraction attraction, LocalTime startTime , Boolean isFavoriteAttraction) {
        setAttraction(attraction);
        setStartTime(startTime);
        setFinishTime(startTime.plusHours(attraction.getDuration()));
        setFavoriteAttraction(isFavoriteAttraction);
        setAttractionId(attraction.getPlaceID());
    }

    public Boolean getFavoriteAttraction() {
        return isFavoriteAttraction;
    }
    public void setAttractionId(String attractionId) {
        this.attractionId = attractionId;
    }
    public String getAttractionId() {
        return attractionId;
    }
    public Attraction getAttraction() {return attraction;}
    public Boolean getIsFavoriteAttraction() {
        return isFavoriteAttraction;
    }
    public void setAttraction(Attraction attraction) {this.attraction = attraction;}
    public LocalTime getStartTime() {return startTime;}
    public void setStartTime(LocalTime startTime) {this.startTime = startTime;}
    public LocalTime getFinishTime() {return finishTime;}
    public void setFinishTime(LocalTime finishTime) {this.finishTime = finishTime;}
    public void setFavoriteAttraction(Boolean favoriteAttraction) {
        isFavoriteAttraction = favoriteAttraction;
    }

    @Override
    public String toString() {
        return "OnePlan{" +
                "attraction=" + attraction +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", isFavoriteAttraction=" + isFavoriteAttraction +
                ", attractionId='" + attractionId + '\'' +
                '}';
    }

}
