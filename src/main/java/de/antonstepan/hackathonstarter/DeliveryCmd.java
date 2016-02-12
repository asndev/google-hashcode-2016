package de.antonstepan.hackathonstarter;

public class DeliveryCmd implements Command {

  private final int droneId;
  private final int warehouseId;
  private final int productTypeId;
  private final int count;

  public DeliveryCmd(int droneId, int warehouseId, int typeId, int count) {
      this.droneId = droneId;
      this.warehouseId = warehouseId;
      this.productTypeId = typeId;
      this.count = count;
    }

  @Override
  public String toString() {
    return String.format("%s D %s %s %s", droneId, warehouseId, productTypeId, count);
  }

}
