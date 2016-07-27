package pokemonfinder.com.pokemonfinder;

public class Pokemon {

    private String name;
    private String lon;
    private String lat;
    private int resourceID;

    public Pokemon(String name, String lat, String lon, int respurceId) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.resourceID = respurceId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLng() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

}
