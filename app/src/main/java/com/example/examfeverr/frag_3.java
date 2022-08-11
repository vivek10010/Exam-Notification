package com.example.examfeverr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class frag_3 extends Fragment {

    RecyclerView ch_rv;

    ChatAdapter chatAdapter;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_frag_3, container, false);



        ch_rv = view.findViewById(R.id.chat_rv);

        initchatList();








       return view;
    }

    private void initchatList() {

        ch_rv.setHasFixedSize(true);
        ch_rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));



        model mdl = (model) getActivity().getIntent().getSerializableExtra("model");

        String messageid = mdl.getId().trim();

        collectionReference = firebaseFirestore.collection("exams").document(messageid).collection("CHAT");
        Query query = collectionReference.orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatModel> option = new FirestoreRecyclerOptions.Builder<ChatModel>()
                .setQuery(query, ChatModel.class)
                .build();

        chatAdapter = new ChatAdapter(option);

        ch_rv.setAdapter(chatAdapter);
        ch_rv.smoothScrollToPosition(chatAdapter.getItemCount());




    }

    @Override
    public void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }
}