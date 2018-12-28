package tracking;

public class Accelerator {

	private long timestamp; 
	private double velX; 
	private double velY; 
	private double velZ;
	public Accelerator(long timestamp, double velX, double velY, double velZ) {
		this.timestamp = timestamp;
		this.velX = velX;
		this.velY = velY;
		this.velZ = velZ;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public double getVelX() {
		return velX;
	}
	public void setVelX(double velX) {
		this.velX = velX;
	}
	public double getVelY() {
		return velY;
	}
	public void setVelY(double velY) {
		this.velY = velY;
	}
	public double getVelZ() {
		return velZ;
	}
	public void setVelZ(double velZ) {
		this.velZ = velZ;
	} 
	
	
	
	
}
