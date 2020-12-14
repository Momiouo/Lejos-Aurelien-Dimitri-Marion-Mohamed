package Vue;
import Moteurs.AvancerOuReculer;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * La classe CapteurUltrasons g�re les interactions avec le capteur � ultrasons.
 * 
 * @author LejosTeam
 *
 */
public class CapteurUltrasons extends Capteur implements SensorPort {
	/**
	 * Attribut qui enregistre la derni�re distance r�cup�r�e par le capteur.
	 */
	private float distance;
	/**
	 * Objet de la classe EV3UltrasonicSensor.
	 */
	private EV3UltrasonicSensor donneesCapteur;
	
	
	/**
	 * @param perception
	 * @param port
	 */
	public CapteurUltrasons(Perception perception, Port port) {
		super(perception,port);
		donneesCapteur = new EV3UltrasonicSensor((lejos.hardware.port.Port) this.getPort());
		setDistance();
	}
	
	
/*
 * 
 */
	/**
	 * Methode qui permet de verifier si l'objet detect� est un palet.
	 * On do�t �viter de franchir la ligne blanche pour ne pas r�cuperer les palets dans les en-buts.
 	 * Prend en compte si un palet est attrap� pendant le deplacement
  	 *	 
	 * @param moteurdeplacement
	 * @param capteurTactile
	 * @param capteurCouleur
	 * @return
	 */
	public boolean VerifSiObjetDetecteEstUnPalet(AvancerOuReculer moteurdeplacement, CapteurTactile capteurTactile, CapteurCouleur capteurCouleur) {
		System.out.println("VerifSiObjetDetecteEstUnPalet");
		boolean palet = true;
		this.setDistance();
		if (distance < 0.326) {//Si la distance est inf�rieur c'est un mur ou un robot.
			System.out.println("Distance directe < 0.326");
			palet = false;
		}else {
			System.out.println("Distance > 0.326");
			capteurTactile.setPression();
			capteurCouleur.setCouleur();
			while (distance > 0.600 && !capteurTactile.getPression() && !capteurCouleur.getCouleur().equals("blanc")) {
				System.out.println("Distance > 0.600");
				moteurdeplacement.avancerSynchro();
				this.setDistance();
				capteurTactile.setPression();
				capteurCouleur.setCouleur();
			}
			moteurdeplacement.sarreterSynchro();//On s'arrete
			
			if(capteurTactile.getPression()) {//On a attrap� un palet sans s'en rendre compte
				return palet;
			}
			
			if(capteurCouleur.getCouleur().equals("blanc")) {//On a pas le droit de franchir une ligne blanche
				return false;
			}
			
			this.setDistance();//Car entre temps le robot a avanc�
			float distanceAparcourir = (float)((distance-0.326)*1000);//m en mm
			System.out.println("distaparcourir : " + distanceAparcourir);
			moteurdeplacement.avancerSurUneDistance(distanceAparcourir+100);//On avance un peu plus pour arriver en dessous de 0.326
			Delay.msDelay(1000);
			this.setDistance();
			System.out.println("La distance est de " + this.getDistance());
			if (distance < 0.326) {
				palet = false;
			}

		}
		return palet;
	}

	/**
	 * M�thode qui permet de r�cup�rer la distance entre le robot (capteur) et un �ventuel objet (mur, palet, robot adverse).
	 * 
	 * @return
	 */
	public float getDistance() {
		return this.distance;
	}
	

	/**
	 * M�thode qui permet de changer la valeur de l�attribut distance dans la classe CapteurUltrason et l�objet Perception.
	 */
	public void setDistance() {
		final SampleProvider sp = donneesCapteur.getDistanceMode();
		float distanceValue = 0;
		float [] sample = new float[sp.sampleSize()];
		sp.fetchSample(sample, 0);
        distanceValue = sample[0];//0.326
        //System.out.println("Valeur recup par le capteur ultrason : " + distanceValue);
		this.distance = distanceValue; 
		//Modification de l'attribut DistanceCapteurUltrasons de l'objet Perception pass� en param�tre du constructeur
		this.getPerception().distanceCapteurUltrasons = distanceValue;
	}
	
	/**
	 * Verifie si l'objet detect� est un mur ou un robot.
	 * @return
	 */
	public boolean murOuRobotDetecte() {
		//System.out.println("murOuRobotDetecte");
		boolean murOuRobot = false;
		this.setDistance();
		if (distance < 0.250) {
			murOuRobot = true;
		}
		return murOuRobot;
	}

	/**
	 * V�rifie si un objet a �t� detect� dans une distance donn�e.
	 * 
	 * @param distanceparam
	 * @return
	 */
	public boolean murOuRobotDetecteAvecDistance(float distanceparam) {
		//System.out.println("murOuRobotDetecte");
		boolean murOuRobot = false;
		this.setDistance();
		if (distance < distanceparam) {
			murOuRobot = true;
		}
		return murOuRobot;
	}
}
