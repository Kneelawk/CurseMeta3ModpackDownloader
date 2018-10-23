package com.github.kneelawk.cursemodpackdownloader.cursemeta3.net;

import java.io.IOException;
import java.lang.ref.Cleaner;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;

public class ClientManager {
	private Cleaner cleaner;
	private SSLConnectionSocketFactory connectionSocketFactory;
	private Registry<ConnectionSocketFactory> registry;
	private RedirectUriSanitizer redirectUriSanitizer =
			new RedirectUriSanitizer();

	private ThreadLocal<CloseableHttpClientWrapper> clients =
			ThreadLocal.withInitial(() -> {
				HttpClientConnectionManager connectionManager =
						new BasicHttpClientConnectionManager(registry);
				return new CloseableHttpClientWrapper(cleaner, HttpClients
						.custom().setRedirectStrategy(redirectUriSanitizer)
						.setSSLSocketFactory(connectionSocketFactory)
						.setConnectionManager(connectionManager).build());
			});

	public ClientManager() {
		cleaner = Cleaner.create();
		try {
			connectionSocketFactory = new SSLConnectionSocketFactory(
					SSLContext.getDefault(), new String[] {
							"TLSv1.2"
					}, null, NoopHostnameVerifier.INSTANCE);
			registry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("https", connectionSocketFactory)
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.build();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public CloseableHttpClient getClient() {
		return clients.get().getClient();
	}

	private static class CloseableHttpClientWrapper implements AutoCloseable {

		private final CloseableHttpClient client;
		private final State state;
		private final Cleaner.Cleanable cleanable;

		public CloseableHttpClientWrapper(Cleaner cleaner,
				CloseableHttpClient client) {
			this.client = client;
			this.state = new State(client);
			this.cleanable = cleaner.register(this, state);
		}

		public CloseableHttpClient getClient() {
			return client;
		}

		@Override
		public void close() throws Exception {
			cleanable.clean();
		}

		private static class State implements Runnable {
			private final CloseableHttpClient client;

			public State(CloseableHttpClient client) {
				this.client = client;
			}

			@Override
			public void run() {
				try {
					System.out.println("Closing client...");
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
