package MainTest;

import Robot.Agent;
import lejos.hardware.Button;
import lejos.utility.Delay;

public class TestFinal1 {
	/*
	 * Premier test final elabor� qui utilisait detecter autour du robot afin de detecter les palets qui l'entourait.
	 * Cependant par manque de pr�cision (le robot n'avance pas droit). il a �t� pr�f�rable d'adopter une autre strat�gie
	 * ne se basant pas sur la pr�cision lors de verification si c'est un palet ou non => Cette strategie consistait a avancer lentement
	 * vers le palet afin de voir si la distance devenait plus grande d'un coup et que donc c'�tait un palet. Malheuresement le robot
	 * n'avancant pas droit cela cr�er des biais et il croit voir un palet alors que le capteur ultrason est juste pass� � gauche ou � droite
	 * du palet...
	 */

	public static void main(String[] args) {

		Agent agent = new Agent();

		System.out.println("Press enter to run MainTest...");
		Button.ENTER.waitForPressAndRelease();

		agent.getAction().detecterAutourDuRobot(true,false);

		System.out.println("Fin du test");
		Delay.msDelay(20000);

	}

}
