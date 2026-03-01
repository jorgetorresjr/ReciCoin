package org.ifpe.recicoin.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ifpe.recicoin.repositories.CollectionPointRepository;
import org.ifpe.recicoin.repositories.CompanyRepository;
import org.ifpe.recicoin.repositories.UserRepository;
import org.ifpe.recicoin.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CollectionPointRepository collectionPointRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        if (uri.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = recoverToken(request);

        if (token != null) {
            try {
                String email = tokenService.validateToken(token);

                UserDetails user = userRepository.findByEmail(email).orElse(null);

                if (user == null) {
                    user = collectionPointRepository.findByEmail(email).orElse(null);
                }

                if (user == null) {
                    user = companyRepository.findByEmail(email).orElse(null);
                }

                if (user != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    SecurityContextHolder.clearContext();
                }

            } catch (Exception e) {
                System.out.println("ERRO AO VALIDAR TOKEN: " + e.getMessage());
                SecurityContextHolder.clearContext();
            }
        } else {
            System.out.println("TOKEN NULO — nenhuma autenticação enviada");
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring(7);
    }

}
