package nl.svenar.powerranks.common.http;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;

import static org.awaitility.Awaitility.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import nl.svenar.powerranks.common.TestDebugger;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class TestHTTPConnection {

    private boolean hasResponse = false;

    @Test
    public void testHttpConnection() {
        TestHTTPConnection self = this;
        PowerHTTPClient powerHttpClient = new PowerHTTPClient(new OkHttpClient()) {

            @Override
            protected void callbackFailure(Call call, IOException exception) {
                TestDebugger.log(self, exception.getMessage());
                hasResponse = true;
                assertTrue(false);
            }

            @Override
            protected void callbackResponse(Call call, Response response) {
                String msg = "";
                try {
                    msg = response.body().string();
                } catch (IOException e) {
                    assertTrue(false);
                }

                Gson gson = new Gson();
                Type mapType = new TypeToken<Map<String, Object>>() {
                }.getType();
                Map<String, Object> map = gson.fromJson(msg, mapType);

                assertEquals(Math.round((double) map.get("status")), 200);
                assertEquals(map.get("message"), "OK");

                hasResponse = true;
            }

        };

        TestDebugger.log(this, "Creating request...");
        powerHttpClient.makeHttpRequest(powerHttpClient.createSimpleRequest("https://powerranks.nl/test.json"));

        await().atMost(2000, TimeUnit.MILLISECONDS).until(() -> hasResponse() == true);
    }

    @Test
    public void testDatabinConnection() {
        DatabinClient client = new DatabinClient("https://databin.svenar.nl", "Databinclient/1.0");

        Map<String, String> data = new HashMap<String, String>();
        data.put("key", "value");
        data.put("key2", "value2");

        Gson gson = new Gson();

        TestDebugger.log(this, "Storing data in Databin...");
        client.postJSON(gson.toJson(data));
        await().atMost(2500, TimeUnit.MILLISECONDS).until(() -> client.hasResponse() == true);

        String key = client.getResponse().get("key");
        assertTrue(key.length() > 0 && !key.startsWith("[FAILED]"));

        TestDebugger.log(this, "Requesting data from Databin...");
        client.getJSON(key);
        await().atMost(2500, TimeUnit.MILLISECONDS).until(() -> client.hasResponse() == true);

        Map<String, String> responseData = client.getResponse();

        TestDebugger.log(this, "Validating data...");

        assertEquals(data.size(), responseData.size());
        for (Entry<String, String> entry : data.entrySet()) {
            assertEquals(entry.getValue(), responseData.get(entry.getKey()));
        }
    }

    private boolean hasResponse() {
        return hasResponse;
    }
}
