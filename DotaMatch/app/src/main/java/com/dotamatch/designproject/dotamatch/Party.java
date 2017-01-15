package com.dotamatch.designproject.dotamatch;

/**
 * Created by evans on 1/13/2017.
 */

public class Party {

    public User leader;
    public User user2;
    public User user3;
    public User user4;
    public User user5;

    public String leader_Key;
    public String user2_Key;
    public String user3_Key;
    public String user4_Key;
    public String user5_Key;

    public int numberOfMembers;

    public boolean carry;
    public boolean solo;
    public boolean offlaner;
    public boolean jungler;
    public boolean support;

    public Party() {
        this.leader = null;
        this.user2 = null;
        this.user3 = null;
        this.user4 = null;
        this.user5 = null;
        this.leader_Key = null;
        this.user2_Key = null;
        this.user3_Key = null;
        this.user4_Key = null;
        this.user5_Key = null;
        this.numberOfMembers = 0;
        this.carry = false;
        this.solo = false;
        this.offlaner = false;
        this.jungler = false;
        this.support = false;
    }
/*
    public Party(User leader, User user2, User user3, User user4, User user5) {
        this.leader = leader;
        this.user2 = user2;
        this.user3 = user3;
        this.user4 = user4;
        this.user5 = user5;
    }
    */
}
