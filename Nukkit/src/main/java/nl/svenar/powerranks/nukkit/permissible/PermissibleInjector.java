package nl.svenar.powerranks.nukkit.permissible;

import java.lang.reflect.Field;

import cn.nukkit.Player;
import nl.svenar.powerranks.nukkit.PowerRanks;

public class PermissibleInjector {

    public static void inject(PowerRanks plugin, Player player) {
        try {
            Field f = Player.class.getDeclaredField("perm");
            f.setAccessible(true);
            f.set(player, new PowerPermissibleBase(player, plugin));
            f.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
