package nl.svenar.common.http;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DatabinClient extends PowerHTTPClient {

    private final String url;
    private final String userAgent;
    private String rawResponse = "";

    public DatabinClient(OkHttpClient httpClient, String url, String userAgent) {
        super(httpClient);

        this.url = url.endsWith("/") ? url : url + "/";
        this.userAgent = userAgent;
    }

    public DatabinClient(String url, String userAgent) {
        super();

        this.url = url.endsWith("/") ? url : url + "/";
        this.userAgent = userAgent;
    }

    public String getUrl() {
        return this.url;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public void postJSON(String text) {
        RequestBody body = RequestBody.create(text, JSON_TYPE);

        Request.Builder requestBuilder = new Request.Builder().url(this.url + "documents")
                .header("User-Agent", this.userAgent).header("Content-Encoding", "gzip");

        Request request = requestBuilder.post(body).build();
        makeHttpRequest(request);
    }

    public void getJSON(String key) {
        Request.Builder requestBuilder = new Request.Builder().url(this.url + "raw/" + key)
                .header("User-Agent", this.userAgent).header("Content-Encoding", "gzip");

        Request request = requestBuilder.get().build();
        makeHttpRequest(request);
    }

    @Override
    protected void callbackFailure(Call call, IOException exception) {
        this.rawResponse = "[FAILED] " + exception.getMessage();
    }

    @Override
    protected void callbackResponse(Call call, Response response) {
        try {
            this.rawResponse = response.body().string();
        } catch (IOException e) {
            this.rawResponse = "[FAILED] " + e.getMessage();
        }
    }

    public boolean hasResponse() {
        return this.rawResponse.length() > 0;
    }

    public Map<String, String> getResponse() {
        String response = this.rawResponse;
        this.rawResponse = "";

        return parseJSON(response);
    }

    public Map<String, String> parseJSON(String response) {
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = gson.fromJson(response, mapType);
        return map;
    }
}
