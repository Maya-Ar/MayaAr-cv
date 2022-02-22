package common;

public class Geometry {
    private String lat;
    private String lng;

    public Geometry(String lat, String lng) {
        setLat(lat);
        setLng(lng);
    }

    public String getLat() {return lat;}
    public String getLng() {return lng;}

    public void setLat(String lat) {this.lat = lat;}
    public void setLng(String lng) {this.lng = lng;}

    @Override
    public String toString() {
        return this.lat +"," + this.lng;
    }


    public class GeometryAPI {
        private Location location;

        public Location getLocation() {return location;}

        public class Location extends Geometry {
            public Location(String lat, String lng) {
                super(lat, lng);
            }
        }

        @Override
        public String toString() {
            return this.location.getLat() + "," + this.getLocation().getLng();
        }
    }
}