package model;

/**
 * The intent for this class is to update/store information about a single branch
 */
public class BranchModel {
	private final String city;
	private final String location;

	public BranchModel(String city, String location) {
		this.city = city;
		this.location = location;
	}
	public String getCity() {
		return city;
	}

	public String getLocation() {
		return location;
	}

    @Override
    public String toString() {
        return "BranchModel{" +
                "city='" + city + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
