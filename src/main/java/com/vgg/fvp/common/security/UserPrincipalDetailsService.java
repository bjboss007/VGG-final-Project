package com.vgg.fvp.common.security;

import com.vgg.fvp.common.data.User;
import com.vgg.fvp.common.data.UserRepository;
import com.vgg.fvp.common.exceptions.ObjectNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {
    private UserRepository repo;
    public UserPrincipalDetailsService(UserRepository repo) {
        this.repo = repo;
    }
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = this.repo.findUserByEmail(s);
        if(user == null)
            throw new ObjectNotFoundException("User not found with username " + s);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        return userPrincipal;
    }
}
