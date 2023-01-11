package main.java.cz.uhk.nedomji1.data;

public class Address {
    private String street, city, country;
    private int evNumber, zipCode;

    public Address(String street, int evNumber, String city, int zipCode, String country) {
        this.street = street;
        this.city = city;
        this.country = country;
        this.evNumber = evNumber;
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getEvNumber() {
        return evNumber;
    }

    public void setEvNumber(int evNumber) {
        this.evNumber = evNumber;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }


}
