package nl.svenar.powerranks.test.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import nl.svenar.common.utils.PowerColor;

public class TestPowerColor {
    
    @Test
    public void testOldColorFormat() {
        PowerColor powerColor = new PowerColor();

        String input = "&4&lHello &c&lWorld";
        String expected = "§4§lHello §c§lWorld";
        String actual = powerColor.formatColor(PowerColor.UNFORMATTED_COLOR_CHAR, input);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHEXColorFormat() {
        PowerColor powerColor = new PowerColor();

        String input = "#FF00AAHello #00FFAA&lWorld";
        String expected1 = "&x&F&F&0&0&A&AHello &x&0&0&F&F&A&A&lWorld";
        String expected2 = "§x§F§F§0§0§A§AHello §x§0§0§F§F§A§A§lWorld";
        String actual1 = powerColor.formatHEX(PowerColor.UNFORMATTED_COLOR_CHAR, input);
        String actual2 = powerColor.formatColor(PowerColor.UNFORMATTED_COLOR_CHAR, actual1);
        assertEquals(expected1, actual1);
        assertEquals(expected2.toLowerCase(), actual2.toLowerCase());
    }
}
