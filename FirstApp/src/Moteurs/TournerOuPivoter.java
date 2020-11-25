package Moteurs;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import Controleur.Action;
import Robot.Agent;
import Vue.CapteurCouleur;
import Vue.CapteurUltrasons;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

public class TournerOuPivoter extends Deplacement {

	private Action action;

	//Constructeur
	public TournerOuPivoter(EV3LargeRegulatedMotor left, EV3LargeRegulatedMotor right, Action action) {
		super(left, right);
		this.action = action;
	}
	
	//Methodes : 

/*
* Pivoter � gauche d�un degr� pass� en param�tre avec les deux roues
* La methode fait appel � enregistrePositionRobot(int degre) pour enregistrer le degr� de la rotation
*/
	public void pivoterAvecDeuxRouesVersLaGauche(int degre) {
		MovePilot movePilot = new MovePilot(56,56,128,this.getLeftMotor(),this.getRightMotor(),true);
		movePilot.rotate(-degre);
		action.enregistrerPositionRobot(-degre);
	}

/*
* Pivoter � droite d�un degr� pass� en param�tre avec les deux roues
* La methode fait appel � enregistrePositionRobot(int degre) pour enregistrer le degr� de la rotation
*/
	
	public void pivoterAvecDeuxRouesVersLaDroite(int degre) {
		MovePilot movePilot = new MovePilot(56,56,128,this.getLeftMotor(),this.getRightMotor(),true);
		movePilot.rotate(degre);
		action.enregistrerPositionRobot(degre);
	}

/*
 * Pivoter en crochet d�un degr� pass� en param�tre (la roue gauche)
 * La methode fait appel � enregistrePositionRobot(int degre) pour enregistrer le degr� de la rotation
 */
	public void pivoterDunDegreDonneEnCrochet(int degre) {//valeur positive == vers la droite
		this.getLeftMotor().rotate((int) (degre*4.5),true);
		this.getLeftMotor().waitComplete();
		action.enregistrerPositionRobot(degre);
	}
	
/* 
 * Methode qui fait pivoter le robot � un degr� pass� en param�tre et detecte tout les objets autour puis aligne le robot vers l'objet le plus proche
 */
	public void pivoterEtDetecterSurUnDegreDonne(Agent agent, int degre) {		
		System.out.println("pivoterEtDetecterSurUnDegreDonne");
		int miniRotate = 0;
		float distancecourante = 0;
		int degrecourant = 0;
		ArrayList<Float> lesdistances = new ArrayList();
		ArrayList<Integer> lespositions = new ArrayList();
		int i = 1;
		
		while (i <= degre) {
			pivoterDunDegreDonneEnCrochet(10);
			agent.getCapteurUltrasons().setDistance();
			distancecourante = agent.getCapteurUltrasons().getDistance();
			//On sauvegarde la distance avec une position correspondante
			lesdistances.add(distancecourante);
			lespositions.add(i+1);
			i+=5;
		}
		
		//On r�cup�re la position de la plus petite valeur (objet le plus proche)
		int minIndice = lesdistances.indexOf(Collections.min(lesdistances));//Recup�re l'index de la plus petite distance
		//Si la distance est trop petite pour etre un palet on cherche une autre valeur
		while(lesdistances.get(minIndice) < 0.320) {
			lesdistances.remove(minIndice);
			minIndice = lesdistances.indexOf(Collections.min(lesdistances));
		}
		int degremin = lespositions.get(minIndice);//Recupere la position en degre de l'objet le plus proche
	
		//---------------------------
		System.out.println("Objet le plus proche" + degremin);	
		//----------------------------
		
		pivoterDunDegreDonneEnCrochet(-degre+(degremin+5));//+5 pour bien s'aligner en face du palet (marge d'erreur)
		
	}

}
