package tth_group.payment_listener.authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tth_group.payment_listener.authentication.dto.SignupRequest;
import tth_group.payment_listener.authentication.dto.SignupResponse;
import tth_group.payment_listener.entity.User;
import tth_group.payment_listener.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenProvider.generateToken(authentication);
            User user = (User) authentication.getPrincipal();

            return ResponseEntity.ok(new JwtResponse(jwt, user.getUsername(), user.getAuthorities()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        // Kiểm tra xem username đã tồn tại chưa
        var a = userRepository.existsByUsername(signupRequest.getUsername());
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new SignupResponse("Username is already taken!"));
        }

        // Kiểm tra xem email đã tồn tại chưa
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new SignupResponse("Email is already in use!"));
        }

        // Tạo mới user
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setDob(signupRequest.getDob());
        user.setEmail(signupRequest.getEmail());

        Set<String> strRoles = signupRequest.getRoles();
        user.setRoles(strRoles != null ? strRoles : new HashSet<>(Set.of("ROLE_USER")));

        // Lưu user vào database
        userRepository.save(user);

        return ResponseEntity.ok(new SignupResponse("User registered successfully!"));
    }

    @GetMapping("/check-token")
    public ResponseEntity<?> checkTokenValidity(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing or invalid");
        }

        String token = authorizationHeader.replace("Bearer ", "");

        try {
            // Kiểm tra tính hợp lệ của token
            if (jwtTokenProvider.validateToken(token)) {
                // Nếu token hợp lệ, trả về mã 200 OK
                return ResponseEntity.ok("Token is valid");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
        }
    }
}

