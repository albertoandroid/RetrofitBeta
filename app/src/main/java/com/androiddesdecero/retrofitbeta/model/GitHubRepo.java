package com.androiddesdecero.retrofitbeta.model;

public class GitHubRepo {

    private String name;

    private Owner owner;

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }
}
