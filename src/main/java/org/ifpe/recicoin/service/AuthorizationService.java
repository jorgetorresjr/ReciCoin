package org.ifpe.recicoin.service;

import org.ifpe.recicoin.repositories.CollectionPointRepository;
import org.ifpe.recicoin.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollectionPointRepository collectionPointRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails user = userRepository.findByEmail(email);
        if (user == null) {
            user = collectionPointRepository.findByEmail(email);
        }
        if (user == null) {
            throw new UsernameNotFoundException("Usuário ou Empresa não encontrado: " + email);
        }
        
        return user;
    }
}