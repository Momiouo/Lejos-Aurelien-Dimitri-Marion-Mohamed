package Vue;
import Robot.Agent;
import lejos.robotics.Color;

/**
 * La classe Perception permet d'enregistrer les perceptions du robot � diff�rents moments en utilisant les capteurs.
 * 
 * @author LejosTeam
 *
 */
public class Perception {
	/**
	 * Attribut enregistrant s'il y a une pression ou non.
	 */
	protected boolean pressionCapteurTactile;
	/**
	 * Objet de la classe Color.
	 */
	protected Color CapteurCouleur;
	/**
	 * Attribut enregistrant les distances r�cup�r�es.
	 */
	protected float distanceCapteurUltrasons;
	/**
	 * Attribut enregistrant le niveau de batterie restant.
	 */
	protected int niveauBatterie;
	/**
	 * Attribut enregistrant si un mur ou un robot est d�t�ct�e.
	 */
	protected boolean unRobotMurEstDetecte;
	/**
	 * Attribut enregistrant si une ligne blanche est d�t�ct�e.
	 */
	protected boolean LigneBlanche;
	/**
	 * Objet de la classe Agent.
	 */
	private Agent agent;
	
	
	/**
	 * @param agent
	 */
	public Perception(Agent agent) {
		this.agent = agent;
	}
	
	/**
	 * Retourne l'attribut niveauBatterie.
	 * 
	 * @return
	 */
	public int getNiveauBatterie() {
		return this.niveauBatterie;
	}
	/**
	 * Met � jour l'attribut niveauBatterie.
	 * 
	 * @param niveau
	 */
	public void setNiveauBatterie(int niveau) {
		this.niveauBatterie=niveau;
	}
	
	/**
	 * M�thode qui permet de r�cup�rer la distance r�cup�r�e par le capteur � ultrasons.
	 * 
	 * @return
	 */
	public float getDistanceCapteurUltrasons(){
		return this.distanceCapteurUltrasons;
	}

	/**
	 * M�thode qui permet de r�cup�rer la couleur r�cup�r�e par le capteur couleur.
	 * 
	 * @return
	 */
	public Color getCouleurCapteurCouleur() {
		return this.CapteurCouleur;
	}

	/**
	 * M�thode qui permet de r�cup�rer la pression d�t�ct�e par le capteur tactile.
	 * 
	 * @return
	 */
	public boolean getPressionCapteurTactile() {
		return this.pressionCapteurTactile;
	}

	/**
	 * M�thode qui permet d�initialiser les valeurs des attributs : distance, couleur, pression. 
	 * En utilisant les classes correspondant � chaque capteur.
	 */
	public void initCapteurs() {
		agent.getCapteurCouleur().setCouleur();
		agent.getCapteurTactile().setPression();
		agent.getCapteurUltrasons().setDistance();
	}
}
