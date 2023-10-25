/**
 * This file is part of PowerRanks, licensed under the MIT License.
 *
 * Copyright (c) svenar (Sven) <powerranks@svenar.nl>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
*/

package nl.svenar.powerranks.common.http;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * HTTP client to be implemented elsewhere
 * 
 * @author svenar
 */
public abstract class PowerHTTPClient {
    public static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

    protected final OkHttpClient httpClient;

    /**
     * Create a PowerHTTPClient and create a OkHttpClient
     */
    public PowerHTTPClient() {
        this.httpClient = new OkHttpClient();
    }

    /**
     * Create a PowerHTTPClient with the provided OkHttpClient
     * 
     * @param httpClient
     */
    public PowerHTTPClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Callback for a failed HTTP request
     * 
     * @param call
     * @param exception
     */
    protected abstract void callbackFailure(Call call, IOException exception);

    /**
     * Callback for a successful HTTP request
     * 
     * @param call
     * @param response
     */
    protected abstract void callbackResponse(Call call, Response response);

    /**
     * Create a simple request to an URL without other parameters
     * 
     * @param url
     * @return Request object with the provided URL
     */
    protected Request createSimpleRequest(String url) {
        return new Request.Builder().url(url).build();
    }

    /**
     * Make a HTTP request and call the calbacks: callbackFailure(...) and
     * callbackResponse(...) afterwards
     * 
     * @param request
     */
    protected void makeHttpRequest(Request request) {
        Call call = this.httpClient.newCall(request);
        PowerHTTPClient self = this;
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException exception) {
                self.callbackFailure(call, exception);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                self.callbackResponse(call, response);
            }

        });
    }
}
