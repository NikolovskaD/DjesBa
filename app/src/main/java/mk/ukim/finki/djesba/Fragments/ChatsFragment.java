package mk.ukim.finki.djesba.Fragments;


import android.Manifest;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mk.ukim.finki.djesba.Chat.ChatListAdapter;
import mk.ukim.finki.djesba.Chat.ChatObject;
import mk.ukim.finki.djesba.R;
import mk.ukim.finki.djesba.User.UserObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    ArrayList<ChatObject> chatList;

    private RecyclerView mChatList;
    private RecyclerView.Adapter mChatListAdapter;
    private RecyclerView.LayoutManager mChatListLayoutManager;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        mChatList = view.findViewById(R.id.chatsList);
        mChatList.setLayoutManager(new LinearLayoutManager(getContext()));

        initializeRecyclerView();
        getUserChatList();
        getPermimssions();
        return view;
    }

    private void getUserChatList(){
        DatabaseReference mUserChatDB = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat");

        mUserChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                        ChatObject mChat = new ChatObject(childSnapshot.getKey());
                        boolean exists = false;
                        for (ChatObject mChatIterator : chatList){
                            if (mChatIterator.getChatId().equals(mChat.getChatId()))
                                exists = true;
                        }
                        if (exists)
                            continue;
                        chatList.add(mChat);
                        getChatData(mChat.getChatId());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getChatData(String chatId) {
        DatabaseReference mChatDB = FirebaseDatabase.getInstance().getReference().child("chat").child(chatId).child("info");
        mChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())   {
                    String chatId = "";

                    if(dataSnapshot.child("id").getValue() != null)
                        chatId = dataSnapshot.child("id").getValue().toString();

                    for(DataSnapshot userSnapshot : dataSnapshot.child("users").getChildren()){
                        for(ChatObject mChat : chatList)
                            if(mChat.getChatId().equals(chatId)){
                                UserObject mUser = new UserObject(userSnapshot.getKey());
                                mChat.addUserToArrayList(mUser);
                                getUserData(mUser);
                            }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getUserData(UserObject mUser) {
        DatabaseReference mUserDb = FirebaseDatabase.getInstance().getReference().child("user").child(mUser.getUid());
        mUserDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserObject mUser = new UserObject(dataSnapshot.getKey());
                if(dataSnapshot.child("notificationKey").getValue() != null)
                    mUser.setNotificationKey(dataSnapshot.child("notificationKey").getValue().toString());

                for(ChatObject mChat : chatList){
                    for(UserObject mUserIt : mChat.getUserObjectArrayList()){
                        if(mUserIt.getUid().equals(mUser.getUid())){
                            mUserIt.setNotificationKey(mUser.getNotificationKey());
                        }
                    }
                }
                mChatListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeRecyclerView() {
        chatList = new ArrayList<>();
        //to scroll seamlessly
        mChatList.setNestedScrollingEnabled(false);
        mChatList.setHasFixedSize(false);

        mChatListLayoutManager = new LinearLayoutManager(mChatList.getContext(),LinearLayoutManager.VERTICAL,false);
        mChatList.setLayoutManager(mChatListLayoutManager);

        mChatListAdapter = new ChatListAdapter(chatList);
        mChatList.setAdapter(mChatListAdapter);
    }

    private void getPermimssions() {
        // za verzii nad Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS},1);
        }
    }

}
