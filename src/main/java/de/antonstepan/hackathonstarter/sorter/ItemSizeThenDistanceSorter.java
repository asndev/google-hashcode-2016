package de.antonstepan.hackathonstarter.sorter;

import java.util.Comparator;

import de.antonstepan.hackathonstarter.Drone;
import de.antonstepan.hackathonstarter.Location;
import de.antonstepan.hackathonstarter.Order;
import de.antonstepan.hackathonstarter.Simulation;

public class ItemSizeThenDistanceSorter implements Comparator<Order> {
  
  private Drone drone;

  public ItemSizeThenDistanceSorter(Drone d) {
    this.drone = d;
  }
  
  @Override
  public int compare(Order o1, Order o2) {
    int o1Size = o1.getItems().size();
    int o2Size = o2.getItems().size();
    Location dronePos = drone.getPosition();
    if (o1Size == o2Size) {
      int o1Dist = Simulation.getDistance(dronePos, o1.getDeliveryLocation());
      int o2Dist = Simulation.getDistance(dronePos, o2.getDeliveryLocation());
      if (o1Dist == o2Dist) {
        return 0;
      } else if (o1Dist < o2Dist) {
        return -1;
      } else {
        return 1;
      }
    } else if (o1Size < o2Size) {
      return -1;
    } else {
      return 1;
    }
  }

}