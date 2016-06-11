import bwapi.Game;
import bwapi.Unit;
import bwapi.UnitType;

import java.util.Iterator;

/**
 * Created by guyver on 2016/3/20.
 */
public class GeyserGatherStrategy {

    private static boolean isBuildingRefinery = false;

    private static boolean isSetToBuildRefinery = false;

    /**
     * build refinery and gather gas
     */
    public static void gatherGeysers(Unit farmer, Game game) {

        if (BasicInfo.Geyser_Farmer.size() <= 3) {

            /**
             * build refinery
             */
            for (Unit neutral : game.getNeutralUnits()) {

                Unit geyser = null;

                if (neutral.getType().equals(UnitType.Resource_Vespene_Geyser)) {

                    /**
                     * get Geyser type unit
                     */
                    geyser = neutral;

                    buildRefinery(game, geyser);
                }
            }

            /**
             * gather gas
             */
            for (Unit neutral : game.self().getUnits()) {

                Unit refinery = null;

                if (neutral.getType().isRefinery()) {

                    /**
                     * get refinery type unit
                     */
                    refinery = neutral;

                    gatherGas(refinery);
                }

            }

        }
    }

    /**
     * sent farmer from WorkingFarmer.Mineral_Addition_Farmer to gather gas, 3 farmer per vespene at most.
     */
    private static void gatherGas(Unit refinery) {
        if (refinery.isCompleted() && BasicInfo.Geyser_Farmer.size() <= 3) {
            for(Iterator it = BasicInfo.Mineral_Addition_Farmer.keySet().iterator(); it.hasNext() && 2 - BasicInfo.Geyser_Farmer.size() >= 0;) {
                /**
                 * get three farmers from Mineral_Addition_Farmer
                 */
                Unit tempGeyserFarmer = (Unit)it.next();
                /**
                 * do gas gather action
                 */
                if(!tempGeyserFarmer.isCarryingMinerals()) {
                    tempGeyserFarmer.gather(refinery);
                    BasicInfo.Geyser_Farmer.add(tempGeyserFarmer);
                    BasicInfo.Mineral_Addition_Farmer.remove(tempGeyserFarmer);
                    System.out.println("+++++++++++++++++++++++++++++++++++++");
                    System.out.println("farmer : " + tempGeyserFarmer.getID() + " is gathering gas");
                    System.out.println("+++++++++++++++++++++++++++++++++++++");
                }
            }
        }
    }

    /**
     * sent one farmer witch from WorkingFarmer.Mineral_Addition_Farmer to build refinery
     */
    private static void buildRefinery(Game game, Unit geyser) {

        if(geyser != null && game.elapsedTime() > 100 && !isBuildingRefinery && game.self().minerals() >= 100) {
            Iterator it = BasicInfo.Mineral_Addition_Farmer.keySet().iterator();
            /**
             * get one farmer from Mineral_Addition_Farmer
             */
            Unit tempGeyserFarmer = null;
            if(it.hasNext()) {
                tempGeyserFarmer = (Unit) it.next();
            }
            /**
             * do refinery building action
             */
            if (geyser != null && !isBuildingRefinery && game.self().minerals() >= 100) {
                if(!tempGeyserFarmer.isCarryingMinerals() && !isSetToBuildRefinery) {
                    System.out.print("geyser.getTilePosition() " + geyser.getTilePosition());
                    tempGeyserFarmer.build(UnitType.Terran_Refinery, geyser.getTilePosition());
                    isSetToBuildRefinery = true;
                    System.out.println("farmer : " + tempGeyserFarmer.getID() + " is on building progress : " + tempGeyserFarmer.isConstructing());
                    System.out.println("farmer : " + tempGeyserFarmer.getID() + " is building refinery");
                }
                if(tempGeyserFarmer.isConstructing()) {
                    isBuildingRefinery = true;
                }
            }
        }
    }
}
