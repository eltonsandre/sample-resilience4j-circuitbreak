package com.github.eltonsandre.sample.circuitbreak.ssl;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientConnectionManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author eltonsandre
 * date 06/04/2019 12:31
 */
@Configuration
@ConditionalOnExpression("${server.ssl.enabled:false}")
@RequiredArgsConstructor
public class SSLConfig {

    @Value("${server.port.http}")
    private int serverPortHttp;

    @Value("${server.port}")
    private int serverPortHttps;

    @Bean
    public ServletWebServerFactory servletContainer() {
        final TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {

            @Override
            protected void postProcessContext(final Context context) {
                final SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");

                final SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");

                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }

        };
        tomcat.addAdditionalTomcatConnectors(this.redirectConnector());

        return tomcat;
    }

    private Connector redirectConnector() {
        final Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);

        connector.setScheme(ApacheHttpClientConnectionManagerFactory.HTTP_SCHEME);
        connector.setPort(this.serverPortHttp);
        connector.setRedirectPort(this.serverPortHttps);
        connector.setSecure(false);

        return connector;
    }
}
