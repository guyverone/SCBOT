/**
 * Created by guyver on 2016/4/5.
 */
public class CheckReference {
    public static void main(String[] args) {
        Boolean a = new Boolean(false);
        System.out.println(a.hashCode());
        setTrue(a);
        System.out.println("setTrue(a) " + a +" " + a.hashCode());
        a = true;
        System.out.println("a = true " + a +" " + a.hashCode());
        System.out.println("========================================");
        Obj obj = new Obj();
        System.out.println("obj " + obj.a +" " + a.hashCode());
        setTrue2(obj);
        System.out.println("setTrue2 obj " + obj.a +" " + a.hashCode());
    }
    private static void setTrue(Boolean a) {
        a = true;
        System.out.println("setTrue " + a +" " + a.hashCode());
    }
    private static void setTrue2(Obj obj) {
        obj.a = true;
        System.out.println("obj.a setTrue " + obj.a +" " + obj.a.hashCode());
    }
}

class Obj {
    Boolean a = false;
}
