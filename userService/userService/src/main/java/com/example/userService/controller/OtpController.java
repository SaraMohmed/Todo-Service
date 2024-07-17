package com.example.userService.controller;

import com.example.userService.dto.MailBody;
import com.example.userService.entity.Otp;
import com.example.userService.entity.User;
import com.example.userService.repository.OtpRepository;
import com.example.userService.repository.UserRepository;
import com.example.userService.services.EmailService;
import com.example.userService.services.UserService;
import com.example.userService.utils.ChangePassword;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/service/forgotPassword")
public class OtpController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(OtpController.class);


    private final PasswordEncoder passwordEcoder;

    public OtpController(PasswordEncoder passwordEcoder) {
        this.passwordEcoder = passwordEcoder;
    }



    @Operation(summary = "Verify email by send otp to email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email sent for verification!",
                    content = @Content(schema = @Schema(implementation = EmailService.class))),
    })
    @PostMapping("/regenrateOtp")
    public ResponseEntity<String> verifyEmail(@RequestParam("mail") String email) {
        logger.debug("Received email verification request for: {}", email);

        if (email == null || email.trim().isEmpty()) {
            logger.warn("Invalid email address: {}", email);
            return ResponseEntity.badRequest().body("Invalid email address.");
        }

        User user = userService.findByEmail(email);
        if (user == null) {
            logger.warn("User not found for email: {}", email);
            return ResponseEntity.badRequest().body("User not found.");
        }

        int otp = otpGenerator();
        logger.debug("Generated OTP: {}", otp);

        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for your Forgot Password request: " + otp)
                .subject("OTP for Forgot Password")
                .build();

        Otp otp1 = Otp.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 3 * 60 * 1000))
                .user(user)
                .build();

        try {
            emailService.sendSimpleMessage(mailBody);
            otpRepository.save(otp1);
            logger.info("OTP email sent to: {}", email);
        } catch (Exception e) {
            logger.error("Error sending email to {}: {}", email, e.getMessage());
            return ResponseEntity.status(500).body("Error sending email");
        }

        return ResponseEntity.ok("Email sent for verification");
    }





    @Operation(summary = "Verify otp")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP verified",
                    content = @Content(schema = @Schema(implementation = OtpController.class))),
    })
    @PostMapping("/activateUsername")
    public ResponseEntity<String> verifyOtp(@RequestParam("otp") String otp, @RequestParam("mail") String email) {
        User user = userService.findByEmail(email);
        Otp otp1 = otpRepository.findOtpAndUser(otp, user);

        if (otp1.getExpirationTime().before(Date.from(Instant.now()))) {
            otpRepository.deleteById(otp1.getId());
            return new ResponseEntity<>("OTP has expired", HttpStatus.EXPECTATION_FAILED);
        }
        user.setEnable(true);
         userRepository.save(user);

            return ResponseEntity.ok("activated username");
    }





    @Operation(summary = "chage Password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password has been changed!",
                    content = @Content(schema = @Schema(implementation = OtpController.class))),
    })
    @PostMapping("/chagePassword")
    public ResponseEntity<String> chagePasswordHandler(@RequestBody ChangePassword changePassword,@RequestParam("otp") String otp, @RequestParam("mail") String email){

        if(!Objects.equals(changePassword.password(), changePassword.repeatPassword())){

            return new ResponseEntity<>("Please enter the password again",HttpStatus.EXPECTATION_FAILED);
        }
        User user = userService.findByEmail(email);
        Otp otp1 = otpRepository.findOtpAndUser(otp, user);

        if (otp1.getExpirationTime().before(Date.from(Instant.now()))) {
            otpRepository.deleteById(otp1.getId());
            return new ResponseEntity<>("OTP has expired", HttpStatus.EXPECTATION_FAILED);
        }
        String ecodedPassword = passwordEcoder.encode(changePassword.password());
        userRepository.updatePassword(email,ecodedPassword);
        return ResponseEntity.ok("Password has been changed");
    }


    private int otpGenerator(){
        Random random = new Random();
        return random.nextInt(100_000,999_999);
    }

}