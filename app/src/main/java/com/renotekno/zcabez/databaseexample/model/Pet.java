package com.renotekno.zcabez.databaseexample.model;

/**
 * Created by zcabez on 16/07/2017.
 */
public class Pet {
    private String name;
    private String breed;
    private int gender;
    private int weight;

    public Pet(String name, String breed, int gender, int weight) {
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.weight = weight;
    }


    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }
}
