import bwapi.*;

/**
 * Created by guyver on 2016/3/20.
 */
public class MineralGatherStrategy extends BasicInfo {

    private static Unit closestMineral = null;
    private static boolean mineralFarmerIsFull = false;

    /**
     * gathering Mineral
     */
    public static void gatherMinerals(Unit farmer, Game game) {

        if (farmer.getType().equals(UnitType.Terran_SCV) || farmer.getType().equals(UnitType.Protoss_Probe) || farmer.getType().equals(UnitType.Zerg_Drone)) {

            if (farmer.isIdle()) {

                /**
                 * check BasicInfo.Mineral_Farmer to see whether it's full of 8 units
                 */
                if(BasicInfo.Mineral_Farmer.size() >= 4) {
                    mineralFarmerIsFull = true;
                }else {
                    mineralFarmerIsFull = false;
                }

                /**
                 * find out the shortest route between farmer and mineral
                 */
                for (Unit neutral : game.getNeutralUnits()) {

                    Unit mineral = null;

                    /**
                     * get Mineral type resource
                     */
                    if (neutral.getType().equals(UnitType.Resource_Mineral_Field)) {
                        mineral = neutral;
                    }

                    /**
                     * sent idle farmer to gather the closest mineral, every farmer attached one of the 8 minerals.
                     */
                    if (mineral != null && !mineralFarmerIsFull && !BasicInfo.Mineral_Farmer.containsValue(mineral) && (closestMineral == null || farmer.getDistance(mineral) < farmer.getDistance(closestMineral))) {
                        closestMineral = mineral;
                        BasicInfo.Mineral_Farmer.put(farmer, closestMineral);
                    }
                    /**
                     * if more farmer have been produced, same strategy for minerals gathering. But those farmer could be do other job on the future.
                     */
                    if (mineral != null && mineralFarmerIsFull && !BasicInfo.Mineral_Farmer.containsKey(farmer) && !BasicInfo.Mineral_Addition_Farmer.containsValue(mineral) && (closestMineral == null || farmer.getDistance(mineral) < farmer.getDistance(closestMineral))) {
                        closestMineral = mineral;
                        BasicInfo.Mineral_Addition_Farmer.put(farmer, closestMineral);
                    }
                }

                /**
                 * gathering minerals
                 */
                if (closestMineral != null && farmer.canGather()) {

                    System.out.println("mineral: " + closestMineral.getID() + " 's being harvest is " + closestMineral.isBeingGathered());
                    System.out.println("farmer: " + farmer.getID() + " 's gatherable is " + farmer.canGather());
                    System.out.println("mineral: " + closestMineral.getID() + " underDisruptionWeb is " + closestMineral.isUnderDisruptionWeb());
                    System.out.println("mineral: " + closestMineral.getID() + " 's position is " + closestMineral.getPosition());
                    System.out.println("farmer: " + farmer.getID() + " 's position is " + farmer.getPosition());
                    System.out.println("distance between mineral: " + closestMineral.getID() + " and farmer: " + farmer.getID() + " is " + farmer.getDistance(closestMineral));
                    boolean flag = farmer.gather(closestMineral);
                    if (flag) {
                        System.out.println("farmer: " + farmer.getID() + " gather mineral: " + closestMineral.getID());
                    } else {
                        System.out.println("farmer: " + farmer.getID() + " can not gather mineral: " + closestMineral.getID());
                        System.out.println("rightClick mineral: " + closestMineral.getID() + " is " + farmer.canRightClick(closestMineral));
                        farmer.canRightClick(closestMineral);
                    }

                    closestMineral = null;
                    System.out.println("===============================");
                }

            }

        }

    }
}