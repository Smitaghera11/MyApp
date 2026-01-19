// package com.example.MyApp.config;

// import com.google.auth.oauth2.GoogleCredentials;
// import com.google.firebase.FirebaseApp;
// import com.google.firebase.FirebaseOptions;
// import org.springframework.context.annotation.Configuration;

// import javax.annotation.PostConstruct;
// import java.io.InputStream;

// @Configuration
// public class FirebaseConfig {

//     @PostConstruct
//     public void initFirebase() {
//         try {
//             InputStream serviceAccount =
//                     getClass().getClassLoader().getResourceAsStream("firebase/firebase-service-account.json");

//             FirebaseOptions options = FirebaseOptions.builder()
//                     .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                     .setDatabaseUrl("https://myapp-48f03-default-rtdb.firebaseio.com/")
//                     .build();

//             if (FirebaseApp.getApps().isEmpty()) {
//                 FirebaseApp.initializeApp(options);
//             }

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }




// package com.example.MyApp.config;

// import com.google.auth.oauth2.GoogleCredentials;
// import com.google.firebase.FirebaseApp;
// import com.google.firebase.FirebaseOptions;
// import org.springframework.context.annotation.Configuration;
// import com.google.auth.oauth2.GoogleCredentials;
// import com.google.firebase.FirebaseApp;
// import com.google.firebase.FirebaseOptions;
// import jakarta.annotation.PostConstruct;
// import org.springframework.context.annotation.Configuration;

// import java.io.ByteArrayInputStream;
// import java.io.InputStream;
// import java.nio.charset.StandardCharsets;

// @Configuration
// public class FirebaseConfig {

//     @PostConstruct
//     public void initialize() {
//         try {
//             InputStream serviceAccount =
//                     getClass().getClassLoader()
//                             .getResourceAsStream("firebase/firebase-service-account.json");

//             if (serviceAccount == null) {
//                 throw new RuntimeException("Firebase service account file not found");
//             }

//             FirebaseOptions options = FirebaseOptions.builder()
//                     .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                     .setDatabaseUrl("https://myapp-48f03-default-rtdb.firebaseio.com/")
//                     .build();

//             if (FirebaseApp.getApps().isEmpty()) {
//                 FirebaseApp.initializeApp(options);
//                 System.out.println("✅ Firebase initialized successfully");
//             }

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }












package com.example.MyApp.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {

            String firebaseKey = System.getenv("FIREBASE_KEY");

            if (firebaseKey == null || firebaseKey.isEmpty()) {
                System.out.println("❌ FIREBASE_KEY not found in environment");
                return;
            }

            InputStream serviceAccount =
                    new ByteArrayInputStream(firebaseKey.getBytes(StandardCharsets.UTF_8));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://myapp-48f03-default-rtdb.firebaseio.com/")
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase initialized successfully");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
