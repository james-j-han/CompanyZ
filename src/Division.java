import java.util.HashMap;

public class Division {
    private int divisionID;
    private String name;
    private String city;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String country;
    private String postalCode;
    private HashMap<Integer, String> divisionsMap;

    public Division() {
        divisionsMap = new HashMap<>();
        divisionsMap.put(1, "Technology Engineering");
        divisionsMap.put(2, "Marketing");
        divisionsMap.put(3, "Human Resources");
        divisionsMap.put(4, "HQ");
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
        setName(divisionsMap.get(divisionID));
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public HashMap<Integer, String> getDivisionsMap() {
        return divisionsMap;
    }
}
