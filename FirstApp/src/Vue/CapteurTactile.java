package Vue;
import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class CapteurTactile extends Capteur {
	private boolean pression;
	private EV3TouchSensor donneesCapteur;
	
	public CapteurTactile(Perception perception, Port sensorPort){
		super(perception, sensorPort);
		donneesCapteur = new EV3TouchSensor((lejos.hardware.port.Port) this.getPort());
		setPression();
	}
	
	public boolean getPression() {
		return this.pression;
	}
	
	public void setPression() {
		final SampleProvider sp = donneesCapteur.getTouchMode();
		boolean touchValue = false;
		float [] sample = new float[sp.sampleSize()];
        sp.fetchSample(sample, 0);
        touchValue = (sample[0]!=0);
        this.pression = touchValue;
        super.getPerception().pressionCapteurTactile = touchValue;
	}
	
}
