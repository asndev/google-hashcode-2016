package de.antonstepan.hackathonstarter.sorter;

import java.util.Comparator;

import de.antonstepan.hackathonstarter.Drone;
import de.antonstepan.hackathonstarter.Location;
import de.antonstepan.hackathonstarter.Order;
import de.antonstepan.hackathonstarter.Simulation;

public class DistancePlusItemsSorter implements Comparator<Order> {
  
  private Drone drone;

  public DistancePlusItemsSorter(Drone d) {
    this.drone = d;
  }
  
  @Override
  public int compare(Order o1, Order o2) {
    int o1Size = o1.getItems().size();
    int o2Size = o2.getItems().size();
    Location dronePos = drone.getPosition();
    int o1Dist = Simulation.getDistance(dronePos, o1.getDeliveryLocation());
    int o2Dist = Simulation.getDistance(dronePos, o2.getDeliveryLocation());
    // TODO ^ add constraint that if items weight > drone Cap => add 2xDistances
    int s1 = o1Dist + o1Size;
    int s2 = o2Dist + o2Size;
    
    if (s1 == s2) {
      return 0;
    } else if (s1 < s2) {
      return -1;
    } else {
      return 1;
    }
  }

}