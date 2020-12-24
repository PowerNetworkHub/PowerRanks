package nl.svenar.PowerRanks.Data;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;

import nl.svenar.PowerRanks.Util;

public final class PermissibleInjector {

	private static final Field HUMAN_ENTITY_PERMISSIBLE_FIELD;
	private static final Field PERMISSIBLE_BASE_ATTATCHMENTS_FIELD;

	static {
		try {
			Field humanEntityPermissibleField;
			try {
				humanEntityPermissibleField = Util.obcClass("entity.CraftHumanEntity").getDeclaredField("perm");
				humanEntityPermissibleField.setAccessible(true);
			} catch (Exception e) {
				humanEntityPermissibleField = Util.obcClass("net.glowstone.entity.GlowHumanEntity").getDeclaredField("permissions");
				humanEntityPermissibleField.setAccessible(true);
			}

			HUMAN_ENTITY_PERMISSIBLE_FIELD = humanEntityPermissibleField;

			PERMISSIBLE_BASE_ATTATCHMENTS_FIELD = PermissibleBase.class.getDeclaredField("attachments");
			PERMISSIBLE_BASE_ATTATCHMENTS_FIELD.setAccessible(true);
		} catch (ClassNotFoundException | NoSuchFieldException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Permissible inject(Player p, Permissible newPermissible) {
		try {
			PermissibleBase oldPermissible = (PermissibleBase) HUMAN_ENTITY_PERMISSIBLE_FIELD.get(p);

			if (newPermissible instanceof PermissibleBase) {
				((List) PERMISSIBLE_BASE_ATTATCHMENTS_FIELD.get(newPermissible)).addAll((List) PERMISSIBLE_BASE_ATTATCHMENTS_FIELD.get(oldPermissible));
			}

			HUMAN_ENTITY_PERMISSIBLE_FIELD.set(p, newPermissible);
			return oldPermissible;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isInjected(Player p) {
		try {
			PermissibleBase permissible = (PermissibleBase) HUMAN_ENTITY_PERMISSIBLE_FIELD.get(p);
			return permissible instanceof PowerPermissibleBase;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Permissible uninject(Player p) {
		try {
			Permissible permissible = (Permissible) HUMAN_ENTITY_PERMISSIBLE_FIELD.get(p);
			if (permissible instanceof PowerPermissibleBase) {
				//HUMAN_ENTITY_PERMISSIBLE_FIELD.set(p, ((PowerPermissibleBase) permissible).getOldPermissible());
				return (PowerPermissibleBase) permissible;
			}

			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static PermissibleBase getPermissible(Player p) {
		try {
			PermissibleBase permissible = (PermissibleBase) HUMAN_ENTITY_PERMISSIBLE_FIELD.get(p);
			return permissible;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}