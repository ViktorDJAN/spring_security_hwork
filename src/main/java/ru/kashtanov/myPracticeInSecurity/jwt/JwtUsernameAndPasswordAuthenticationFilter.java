package ru.kashtanov.myPracticeInSecurity.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    //it is passing the UsernamePasswordAuthenticationToken to the default AuthenticationProvider, which will use the
    // userDetailsService to get the user based on username and compare that user's password with the one in the authentication token.
    //In general, the AuthenticationManager passes some sort of AuthenticationToken to the each of it's AuthenticationProviders
    // and they each inspect it and, if they can use it to authenticate, they return with an indication of "Authenticated", "Unauthenticated",
    // or "Could not authenticate" (which indicates the provider did not know how to handle the token, so it passed on processing it)
    //This is the mechanism that allows you to plug in other authentication schemes, like authenticating against an LDAP or Active Directory server, or OpenID, and is one of the main extension points within the Spring Security framework.
    private final AuthenticationManager authenticationManager;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper()//reads from stream and put value
                    .readValue(request.getInputStream(),UsernameAndPasswordAuthenticationRequest.class); // into a pointed class
//The principal is the currently logged in user. However, you retrieve it through the security context which is bound
// to the current thread and as such, it's also bound to the current request and its session.
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );
            Authentication authenticate = authenticationManager.authenticate(authentication);
            return authenticate;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
