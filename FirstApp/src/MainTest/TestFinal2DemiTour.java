package MainTest;

import Robot.Agent;
import lejos.hardware.Button;
import lejos.utility.Delay;

public class TestFinal2DemiTour {
	
	public static void main(String[] args) throws Exception {	
		
		TestFinal2 test = new TestFinal2();
		test.start();
		//test.fermerLesPinces();//car pince ouvertes au depart sinon a enlever
		test.codeBourrinCarLeRobotAvancePasDroit();
		
		System.out.println("Fin du test");
		Delay.msDelay(20000);
	}

	private Agent agent;
	private int degrestournes;
	private Boolean bonneDirection;
	private int nbDemiTour;
	
	public void start() {
		agent = new Agent();
		bonneDirection = true;
		nbDemiTour = 0;
		//degrestournes = 0;//Faire avec les couleurs peut-etre.
		System.out.println("Press enter to run testBourrin...");
		Button.ENTER.waitForPressAndRelease();
	}
	
	public void codeBourrinCarLeRobotAvancePasDroit() throws Exception {	
		boolean loop = true;
		agent.getPerceptionAct().initCapteurs();//On init la valeur initiale � tous les capteurs
		
		agent.getPinces().ouverture();//Ouvre que si pas d�ja ouvert
		agent.getAvancerOuReculer().avancerSynchro(); // On avance
		//Boucle : 
		while(loop) {
			//agent.getPerceptionAct().initCapteurs();//On set la valeur des capteurs � chaque boucle
			agent.getCapteurCouleur().setCouleur();
			if(agent.getCapteurCouleur().couleurEstBlanche()) {
				break;
			}
			agent.getCapteurTactile().setPression();
			if(agent.getCapteurTactile().getPression()) {
				break;
			}
			//agent.getCapteurUltrasons().setDistance(); ca le fait deja dans murouRobotDEtecte
			if(agent.getCapteurUltrasons().murOuRobotDetecte()) {
				break;
			}
		}
		
		//Stop
		agent.getAvancerOuReculer().sarreterSynchro();
		
		//En fonction des cas :
		if(agent.getAction().robotEstBloque()) {//Robot bloqu�
			System.out.println("Le Robot Est Bloqu�");
			Delay.msDelay(5000);
			agent.getAvancerOuReculer().reculerPourUnTemps(1.5f);
			agent.getTournerOuPivoter().pivoterAvecDeuxRouesVersLaDroite(45);//A optimiser droite ou gauche d�pend de la distance du mur
			System.out.println("Fin du d�bloquage");
			Delay.msDelay(5000);
			codeBourrinCarLeRobotAvancePasDroit();
			return;
		}else if(agent.getCapteurCouleur().couleurEstBlanche()) {//Couleur blanche
			System.out.println("Couleur blanche d�t�ct�e sans palet");
			Delay.msDelay(5000);
			agent.getAvancerOuReculer().reculerPourUnTemps(1.5f);
			this.demiTour();
			System.out.println("Fin du traitement de la couleur blanche");
			Delay.msDelay(5000);
			codeBourrinCarLeRobotAvancePasDroit();
			return;
		}else {//Pression tactile
			System.out.println("Pression tactile d�t�ct�e : direction == " + degrestournes);
			Delay.msDelay(5000);
			agent.getPinces().fermeture();
			//agent.getTournerOuPivoter().pivoterAvecDeuxRouesVersLaGauche(degrestournes);//Direction l'en but adverse
			if(!this.bonneDirection) {
				this.demiTour();
			}
			this.avancerJusquaLenButAdverseEnEvitantLesMurs();//Risque de pousser les autres palets
			//On depose le palet :
			agent.getPinces().ouverture();
			agent.getAvancerOuReculer().reculerPourUnTemps(1.5f); 
			this.demiTour();//A reflechir car si l'arriere du robot est bloqu� il se peut qu'il n'arrive pas a se diriger vers l'en but
			System.out.println("Fin du traitement de la pression tactile");
			Delay.msDelay(5000);
			codeBourrinCarLeRobotAvancePasDroit();
			return;
		}
	}
	
	public void fermerLesPinces() {
		agent.getPinces().setPincesOuvertes(true);
		agent.getPinces().fermeture();
	}
	
	public void avancerJusquaLenButAdverseEnEvitantLesMurs() throws Exception {
		System.out.println("avancerJusquaUneLigneBlancheEtEviterObstacle");
		boolean boucle = true;
		while(boucle) {
			if(agent.getCapteurUltrasons().murOuRobotDetecteAvecDistance(0.150f)) {//Cas d'arret 1
				agent.getAvancerOuReculer().sarreterSynchro();
				robotEstBloqueAvecUnPalet();
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
	

	public void robotEstBloqueAvecUnPalet() throws Exception {
		System.out.println("Le Robot Est Bloqu� on le remet en direction de l'en but");//Normalement il est d�ja en bonne direction vers l'en but ici c'est le cas ou le robot rencontre un autre robot
		Delay.msDelay(5000);
		agent.getAvancerOuReculer().reculerPourUnTemps(1.5f);
		agent.getTournerOuPivoter().pivoterAvecDeuxRouesVersLaGauche(20); //A reflechir (optimiser)
		avancerJusquaLenButAdverseEnEvitantLesMurs();
	}

	
	public void demiTour() {
		nbDemiTour ++;
		agent.getTournerOuPivoter().pivoterAvecDeuxRouesVersLaDroite(180);
		if(nbDemiTour % 2 == 1) {
			bonneDirection = false;
		}else {
			bonneDirection = true;
		}
		
	}
}
