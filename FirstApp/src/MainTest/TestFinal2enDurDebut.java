package MainTest;

import Robot.Agent;
import lejos.hardware.Button;
import lejos.utility.Delay;

public class TestFinal2enDurDebut {
	/*
	 * Ce test est le programme qui va �tre lanc� lors de la comp�tition. Il utilise de nombreuses fonctions d�velopp�es dans les diff�rentes classes
	 * des packages Controleur,Moteurs,Robot et Agent. Cependant il utilise seulement des fonctions de bas niveau car vu que nous avons chang� notre strat�gie
	 * il aurait fallu changer le contenu des fonctions. C'est pour cela que ce test s'�xecute surtout dans cette classe (TestFinal2enDurDebut).
	 * Il est compos� d'une premi�re partie qui g�re les premi�res actions (r�cup�rer le premier palet et l'amener vers l'en-but adverse), d'une boucle principale qui fait avancer
	 * le robot tant qu'il n'est pas bloqu�, qu'il n'a pas toucher un palet ou qu''il n'a pas atteint une ligne blanche. Ce programme pourrait �tre bien plus optimis�
	 * cependant par manque de temps et de pr�cision nous avons pr�f�r� une strat�gie comme telle. Voici les principales conditions :
	 * 
	 * Robot bloqu� -> Reculer + Rotation de 45�.
	 * 
	 * Robot atteint une ligne blanche sans palet -> Reculer + rotation de 135�.
	 * 									avec palet -> Ouvrir Les pinces + reculer + Rotation 180�
	 * 
	 * Robot touche un palet -> Si on est dans la bonneDirection -> avancer
	 * 							-> sinon -> rotation 180� 
	 * 
	 * Pour calculer la bonne direction nous avons utilis� un attribut degrestournes qui enregistre toutes les rotations du robot pr�cis�ment sauf dans le cas 
	 * o� le robot serait bloqu� dans sa rotation (un palet, un autre robot ou un mur le g�ne).Ainsi si l'attribut degrestournes est compris entre 0 et 180 il est
	 * dans la bonne direction et l'inverse sinon. A savoir : le robot regardant l'en but adverse perpendiculairement est � 90�.
	 *
	 * Exemple : direction � gauche = 0/ direction en face = 90�/ direction � droite = 180�/direction derri�re = 270� et direction � gauche = 360� ou 0�.
	 * 
	 */
	

	public static void main(String[] args) throws Exception {	
				
		TestFinal2enDurDebut test = new TestFinal2enDurDebut();
		
		test.start();
		test.mainLoop();
		
		System.out.println("Fin du test");
		Delay.msDelay(20000);
	}

	private Agent agent;
	private int degrestournes;
	private boolean bonneDirection;
	private boolean finVersLaDroite;
	
	public void start() {
		agent = new Agent();
		
		//Debut premiereAction
				int positionInitiale = 1;//Milieu par d�faut
				System.out.println("Press left or right to run testFinalAvecPremieresActions...");
				Button.waitForAnyPress();
				if(Button.RIGHT.isDown()) {
					positionInitiale = 2;
					System.out.println("Right");
				}else if(Button.LEFT.isDown()) {
					positionInitiale = 0;
					System.out.println("Left");
				}
				agent.getAction().premieresActions(positionInitiale);
		//Fin premieres Action

		//System.out.println("Press Enter to run testFinalSansPremieresActions...");
		//Button.waitForAnyPress();
	
				//A changer en fonction de si on mets premieresAction ou pas
		degrestournes = 270;//Par rapport au stade (car de base le robot est � 90 degr�s il est perpendiculaire � l'horizontale)
		bonneDirection = false;
		finVersLaDroite = true;
	}
	
