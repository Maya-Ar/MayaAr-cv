package googleAPI;

import java.util.ArrayList;

public class JsonPlaceID {

    private ArrayList<Candidate> candidates;
    public ArrayList<Candidate> getCandidates() {
        return candidates;
    }

    @Override
    public String toString() {
        return "googleAPI.PlaceID{" +
                "candidates=" + candidates +
                '}';
    }

    public class Candidate
    {
        private String place_id;
        public String getPlaceId()
        {
            return this.place_id;
        }

        @Override
        public String toString() {
            return "Candidate{" +
                    "place_id='" + place_id + '\'' +
                    '}';
        }
    }
}
