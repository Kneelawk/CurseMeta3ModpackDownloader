package com.github.kneelawk.cursemodpackdownloader.cursemeta3.net;

import org.apache.commons.codec.DecoderException;
import org.apache.http.ProtocolException;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.util.TextUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class RedirectUriSanitizer extends DefaultRedirectStrategy {
    @Override
    protected URI createLocationURI(String location) throws ProtocolException {
        try {
            URIBuilder b = new URIBuilder(
                    CurseURIUtils.sanitizeUri(location, true).normalize());
            String host = b.getHost();
            if (host != null) {
                b.setHost(host.toLowerCase(Locale.ROOT));
            }
            String path = b.getPath();
            if (TextUtils.isEmpty(path)) {
                b.setPath("/");
            }

            URI result = b.build();

            // debugging can be done here

            return result;
        } catch (URISyntaxException e) {
            throw new ProtocolException("Invalid redirect URI: " + location, e);
        } catch (DecoderException e) {
            throw new ProtocolException("Invalid redirect URI: " + location, e);
        }
    }
}
