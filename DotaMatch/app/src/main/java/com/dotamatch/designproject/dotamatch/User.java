package com.dotamatch.designproject.dotamatch;

/**
 * Created by evans on 12/10/2016.
 */

public class User {

    public String DotaName;
    public int MMR;
    public String role;
    public float rating;

    public User() {

    }

    public User(String dotaName, int MMR, String role, float rating) {
        this.DotaName = dotaName;
        this.MMR = MMR;
        this.role = role;
        this.rating = rating;
    }
}
