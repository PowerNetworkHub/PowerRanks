package nl.svenar.powerranks.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.svenar.powerranks.data.PRPermission;
import nl.svenar.powerranks.data.PRPlayer;

public class PermissionUtils {

    private static String multiOptionPermissionRegex = "[{]((\\w){1,64},){1,64}(\\w){1,64}[}]";
    private static String multiNumberRangePermissionRegex = "[\\[]\\d{1,6}-\\d{1,6}[\\]]";

    public static boolean hasPermission(PRPlayer prPlayer, String permission) {
        if (!prPlayer.isOnline()) {
            return false;
        }
        
        boolean isAllowed = prPlayer.isPermissionAllowed(permission, prPlayer.getPlayer().getWorld());

        if (!isAllowed) {
            for (String permissionNode : generateWildcardTree(permission)) {
                if (prPlayer.isPermissionAllowed(permissionNode, prPlayer.getPlayer().getWorld())) {
                    isAllowed = true;
                    break;
                }
            }
        }

        return isAllowed;
    }

    public static boolean hasPermission(PRPlayer prPlayer, String permission, boolean fallbackValue) {
        boolean playerHasPermission = false;
        for (PRPermission prPermission : prPlayer.getAllPermissions()) {
            if (prPermission.getPermission().getName().equals(permission)) {
                playerHasPermission = true;
                break;
            }
        }
        return playerHasPermission ? hasPermission(prPlayer, permission) : hasPermission(prPlayer, permission) || fallbackValue;
    }

    public static List<String> generateWildcardTree(String permission) {
        List<String> permissions = new ArrayList<String>();

        final String[] nodes = permission.split("\\.");

        permissions.add("*");

        String currentNodeProgress = "";
        for (int i = 0; i < nodes.length; i++) {
            if (i == 0) {
                currentNodeProgress = nodes[i];
            } else {
                currentNodeProgress += "." + nodes[i];
            }

            if (!permission.equals(currentNodeProgress)) {
                permissions.add(currentNodeProgress + ".*");
            }

            // if (permissions.contains(currentNodeProgress) ||
            // permissions.contains(currentNodeProgress + ".*")) {
            // return true;
            // }

        }

        return permissions;
    }

    public static List<String> generateMultiDimensionalPermissions(String permission) {
        List<String> permissions = new ArrayList<String>();

        for (String multiOptionPermission : generateMultiOptionDimensionalPermissions(permission)) {
            for (String perm : generateMultiNumberDimensionalPermissions(multiOptionPermission)) {
                permissions.add(perm);
            }
        }

        return permissions;
    }

    private static List<String> generateMultiOptionDimensionalPermissions(String permission) {
        List<String> permissions = new ArrayList<String>();

        Pattern multiOptionPermissionPattern = Pattern.compile(multiOptionPermissionRegex);
        Matcher multiOptionPermissionMatcher = multiOptionPermissionPattern.matcher(permission);

        if (multiOptionPermissionMatcher.find()) {
            String found = multiOptionPermissionMatcher.group(0);
            for (String match : found.replace("{", "").replace("}", "").split(",")) {
                permissions.add(permission.replace(found, match));
            }
        }

        List<String> newPermissions = new ArrayList<String>();
        for (String perm : permissions) {
            if (multiOptionPermissionPattern.matcher(perm).find()) {
                for (String permissionNode : generateMultiOptionDimensionalPermissions(perm)) {
                    newPermissions.add(permissionNode);
                }
            }
        }
        for (String newPermission : newPermissions) {
            permissions.add(newPermission);
        }

        List<String> outputPermissions = new ArrayList<String>();
        for (String perm : permissions) {
            if (!multiOptionPermissionPattern.matcher(perm).find()) {
                outputPermissions.add(perm);
            }
        }

        return outputPermissions;
    }

    private static List<String> generateMultiNumberDimensionalPermissions(String permission) {
        List<String> permissions = new ArrayList<String>();

        Pattern multiNumberPermissionPattern = Pattern.compile(multiNumberRangePermissionRegex);
        Matcher multiNumberPermissionMatcher = multiNumberPermissionPattern.matcher(permission);

        if (multiNumberPermissionMatcher.find()) {
            String found = multiNumberPermissionMatcher.group(0);
            String[] foundSplit = found.replace("[", "").replace("]", "").split("-");
            try {
                int low = Integer.parseInt(foundSplit[0]);
                int high = Integer.parseInt(foundSplit[1]) + 1;
                for (int i = low; i < high; i++) {
                    permissions.add(permission.replace(found, String.valueOf(i)));
                }
            } catch (Exception e) {
            }
        }

        List<String> newPermissions = new ArrayList<String>();
        for (String perm : permissions) {
            if (multiNumberPermissionPattern.matcher(perm).find()) {
                for (String permissionNode : generateMultiNumberDimensionalPermissions(perm)) {
                    newPermissions.add(permissionNode);
                }
            }
        }
        for (String newPermission : newPermissions) {
            permissions.add(newPermission);
        }

        List<String> outputPermissions = new ArrayList<String>();
        for (String perm : permissions) {
            if (!multiNumberPermissionPattern.matcher(perm).find()) {
                outputPermissions.add(perm);
            }
        }

        return outputPermissions;
    }

    public static boolean isMultiDimensional(String permission) {
        Pattern multiOptionPermissionPattern = Pattern.compile(multiOptionPermissionRegex);
        Matcher multiOptionPermissionMatcher = multiOptionPermissionPattern.matcher(permission);

        Pattern multiNumberPermissionPattern = Pattern.compile(multiNumberRangePermissionRegex);
        Matcher multiNumberPermissionMatcher = multiNumberPermissionPattern.matcher(permission);

        return multiOptionPermissionMatcher.find() || multiNumberPermissionMatcher.find();
    }
}
