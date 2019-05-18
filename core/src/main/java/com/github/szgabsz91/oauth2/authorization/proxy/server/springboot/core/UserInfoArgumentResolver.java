package com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.core;

import com.github.szgabsz91.oauth2.authorization.proxy.server.springboot.providers.api.model.UserInfo;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;

/**
 * Custom {@link HandlerMethodArgumentResolver} implementation that can resolve {@link UserInfo} and
 * {@link Mono} of {@link UserInfo} parameters in Spring WebFlux controller methods.
 *
 * @author szgabsz91
 */
public class UserInfoArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * Returns if this implementation supports the given {@link MethodParameter}.
     * @param methodParameter the {@link MethodParameter}
     * @return <code>true</code> if the given {@link MethodParameter}'s type is {@link UserInfo},
     *         <code>false</code> otherwise
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return isUserInfoMono(methodParameter) ||
                methodParameter.getParameterType().equals(UserInfo.class);
    }

    /**
     * Resolves the {@link UserInfo} method parameter from the given {@link ServerWebExchange}.
     * @param methodParameter the {@link MethodParameter}
     * @param bindingContext the {@link BindingContext}
     * @param serverWebExchange the {@link ServerWebExchange}
     * @return the {@link UserInfo}
     */
    @Override
    public Mono<Object> resolveArgument(MethodParameter methodParameter, BindingContext bindingContext, ServerWebExchange serverWebExchange) {
        return serverWebExchange.getPrincipal()
                .map(principal -> {
                    var authentication = (Authentication) principal;
                    return authentication.getPrincipal();
                })
                .map(userInfo -> {
                    if (isUserInfoMono(methodParameter)) {
                        return Mono.just(userInfo);
                    }
                    return userInfo;
                });
    }

    private static boolean isUserInfoMono(MethodParameter methodParameter) {
        var genericParameterType = methodParameter.getGenericParameterType();

        if (genericParameterType instanceof ParameterizedType) {
            var parameterizedType = (ParameterizedType) genericParameterType;

            if (parameterizedType.getRawType().equals(Mono.class)) {
                var actualTypeArguments = parameterizedType.getActualTypeArguments();
                var actualTypeArgument = actualTypeArguments[0];
                var actualTypeArgumentString = actualTypeArgument.getTypeName();
                return UserInfo.class.getName().equals(actualTypeArgumentString);
            }
        }

        return false;
    }

}
