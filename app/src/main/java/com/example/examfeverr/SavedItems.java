package com.example.examfeverr;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SavedItems extends AppCompatActivity {




    LinearLayoutManager linearLayoutManager;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;


    String uid;
    Boolean fvrt_Checker = false;
    model examModel;

    ImageButton savedBackBtn;


    RecyclerView recyclerView;
    FirebaseRecyclerAdapter adapter;

    private TextView emptyView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_items);


        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);

        recyclerView = (RecyclerView)findViewById(R.id.rv_saved);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(null);

        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();



        savedBackBtn = findViewById(R.id.saved_backbtn);

        emptyView = (TextView) findViewById(R.id.empty_view);
//        Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();

        DatabaseReference ref = firebaseDatabase.getReference("favouriteList");
        Query queries = ref.orderByChild(uid);


        queries.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
                else{
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("favouriteList").child(uid), model.class)
                        .build();

         adapter = new FirebaseRecyclerAdapter<model, myviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull model model) {

                final String postkey = getRef(position).getKey();

                String id = getItem(position).getId();

                holder.bind(model);
                holder.imgbtn.setImageResource(R.drawable.ic_baseline_turned_in_24);


                holder.imgbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference fvrtref = firebaseDatabase.getReference("favourites");
                        DatabaseReference fvrt_listRef = firebaseDatabase.getReference("favouriteList").child(currentUser);

                        fvrtref.child(id).child(currentUser).removeValue();
                        delete(id);

                        }
                });
            }

            @Override
            public myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item, parent, false);

                return new myviewholder(view);
            }


        };

        recyclerView.setAdapter(adapter);

        savedBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void delete(String id) {

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference fvrtref = firebaseDatabase.getReference("favourites");
        DatabaseReference fvrt_listRef = firebaseDatabase.getReference("favouriteList").child(currentUser);
        Query query = fvrt_listRef.orderByChild("id").equalTo(id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    dataSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public class myviewholder extends RecyclerView.ViewHolder{

        TextView name, link;
        ImageButton imgbtn;
        model usermodel;




        public myviewholder(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.tv1);
            link = itemView.findViewById(R.id.tv2);
            imgbtn = itemView.findViewById(R.id.item_fvrt);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), exam_page_layout.class);
                    intent.putExtra("model", usermodel);
                    itemView.getContext().startActivity(intent);
                    finish();

                }
            });
        }

        public void bind(model model) {
            usermodel = model;
            name.setText(model.getNAME());
            link.setText(model.getTitle());
        }


    }




    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.startListening();
    }

}