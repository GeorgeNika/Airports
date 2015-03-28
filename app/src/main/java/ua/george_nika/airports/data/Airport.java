package ua.george_nika.airports.data;

public class Airport {

    private Integer _id;
    private String iata_code;
    private String icao_code;
    private String name_rus;
    private String name_eng;
    private String city_rus;
    private String city_eng;
    private Float gmt_offset;
    private String country_rus;
    private String country_eng;
    private String iso_code;
    private Float latitude;
    private Float longitude;
    private String website ;


    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getIata_code() {
        return iata_code;
    }

    public void setIata_code(String iata_code) {
        this.iata_code = iata_code;
    }

    public String getIcao_code() {
        return icao_code;
    }

    public void setIcao_code(String icao_code) {
        this.icao_code = icao_code;
    }

    public String getName_rus() {
        return name_rus;
    }

    public void setName_rus(String name_rus) {
        this.name_rus = name_rus;
    }

    public String getName_eng() {
        return name_eng;
    }

    public void setName_eng(String name_eng) {
        this.name_eng = name_eng;
    }

    public String getCity_rus() {
        return city_rus;
    }

    public void setCity_rus(String city_rus) {
        this.city_rus = city_rus;
    }

    public String getCity_eng() {
        return city_eng;
    }

    public void setCity_eng(String city_eng) {
        this.city_eng = city_eng;
    }

    public Float getGmt_offset() {
        return gmt_offset;
    }

    public void setGmt_offset(Float gmt_offset) {
        this.gmt_offset = gmt_offset;
    }

    public String getCountry_rus() {
        return country_rus;
    }

    public void setCountry_rus(String country_rus) {
        this.country_rus = country_rus;
    }

    public String getCountry_eng() {
        return country_eng;
    }

    public void setCountry_eng(String country_eng) {
        this.country_eng = country_eng;
    }

    public String getIso_code() {
        return iso_code;
    }

    public void setIso_code(String iso_code) {
        this.iso_code = iso_code;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
