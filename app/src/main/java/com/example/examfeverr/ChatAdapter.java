package com.example.examfeverr;

import android.app.Activity;
import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class ChatAdapter extends FirestoreRecyclerAdapter<ChatModel, ChatAdapter.ChatViewHolder>
{

    public ChatAdapter(@NonNull FirestoreRecyclerOptions<ChatModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull ChatModel chatModel) {
//        holder.message.setText(chatModel.getMessage());
          holder.setMessage(chatModel.getMessage(), chatModel.getUserid(), chatModel.getUsername(), chatModel.getTimestamp());

    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(v);
    }

    class ChatViewHolder extends RecyclerView.ViewHolder
    {

        TextView senderTv, receiverTv, sendersName, receiversName;
        LinearLayout sender_ll, receiver_ll;


        public ChatViewHolder(@NonNull View itemView)
        {
            super(itemView);

        }

        public void setMessage(String  message, String userid, String username, long timestamp)
        {
            sender_ll = itemView.findViewById(R.id.ll_sender_chat);
            receiver_ll = itemView.findViewById(R.id.ll_receiver_chat);

            senderTv = itemView.findViewById(R.id.msg_sender_tv);
            receiverTv = itemView.findViewById(R.id.msg_receiver_tv);

            sendersName = itemView.findViewById(R.id.tv_sender_chat_name);
            receiversName = itemView.findViewById(R.id.tv_receiver_chat_name);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String currentUser = user.getUid();

            if(currentUser.equals(userid) )
            {
                receiver_ll.setVisibility(View.GONE);
                senderTv.setText(message);
                sendersName.setText(username);
            }
            else
            {
                sender_ll.setVisibility(View.GONE);
                receiverTv.setText(message);
                receiversName.setText(username);
            }
        }



    }
}
