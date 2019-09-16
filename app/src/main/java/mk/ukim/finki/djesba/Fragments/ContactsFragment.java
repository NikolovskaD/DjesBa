package mk.ukim.finki.djesba.Fragments;


import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import mk.ukim.finki.djesba.R;
import mk.ukim.finki.djesba.User.UserListAdapter;
import mk.ukim.finki.djesba.User.UserObject;
import mk.ukim.finki.djesba.Utils.CountryToPhonePrefix;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;

    ArrayList<UserObject> contactList, userList;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        contactList = new ArrayList<>();
        userList = new ArrayList<>();

        mUserList = view.findViewById(R.id.userList);
        mUserList.setLayoutManager(new LinearLayoutManager(getContext()));

        Button mCreate = view.findViewById(R.id.create);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createChat();
            }
        });
        initializeRecyclerView();
        getContactList();

        return view;
    }

    private void createChat(){

        String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("user");
        DatabaseReference chatInfoDb = FirebaseDatabase.getInstance().getReference().child("chat").child(key).child("info");

        HashMap newChatMap = new HashMap();
        newChatMap.put("id", key);
        newChatMap.put("users/" + FirebaseAuth.getInstance().getUid(), true);

        Boolean validChat = false;
        for(UserObject mUser : userList){
            if(mUser.getSelected()){
                validChat = true;
                newChatMap.put("users/" + mUser.getUid(), true);
                userDb.child(mUser.getUid()).child("chat").child(key).setValue(true);
            }
        }

        if(validChat){
            chatInfoDb.updateChildren(newChatMap);
            userDb.child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(true);
        }

    }

    private void getContactList(){
        String ISOprefix = getCountryISO();
        Cursor phones = mUserList.getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while (phones.moveToNext()){
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phone = phone.replace(" ","");
            phone = phone.replace("-","");
            phone = phone.replace("(","");
            phone = phone.replace(")","");

            if (!String.valueOf(phone.charAt(0)).equals("+"))
                phone = ISOprefix + phone;

            UserObject mContact = new UserObject("",name,phone);
            contactList.add(mContact);

            getUserDetails(mContact);
        }
    }

    private void getUserDetails(UserObject mContact) {
        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user");
        Query query = mUserDB.orderByChild("phone").equalTo(mContact.getPhone());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String phone = "";
                    String name = "";
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                        if (childSnapshot.child("phone").getValue() != null){
                            phone = childSnapshot.child("phone").getValue().toString();
                        }
                        if (childSnapshot.child("name").getValue() != null){
                            name = childSnapshot.child("name").getValue().toString();
                        }

                        UserObject mUser = new UserObject(childSnapshot.getKey(),name,phone);
                        if (name.equals(phone)){
                            for (UserObject mContactIterator : contactList){
                                if (mContactIterator.getPhone().equals(mUser.getPhone())){
                                    mUser.setName(mContactIterator.getName());
                                }
                            }
                        }

                        userList.add(mUser);

                        //mu javuvame na RecyclerView deka nesto se smenilo, za da znae da go prikaze
                        mUserListAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //iso - bukvite identifikatori za drzava
    private String getCountryISO(){
        String iso = null;

        TelephonyManager telephonyManager = (TelephonyManager) mUserList.getContext().getSystemService(mUserList.getContext().TELEPHONY_SERVICE);
        if (telephonyManager.getNetworkCountryIso() != null){
            if (!telephonyManager.getNetworkCountryIso().toString().equals("")){
                iso = telephonyManager.getNetworkCountryIso().toString();
            }
        }

        return CountryToPhonePrefix.getPhone(iso);
    }

    private void initializeRecyclerView() {
        //to scroll seamlessly
        mUserList.setNestedScrollingEnabled(false);
        mUserList.setHasFixedSize(false);

        mUserListLayoutManager = new LinearLayoutManager(mUserList.getContext(),LinearLayoutManager.VERTICAL,false);
        mUserList.setLayoutManager(mUserListLayoutManager);

        mUserListAdapter= new UserListAdapter(userList);
        mUserList.setAdapter(mUserListAdapter);
    }

}
