package com.example.MyApp.service;

import com.example.MyApp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CountDownLatch;

import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

    public void saveUser(User user) {

        String safeEmail = user.getEmail().replace(".", "_");

        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference("users");

        ref.child(safeEmail).setValueAsync(user);
    }

    public boolean emailExists(String email) {
        
        String safeEmail = email.replace(".", "_");
        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference("users")
                .child(safeEmail);
        
        final boolean[] exists = {false};
        final CountDownLatch latch = new CountDownLatch(1);
        
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                exists[0] = snapshot.exists();
                latch.countDown();
            }
        
            @Override
            public void onCancelled(DatabaseError error) {
                exists[0] = false;
                latch.countDown();
            }
        });
    
        try {
            latch.await(); // wait until Firebase responds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
        return exists[0];
    }

}
