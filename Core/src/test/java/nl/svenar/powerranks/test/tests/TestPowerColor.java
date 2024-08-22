package nl.svenar.powerranks.test.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Instant;

import org.junit.Test;

import nl.svenar.powerranks.common.utils.PowerColor;
import nl.svenar.powerranks.test.util.TestDebugger;

public class TestPowerColor {

    @Test
    public void testOldColorFormat() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[testOldColorFormat] Start");

        PowerColor powerColor = new PowerColor();

        String input = "&4&lHello &c&lWorld";
        String expected = "§4§lHello §c§lWorld";
        String actual = powerColor.formatColor(PowerColor.UNFORMATTED_COLOR_CHAR, input);

        if (!expected.equalsIgnoreCase(actual)) {
            TestDebugger.log(this,
                    "[testOldColorFormat] (" + (expected.equalsIgnoreCase(actual))
                            + "): "
                            + expected.toLowerCase() + " == " + actual.toLowerCase());
        }
        assertEquals(expected.toLowerCase(), actual.toLowerCase());

        TestDebugger.log(this, "[testOldColorFormat] Done!");
    }

    @Test
    public void testHEXColorFormat() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[testHEXColorFormat] Start");

        PowerColor powerColor = new PowerColor();

        String input = "#FF00AAHello #00FFAA&lWorld";
        String expected1 = "&x&F&F&0&0&A&AHello &x&0&0&F&F&A&A&lWorld";
        String expected2 = "§x§F§F§0§0§A§AHello §x§0§0§F§F§A§A§lWorld";
        String actual1 = powerColor.formatHEX(PowerColor.UNFORMATTED_COLOR_CHAR, input);
        String actual2 = powerColor.formatColor(PowerColor.UNFORMATTED_COLOR_CHAR, actual1);

        if (!expected1.equalsIgnoreCase(actual1)) {
            TestDebugger.log(this, "[testHEXColorFormat] ("
                    + (expected1.equalsIgnoreCase(actual1))
                    + "): " + expected1.toLowerCase() + " == " + actual1.toLowerCase());
        }
        assertEquals(expected1.toLowerCase(), actual1.toLowerCase());
        if (!expected2.equalsIgnoreCase(actual2)) {
            TestDebugger.log(this, "[testHEXColorFormat] ("
                    + (expected2.equalsIgnoreCase(actual2))
                    + "): " + expected2.toLowerCase() + " == " + actual2.toLowerCase());
        }
        assertEquals(expected2.toLowerCase(), actual2.toLowerCase());

        TestDebugger.log(this, "[testHEXColorFormat] Done!");
    }

    @Test
    public void testSpecialColorFormat() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[testSpecialColorFormat] Start");

        PowerColor powerColor = new PowerColor();

        TestDebugger.log(this, "[testSpecialColorFormat interpolation] Start");
        String interpolateColor1 = "#DDEEFF";
        String interpolateColor2 = "#AABBCC";
        String interpolateOutput = powerColor.interpolateColor(interpolateColor1, interpolateColor2, 0.5);
        String interpolateExpected = "#c3d4e5";

        if (!interpolateExpected.equalsIgnoreCase(interpolateOutput)) {
            TestDebugger.log(this,
                    "[testSpecialColorFormat interpolation] ("
                            + (interpolateExpected.toLowerCase()
                                    .equals(interpolateOutput.toLowerCase()))
                            + "): "
                            + interpolateExpected.toLowerCase() + " == "
                            + interpolateOutput.toLowerCase());
        }
        assertEquals(interpolateOutput.toLowerCase(), interpolateExpected.toLowerCase());
        TestDebugger.log(this, "[testSpecialColorFormat interpolation] OK");

        TestDebugger.log(this, "[testSpecialColorFormat rainbow] Start");
        String inputRainbow = "[rainbow]Some rainbow text test[/rainbow]";
        String expectedRainbow1 = "#e81416s#ef440eo#f77407m#ffa500e#ffa500 #fdbc12r#fbd324a#faeb36i#faeb36n#cfdd2ab#a4d01fo#79c314w#79c314 #68ab5at#5894a0e#487de7x#487de7t#4965ce #4a4db5t#4b369de#4b369ds#70369dt&r";
        String expectedRainbow2 = "§x§e§8§1§4§1§6s§x§e§f§4§4§0§eo§x§f§7§7§4§0§7m§x§f§f§a§5§0§0e§x§f§f§a§5§0§0 §x§f§d§b§c§1§2r§x§f§b§d§3§2§4a§x§f§a§e§b§3§6i§x§f§a§e§b§3§6n§x§c§f§d§d§2§ab§x§a§4§d§0§1§fo§x§7§9§c§3§1§4w§x§7§9§c§3§1§4 §x§6§8§a§b§5§at§x§5§8§9§4§a§0e§x§4§8§7§d§e§7x§x§4§8§7§d§e§7t§x§4§9§6§5§c§e §x§4§a§4§d§b§5t§x§4§b§3§6§9§de§x§4§b§3§6§9§ds§x§7§0§3§6§9§dt§r";
        String actualRainbow1 = powerColor.formatSpecial(PowerColor.UNFORMATTED_COLOR_CHAR, inputRainbow);
        String actualRainbow2 = powerColor.formatColor(PowerColor.UNFORMATTED_COLOR_CHAR,
                powerColor.formatHEX(PowerColor.UNFORMATTED_COLOR_CHAR, actualRainbow1));

        if (!expectedRainbow1.equalsIgnoreCase(actualRainbow1)) {
            TestDebugger.log(this,
                    "[testSpecialColorFormat rainbow #1] ("
                            + (expectedRainbow1.equalsIgnoreCase(actualRainbow1))
                            + "): "
                            + expectedRainbow1.toLowerCase() + " == "
                            + actualRainbow1.toLowerCase());
        }
        assertEquals(expectedRainbow1.toLowerCase(), actualRainbow1.toLowerCase());
        if (!expectedRainbow2.equalsIgnoreCase(actualRainbow2)) {
            TestDebugger.log(this,
                    "[testSpecialColorFormat rainbow #2] ("
                            + (expectedRainbow2.equalsIgnoreCase(actualRainbow2))
                            + "): "
                            + expectedRainbow2.toLowerCase() + " == "
                            + actualRainbow2.toLowerCase());
        }
        assertEquals(expectedRainbow2.toLowerCase(), actualRainbow2.toLowerCase());
        TestDebugger.log(this, "[testSpecialColorFormat rainbow] OK");

        TestDebugger.log(this, "[testSpecialColorFormat gradient] Start");
        String inputGradient = "[gradient=#489DF8,#D30EEE]Some gradient text test[/gradient]";
        String expectedGradient1 = "#489df8s#4e96f7o#5490f7m#5a89f6e#6182f6 #677cf5g#6d76f5r#746ff4a#7a69f4d#8062f3i#875bf3e#8d55f3n#934ff2t#9a48f2 #a042f1t#a63bf1e#ad34f0x#b32ef0t#b927ef #c021eft#c61beee#cc14ees#d30eeet&r";
        String expectedGradient2 = "§x§4§8§9§d§f§8s§x§4§e§9§6§f§7o§x§5§4§9§0§f§7m§x§5§a§8§9§f§6e§x§6§1§8§2§f§6 §x§6§7§7§c§f§5g§x§6§d§7§6§f§5r§x§7§4§6§f§f§4a§x§7§a§6§9§f§4d§x§8§0§6§2§f§3i§x§8§7§5§b§f§3e§x§8§d§5§5§f§3n§x§9§3§4§f§f§2t§x§9§a§4§8§f§2 §x§a§0§4§2§f§1t§x§a§6§3§b§f§1e§x§a§d§3§4§f§0x§x§b§3§2§e§f§0t§x§b§9§2§7§e§f §x§c§0§2§1§e§ft§x§c§6§1§b§e§ee§x§c§c§1§4§e§es§x§d§3§0§e§e§et§r";
        String actualGradient1 = powerColor.formatSpecial(PowerColor.UNFORMATTED_COLOR_CHAR, inputGradient);
        String actualGradient2 = powerColor.formatColor(PowerColor.UNFORMATTED_COLOR_CHAR,
                powerColor.formatHEX(PowerColor.UNFORMATTED_COLOR_CHAR, actualGradient1));

        if (!expectedGradient1.equalsIgnoreCase(actualGradient1)) {
            TestDebugger.log(this,
                    "[testSpecialColorFormat gradient #1] ("
                            + (expectedGradient1.equalsIgnoreCase(actualGradient1))
                            + "): "
                            + expectedGradient1.toLowerCase() + " == "
                            + actualGradient1.toLowerCase());
        }
        assertEquals(expectedGradient2.toLowerCase(), actualGradient2.toLowerCase());
        if (!expectedGradient2.equalsIgnoreCase(actualGradient2)) {
            TestDebugger.log(this,
                    "[testSpecialColorFormat gradient #2] ("
                            + (expectedGradient2.equalsIgnoreCase(actualGradient2))
                            + "): "
                            + expectedGradient2.toLowerCase() + " == "
                            + actualGradient2.toLowerCase());
        }
        assertEquals(expectedGradient2.toLowerCase(), actualGradient2.toLowerCase());
        TestDebugger.log(this, "[testSpecialColorFormat gradient] OK");

        String inputAll = "[gradient=#489DF8,#D30EEE]Some gradient text test[/gradient] Some unformatted text [rainbow]Some rainbow text test[/rainbow]";
        String expectedAll1 = "#489df8s#4e96f7o#5490f7m#5a89f6e#6182f6 #677cf5g#6d76f5r#746ff4a#7a69f4d#8062f3i#875bf3e#8d55f3n#934ff2t#9a48f2 #a042f1t#a63bf1e#ad34f0x#b32ef0t#b927ef #c021eft#c61beee#cc14ees#d30eeet some unformatted text #e81416s#ef440eo#f77407m#ffa500e#ffa500 #fdbc12r#fbd324a#faeb36i#faeb36n#cfdd2ab#a4d01fo#79c314w#79c314 #68ab5at#5894a0e#487de7x#487de7t#4965ce #4a4db5t#4b369de#4b369ds#70369dt&r";
        String expectedAll2 = "§x§4§8§9§d§f§8S§x§4§e§9§6§f§7o§x§5§4§9§0§f§7m§x§5§a§8§9§f§6e§x§6§1§8§2§f§6 §x§6§7§7§c§f§5g§x§6§d§7§6§f§5r§x§7§4§6§f§f§4a§x§7§a§6§9§f§4d§x§8§0§6§2§f§3i§x§8§7§5§b§f§3e§x§8§d§5§5§f§3n§x§9§3§4§f§f§2t§x§9§a§4§8§f§2 §x§a§0§4§2§f§1t§x§a§6§3§b§f§1e§x§a§d§3§4§f§0x§x§b§3§2§e§f§0t§x§b§9§2§7§e§f §x§c§0§2§1§e§ft§x§c§6§1§b§e§ee§x§c§c§1§4§e§es§x§d§3§0§e§e§et Some unformatted text §x§e§8§1§4§1§6S§x§e§f§4§4§0§eo§x§f§7§7§4§0§7m§x§f§f§a§5§0§0e§x§f§f§a§5§0§0 §x§f§d§b§c§1§2r§x§f§b§d§3§2§4a§x§f§a§e§b§3§6i§x§f§a§e§b§3§6n§x§c§f§d§d§2§ab§x§a§4§d§0§1§fo§x§7§9§c§3§1§4w§x§7§9§c§3§1§4 §x§6§8§a§b§5§at§x§5§8§9§4§a§0e§x§4§8§7§d§e§7x§x§4§8§7§d§e§7t§x§4§9§6§5§c§e §x§4§a§4§d§b§5t§x§4§b§3§6§9§de§x§4§b§3§6§9§ds§x§7§0§3§6§9§dt§r";
        String actualAll1 = powerColor.formatSpecial(PowerColor.UNFORMATTED_COLOR_CHAR, inputAll);
        String actualAll2 = powerColor.formatColor(PowerColor.UNFORMATTED_COLOR_CHAR,
                powerColor.formatHEX(PowerColor.UNFORMATTED_COLOR_CHAR, actualAll1));
        
        if (!expectedAll1.equalsIgnoreCase(actualAll1)) {
            TestDebugger.log(this,
                    "[testSpecialColorFormat all #1] ("
                            + (expectedAll1.equalsIgnoreCase(actualAll1))
                            + "): " + expectedAll1.toLowerCase() + " == "
                            + actualAll1.toLowerCase());
        }
        assertEquals(expectedAll1.toLowerCase(), actualAll1.toLowerCase());
        if (!expectedAll2.equalsIgnoreCase(actualAll2)) {
            TestDebugger.log(this,
                    "[testSpecialColorFormat all #2] ("
                            + (expectedAll2.equalsIgnoreCase(actualAll2))
                            + "): " + expectedAll2.toLowerCase() + " == "
                            + actualAll2.toLowerCase());
        }
        assertEquals(expectedAll2.toLowerCase(), actualAll2.toLowerCase());

        TestDebugger.log(this, "[testSpecialColorFormat chat] Start");
        String inputChat = "&aworld&r [gradient=#127e00,#3eaf18]MEMBER[/gradient] &rsvenar&r: &r[rainbow]This is a rainbow test[/rainbow] test123456 [gradient=#EE896A,#AA12EA]test123[/gradient]";
        String expectedChat1 = "&aworld&r #127e00m#1a8704e#239109m#2c9b0eb#35a513e#3eaf18r &rsvenar&r: &r#e81416t#ef440eh#f77407i#ffa500s#ffa500 #fdbc12i#fbd324s#faeb36 #faeb36a#cfdd2a #a4d01fr#79c314a#79c314i#68ab5an#5894a0b#487de7o#487de7w#4965ce #4a4db5t#4b369de#4b369ds#70369dt test123456 #ee896at#e2757fe#d76194s#cc4daat#c039bf1#b525d42#aa12ea3&r";
        String expectedChat2 = "§aworld§r §x§1§2§7§e§0§0M§x§1§a§8§7§0§4E§x§2§3§9§1§0§9M§x§2§c§9§b§0§eB§x§3§5§a§5§1§3E§x§3§e§a§f§1§8R §rsvenar§r: §r§x§e§8§1§4§1§6T§x§e§f§4§4§0§eh§x§f§7§7§4§0§7i§x§f§f§a§5§0§0s§x§f§f§a§5§0§0 §x§f§d§b§c§1§2i§x§f§b§d§3§2§4s§x§f§a§e§b§3§6 §x§f§a§e§b§3§6a§x§c§f§d§d§2§a §x§a§4§d§0§1§fr§x§7§9§c§3§1§4a§x§7§9§c§3§1§4i§x§6§8§a§b§5§an§x§5§8§9§4§a§0b§x§4§8§7§d§e§7o§x§4§8§7§d§e§7w§x§4§9§6§5§c§e §x§4§a§4§d§b§5t§x§4§b§3§6§9§de§x§4§b§3§6§9§ds§x§7§0§3§6§9§dt test123456 §x§e§e§8§9§6§at§x§e§2§7§5§7§fe§x§d§7§6§1§9§4s§x§c§c§4§d§a§at§x§c§0§3§9§b§f1§x§b§5§2§5§d§42§x§a§a§1§2§e§a3§r";
        String actualChat1 = powerColor.formatSpecial(PowerColor.UNFORMATTED_COLOR_CHAR, inputChat);
        String actualChat2 = powerColor.formatColor(PowerColor.UNFORMATTED_COLOR_CHAR,
                powerColor.formatHEX(PowerColor.UNFORMATTED_COLOR_CHAR, actualChat1));

        if (!expectedChat1.equalsIgnoreCase(actualChat1)) {
            TestDebugger.log(this,
                    "[testSpecialColorFormat chat #1] ("
                            + (expectedChat1.equalsIgnoreCase(actualChat1))
                            + "): "
                            + expectedChat1.toLowerCase() + " == "
                            + actualChat1.toLowerCase());
        }
        assertEquals(expectedChat2.toLowerCase(), actualChat2.toLowerCase());

        if (!expectedChat2.equalsIgnoreCase(actualChat2)) {
            TestDebugger.log(this,
                    "[testSpecialColorFormat chat #2] ("
                            + (expectedChat2.equalsIgnoreCase(actualChat2))
                            + "): "
                            + expectedChat2.toLowerCase() + " == "
                            + actualChat2.toLowerCase());
        }
        assertEquals(expectedChat2.toLowerCase(), actualChat2.toLowerCase());
        TestDebugger.log(this, "[testSpecialColorFormat chat] OK");

        TestDebugger.log(this, "[testSpecialColorFormat] Done!");
    }

    @Test
    public void testPerformance() {
        int numItems = 10000;
        float maxDurationEachItem = 0.05f;
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[testPerformance] Start");

        PowerColor powerColor = new PowerColor();

        String inputString = "[gradient=#000000,#FFFFFF]This is a test message[/gradient]";

        Instant start = Instant.now();
        for (int i = 0; i < numItems; i++) {
            powerColor.formatSpecial(PowerColor.UNFORMATTED_COLOR_CHAR, inputString);
        }
        Instant end = Instant.now();
        assertTrue(end.toEpochMilli() - start.toEpochMilli() < maxDurationEachItem * numItems);
        TestDebugger.log(this, "[testPerformance] " + (end.toEpochMilli() - start.toEpochMilli()) + "ms (max: " + Math.round(maxDurationEachItem * numItems) + "ms)");

        TestDebugger.log(this, "[testPerformance] Done!");
    }

}
