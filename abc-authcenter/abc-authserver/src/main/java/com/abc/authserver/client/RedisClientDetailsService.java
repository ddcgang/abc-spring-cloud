package com.abc.authserver.client;


import com.alibaba.fastjson.JSONObject;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;


import javax.sql.DataSource;

public class RedisClientDetailsService extends JdbcClientDetailsService {
    public RedisClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    static final String REDIS_KEY_CLIENT_DETAILS = "abc-auth-token:oauth:oauth_client_details";

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        return getClientDetails(clientId);
    }

    /*获取ClientDetails
     *
     * */
    private ClientDetails getClientDetails(String clientId) {
        ClientDetails clientDetails = null;
        String redisValue = (String) stringRedisTemplate.boundHashOps(REDIS_KEY_CLIENT_DETAILS).get(clientId);
        if (StringUtils.isBlank(redisValue)) {
            clientDetails = super.loadClientByClientId(clientId);
            stringRedisTemplate.boundHashOps(REDIS_KEY_CLIENT_DETAILS).put(clientId, JSONObject.toJSONString(clientDetails));
        } else {
            clientDetails = JSONObject.parseObject(redisValue, BaseClientDetails.class);
        }
        return clientDetails;
    }
}
