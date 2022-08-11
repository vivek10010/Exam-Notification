package com.example.examfeverr;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RecyclerAdapter extends FirestoreRecyclerAdapter<model, RecyclerAdapter.myViewholder> {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    ImageButton fvrt_btn;

    Boolean fvrt_Checker = false;

    model examModel;





    public RecyclerAdapter(@NonNull FirestoreRecyclerOptions<model> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, int position, @NonNull model model) {



        fvrt_btn = holder.itemView.findViewById(R.id.item_fvrt);

        final String postkey = getSnapshots().getSnapshot(position).getId();

        String name = getItem(position).getNAME();
        String title = getItem(position).getTitle();
        String age = getItem(position).getAge();
        String date = getItem(position).getDates();
        String desc = getItem(position).getDesc();
        String fee = getItem(position).getFee();
        String id = getItem(position).getId();

        holder.bind(model);
        holder.favouriteChecker(postkey);

        holder.fvrt_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fvrt_Checker = true;
                String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference fvrtref = firebaseDatabase.getReference("favourites");
                DatabaseReference fvrt_listRef = firebaseDatabase.getReference("favouriteList").child(currentUser);

                fvrtref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(fvrt_Checker.equals(true))
                        {
                            if(snapshot.child(id).hasChild(currentUser)){
                                fvrtref.child(id).child(currentUser).removeValue();
                                delete(id);
                                fvrt_Checker = false;
                            }
                            else {
                                    examModel = new model();

                                    fvrtref.child(postkey).child(currentUser).setValue(true);
                                    examModel.setNAME(name);
                                    examModel.setTitle(title);
                                    examModel.setAge(age);
                                    examModel.setDates(date);
                                    examModel.setDesc(desc);
                                    examModel.setFee(fee);
                                    examModel.setId(id);

                                    String idd = fvrt_listRef.push().getKey();
                                    fvrt_listRef.child(idd).setValue(examModel);
                                    fvrt_Checker = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });



    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new myViewholder(view);

    }

    void delete(String id)
    {

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



    class myViewholder extends RecyclerView.ViewHolder
    {
        ImageButton fvrt_btn1;
        TextView name, title;
        model usermodel;

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference favouriteRef;

        public myViewholder(@NonNull View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.tv1);
            name = (TextView)itemView.findViewById(R.id.tv2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), exam_page_layout.class);
                    intent.putExtra("model", usermodel);
                    itemView.getContext().startActivity(intent);

                }
            });

        }

        public void bind(model mdl)
        {



            usermodel = mdl;
            name.setText(mdl.getNAME());
            title.setText(mdl.getTitle());
        }

        public void favouriteChecker(String postkey) {

            fvrt_btn1 = itemView.findViewById(R.id.item_fvrt);

            favouriteRef = firebaseDatabase.getReference("favourites");

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();

            favouriteRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(postkey).hasChild(uid)){
                        fvrt_btn1.setImageResource(R.drawable.ic_baseline_turned_in_24);
                    }
                    else
                        fvrt_btn1.setImageResource(R.drawable.ic_baseline_turned_in_not_24);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }




}
