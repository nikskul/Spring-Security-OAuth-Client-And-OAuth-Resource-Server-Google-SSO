package ru.nikskul.sandbox.spring.security.services;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@Configuration
public class WebConfiguration {
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/api/**")
                        .filters(f -> f.filter((exchange, chain) ->
                                exchange.getPrincipal()
                                        .cast(OAuth2AuthenticationToken.class)
                                        .flatMap(auth -> {

                                            if (auth.getPrincipal() instanceof OidcUser oidcUser) {
                                                String idToken = oidcUser.getIdToken().getTokenValue();

                                                var request = exchange.getRequest().mutate()
                                                        .header("Authorization", "Bearer " + idToken)
                                                        .build();

                                                var newExchange = exchange.mutate()
                                                        .request(request)
                                                        .build();

                                                return chain.filter(newExchange);
                                            }

                                            return chain.filter(exchange);
                                        })
                                        .switchIfEmpty(chain.filter(exchange))
                        ))
                        .uri("http://localhost:8082")
                )
                .build();
    }
}
