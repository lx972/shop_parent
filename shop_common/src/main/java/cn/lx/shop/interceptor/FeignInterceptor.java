package cn.lx.shop.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * cn.lx.shop.interceptor
 *
 * @Author Administrator
 * @date 10:05
 */
public class FeignInterceptor implements RequestInterceptor {

    /**
     * 这里会拦截所有的feign请求
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //将父请求中的所有请求头信息全部复制一遍到feign请求中，这样父请求中的令牌自然也就到了feign中
        //得到当前请求的全部信息
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes!=null){
            HttpServletRequest request = servletRequestAttributes.getRequest();
            //得到当前请求的所有请求头名字
            Enumeration<String> headerNames =request .getHeaderNames();
            if (headerNames!=null){
                while (headerNames.hasMoreElements()){
                    String headerName = headerNames.nextElement();
                    if (headerName.equalsIgnoreCase("Authorization")){
                        //如果是令牌，就将令牌加入
                        String headerValue = request.getHeader(headerName);
                        requestTemplate.header(headerName,headerValue);
                        return;
                    }

                }
            }
        }
    }
}
