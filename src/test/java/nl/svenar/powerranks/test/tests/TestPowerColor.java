package nl.svenar.powerranks.test.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import nl.svenar.common.utils.PowerColor;
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

        if (!expected.toLowerCase().equals(actual.toLowerCase())) {
            TestDebugger.log(this, "[testOldColorFormat] (" + (expected.toLowerCase().equals(actual.toLowerCase())) + "): "
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

        if (!expected1.toLowerCase().equals(actual1.toLowerCase())) {
            TestDebugger.log(this, "[testHEXColorFormat] (" + (expected1.toLowerCase().equals(actual1.toLowerCase()))
                + "): " + expected1.toLowerCase() + " == " + actual1.toLowerCase());
        }
        assertEquals(expected1.toLowerCase(), actual1.toLowerCase());
        if (!expected2.toLowerCase().equals(actual2.toLowerCase())) {
            TestDebugger.log(this, "[testHEXColorFormat] (" + (expected2.toLowerCase().equals(actual2.toLowerCase()))
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

        if (!interpolateExpected.toLowerCase().equals(interpolateOutput.toLowerCase())) {
            TestDebugger.log(this,
                    "[testSpecialColorFormat interpolation] ("
                            + (interpolateExpected.toLowerCase().equals(interpolateOutput.toLowerCase())) + "): "
                            + interpolateExpected.toLowerCase() + " == " + interpolateOutput.toLowerCase());
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

        if (!expectedRainbow1.toLowerCase().equals(actualRainbow1.toLowerCase())) {
            TestDebugger.log(this,
                    "[testSpecialColorFormat rainbow] ("
                            + (expectedRainbow1.toLowerCase().equals(actualRainbow1.toLowerCase())) + "): "
                            + expectedRainbow1.toLowerCase() + " == " + actualRainbow1.toLowerCase());
        }
        assertEquals(expectedRainbow1.toLowerCase(), actualRainbow1.toLowerCase());
        if (!expectedRainbow2.toLowerCase().equals(actualRainbow2.toLowerCase())) {
            TestDebugger.log(this,
                    "[testSpecialColorFormat rainbow] ("
                            + (expectedRainbow2.toLowerCase().equals(actualRainbow2.toLowerCase())) + "): "
                            + expectedRainbow2.toLowerCase() + " == " + actualRainbow2.toLowerCase());
        }
        assertEquals(expectedRainbow2.toLowerCase(), actualRainbow2.toLowerCase());
        TestDebugger.log(this, "[testSpecialColorFormat rainbow] OK");

        TestDebugger.log(this, "[testSpecialColorFormat gradient] Start");
        String inputGraidient = "[gradient=#489DF8,#D30EEE]Some gradient text test[/gradient]";
        String expectedGradient1 = "#489df8s#4e96f7o#5490f7m#5a89f6e#6183f6 #677cf5g#6d76f5r#746ff4a#7a69f4d#8062f3i#875cf3e#8d55f3n#934ff2t#9a48f2 #a042f1t#a63bf1e#ad35f0x#b32ef0t#b927ef #c021eft#c61beee#cc14ees#d30eeet&r";
        String expectedGradient2 = "§x§4§8§9§d§f§8s§x§4§e§9§6§f§7o§x§5§4§9§0§f§7m§x§5§a§8§9§f§6e§x§6§1§8§3§f§6 §x§6§7§7§c§f§5g§x§6§d§7§6§f§5r§x§7§4§6§f§f§4a§x§7§a§6§9§f§4d§x§8§0§6§2§f§3i§x§8§7§5§c§f§3e§x§8§d§5§5§f§3n§x§9§3§4§f§f§2t§x§9§a§4§8§f§2 §x§a§0§4§2§f§1t§x§a§6§3§b§f§1e§x§a§d§3§5§f§0x§x§b§3§2§e§f§0t§x§b§9§2§7§e§f §x§c§0§2§1§e§ft§x§c§6§1§b§e§ee§x§c§c§1§4§e§es§x§d§3§0§e§e§et§r";
        String actualGraidient1 = powerColor.formatSpecial(PowerColor.UNFORMATTED_COLOR_CHAR, inputGraidient);
        String actualGraidient2 = powerColor.formatColor(PowerColor.UNFORMATTED_COLOR_CHAR,
                powerColor.formatHEX(PowerColor.UNFORMATTED_COLOR_CHAR, actualGraidient1));

        if (!expectedGradient1.toLowerCase().equals(actualGraidient1.toLowerCase())) {
            TestDebugger.log(this,
                    "[testSpecialColorFormat gradient] ("
                            + (expectedGradient1.toLowerCase().equals(actualGraidient1.toLowerCase())) + "): "
                            + expectedGradient1.toLowerCase() + " == " + actualGraidient1.toLowerCase());
        }
        assertEquals(expectedGradient2.toLowerCase(), actualGraidient2.toLowerCase());
        if (!expectedGradient2.toLowerCase().equals(actualGraidient2.toLowerCase())) {
            TestDebugger.log(this,
                    "[testSpecialColorFormat gradient] ("
                            + (expectedGradient2.toLowerCase().equals(actualGraidient2.toLowerCase())) + "): "
                            + expectedGradient2.toLowerCase() + " == " + actualGraidient2.toLowerCase());
        }
        assertEquals(expectedGradient2.toLowerCase(), actualGraidient2.toLowerCase());
        TestDebugger.log(this, "[testSpecialColorFormat gradient] OK");

        String inputAll = "[gradient=#489DF8,#D30EEE]Some gradient text test[/gradient] Some unformatted text [rainbow]Some rainbow text test[/rainbow]";
        String expectedAll1 = "#489df8s#4e96f7o#5490f7m#5a89f6e#6183f6 #677cf5g#6d76f5r#746ff4a#7a69f4d#8062f3i#875cf3e#8d55f3n#934ff2t#9a48f2 #a042f1t#a63bf1e#ad35f0x#b32ef0t#b927ef #c021eft#c61beee#cc14ees#d30eeet&r some unformatted text #e81416s#ef440eo#f77407m#ffa500e#ffa500 #fdbc12r#fbd324a#faeb36i#faeb36n#cfdd2ab#a4d01fo#79c314w#79c314 #68ab5at#5894a0e#487de7x#487de7t#4965ce #4a4db5t#4b369de#4b369ds#70369dt&r";
        String expectedAll2 = "§x§4§8§9§d§f§8s§x§4§e§9§6§f§7o§x§5§4§9§0§f§7m§x§5§a§8§9§f§6e§x§6§1§8§3§f§6 §x§6§7§7§c§f§5g§x§6§d§7§6§f§5r§x§7§4§6§f§f§4a§x§7§a§6§9§f§4d§x§8§0§6§2§f§3i§x§8§7§5§c§f§3e§x§8§d§5§5§f§3n§x§9§3§4§f§f§2t§x§9§a§4§8§f§2 §x§a§0§4§2§f§1t§x§a§6§3§b§f§1e§x§a§d§3§5§f§0x§x§b§3§2§e§f§0t§x§b§9§2§7§e§f §x§c§0§2§1§e§ft§x§c§6§1§b§e§ee§x§c§c§1§4§e§es§x§d§3§0§e§e§et§r some unformatted text §x§e§8§1§4§1§6s§x§e§f§4§4§0§eo§x§f§7§7§4§0§7m§x§f§f§a§5§0§0e§x§f§f§a§5§0§0 §x§f§d§b§c§1§2r§x§f§b§d§3§2§4a§x§f§a§e§b§3§6i§x§f§a§e§b§3§6n§x§c§f§d§d§2§ab§x§a§4§d§0§1§fo§x§7§9§c§3§1§4w§x§7§9§c§3§1§4 §x§6§8§a§b§5§at§x§5§8§9§4§a§0e§x§4§8§7§d§e§7x§x§4§8§7§d§e§7t§x§4§9§6§5§c§e §x§4§a§4§d§b§5t§x§4§b§3§6§9§de§x§4§b§3§6§9§ds§x§7§0§3§6§9§dt§r";
        String actualAll1 = powerColor.formatSpecial(PowerColor.UNFORMATTED_COLOR_CHAR, inputAll);
        String actualAll2 = powerColor.formatColor(PowerColor.UNFORMATTED_COLOR_CHAR,
                powerColor.formatHEX(PowerColor.UNFORMATTED_COLOR_CHAR, actualAll1));

        if (!expectedAll1.toLowerCase().equals(actualAll1.toLowerCase())) {
            TestDebugger.log(this,
                    "[testSpecialColorFormat all] (" + (expectedAll1.toLowerCase().equals(actualAll1.toLowerCase()))
                            + "): " + expectedAll1.toLowerCase() + " == " + actualAll1.toLowerCase());
        }
        assertEquals(expectedAll1.toLowerCase(), actualAll1.toLowerCase());
        if (!expectedAll2.toLowerCase().equals(actualAll2.toLowerCase())) {
            TestDebugger.log(this,
                    "[testSpecialColorFormat all] (" + (expectedAll2.toLowerCase().equals(actualAll2.toLowerCase()))
                            + "): " + expectedAll2.toLowerCase() + " == " + actualAll2.toLowerCase());
        }
        assertEquals(expectedAll2.toLowerCase(), actualAll2.toLowerCase());

        TestDebugger.log(this, "[testSpecialColorFormat] Done!");
    }
}
