import Moteurs.AvancerOuReculer;
import Moteurs.Deplacement;
import Moteurs.TournerOuPivoter;
import Vue.CapteurCouleur;
import Vue.Perception;
import lejos.hardware.motor.Motor;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;

public class testDeplacement {

	public static void main(String[] args) {
		//Les moteurs des roues doivent etre branch�s sur le port B et C (A mettre dans la m�thode init() classe Action).
		RegulatedMotor leftMotor = Motor.B;
	    RegulatedMotor rightMotor = Motor.C;
	    
	    //Cr�ation des objets pour le d�placement
		AvancerOuReculer avanceroureculer = new AvancerOuReculer(leftMotor,rightMotor);
		TournerOuPivoter tourneroupivoter = new TournerOuPivoter(leftMotor,rightMotor);

		System.out.println("J'avance 3 secondes");
		avanceroureculer.avancerPourUnTemps(3);
		
		System.out.println("Je recule 3 secondes");
		avanceroureculer.reculerPourUnTemps(3);
		
		System.out.println("Je pivote de 90 degr�s dans un sens");
		tourneroupivoter.pivoterDunDegreDonne(90);
		
		System.out.println("Je pivote de 90 degr�s dans l'autre sens");
		tourneroupivoter.pivoterDunDegreDonne(-90);
		
		System.out.println("On test de combien de centim�tre j'avance pour 1 r�volution");
		avanceroureculer.avancerSurUneDistance(0);
		
		System.out.println("On test de combien de centim�tre j'avance pour 1 r�volution");
		avanceroureculer.reculerSurUneDistance(0);
		
		System.out.println("Je tourne vers la droite");
		tourneroupivoter.tournerSurUnTempsEtUneDirectionVague(3, 1);//1 > 0 => vers la droite
		
		System.out.println("Je tourne vers la gauche");
		tourneroupivoter.tournerSurUnTempsEtUneDirectionVague(3, -1);//-1 < 0 => vers la gauche
		
		//Creation d'une perception
		Perception perception = new Perception();
		//Cr�ation d'un objet CapteurCouleur
		CapteurCouleur capteurCouleur = new CapteurCouleur(perception);
		capteurCouleur.setCouleur();//Mets � jour l'attribut couleur de la classe capteurcouleur et perception. 
		capteurCouleur.setPort(1);//Capteur branch� sur le port n�1
		System.out.println("Avancer jusqu'a une ligne");
		avanceroureculer.avancerJusquaUneLigne(capteurCouleur, Color));
		
		System.out.println("Fin du test");
	}

}
