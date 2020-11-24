package Controleur;

import Moteurs.AvancerOuReculer;
import Moteurs.Pinces;
import Moteurs.TournerOuPivoter;
import Robot.Agent;
import Vue.CapteurUltrasons;
import Vue.Perception;
import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.robotics.Color;

// Classe pour les actions requises � la comp�tition

public class Action {
	
	private Agent agent;
	private Perception perceptionAct;
	private Perception perceptionPrec;
	private int historiqueDegres;
	private int nbPaletMarque;
	
	// Constructeur:
	public Action(Perception p1, Perception p2, Agent agent) {
		this.setPerceptionAct(p1);
		this.setPerceptionPrec(p2);
		this.setAgent(agent);
		this.setHistoriqueDegres(0);
		this.setNbPaletMarque(0);
	}
	// Methodes :
	
	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}
//R�cup�re l�objet perception pr�c�dent
	public Perception getPerceptionPrec() {
		return perceptionPrec;
	}
//Change ou initialise l�attribut perceptionPrec.
	public void setPerceptionPrec(Perception perceptionPrec) {
		this.perceptionPrec = perceptionPrec;
	}
//R�cup�re l�objet perception le plus r�cent
	public Perception getPerceptionAct() {
		return perceptionAct;
	}
//Change ou initialise l�attribut perceptionAct.
	public void setPerceptionAct(Perception perceptionAct) {
		this.perceptionAct = perceptionAct;
	}
	public int getHistoriqueDegres() {
		return historiqueDegres;
	}

	public void setHistoriqueDegres(int historiqueDegres) {
		this.historiqueDegres = historiqueDegres;
	}

//Reaction du robot � la recup�ration d'un palet
	public void onAUnPalet() {
		System.out.println("onAUnPalet");
		agent.getPinces().fermeture();
		agent.getAction().allerVersLenButAdverse();
	}
//Permet de faire pivoter le robot doucement afin de r�cup�rer la majorit� des distances des objets environnants
	public void detecterAutourDuRobot(boolean start, boolean detectionDecalee) {
		System.out.println("detecterAutourDuRobot");
		int nbligneblancheAfranchir;
		if(start) {
			agent.getPinces().ouverture();
		}if(detectionDecalee) {
			agent.getTournerOuPivoter().pivoterDunDegreDonneEnCrochet(90);
		}
		//Fonction qui aligne le robot vers un objet
		agent.getTournerOuPivoter().pivoterEtDetecterSurUnDegreDonne(agent, 180);
		//fonction qui avance le robot pour verif si c'est un palet
		if(agent.getCapteurUltrasons().VerifSiObjetDetecteEstUnPalet(agent.getAvancerOuReculer(),agent.getCapteurTactile(),agent.getCapteurCouleur())) {
			System.out.println("On a un palet en face de soit !!!!");
			//On a un palet en face de soit, on avance pour declenche la pression capteur tactile.
			agent.getAvancerOuReculer().avancerTqCapteurPressionPasEnfonce(agent.getCapteurTactile(),this,agent.getCapteurCouleur());
		}else {
			//C'est un mur ou robot adverse ou un palet derriere ligne blanche on recommence la recherche
			detecterAutourDuRobot(false,true);
		}
	}
//ouvre les pinces du robot, remet � O les tachometres, perceptionprec = perceptionact
	public void init() {
		this.perceptionPrec = this.perceptionAct;
		agent.getPinces().ouverture();
		agent.getAvancerOuReculer().resetTachoMetre();
	}
	
//Methode pour la 1er action: Recuperer le premier paler et le deposer dans l'enbut adverse
	public void premieresActions() {
		boolean loop = true;
		System.out.println("Press enter to run premieresActions...");
		Button.ENTER.waitForPressAndRelease();
		while(loop) {
			agent.getPerceptionAct().initCapteurs();//On mets � jour les valeurs des capteurs
			if (robotEstBloque()){
				System.out.println("Je suis bloqu� ... appeler la fonction reagirRobotBloque");
				loop = false;
			}else {
				this.init();//On ouvre les pinces (position initiale)
				agent.getAvancerOuReculer().avancerTqCapteurPressionPasEnfonceTest(agent.getCapteurTactile());//On avance tq on a pas toucher le palet
				agent.getPinces().fermeture();//On ferme les pinces
				
				agent.getTournerOuPivoter().pivoterDunDegreDonneEnCrochet(-45);//On evite les autres palets
				agent.getAvancerOuReculer().avancerPourUnTemps(1);
				agent.getTournerOuPivoter().pivoterDunDegreDonneEnCrochet(45);//On re s'aligne
				
				agent.getAvancerOuReculer().avancerJusquaUneLigne(agent.getCapteurCouleur(), "blanc");//On avance tant que la couleur n'est pas blanche
				this.deposerLePalet(true);//On lance les actions pour d�poser le palet (cod� en dur).
				loop = false;
				System.out.println("Fin de la fonction premieresActions");
			}
		}
	}
	
