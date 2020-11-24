package Vue;
import Robot.Agent;
import lejos.robotics.Color;

public class Perception {
	protected boolean pressionCapteurTactile;
	protected Color CapteurCouleur;
	protected float distanceCapteurUltrasons;
	protected int niveauBatterie;
	protected boolean unRobotMurEstDetecte;
	protected boolean LigneBlanche;
	private Agent agent;
	
	// Constructeur:
	
	public Perception(Agent agent) {
		this.agent = agent;
	}
	// Methodes :
	
	public int getNiveauBatterie() {
		return this.niveauBatterie;
	}
	public void setNiveauBatterie(int niveau) {
		this.niveauBatterie=niveau;
	}
	
// Methode qui permet de r�cup�rer la distance entre le robot et un �ventuel objet (mur, palet, robot adverse)
	public float getDistanceCapteurUltrasons(){
		return this.distanceCapteurUltrasons;
	}
// Methode qui permet de r�cup�rer les donn�es relatives aux couleurs
	public Color getCouleurCapteurCouleur() {
		return this.CapteurCouleur;
	}
//Methode qui permet de savoir si le capteur est �press� ou non
	public boolean getPressionCapteurTactile() {
		return this.pressionCapteurTactile;
	}
/*Mehode qui permet d�initialiser les valeurs des attributs : distance, couleur, pression. 
 *En utilisant les classes correspondantes � chaque capteur/attribut.
 */
	public void initCapteurs() {//Recup�re les r�sultat des capteurs et set la valeur des attributs correspondants.
		agent.getCapteurCouleur().setCouleur();
		agent.getCapteurTactile().setPression();
		agent.getCapteurUltrasons().setDistance();
	}
}
