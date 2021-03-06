package MainTest.TestMoteurs;
import Moteurs.AvancerOuReculer;
import Moteurs.Deplacement;
import Moteurs.TournerOuPivoter;
import Robot.Agent;
import Vue.CapteurCouleur;
import Vue.CapteurUltrasons;
import Vue.Perception;
import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class testDeplacement {
	//Les moteurs des roues doivent etre branch�s sur le port B et C
	public static void main(String[] args) {

		System.out.println("Press enter to run testDeplacement...");
		Button.ENTER.waitForPressAndRelease();
		
		Agent agent = new Agent();

		System.out.println("J'avance 2 secondes");
		agent.getAvancerOuReculer().avancerPourUnTemps(2);
		Delay.msDelay(2000);

		System.out.println("Je recule 2 secondes");
		agent.getAvancerOuReculer().reculerPourUnTemps(2);

		Delay.msDelay(2000);

		System.out.println("On avance de 30 cm");
		agent.getAvancerOuReculer().avancerSurUneDistance(300);

		Delay.msDelay(2000);

		System.out.println("On recule de 30cm");
		agent.getAvancerOuReculer().reculerSurUneDistance(300);

		Delay.msDelay(5000);

		System.out.println("On avance jusqu'a la ligne blanche");
		agent.getAvancerOuReculer().avancerJusquaUneLigne(agent.getCapteurCouleur(), "blanc");

		Delay.msDelay(2000);


		System.out.println("Je pivote de 90 degr�s dans un sens");
		agent.getTournerOuPivoter().pivoterDunDegreDonneEnCrochet(90);

		Delay.msDelay(2000);

		System.out.println("Je pivote de 90 degr�s dans l'autre sens");
		agent.getTournerOuPivoter().pivoterDunDegreDonneEnCrochet(-90);

		System.out.println("Fin du test");
		Delay.msDelay(5000);	
	}

}
