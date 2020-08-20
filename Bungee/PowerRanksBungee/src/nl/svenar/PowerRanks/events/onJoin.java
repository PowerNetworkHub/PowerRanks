package nl.svenar.PowerRanks.events;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class onJoin implements Listener {

	@EventHandler
    public void onPostLogin(PostLoginEvent event) {
		ProxiedPlayer player = event.getPlayer();
		player.sendMessage(new ComponentBuilder("Hello " + player.getName() + "!").create());
    }
}