package hcmute.danbaonguyen19110036.appzalo.Utils;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hcmute.danbaonguyen19110036.appzalo.Model.User;

public class Util {
    public static int TITLE_REQUEST;
    public static User currentUser;
    public static void getCurrentUser(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth){
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
