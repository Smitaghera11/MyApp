package com.example.MyApp.controller;

import com.example.MyApp.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.CountDownLatch;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import org.springframework.ui.Model;


@Controller
public class PageController {

    @GetMapping("/")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin() {
        try {
            // your login logic
            return "dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            return "login";
        }
    }


    @PostMapping("/doSignup")
    public String doSignup(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password
    ) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        String userId = ref.push().getKey();

        User user = new User(name, email, password);
        ref.child(userId).setValueAsync(user);

        return "redirect:/login";
    }

    @PostMapping("/doLogin")
    public String doLogin(
            @RequestParam String email,
            @RequestParam String password,
            Model model) {

        try {
            // TEMP TEST (VERY IMPORTANT)
            System.out.println("Login attempt: " + email);

            // 👉 TEMP: SKIP FIREBASE (to confirm dashboard works)
            model.addAttribute("email", email);
            return "dashboard";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Login failed");
            return "login";
        }
    }




    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}
