//package com.vgg.fvp.common.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.SecurityConfig;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.ServletException;
//import javax.validation.Valid;
//import java.io.IOException;
//import java.util.ArrayList;
//
//
//@Import(SecurityConfig.class)
//@RestController
//@RequestMapping("/api/fvp/login")
//public class LoginController {
//
//    @Autowired
//    private final JwtAuthenticationFilter authenticationFilter;
//
//
//    private AuthenticationManager authenticationManager;
//
//    public LoginController(JwtAuthenticationFilter authenticationFilter) {
//        this.authenticationFilter = authenticationFilter;
//    }
//
//    @PostMapping("")
//    public ResponseEntity login(@Valid @RequestBody LoginViewModel loginViewModel) throws IOException, ServletException {
//
//        authenticate(loginViewModel);
//        String token = authenticationFilter.generatToken(loginViewModel.getUsername());
//
//        return ResponseEntity.ok(token);
//    }
//
//    public void authenticate(LoginViewModel loginViewModel){
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                loginViewModel.getUsername(),
//                loginViewModel.getPassword(),
//                new ArrayList<>()
//        );
//        this.authenticationManager.authenticate(authenticationToken);
//    }
//}
