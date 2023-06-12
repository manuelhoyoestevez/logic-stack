package com.mhe.dev.logic.stack.infrastructure.rest.spring.security;

import com.mhe.dev.logic.stack.infrastructure.rest.spring.dto.ErrorDto;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * JWT Authorization Filter.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter
{
    private static final Logger filterLogger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private final JwtConsumer jwtConsumer;

    private final String headerAuthorizationKey;

    private final String tokenBearerPrefix;

    private final String roleName;

    /**
     * Constructor.
     *
     * @param authManager            Authentication Manager.
     * @param jwtConsumer            JWT Consumer.
     * @param headerAuthorizationKey Authorization header.
     * @param tokenBearerPrefix      Token bearer prefix.
     * @param roleName               Role name.
     */
    public JwtAuthorizationFilter(AuthenticationManager authManager, JwtConsumer jwtConsumer,
                                  String headerAuthorizationKey, String tokenBearerPrefix, String roleName)
    {
        super(authManager);
        this.jwtConsumer = jwtConsumer;
        this.headerAuthorizationKey = headerAuthorizationKey;
        this.tokenBearerPrefix = tokenBearerPrefix;
        this.roleName = roleName;
    }

    private static void answerError(HttpServletResponse response, ErrorDto errorDto)
        throws IOException
    {
        filterLogger.info("Answering: {}", errorDto);
        response.setContentType("application/json");
        response.setStatus(errorDto.getCode());
        response.getOutputStream().println("{ "
            + "\"code\": " + errorDto.getCode() + ","
            + "\"title\": \"" + errorDto.getTitle() + "\", "
            + "\"description\" : \"" + errorDto.getDescription() + "\""
            + " }");
        response.flushBuffer();
    }

    private static ErrorDto newErrorDto(Integer code, String title, String description)
    {
        return new ErrorDto()
            .code(code)
            .title(title)
            .description(description);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
        throws IOException
    {
        String header = req.getHeader(headerAuthorizationKey);

        if (header == null)
        {
            answerError(res,
                newErrorDto(401, "Missing '" + headerAuthorizationKey + "' header", ""));
            return;
        }

        if (!header.startsWith(tokenBearerPrefix))
        {
            answerError(res, newErrorDto(401,
                "Invalid '" + headerAuthorizationKey + " header'. Must start with '"
                    + tokenBearerPrefix + "' prefix", ""));
            return;
        }

        String token = header.replace(tokenBearerPrefix, "");

        try
        {
            filterLogger.info("Token: {}", token);
            UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
            filterLogger.info("Token is OK with: {}", authentication.getName());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(req, res);
        } catch (InvalidJwtException exception)
        {
            answerError(res, newErrorDto(403, "Invalid 'Authorization' JWT token", ""));
        } catch (MalformedClaimException exception)
        {
            answerError(res, newErrorDto(401, "Malformed 'Authorization' JWT token", ""));
        } catch (Exception exception)
        {
            answerError(res,
                newErrorDto(500, exception.getMessage(), exception.getClass().getName()));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token)
        throws InvalidJwtException, MalformedClaimException
    {
        JwtContext jwtContext = jwtConsumer.process(token);
        String user = jwtContext.getJwtClaims().getSubject();
        String role = jwtContext.getJwtClaims().getClaimValueAsString(roleName);
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
        grantedAuthorities.add(authority);
        return new UsernamePasswordAuthenticationToken(user, null, grantedAuthorities);
    }
}
