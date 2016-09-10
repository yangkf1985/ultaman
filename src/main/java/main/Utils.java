package main;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


class Utils {
    private Utils() {
    }

    static HttpClient createHttpClient() {
        HttpClientBuilder b = HttpClientBuilder.create();
        SSLConnectionSocketFactory sslSocketFactory = null;
        try {
            sslSocketFactory = new SSLConnectionSocketFactory(SSLContexts.custom()
                    .loadTrustMaterial(null,
                            (TrustStrategy) (x509Certificates, s) -> true)
                    .build(), NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException | KeyManagementException
                | KeyStoreException e) {
            e.printStackTrace();
        }

        assert sslSocketFactory != null;
        PoolingHttpClientConnectionManager connMgr =
                new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory
                                .getSocketFactory())
                        .register("https", sslSocketFactory)
                        .build());
        connMgr.setMaxTotal(500);
        connMgr.setDefaultMaxPerRoute(20);
        b.setConnectionManager(connMgr);

        return b.build();
    }

    static List<String> gen_IPs() {
        List<String> rs = new ArrayList<>();
        String[] subnets = {
                "64.18.0.0/20",
                "64.233.160.0/19",
                "66.102.0.0/20",
                "66.249.80.0/20",
                "72.14.192.0/18",
                "74.125.0.0/16",
                "108.177.8.0/21",
                "173.194.0.0/16",
                "207.126.144.0/20",
                "209.85.128.0/17",
                "216.58.192.0/19",
                "216.239.32.0/19"
        };
        for (String subnet : subnets) {
            SubnetUtils utils = new SubnetUtils(subnet);
            rs.addAll(Arrays.asList(utils.getInfo().getAllAddresses()));
        }

        return rs;
    }

    public static void main(String[] args) {
        List<String> ip_array = Utils.gen_IPs();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            System.out.println(random.nextInt(ip_array.size()));
        }
    }
}
