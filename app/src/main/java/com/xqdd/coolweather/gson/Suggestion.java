package com.xqdd.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 10324 on 2017/10/4.
 */

public class Suggestion {
    @SerializedName("car_washing")
    public CarWashing carWashing;
    public Dressing dressing;
    public Flu flu;
    public Sport sport;
    public Travel travel;
    @SerializedName("uv")
    public UltravioletRay ultravioletRay;


    public class CarWashing {
        public String brief;
        public String details;
    }

    public class Dressing {
        public String brief;
        public String details;
    }

    public class Flu {
        public String brief;
        public String details;
    }

    public class Sport {
        public String brief;
        public String details;
    }

    public class Travel {
        public String brief;
        public String details;
    }

    public class UltravioletRay {
        public String brief;
        public String details;
    }
}
