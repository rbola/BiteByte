package com.main.bitebyte.user; 

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.bitebyte.security.JwtUtils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${jwt.expiration.ms}")
    private long jwtExpirationMs;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        logger.info("Received signin request for user: {}", loginRequest.getUsername());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String jwt = Jwts.builder()
                    .setSubject(userDetails.getUsername())
                    .claim("roles", roles)  // Add this line to include roles in the token
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                    .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .compact();

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("username", userDetails.getUsername());
            
            response.put("roles", roles);

            logger.info("User {} successfully authenticated. Roles: {}", loginRequest.getUsername(), roles);
            logger.debug("Generated JWT token: {}", jwt);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            logger.error("Authentication failed for user: {}. Invalid credentials.", loginRequest.getUsername());
            return ResponseEntity.badRequest().body("Error: Invalid username or password");
        } catch (AuthenticationException e) {
            logger.error("Authentication error for user: {}. Reason: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.badRequest().body("Error: Authentication failed");
        } catch (JwtException e) {
            logger.error("JWT error for user: {}", loginRequest.getUsername(), e);
            return ResponseEntity.internalServerError().body("Error: Token generation failed");
        } catch (Exception e) {
            logger.error("Unexpected error during authentication for user: {}", loginRequest.getUsername(), e);
            return ResponseEntity.internalServerError().body("Error: An unexpected error occurred");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        logger.info("Received signup request for user: {}", signUpRequest.getUsername());

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            logger.warn("Username {} is already taken", signUpRequest.getUsername());
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            logger.warn("Email {} is already in use", signUpRequest.getEmail());
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        User user = new User(signUpRequest.getUsername(),
                             passwordEncoder.encode(signUpRequest.getPassword()),
                             signUpRequest.getEmail(),
                             java.util.Collections.singleton(Role.USER));

        userRepository.save(user);

        logger.info("User registered successfully: {}", signUpRequest.getUsername());
        return ResponseEntity.ok("User registered successfully!");
    }

    public static class LoginRequest {
        private String username;
        private String password;

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class SignupRequest {
        private String username;
        private String email;
        private String password;

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}