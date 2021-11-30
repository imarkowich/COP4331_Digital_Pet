package com.example.testingmapproject;

import java.io.Serializable;

public class WalkObject implements Serializable {

    private String date;
    private Integer Duration;
    private Integer steps;
    private float Distance;

    public WalkObject(String date, Integer Duration, Integer steps, float Distance) {
        this.date = date;
        this.Duration = Duration;
        this.steps = steps;
        this.Distance = Distance;
    }


    // setters

    public void setDate(String date) {
        this.date = date;
    }

    public void setDuration(Integer duration) {
        Duration = duration;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public void setDistance(float distance) {
        Distance = distance;
    }


    // getters
    public String getDate() {
        return date;
    }

    public Integer getDuration() {
        return Duration;
    }

    public float getDistance() {
        return Distance;
    }

    public Integer getSteps() {
        return steps;
    }


}
