package com.omexit.config;

import com.omexit.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

/**
 * <p>
 * The Class OAuth2Config defines the authorization server that would
 * authenticate the user and define the client that seeks authorization on the
 * resource owner's behalf.
 * </p>
 */
@Configuration
@EnableAuthorizationServer
public class OAuthConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${signing-key:oui214hmui23o4hm1pui3o2hp4m1o3h2m1o43}")
    private String signingKey;

    public OAuthConfiguration() {
        super();
    }

//    private TokenStore tokenStore = new InMemoryTokenStore();

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * <p>The OAuth2 tokens are defined in the datasource defined in the
     * <code>kyc-auth.yml</code> file stored in the Spring Cloud config
     * github repository.
     * </p>
     *
     * @return
     */
    @Bean
    public JdbcTokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signingKey);
        return converter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security)
            throws Exception {
        security.passwordEncoder(passwordEncoder);
    }


    /**
     * We set our authorization storage feature specifying that we would use the
     * JDBC store for token and authorization code storage.<br>
     * <br>
     * <p>
     * We also attach the {@link AuthenticationManager} so that password grants
     * can be processed.
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        endpoints
                .tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
//                .authorizationCodeServices(authorizationCodeServices())
                .accessTokenConverter(accessTokenConverter())
//                .approvalStoreDisabled()
        ;
    }


    /**
     * Setup the client application which attempts to get access to user's
     * account after user permission.
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {

        clients.jdbc(dataSource)
                .passwordEncoder(passwordEncoder)
                .withClient("client")

                .authorizedGrantTypes("authorization_code", "client_credentials",
                        "refresh_token", "password", "implicit")
                .authorities("ROLE_CLIENT")
                .resourceIds("apis")
                .scopes("read")
                .secret("secret")
                .accessTokenValiditySeconds(300);

    }

    /**
     * Configure the {@link AuthenticationManagerBuilder} with initial
     * configuration to setup users.
     *
     * @author omexit
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Configuration
    protected static class AuthenticationManagerConfiguration extends
            GlobalAuthenticationConfigurerAdapter {

        @Autowired
        UserService userService;

        private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
        }

        public void configure(AuthenticationManagerBuilder auth) throws Exception {
        }
    }
}