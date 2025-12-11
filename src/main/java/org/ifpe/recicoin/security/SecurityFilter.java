package org.ifpe.recicoin.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ifpe.recicoin.repositories.CollectionPointRepository;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        if (uri.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request);

        // 🔍 LOG 1 — token recebido
        System.out.println("TOKEN = " + token);

        if (token != null) {
            try {
                var email = tokenService.validateToken(token);

                // 🔍 LOG 2 — email extraído do token
                System.out.println("EMAIL = " + email);

                UserDetails user = userRepository.findByEmail(email);

                if (user == null) {
                    user = collectionPointRepository.findByEmail(email);
                }

                // 🔍 LOG 3 — usuário encontrado (ou null)
                System.out.println("USER = " + user);

                if (user != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (Exception e) {
                System.out.println("ERRO AO VALIDAR TOKEN: " + e.getMessage());
                SecurityContextHolder.clearContext();
            }
        } else {
            // 🔍 LOG 4 — token ausente
            System.out.println("TOKEN NULO — nenhuma autenticação enviada");
        }

        filterChain.doFilter(request, response);
    }


    private String recoverToken(HttpServletRequest request) {
        var header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }

        return header.substring(7);
    }

}
