package nl.svenar.powerranks.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import nl.svenar.powerranks.common.PowerRanksBase;

public class PowerRanks extends Plugin {

    private PowerRanksBase prBase;

    @Override
    public void onEnable() {
        this.prBase = new PowerRanksBase(getLogger(), ProxyServer.getInstance().getVersion(), getDescription().getName(), getDescription().getVersion(), "bungeecord");
        this.prBase.onEnable();
    }

    @Override
    public void onDisable() {
        this.prBase.onDisable();
    }
}
