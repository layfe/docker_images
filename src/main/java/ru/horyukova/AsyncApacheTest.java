package ru.horyukova;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.Method;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.http2.HttpVersionPolicy;
import org.apache.hc.core5.reactor.ssl.TlsDetails;
import org.apache.hc.core5.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.util.concurrent.Future;

import static org.apache.hc.core5.http.ContentType.APPLICATION_JSON;

/**
 * Test to check AsyncApachelClient
 *
 * @author horyukova
 * @since 03.09.2021
 */
public class AsyncApacheTest {
    private static final Logger log = LoggerFactory.getLogger(AsyncApacheTest.class);

    public static void main(String[] args) throws Exception {
        String body = "body";
        URI uri = URI.create("https://localhost:12345");

        CloseableHttpAsyncClient apacheHttpAsyncClient = createApacheHttpAsyncClient();
        apacheHttpAsyncClient.start();

        Future<SimpleHttpResponse> execute = apacheHttpAsyncClient.execute(prepareApacheRequest(uri, body), new FutureCallback<>() {
            @Override
            public void completed(SimpleHttpResponse response) {
            }

            @Override
            public void failed(Exception ex) {
            }

            @Override
            public void cancelled() {
            }
        });

        execute.get();
    }

    private static SimpleHttpRequest prepareApacheRequest(URI uri, String body) {
        org.apache.hc.core5.http.ContentType contentType = APPLICATION_JSON;

        SimpleHttpRequest simpleHttpRequest = new SimpleHttpRequest(Method.POST.name(), uri);

        simpleHttpRequest.setBody(body.getBytes(contentType.getCharset()), contentType);

        simpleHttpRequest.addHeader("Content-Type", contentType.getMimeType());

        return simpleHttpRequest;
    }

    private static CloseableHttpAsyncClient createApacheHttpAsyncClient() throws Exception {
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(new TrustAllStrategy())
                .build();

        TlsStrategy tlsStrategy = ClientTlsStrategyBuilder.create()
                .setSslContext(sslContext)
                .setTlsDetailsFactory(sslEngine -> new TlsDetails(sslEngine.getSession(), sslEngine.getApplicationProtocol()))
                .build();

        PoolingAsyncClientConnectionManager cm = PoolingAsyncClientConnectionManagerBuilder.create()
                .setTlsStrategy(tlsStrategy)
                .build();

        return HttpAsyncClients.custom()
                .setVersionPolicy(HttpVersionPolicy.FORCE_HTTP_1)
                .setConnectionManager(cm)
                .build();
    }
}
