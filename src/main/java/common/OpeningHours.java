package common;

import java.util.ArrayList;

public class OpeningHours {
    Boolean open_now;
    ArrayList<DayOpeningHoursJson> periods = new ArrayList();
    ArrayList<String> weekday_text = new ArrayList();

    public ArrayList<String> getWeekday_text(){ return weekday_text;}
    public Boolean getOpen_now() {
        return open_now;
    }
    public ArrayList<DayOpeningHoursJson> getPeriods() {
        return periods;
    }


    @Override
    public String toString() {
        String result = "OpeningHours{" + "open_now = " + open_now + "\n"+ "periods = \n";
        for(DayOpeningHoursJson day: periods)
            result += day.toString();
        return result;
    }

    public class DayOpeningHoursJson {
        DetailsHours close;
        DetailsHours open;

        public DetailsHours getClose() {
            return close;
        }

        public DetailsHours getOpen() {
            return open;
        }

        @Override
        public String toString() {
            StringBuilder openingHourStr = new StringBuilder();
            StringBuilder closingHourStr = new StringBuilder();

            openingHourStr.append(this.getOpen().getTime());  //Opening time
            openingHourStr.insert(2, ':'); // insert ':'

            closingHourStr.append(this.getClose().getTime());  //Closing time
            closingHourStr.insert(2, ':'); // insert ':'
            return openingHourStr + "-" + closingHourStr;
        }

        public class DetailsHours {
            int day;
            String time;

            public int getDay() {
                return day;
            }

            public String getTime() {
                return time;
            }

            @Override
            public String toString() {


                return "DetailsHours{" +
                        "day = " + day +
                        ", time = '" + time + '\'' +
                        '}';
            }
        }
    }
}
