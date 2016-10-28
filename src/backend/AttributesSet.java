package backend;

import java.io.Serializable;

public class AttributesSet implements Serializable{
	
	private Integer hourOfDay;
	private String partOfDay;
	private String activity;
	private Integer callCount;
	private String locationType;
	
	public Integer getHourOfDay() {
		return hourOfDay;
	}



	public void setHourOfDay(Integer hourOfDay) {
		this.hourOfDay = hourOfDay;
	}



	public String getPartOfDay() {
		return partOfDay;
	}



	public void setPartOfDay(String partOfDay) {
		this.partOfDay = partOfDay;
	}



	public String getActivity() {
		return activity;
	}



	public void setActivity(String activity) {
		this.activity = activity;
	}



	public Integer getCallCount() {
		return callCount;
	}



	public void setCallCount(Integer callCount) {
		this.callCount = callCount;
	}



	public String getLocationType() {
		return locationType;
	}



	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

}
