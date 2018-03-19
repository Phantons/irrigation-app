package es.upm.etsit.irrigation.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class Mode implements Serializable {

    private static final long serialVersionUID = 2L;
    private transient int ID;
    private String name;

    private List<Zone> zones = new ArrayList<Zone>();

    private boolean isActive = false;

    public Mode(int _ID, String _name) {
        ID = _ID;
        name = _name;
    }

    public int getID() {
        return ID;
    }

    public void setName(String _name) {
        name = _name;
    }

    public void setID(int id) {
      this.ID = id;
    }

    public String getName() {
        return name;
    }

    /**
     * @return the zones
     */
    public List<Zone> getZones() {
        return zones;
    }

    /**
     * @param zones the zones to set
     */
    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }

    /**
     * @param zone the zone to set
     */
    public void setZone(int  nZone, Zone zone) {
        zones.set(nZone, zone);
    }

    /**
     * añade nueva zona a la matriz zonas de un Modo
     * @param zone the zone to add
     */
    public void addZone(Zone zone) {
        this.zones.add(zone);
    }

    /**
     * @return the isActive
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

}
