package nl.svenar.powerranks.common.storage;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import nl.svenar.powerranks.common.TestDebugger;
import nl.svenar.powerranks.common.storage.provided.YAMLConfigManager;

public class TestPowerConfigManager {
    
    @Test
    public void testConfigManager() throws IOException {
        TestDebugger.log(this, "Initializing config manager");
        PowerConfigManager powerConfigManager = new YAMLConfigManager(".", "testConfig.yml");

        Map<String, String> map = new HashMap<String, String>();
        map.put("abc.123", "value1");
        map.put("abc.456", "value2");
        List<String> list = new ArrayList<String>();
        list.add("abc.123");
        list.add("def.456");

        TestDebugger.log(this, "Storing data in the config manager");
        powerConfigManager.setString("string.test", "abc123");
        powerConfigManager.setInt("int.test", 123);
        powerConfigManager.setBool("bool.test", true);
        powerConfigManager.setFloat("float.test", 123.456F);
        powerConfigManager.setMap("map.test", map);
        powerConfigManager.setList("list.test", list);

        TestDebugger.log(this, "Saving config manager data");
        powerConfigManager.save();

        TestDebugger.log(this, "Reloading config manager data");
        powerConfigManager.reload();

        TestDebugger.log(this, "Validating data");
        assertEquals("abc123", powerConfigManager.getString("string.test", null));
        assertEquals(123, powerConfigManager.getInt("int.test", -1));
        assertEquals(true, powerConfigManager.getBool("bool.test", false));
        assertEquals(123.456F, powerConfigManager.getFloat("float.test", 0F), 3);
        assertEquals(map.toString(), powerConfigManager.getMap("map.test", new HashMap<String, String>()).toString());
        assertEquals(list.toString(), powerConfigManager.getList("list.test", new ArrayList<String>()).toString());

        TestDebugger.log(this, "Cleaning up");
        File f = new File("testConfig.yml");
        f.delete();
    }

}
