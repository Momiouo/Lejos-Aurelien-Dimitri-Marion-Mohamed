package Moteurs;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;


/**
 * Deplacement est une classe abstraitre permettant de g�rer les d�placements du robot.
 * 
 * @author LejosTeam
 *
 */
public abstract class Deplacement {
	
    /**
     * Moteur gauche du robot.
     */
    private EV3LargeRegulatedMotor leftMotor;
    /**
     * Moteur droit du robot
     */
    private EV3LargeRegulatedMotor rightMotor;
    /**
     * Vitesse des moteurs.
     */
    private int speed;
    /**
     * Acc�l�ration des moteurs
     */
    private int acceleration;
    
    
    /**
     * 
     * @param left
     * @param right
     */
    public Deplacement(EV3LargeRegulatedMotor left, EV3LargeRegulatedMotor right) {
    	this.setLeftMotor(left);
    	this.setRightMotor(right);
    	this.rightMotor.setAcceleration(200);
    	this.leftMotor.setAcceleration(200);
    	this.rightMotor.setSpeed(600);
    	this.leftMotor.setSpeed(600);
    }
    
    //M�thodes : 
    
	/**
	 * Retourne l'attribut rightMotor.
	 * 
	 * @return Une instance de EV3LargeRegulatedMotor, qui correspond au moteur droit du robot.
	 */
	public EV3LargeRegulatedMotor getRightMotor() {
		return rightMotor;
	}

	/**
	 * Met � jour l'attribut rightMotor.
	 * 
	 * @param rightMotor
	 */
	public void setRightMotor(EV3LargeRegulatedMotor rightMotor) {
		this.rightMotor = rightMotor;
	}

	/**
	 * Retourne l'attribut leftMotor.
	 * 
	 * @return Une instance de EV3LargeRegulatedMotor, qui correspond au moteur gauche du robot.
	 */
	public EV3LargeRegulatedMotor getLeftMotor() {
		return leftMotor;
	}

	/**
	 * Met � jour l'attribut leftMotor.
	 * 
	 * @param leftMotor
	 */
	public void setLeftMotor(EV3LargeRegulatedMotor leftMotor) {
		this.leftMotor = leftMotor;
	}

	/**
	 * Retourne l'attribut speed.
	 * 
	 * @return L'attribut speed, la vitesse param�tr�e pour les moteurs.
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Met � jour l'attribut speed.
	 * 
	 * @param speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * Retourne l'attribut acceleration.
	 * 
	 * @return L'attribut acceleration.
	 */
	public int getAcceleration() {
		return acceleration;
	}

	/**
	 * Met � jour l'attribut acceleration.
	 * 
	 * @param acceleration
	 */
	public void setAcceleration(int acceleration) {
		this.acceleration = acceleration;
	}
  

    /**
     * Methode pour mettre � jour l'acceleration et la vitesse des moteurs,en fonction des attributs speed 
     * et acceleration deja initialis�s.
     */
    public void updateAccelerationSpeedMotors() {
    	this.rightMotor.setSpeed(speed);
    	this.rightMotor.setAcceleration(acceleration);
    	this.leftMotor.setSpeed(speed);
    	this.leftMotor.setAcceleration(acceleration);
    }
   
    /**
     * M�thode pour r�initialiser les valeurs des tachometres des deux moteurs.
     */
    public void resetTachoMetre() {
    	leftMotor.resetTachoCount();
        rightMotor.resetTachoCount();
    }



	
}
