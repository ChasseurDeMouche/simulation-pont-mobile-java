package ontmobile;

import pontmobile.modele.Pont;
import pontmobile.modele.EtatPont;

public class Main {
    public static void main(String[] args) {
        Pont pont = new Pont(8);
        System.out.println("Nombre de segments : " + pont.getNombreSegments());
        System.out.println("État initial : " + pont.getEtat());

        if (pont.getEtat() == EtatPont.FERME) {
            System.out.println("Le pont est bien fermé par défaut.");
        }
    }
}
