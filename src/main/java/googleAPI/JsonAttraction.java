package googleAPI;

import common.AttractionType;
import common.Geometry;
import common.OpeningHours;

import java.util.ArrayList;
import java.util.List;


public class JsonAttraction {

    private List html_attributions = new ArrayList<String>();
    private JsonResult result;
    private String status;

    @Override
    public String toString() {
        return "engine.attraction.Attraction{" +
                "html_attributions = " + html_attributions + "\n" +
                "result = \n" + result + "\n" +
                "status = " + status +
                '}';
    }

    public List getHtml_attributions() {
        return html_attributions;
    }

    public JsonResult getResult() {
        return result;
    }

    public String getStatus() {
        return status;
    }

    public class JsonResult {
        String formatted_address;
        String formatted_phone_number;
        String name;


        String place_id;
        String website;
        OpeningHours opening_hours;
        String business_status;
        ArrayList<AttractionType> types = new ArrayList();
        Geometry.GeometryAPI geometry;

        public void setOpening_hours(OpeningHours opening_hours) {
            this.opening_hours = opening_hours;
        }
        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }


        public String AttractionTypesToStr() {
            StringBuilder stringBuilder = new StringBuilder();
            int arrSize = this.types.size();
            for (int i = 0; i < arrSize - 1; ++i) {
                if (types.get(i) != null) {
                    stringBuilder.append(types.get(i));
                    stringBuilder.append(',');
                }
            }
            stringBuilder.append(types.get(arrSize - 1));
            return stringBuilder.toString();
        }

        public String getFormatted_address() {
            return formatted_address;
        }
        public String getFormatted_phone_number() {
            return formatted_phone_number;
        }
        public String getName() {
            return name;
        }
        public String getPlace_id() {
            return place_id;
        }
        public OpeningHours getOpening_hours() {
            return opening_hours;
        }
        public String getBusiness_status() {
            return business_status;
        }
        public ArrayList<AttractionType> getTypes() {
            return types;
        }
        public Geometry.GeometryAPI getGeometryAPI() {return geometry;}

        @Override
        public String toString() {
            return "JsonResult{ \n" +
                    "formatted_address = " + formatted_address + "\n" +
                    "formatted_phone_number = " + formatted_phone_number + "\n" +
                    "name = " + name + "\n" +
                    "opening_hours = " + opening_hours +
                    '}';
        }
    }
}
