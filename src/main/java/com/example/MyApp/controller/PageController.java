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
            Model model
    ) throws InterruptedException {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        final boolean[] loginSuccess = {false};
        final CountDownLatch latch = new CountDownLatch(1);

        ref.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                User user = userSnapshot.getValue(User.class);

                                if (user != null && user.getPassword().equals(password)) {
                                    loginSuccess[0] = true;
                                    break;
                                }
                            }
                        }

                        latch.countDown(); // Firebase finished
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        latch.countDown();
                    }
                });

        // ⏳ WAIT for Firebase result
        latch.await();

        if (loginSuccess[0]) {
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }



    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "/dashboard";
    }
}
