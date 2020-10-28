package com.abc.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class GateWayFilter extends ZuulFilter {
    /**
     * 请求前pre
     * 请求后post
     *
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 顺序
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * ture 可用
     * false 不可用
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 任务object类型都是放行
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //System.out.println("经过过滤器了");
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String header = request.getHeader("Authorization");//Authorization敏感词 需配置sensitive-headers: null 否则不转发
        if (header != null && !"".equals(header)) {
            requestContext.addZuulRequestHeader("Authorization", header);
        }
        return null;
    }
}
