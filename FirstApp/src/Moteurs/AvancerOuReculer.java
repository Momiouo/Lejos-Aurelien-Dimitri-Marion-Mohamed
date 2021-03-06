package Moteurs;

import Controleur.Action;
import Robot.Agent;
import Vue.CapteurCouleur;
import Vue.CapteurTactile;
import Vue.CapteurUltrasons;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

/**
 * La classe AvancerOuReculer g�re les d�placements lin�aires.
 * 
 * @author LejosTeam
 *
 */
public class AvancerOuReculer extends Deplacement {

	/**
	 * @param left
	 * @param right
	 */
	public AvancerOuReculer(EV3LargeRegulatedMotor left, EV3LargeRegulatedMotor right) {
		super(left, right);
		right.synchronizeWith(new EV3LargeRegulatedMotor[] {left});		
	}
	
	/**
	 * Permet de faire touner vers l'avant (sens horaire) les deux moteurs en synchronisation
	 */
	public void avancerSynchro() {
		this.getLeftMotor().startSynchronization();
		this.getLeftMotor().forward();
		this.getRightMotor().forward();
		this.getLeftMotor().endSynchronization();
	}
	
	/**
	 * Permet de faire tourner les deux moteurs en sens anti-horaire et en synchronisation
	 */
	public void reculerSynchro() {
		this.getLeftMotor().startSynchronization();
		this.getLeftMotor().backward();
		this.getRightMotor().backward();
		this.getLeftMotor().endSynchronization();
	}
	
	/**
	 * Permet d'arreter les deux moteurs en m�me temps
	 */
	public void sarreterSynchro() {
		this.getRightMotor().stop(true);
		this.getLeftMotor().stop();
	}
	

	/**
	 * 
	 * Methode pour avancer jusqu'� une ligne de couleur pass� en param�tre.
	 * Utilise le capteur couleur.
 	 *
	 * @param capteurCouleur
	 * @param couleur
	 */
	public void avancerJusquaUneLigne(CapteurCouleur capteurCouleur,String couleur) {
		avancerSynchro();
		while(capteurCouleur.getCouleur() != couleur) {
			capteurCouleur.setCouleur();
		}
		sarreterSynchro();
	}
	
	
	/**
	 * Methode pour avancer jusqu'� une ligne de couleur pass� en param�tre, en evitant les obstacles, c�d les murs ou le robot adverse.
	 *Permet de reagir face � un obstacle, si par exemple le robot fait face � un mur ou est bloqu�.
 	 *Utilise le capteur couleur et ultrasons.
 	 *
	 * @param capteurCouleur
	 * @param capteurUltrasons
	 * @param action
	 * @param couleur
	 */
	public void avancerJusquaUneLigneEtEviterObstacle(CapteurCouleur capteurCouleur,CapteurUltrasons capteurUltrasons,Action action,String couleur) {
		System.out.println("avancerJusquaUneLigneEtEviterObstacle");
		boolean boucle = true;
		while(boucle) {
			if(!capteurUltrasons.murOuRobotDetecte()) {
				capteurCouleur.setCouleur();
				if (capteurCouleur.getCouleur() != couleur) {
					avancerSynchro();
				}else{
					//On a trouv� la bonne couleur on s'arrete
					sarreterSynchro();
					action.deposerLePalet();
					boucle = false;
				}
			}else {//On doit changer de trajectoire
				action.reagirRobotBloque();
			}
		}
	}


	/**
	 * Avancer sur une distance pass�e en param�tre.
	 * @param distance
	 */
	public void avancerSurUneDistance(float distance) {//Distance en mm
		MovePilot movePilot = new MovePilot(56,56,147,this.getLeftMotor(),this.getRightMotor(),false);
		movePilot.travel(distance);
	}
	

	/**
	 * Reculer sur une distance pass�e en param�tre.
	 * @param distance
	 */
	public void reculerSurUneDistance(float distance) { //distance en mm
		MovePilot movePilot = new MovePilot(56,56,147,this.getLeftMotor(),this.getRightMotor(),true);
		movePilot.travel(distance);
	}


	/**
	 * Avancer pendant un temps pr�cis.
	 * @param seconde
	 */
	public void avancerPourUnTemps(float seconde) {
		avancerSynchro();
		Delay.msDelay((long) (seconde*1000));
		sarreterSynchro();
	}
	
	/**
	 * Avancer pendant un temps pr�cis avec la classe MovePilot afin de voir si c'est plus pr�cis.
	 * 
	 * @param seconde
	 */
	public void avancerPourUnTempsMovePilot(float seconde) {
		MovePilot movePilot = new MovePilot(56,56,147,this.getLeftMotor(),this.getRightMotor(),false);
		movePilot.forward();
		Delay.msDelay((long) (seconde*1000));
		movePilot.stop();
	}
	

	/**
	 * Reculer pendant un temps pr�cis
	 * 
	 * @param seconde
	 */
	public void reculerPourUnTemps(float seconde) {
		this.reculerSynchro();
		Delay.msDelay((long) (seconde*1000));
		sarreterSynchro();
	}

	/**
	 * Avancer tant que la pression du capteur tactile n'est pas activ�e et que l'on a pas crois� de ligne blanche sinon on re fait une d�tection des objets autour.
	 *
	 * @param capteurTactile
	 * @param action
	 * @param capteurCouleur
	 */
	public void avancerTqCapteurPressionPasEnfonce(CapteurTactile capteurTactile, Action action, CapteurCouleur capteurCouleur) {
		System.out.println("avancerTqCapteurPressionPasEnfonce");
		capteurCouleur.setCouleur();
		if(capteurCouleur.getCouleur().equals("blanc")) {//Verification qu'on ne pas pas sur une ligne blanche sinon :
			System.out.println("J'ai detecte du blanc");
			action.detecterAutourDuRobot(false,false);//On a rat� le palet, on refait une d�t�ction.
		}else {
			capteurTactile.setPression();
			if(capteurTactile.getPression()) {//Capteur enfonc�e :
				action.onAUnPalet();
			}else {
				avancerSynchro();
				while(capteurTactile.getPression() == false && !capteurCouleur.getCouleur().equals("blanc")) {
					capteurCouleur.setCouleur();
					capteurTactile.setPression();
				}
				sarreterSynchro();
				if(capteurTactile.getPression()) {//une pression on arrete le robot :
					action.onAUnPalet();
				}else {
					action.detecterAutourDuRobot(false,false);//On a rat� le palet, on refait une d�t�ction.
				}
			}
		}
	}
	

	/**
	 * Avancer tant que la pression du capteur tactile n'est pas activ�e
	 * 
	 * @param capteur
	 */
	public void avancerTqCapteurPressionPasEnfonceTest(CapteurTactile capteur) {//Pour le codage en dur -> premieresAction
		avancerSynchro();
		while(capteur.getPression() == false) {
			capteur.setPression();
		}
		sarreterSynchro();
	}
	
}
