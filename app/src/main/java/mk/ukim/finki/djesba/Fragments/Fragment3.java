package mk.ukim.finki.djesba.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import mk.ukim.finki.djesba.MainPageActivity;
import mk.ukim.finki.djesba.R;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment3 extends Fragment {

    ViewPager viewPager;
    TextView done;
    private EditText userName;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    public Fragment3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fragment3, container, false);
        viewPager = getActivity().findViewById(R.id.introPager);
        userName = view.findViewById(R.id.name);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        done = view.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prodolzi so MainPageActivity
                //getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                updateName();
            }
        });

        return view;
    }

    private void updateName()
    {
        boolean flag;
        final String setUserName = userName.getText().toString();
        if (TextUtils.isEmpty(setUserName))
        {
            flag = false;
            Toast.makeText(this.getContext(), "Please write your user name first....", Toast.LENGTH_SHORT).show();
        }
        else
        {
            flag = true;
            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("name", setUserName);
            RootRef.child("user").child(currentUserID).updateChildren(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(Fragment3.this.getContext(), "Registered Successfully...", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(Fragment3.this.getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        if(flag)
            startActivity(new Intent(getContext(),MainPageActivity.class));
    }

}

