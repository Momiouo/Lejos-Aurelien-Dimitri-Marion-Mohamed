package MainTest;

import Robot.Agent;
import lejos.hardware.Button;
import lejos.utility.Delay;

/**
 * Premier test final �labor� qui utilise la fonction : "detecterAutourDuRobot" qui a pour but de d�tecter les palets qui l'entourent.
 * Cependant par manque de pr�cision (le robot n'avance pas droit). il a �t� pr�f�rable d'adopter une nouvelle strat�gie
 * ne se basant pas sur la pr�cision car lorsque l'on doit v�rifier si c'est un palet ou non => cela consistait � avancer lentement
 * vers le palet afin de voir si la distance devenait plus grande d'un coup et que donc c'�tait un palet. Malheureusement le robot
 * n'avan�ant pas droit cela cr�e des impr�cisions et il croit d�tecter un palet alors que le capteur ultrason � d�tecter plus � gauche ou plus � droite
 * du palet.
 * 
 * 
 * @author LejosTeam
 *
 */
public class TestFinal1 {
	/*
	 * 
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
