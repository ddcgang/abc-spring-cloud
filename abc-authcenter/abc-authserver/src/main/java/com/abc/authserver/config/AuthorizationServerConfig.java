package com.abc.authserver.config;

import com.abc.authserver.client.RedisClientDetailsService;
import com.abc.authserver.service.impl.UserDetailsServiceImpl;
import com.abc.authserver.service.SingleTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;


import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    DataSource dataSource;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RedisConnectionFactory redisConnectionFactory;
    @Autowired
    UserDetailsServiceImpl userDetailsService;


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(new BCryptPasswordEncoder())
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .exceptionTranslator(webResponseExceptionTranslator())
                .tokenStore(tokenStore())
                .tokenServices(tokenServices(endpoints));
    }

    @Bean
    public ClientDetailsService clientDetails() {
        return new RedisClientDetailsService(dataSource);
    }

    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix("abc-auth-token:");
        return redisTokenStore;


        /*jwt token
        return new JwtTokenStore(jwtAccessTokenConverter());
        */
    }

    /*
     * JwtToken配置
     * */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("123");//对称加密,资源服务器需要和这个一致
        return jwtAccessTokenConverter;
    }

    /*
     * JwtToken配置
     * 配置返回jwt格式的token 转换
     * */
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter()));
        return tokenEnhancerChain;
    }


    private SingleTokenServices tokenServices(AuthorizationServerEndpointsConfigurer endpoints) {
        SingleTokenServices tokenServices = new SingleTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);//支持刷新token
        tokenServices.setReuseRefreshToken(true);
        tokenServices.setAccessTokenValiditySeconds(60 * 60);
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        //addUserDetailsService(tokenServices, this.userDetailsService);
        return tokenServices;
    }

    /*
        private void addUserDetailsService(SingleTokenServices tokenServices, UserDetailsServiceImpl userDetailsService) {
            if (userDetailsService != null) {
                PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
                provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(
                        userDetailsService));
                tokenServices.setAuthenticationManager(new ProviderManager(Arrays.asList(provider)));
            }
        }*/
    @Bean
    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
        return new DefaultWebResponseExceptionTranslator() {
            @Override
            public ResponseEntity translate(Exception e) throws Exception {
                ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);
                OAuth2Exception body = responseEntity.getBody();
                HttpHeaders headers = new HttpHeaders();
                headers.setAll(responseEntity.getHeaders().toSingleValueMap());
                return new ResponseEntity<>(body, headers, responseEntity.getStatusCode());
            }
        };
    }
}