	public void mainLoop() throws Exception {
		//System.out.println("Lancement de la boucle");
		boolean loop = true;
		agent.getPerceptionAct().initCapteurs();//On init la valeur initiale � tous les capteurs
		agent.getPinces().ouverture();//Ouvre que si pas d�ja ouvert
		agent.getAvancerOuReculer().avancerSynchro(); // On avance
		//Boucle : 
		while(loop) {
			agent.getCapteurCouleur().setCouleur();
			if(agent.getCapteurCouleur().couleurEstBlanche()) {
				break;
			}
			agent.getCapteurTactile().setPression();
			if(agent.getCapteurTactile().getPression()) {
				break;
			}
			if(agent.getCapteurUltrasons().murOuRobotDetecte()) {
				break;
			}
		}
		
		//Stop
		agent.getAvancerOuReculer().sarreterSynchro();
		
		//En fonction des cas :
		if(agent.getAction().robotEstBloque()) {//Robot bloqu�
			System.out.println("Le Robot Est Bloqu�");
			//Delay.msDelay(5000);
			agent.getAvancerOuReculer().reculerPourUnTemps(1.5f);
			agent.getTournerOuPivoter().pivoterAvecDeuxRoues(45);
			calculDegre(45);
			System.out.println("Fin du d�bloquage");
			//Delay.msDelay(5000);
			mainLoop();
		}else if(agent.getCapteurCouleur().couleurEstBlanche()) {//Couleur blanche
			System.out.println("Couleur blanche d�t�ct�e sans palet");
			//Delay.msDelay(5000);
			agent.getAvancerOuReculer().reculerPourUnTemps(1.5f);
			agent.getTournerOuPivoter().pivoterAvecDeuxRoues(135);
			calculDegre(135);
			System.out.println("Fin du traitement de la couleur blanche");
			//Delay.msDelay(5000);
			mainLoop();
		}else if(agent.getCapteurTactile().getPression()){//Pression tactile
			System.out.println("Pression tactile d�t�ct�e : direction == " + bonneDirection);
			//Delay.msDelay(5000);
			agent.getPinces().fermeture();
			if(!bonneDirection) {
				agent.getTournerOuPivoter().pivoterAvecDeuxRouesAvecPalet(180);
				calculDegre(180);
			}
			this.avancerJusquaLenButAdverseEnEvitantLesMurs();
			//On depose le palet :
			agent.getPinces().ouverture();
			agent.getAvancerOuReculer().reculerPourUnTemps(1.5f); 
			if(finVersLaDroite) {
				agent.getTournerOuPivoter().pivoterAvecDeuxRoues(180);
				calculDegre(180);
				finVersLaDroite = false;
			}else {
				agent.getTournerOuPivoter().pivoterAvecDeuxRoues(-180);
				calculDegre(-180);
				finVersLaDroite = true;
			}
			System.out.println("Fin du traitement de la pression tactile");
			//Delay.msDelay(5000);
			mainLoop();
		}else {
			//On a un mur mal d�t�cter
			agent.getAvancerOuReculer().reculerPourUnTemps(1.5f);
			agent.getTournerOuPivoter().pivoterAvecDeuxRoues(45);
			calculDegre(45);
			mainLoop();
		}
	}
	
	public void calculDegre(int degre) throws Exception {
		//Calcul bonne direction :
		
		degrestournes += degre;
		
		if(degrestournes > 360) {
			degrestournes -= 360;
		}else if(degrestournes < 0) {
			degrestournes += 360;
		}
		
		if(degrestournes >= 0 && degrestournes <= 180) {
			bonneDirection = true;
		}else if(degrestournes >= 180 && degrestournes <= 360) {
			bonneDirection = false;
		}
		
		//Fin calcul bonne direction 
		System.out.println("bonnedirection : " + bonneDirection);
		System.out.println("degre : " + degrestournes);
		//Delay.msDelay(5000);
	}
	
	public void avancerJusquaLenButAdverseEnEvitantLesMurs() throws Exception {
		System.out.println("avancerJusquaUneLigneBlancheEtEviterObstacle");
		boolean boucle = true;
		while(boucle) {//
			if(agent.getCapteurUltrasons().murOuRobotDetecteAvecDistance(0.150f)) {//Cas d'arret 1
				agent.getAvancerOuReculer().sarreterSynchro();
				robotEstBloque();
				break;
			}else {
				agent.getCapteurCouleur().setCouleur();
				if (agent.getCapteurCouleur().getCouleur() == "blanc") {//Cas d'arret 2
					agent.getAvancerOuReculer().sarreterSynchro();
					break;
				}
			agent.getAvancerOuReculer().avancerSynchro();
			}
		}
	}
	
//Fonction qui fait reculer le robot si il rencontre un robot adverse ou un mur et s'il est perdu il pivote de 45 deg
	public void robotEstBloque() throws Exception {
		System.out.println("Le Robot Est Bloqu� on le remet en direction de l'en but");//Normalement il est d�ja en bonne direction vers l'en but ici c'est le cas ou le robot rencontre un autre robot
		//Delay.msDelay(5000);
		agent.getAvancerOuReculer().reculerPourUnTemps(1.5f);
		if(!bonneDirection) {
			agent.getTournerOuPivoter().pivoterAvecDeuxRouesAvecPalet(180);
			calculDegre(180);
		}else{
			agent.getTournerOuPivoter().pivoterAvecDeuxRouesAvecPalet(45);
			calculDegre(45);
			if(!bonneDirection) {
				agent.getTournerOuPivoter().pivoterAvecDeuxRouesAvecPalet(180);
				calculDegre(180);	
			}
		}
		avancerJusquaLenButAdverseEnEvitantLesMurs();
	}

}
