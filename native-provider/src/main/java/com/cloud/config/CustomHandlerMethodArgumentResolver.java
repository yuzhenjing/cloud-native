package com.cloud.config;

import com.cloud.annotation.CustomRequestBody;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yzz
 */
public class CustomHandlerMethodArgumentResolver extends HandlerMethodArgumentResolverComposite {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        return MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType()) && parameter.hasParameterAnnotation(CustomRequestBody.class);
    }


    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.state(servletRequest != null, "No HttpServletRequest");
        return super.resolveArgument(parameter, modelAndViewContainer, webRequest, webDataBinderFactory);
    }
}
