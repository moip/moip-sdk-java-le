package br.com.moip.resource;

public class Address {

    private String streetNumber;
    private String street;
    private String city;
    private String complement;
    private String district;
    private String zipCode;
    private String state;
    private String country;

    public String getStreet() { return street; }

    public String getStreetNumber() { return streetNumber; }

    public String getComplement() { return complement; }

    public String getDistrict() { return district; }

    public String getCity() { return city; }

    public String getState() { return state; }

    public String getZipCode() { return zipCode; }

    public String getCountry() { return country; }

    @Override
    public String toString() {
        return new StringBuilder()
                .append('{')
                .append("streetNumber='").append(streetNumber).append('\'')
                .append(", street='").append(street).append('\'')
                .append(", city='").append(city).append('\'')
                .append(", complement='").append(complement).append('\'')
                .append(", district='").append(district).append('\'')
                .append(", zipCode='").append(zipCode).append('\'')
                .append(", state='").append(state).append('\'')
                .append(", country='").append(country).append('\'')
                .append('}').toString();
    }
}
