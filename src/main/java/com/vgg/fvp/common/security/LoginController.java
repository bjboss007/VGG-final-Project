package com.vgg.fvp.common.security;

import com.vgg.fvp.common.data.User;
import com.vgg.fvp.common.data.UserService;
import com.vgg.fvp.common.utils.AppResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;


@RestController
@RequestMapping("/api/fvp/v1/")
public class LoginController {

    private JwtProvider tokenProvider;
    private AuthenticationManager authenticationManager;
    private UserService userService;

    public LoginController(JwtProvider tokenProvider, AuthenticationManager authenticationManager, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("auth")
    public ResponseEntity login(@Valid @RequestBody LoginViewModel loginViewModel) throws IOException, ServletException {
        Authentication authentication = authenticate(loginViewModel);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new AppResponse(jwt, "Bearer"));
    }

    @GetMapping("all-users")
    public ResponseEntity getAllUsers(Pageable pageable){
        Page<User> allUsers = userService.getAllUsers(pageable);
        return ResponseEntity.ok(allUsers);
    }

    public Authentication authenticate(LoginViewModel loginViewModel){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginViewModel.getUsername(),
                loginViewModel.getPassword(),
                new ArrayList<>()
        );
        Authentication authenticate =  authenticationManager.authenticate(authenticationToken);

        return authenticate;
    }
}
