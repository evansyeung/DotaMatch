package com.dotamatch.designproject.dotamatch;

import java.util.ArrayList;

/**
 * Created by evans on 12/10/2016.
 */

public class User {

    public String DotaName;
    public int MMR;
    public String role;
    public float ratingAverage;     //Unused varible since Firebase automatically creates ratingAverage when adding User to database
    public ArrayList<Float> ratings = new ArrayList<Float>();

    public User() {

    }

    public User(String dotaName, int MMR, String role, ArrayList<Float> ratings) {
        this.DotaName = dotaName;
        this.MMR = MMR;
        this.role = role;
        this.ratings = ratings;
    }

    public float getRatingAverage() {
        float total = 0;
        for(int i = 0; i < ratings.size(); i++) {
            total += ratings.get(i);
        }
        return total/ratings.size();
    }
}
