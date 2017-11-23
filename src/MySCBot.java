import bwapi.*;
import bwta.BWTA;

import java.util.*;

/**
 * Created by guyver on 2016/3/19.
 */
public class MySCBot  extends DefaultBWListener {
    private Mirror mirror = new Mirror();
    private Game game = null;
    private Player self = null;

    private Set<Unit> farmerNum = new HashSet<>();
    private Set<Unit> commanderBaseNum = new HashSet<>();
    private Set<Unit> otherNum = new HashSet<>();

    @Override
    public void onStart() {
        game = mirror.getGame();
        self = game.self();

        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");
    }

    @Override
    public void onFrame() {
        game.drawTextScreen(10, 10, "Player : " + self.getName() + " elapsed time : " + game.elapsedTime());
        for(Unit unit : self.getUnits()) {
            if(unit.getType().equals(UnitType.Terran_Command_Center)) {

                BasicInfo.commandCenter = unit;

                /**
                 * max 25 farmers per command center
                 */
                if(self.minerals() >=50
                        && !(game.elapsedTime() >= 50 && game.elapsedTime() <= 180)
                        && !(BasicInfo.Mineral_Farmer.size() + BasicInfo.Mineral_Addition_Farmer.size() + BasicInfo.Geyser_Farmer.size() > 25)) {
                    unit.train(UnitType.Terran_SCV);
                }

            }

            if(BasicInfo.Mineral_Addition_Farmer.containsKey(unit)) {

                if(self.minerals() >=100
                        && (
                        (game.elapsedTime() <= 10070  && BasicInfo.Mineral_Farmer.size() >= 4)
                        || (self.minerals() >=550 && (self.supplyTotal() - self.supplyUsed() <= 6))
                        )
                        ) {

                   // if(BasicInfo.SupplyDepot_Farmer.size() == 0) {
                        TilePosition tp = null;
                        for(int i=100; i<1000000; i++) {
                            if(game.canBuildHere(tp = new TilePosition(125, 25), UnitType.Terran_Supply_Depot, unit)) {
                                buildSupplyDepot(unit, tp);
                                break;
                            }else if (game.canBuildHere(tp = new TilePosition(unit.getTilePosition().getX() - i, unit.getTilePosition().getY() + i), UnitType.Terran_Supply_Depot, unit)) {
                                buildSupplyDepot(unit, tp);
                                break;
                            }else if (game.canBuildHere(tp = new TilePosition(unit.getTilePosition().getX() + i, unit.getTilePosition().getY() - i), UnitType.Terran_Supply_Depot, unit)) {
                                buildSupplyDepot(unit, tp);
                                break;
                            }else if (game.canBuildHere(tp = new TilePosition(unit.getTilePosition().getX() - i, unit.getTilePosition().getY() - i), UnitType.Terran_Supply_Depot, unit)) {
                                buildSupplyDepot(unit, tp);
                                break;
                            }
                        }
                  //  }else {
                        /*for(Iterator it = BasicInfo.SupplyDepot_Farmer.iterator(); it.hasNext();) {
                            Unit farmer = (Unit)it.next();
                            if(farmer.isCompleted() && !farmer.isConstructing()) {
                                System.out.println("farmer : " + unit.getID() + " is finish of building supply depot");
                                BasicInfo.SupplyDepot_Farmer.remove(unit);
                            }
                        }*/
                  //  }
                }

            }

            MineralGatherStrategy.gatherMinerals(unit, game);
            GeyserGatherStrategy.gatherGeysers(unit, game);

            if(UnitType.Terran_Command_Center.equals(unit.getType())) {
                if(!commanderBaseNum.contains(unit)) {
                    commanderBaseNum.add(unit);
                }
            }else if((UnitType.Terran_SCV.equals(unit.getType()) || UnitType.Protoss_Probe.equals(unit.getType()) || UnitType.Zerg_Drone.equals(unit.getType()))) {
                if(!farmerNum.contains(unit)) {
                    farmerNum.add(unit);
                }
            }else {
                otherNum.add(unit);
            }

            StringBuffer commanderBaseNumStringBuffer = new StringBuffer();
            for(Iterator it=commanderBaseNum.iterator();it.hasNext();) {
                commanderBaseNumStringBuffer.append(((Unit)it.next()).getID() + " ");
            }

            StringBuffer farmerNumStringBuffer = new StringBuffer();
            for(Iterator it=farmerNum.iterator();it.hasNext();) {
                farmerNumStringBuffer.append(((Unit)it.next()).getID() + " ");
            }

            game.drawTextScreen(10, 30, "commanderBaseNum : " + commanderBaseNum.size() + " \nall commanderBases' ids : [ "+commanderBaseNumStringBuffer.toString() + "]");
            game.drawTextScreen(10, 70, "farmerNum : " + farmerNum.size() + " \nall farmers' ids : [ "+farmerNumStringBuffer.toString() + "]");
            game.drawTextScreen(10, 110, "otherNum : " + otherNum.size());

            StringBuffer mineralFarmerStringBuffer = new StringBuffer();
            for(Iterator it = BasicInfo.Mineral_Farmer.keySet().iterator(); it.hasNext();) {
                mineralFarmerStringBuffer.append(((Unit)it.next()).getID() + " ");
            }
            StringBuffer mineralAdditionFarmerStringBuffer = new StringBuffer();
            for(Iterator it = BasicInfo.Mineral_Addition_Farmer.keySet().iterator(); it.hasNext();) {
                mineralAdditionFarmerStringBuffer.append(((Unit)it.next()).getID() + " ");
            }
            StringBuffer geyserFarmerStringBuffer = new StringBuffer();
            for(Iterator it = BasicInfo.Geyser_Farmer.iterator(); it.hasNext();) {
                geyserFarmerStringBuffer.append(((Unit)it.next()).getID() + " ");
            }
            StringBuffer supplyDepotFarmerStringBuffer = new StringBuffer();
            for(Iterator it = BasicInfo.SupplyDepot_Farmer.iterator(); it.hasNext();) {
                supplyDepotFarmerStringBuffer.append(((Unit)it.next()).getID() + " ");
            }

            game.drawTextScreen(10, 130, "Mineral_Farmer_Num : " + BasicInfo.Mineral_Farmer.size() + "\nMineral_Farmers's ids :[" + mineralFarmerStringBuffer.toString() + "]");
            game.drawTextScreen(10, 170, "Mineral_Addition_Farmer_Num : " + BasicInfo.Mineral_Addition_Farmer.size() + "\nMineral_Addition_Farmer's ids :[" + mineralAdditionFarmerStringBuffer.toString() + "]");
            game.drawTextScreen(10, 210, "Geyser_Farmer_Num : " + BasicInfo.Geyser_Farmer.size() + "\nMineral_Farmers's ids :[" + geyserFarmerStringBuffer.toString() + "]");
            game.drawTextScreen(10, 240, "SupplyDepot_Farmer_Num : " + BasicInfo.SupplyDepot_Farmer.size() + "\nSupplyDepot_Farmer's ids :[" + supplyDepotFarmerStringBuffer.toString() + "]");

            //for(Iterator it=BasicInfo.Mineral_Farmer.keySet().iterator();it.hasNext();) {
                Unit ut = (Unit)BasicInfo.Mineral_Farmer.keySet().iterator().next();
                game.drawTextScreen(10, 270, "tilePosition : " + "" + (ut).getTilePosition().toString() + " position " + (ut).getPosition().toString());
            //}
            //game.drawTextScreen(180,270,"self.getStartLocation() : " + self.getStartLocation());
        }
    }

    public void run() {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    public void buildSupplyDepot(Unit unit, TilePosition tp) {
        System.out.println("farmer : " + unit.getID() + " is going to build supply depot");
        /*for(Iterator it = BasicInfo.Mineral_Farmer.keySet().iterator(); it.hasNext();) {
            Unit unit1 = (Unit)it.next();
            unit1.move(new Position(2000,2000));
        }
        for(Iterator it = BasicInfo.Mineral_Addition_Farmer.keySet().iterator(); it.hasNext();) {
            Unit unit2 = (Unit)it.next();
            if(unit2 != unit) {
                unit2.move(new Position(2000,2000));
            }
        }
        for(Iterator it = BasicInfo.Geyser_Farmer.iterator(); it.hasNext();) {
            Unit unit3 = (Unit)it.next();
            unit3.move(new Position(2000,2000));
        }*/
        //unit.build(UnitType.Terran_Supply_Depot,tp);
        /*BasicInfo.SupplyDepot_Farmer.add(unit);
        BasicInfo.Mineral_Addition_Farmer.remove(unit);*/
    }
}
