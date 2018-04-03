package es.upm.etsit.irrigation.shared;

import java.io.Serializable;

public class Zone implements Serializable {
  private static final long serialVersionUID = 2L;
  
  private final String name;
  private final int pinAddress;
  private Schedule schedule;
  private boolean shouldTakeWeather = false;
  private boolean isWatering = false;
  
  public Zone(String _name, int _pinAddress) {
    name = _name;
    pinAddress = _pinAddress;
  }

  public String getName() {
    return name;
  }
  
  public int getPinAddress() {
    return pinAddress;
  }

  public Schedule getSchedule() {
    return schedule;
  }

  public void setSchedule(Schedule schedule) {
    this.schedule = schedule;
  }

  public boolean shouldTakeWeather() {
    return shouldTakeWeather;
  }

  public void setShouldTakeWeather(boolean shouldTakeWeather) {
    this.shouldTakeWeather = shouldTakeWeather;
  }

  /**
   * @return the isActive
   */
  public boolean isWatering() {
    return isWatering;
  }

  /**
   * @param isActive the isActive to set
   */
  public void setWatering(boolean isActive) {
    this.isWatering = isActive;
  }
}
