package mobile.com.fourex;

public class Venue {
    private String name;
    private String address;
    private String distance;

    Venue(String name, String address, String distance){
        this.name = name;
        this.address = address;
        this.distance = distance;
    }

    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public String getDistance(){
        return distance;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setDistance(String distance){
        this.distance = distance;
    }
}
