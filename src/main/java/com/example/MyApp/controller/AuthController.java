package com.example.MyApp.controller;

import com.example.MyApp.service.FirebaseService;
import com.example.MyApp.service.EmailService;
import com.example.MyApp.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.MyApp.model.User;
// import com.google.firebase.internal.FirebaseService;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FirebaseService firebaseService;

    @PostMapping("/sendOtp")
    public String sendOtp(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
    
        if (firebaseService.emailExists(email)) {
            model.addAttribute("error", "Email already taken, try another email");
            return "signup";
        }
    
        String otp = otpService.generateOtp(email);
        emailService.sendOtp(email, otp);
    
        session.setAttribute("name", name);
        session.setAttribute("email", email);
        session.setAttribute("password", password);
    
        return "verify-otp";
    }


    @PostMapping("/verifyOtp")
    public String verifyOtp(@RequestParam String otp,
                            HttpSession session,
                            Model model) {

        String email = (String) session.getAttribute("email");

        if (otpService.validateOtp(email, otp)) {

            User user = new User(
                    (String) session.getAttribute("name"),
                    email,
                    (String) session.getAttribute("password")
            );

            firebaseService.saveUser(user);
            otpService.clearOtp(email);

            return "redirect:/login";
        }

        model.addAttribute("error", "Invalid OTP");
        return "verify-otp";
    }
}