/*Methode pour aller vers l'enbut adverse
 * Cette fonction permet d�une part d�orienter le robot en direction de l�en-but adverse en le faisant pivoter du nombre
 * de degr� pivot� depuis le lancement du programme (de + ou - 360 degr�s selon la direction souhait�e)
 * Et d�une autre part, une fois le robot orient� en direction de l�en-but adverse, la fonction permet de faire avancer le robot
 jusqu�� atteindre la ligne blanche
 */
	public void allerVersLenButAdverse() {
		System.out.println("allerVersLenButAdverse");
		//On oriente notre robot vers l'en but adverse en regardant de combien on a deja pivoter de degr� notre robot.
		if(historiqueDegres<=180) {
			agent.getTournerOuPivoter().pivoterDunDegreDonneEnCrochet(-historiqueDegres);
		}
		else if(historiqueDegres>180) {
			agent.getTournerOuPivoter().pivoterDunDegreDonneEnCrochet(360-historiqueDegres);
		}
		else if(historiqueDegres<=-180) {
			agent.getTournerOuPivoter().pivoterDunDegreDonneEnCrochet(-360-historiqueDegres);
		}
		//Avancer jusqu'a la ligne blanche tout en evitant les murs, robot et palet
		agent.getAvancerOuReculer().avancerJusquaUneLigneEtEviterObstacle(agent.getCapteurCouleur(),agent.getCapteurUltrasons(),agent.getAction(),"blanc");
	}
	
//Methode qui permet d'enregistrer de combien le robot pivote.
	public void enregistrerPositionRobot(int degre) {
		this.historiqueDegres += degre;
		if(historiqueDegres>=360) {
			historiqueDegres = historiqueDegres -360;
		}
		else if (historiqueDegres <= -360) {
			historiqueDegres = historiqueDegres + 360;
		}
		//Eventuellement : A chaque passage sur une ligne de couleur on enregistre la couleur si elle nous int�rresse (Notre camp + couleur blanche)
	}

/*Verifier si le robot est bloqu�
 * Cette fonction permet de calculer de combien de degr� le robot a pivot� depuis le lancement du programme
 */
	public boolean robotEstBloque() {
		return agent.getCapteurUltrasons().murOuRobotDetecte();
	}

/*Reagir quand le robot est bloqu�
 * Cette fonction permet de r�agir en cas de blocage du robot : si les pinces sont ferm�es (= le robot tient un palet) alors
 * on fait reculer le robot pendant 2 secondes puis on le fait aller vers l�en-but adverse.
 * Si les pinces sont ouvertes (= pas de palet entre les pinces) la fonction fait reculer le robot pendant 2 secondes puis lui
 fait chercher un palet en analysant ce qui l�entoure
 */

	public void reagirRobotBloque() {//�a c'est plus si le robot est coinc� vraiment dans un coin
		System.out.println("reagirRobotBloque");
		if(agent.getPinces().isPincesOuvertes()==false) {
				agent.getAvancerOuReculer().reculerPourUnTemps(2);//On recule
				allerVersLenButAdverse();
		}
		else {
			agent.getAvancerOuReculer().reculerPourUnTemps(2);//On recule
			detecterAutourDuRobot(false, true);
		}
		
		//utiliser detecterAutourDuRobot ?
		//A faire : On pivote pour recup des distances et voir quelle est la meilleur direction o� on peut aller.
	}

/*Deposer le palet
 * Cette fonction permet de d�poser le palet : le robot recule pendant un court instant, ouvre les pinces pour relacher 
 * le palet, recule de nouveau puis pivote de 50 degr�s (ou 90 ?)
 */
	public void deposerLePalet(boolean premieresAction) {
		System.out.println("deposerLePalet");
		nbPaletMarque++;
		//On recule un peu
		//agent.getAvancerOuReculer().reculerPourUnTemps(0.4f);en commentaire car il va pas si vite
		//On ouvre les pinces
		agent.getPinces().ouverture();
		//On recule
		agent.getAvancerOuReculer().reculerPourUnTemps(0.8f); 
		//on pivote de 50 degrees environ
		agent.getTournerOuPivoter().pivoterDunDegreDonneEnCrochet(180);
		
		
		//------------------ Les cas d'arrets ------------------------
		
		if(premieresAction) {//Fin test premieresActions
			//agent.getPinces().fermeture();
			//System.exit(0); pour le test bourrin
		}else {
			//Pour le test d'1 palet on se stop--------------
			agent.getPinces().fermeture();
			System.exit(0);
			//---------------------------------
			
			//Sinon
			if(nbPaletMarque == 4) {//On donne un nombre de palet � rentrer puis on le stop si il a atteint
				agent.getPinces().fermeture();
				System.exit(0);
			}else {
				this.detecterAutourDuRobot(false,false);
			}
		}
		
	}

	public int getNbPaletMarque() {
		return nbPaletMarque;
	}

	public void setNbPaletMarque(int nbPaletMarque) {
		this.nbPaletMarque = nbPaletMarque;
	}

}
