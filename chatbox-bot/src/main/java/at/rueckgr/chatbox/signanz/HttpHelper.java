package at.rueckgr.chatbox.signanz;

import at.rueckgr.chatbox.Setting;
import at.rueckgr.chatbox.service.MailService;
import at.rueckgr.chatbox.service.database.SettingsService;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

@ApplicationScoped
public class HttpHelper {
    private @Inject SettingsService settingsService;
    private @Inject MailService mailService;

    public String fetchChatboxArchiveUrl(String url) throws IOException {
        String host = settingsService.getSetting(Setting.HOSTNAME);
        int port = Integer.parseInt(settingsService.getSetting(Setting.PORT));
        String scheme = settingsService.getSetting(Setting.SCHEME);
        String username = settingsService.getSetting(Setting.UPDATE_USERNAME);
        String password = settingsService.getSetting(Setting.UPDATE_PASSWORD);

        HttpHost target = new HttpHost(host, port, scheme);
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(target.getHostName(), target.getPort()), new UsernamePasswordCredentials(username, password));
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();

        CloseableHttpResponse response = null;
        try {
            AuthCache authCache = new BasicAuthCache();
            BasicScheme basicAuth = new BasicScheme();
            authCache.put(target, basicAuth);

            HttpClientContext localContext = HttpClientContext.create();
            localContext.setAuthCache(authCache);

            HttpGet getRequest = new HttpGet(url);
            response = httpClient.execute(target, getRequest, localContext);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                mailService.sendHttpRequestFailedMail(url, response.getStatusLine());

                return null;
            }
            else {
                InputStream inputStream = response.getEntity().getContent();
                StringWriter writer = new StringWriter();
                IOUtils.copy(inputStream, writer);
                return writer.toString();
            }
        }
        finally {
            try {
                httpClient.close();
            }
            catch (IOException e) {
                // ignore
            }

            if(response != null) {
                try {
                    response.close();
                }
                catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}
