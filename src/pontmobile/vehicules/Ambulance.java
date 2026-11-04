package pontmobile.vehicules;

import pontmobile.Pont;
import pontmobile.SensVehicule;

public class Ambulance extends VehiculePrioritaire {

    public Ambulance(String id, Pont pont, SensVehicule sens) {
        super(id, pont, sens);
    }
}