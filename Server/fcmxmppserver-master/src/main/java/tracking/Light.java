package tracking;

public class Light {

	private long timestamp;
	private double brightness;
	public Light(long timestamp, double brightness) {
		super();
		this.timestamp = timestamp;
		this.brightness = brightness;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public double getBrightness() {
		return brightness;
	}
	public void setBrightness(double brightness) {
		this.brightness = brightness;
	} 
	
}
