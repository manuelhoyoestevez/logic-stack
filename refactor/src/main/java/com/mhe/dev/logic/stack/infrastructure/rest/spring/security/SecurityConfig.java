package com.mhe.dev.logic.stack.infrastructure.rest.spring.security;

import java.nio.charset.StandardCharsets;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Security Config.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Value("${jwt.issuerInfo}")
    private String issuerInfo;

    @Value("${jwt.clientSecret}")
    private String clientSecret;

    @Value("${jwt.headerAuthorizationKey}")
    private String headerAuthorizationKey;

    @Value("${jwt.tokenBearerPrefix}")
    private String tokenBearerPrefix;

    @Value("${jwt.roleName}")
    private String roleName;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .cors()
            .and()
            .csrf().disable()
            .antMatcher("/whitelist/**").authorizeRequests().anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource()
    {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    JwtConsumer jwtConsumer()
    {
        return new JwtConsumerBuilder()
            .setRequireExpirationTime()
            .setAllowedClockSkewInSeconds(30)
            .setRequireSubject()
            .setExpectedIssuer(issuerInfo)
            .setVerificationKey(new HmacKey(clientSecret.getBytes(StandardCharsets.UTF_8)))
            .setJwsAlgorithmConstraints(
                AlgorithmConstraints.ConstraintType.PERMIT,
                AlgorithmIdentifiers.HMAC_SHA512
            )
            .build();
    }

    JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception
    {
        return new JwtAuthorizationFilter(authenticationManager(), jwtConsumer(),
            headerAuthorizationKey, tokenBearerPrefix, roleName);
    }
}
