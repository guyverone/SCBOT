import bwapi.Unit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by guyver on 2016/3/20.
 */
public class BasicInfo {
    public static Map<Unit, Unit> Mineral_Farmer = new HashMap<>();
    public static Map<Unit, Unit> Mineral_Addition_Farmer = new HashMap<>();

    public static Set<Unit> Geyser_Farmer = new HashSet<>();

    public static Set<Unit> SupplyDepot_Farmer = new HashSet<>();

    public static Unit commandCenter = null;
}
