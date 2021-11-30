package com.example.testingmapproject;

public class PetInfo {

    public PetInfo() {}


    String username;
    String petName;
    String breed;



     public PetInfo(String username, String petName, String breed) {

         this.username = username;
         this.petName = petName;
         this.breed = breed;
    }



    //getters

    public String getUsername() {

         return username;

    }



    public String getPetName() {

        return petName;

    }

    public String getBreed() {

        return breed;

    }


    //setters
    public void setPetName(String petName) {

        this.petName = petName;

    }

    public void setBreed(String breed) {

        this.breed = breed;

    }

    public void setUsername(String username) {

         this.username = username;

    }


}
