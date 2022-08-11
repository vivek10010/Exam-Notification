package com.example.examfeverr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class Dashboard extends AppCompatActivity {



    ImageButton dbMenu;
    RecyclerView recyclerView, h_recyclerView;
    LinearLayoutManager linearLayoutManager, hLinearLayoutManager;

    BottomSheetMenu bottomSheetMenu;

    //FirestoreRecyclerAdapter adapter;
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore db, db1;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference fvrtref, fvrt_listRef;

    Button search;
    RecyclerAdapter adapter;

    Boolean fvrt_checker = false;
    String currentUser;

    model examModel;

    ArrayList<HorizontalRecViewModel> datalist;
    RecyclerAdapter horizontalRecyclerAdapter;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomSheetMenu = new BottomSheetMenu();



        search = (Button) findViewById(R.id.search_et);
        dbMenu = findViewById(R.id.dashboard_menu);







        //progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        examModel = new model();

        datalist = new ArrayList<>();



        recyclerView = (RecyclerView) findViewById(R.id.recview);
        h_recyclerView = findViewById(R.id.horizontal_rv);

        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(null);

        hLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        h_recyclerView.setLayoutManager(hLinearLayoutManager);
        h_recyclerView.setItemAnimator(null);

        user = FirebaseAuth.getInstance().getCurrentUser();
        currentUser = user.getUid();

        fvrtref = firebaseDatabase.getReference("favourites");
        fvrt_listRef = firebaseDatabase.getReference("favouriteList").child(currentUser);




        //setOnClickListener();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, Search.class));
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Query query = db.collection("exams");


        db.collection("exams").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();

//                    Log.w(TAG, "listen:error", error);

                    return;
                }
/*
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        Log.d(TAG, "New city: " + dc.getDocument().getData());
                    }
                }


 */
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });





        Query query1 = db.collection("latest");


        FirestoreRecyclerOptions<model> options = new FirestoreRecyclerOptions.Builder<model>()
                .setQuery(query, model.class)
                .build();

        adapter = new RecyclerAdapter(options);
        recyclerView.setAdapter(adapter);



        horizontalRecyclerAdapter = new RecyclerAdapter(options);
        h_recyclerView.setAdapter(adapter);

        /*
        db1 = FirebaseFirestore.getInstance();
        db1.collection("latest").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list)
                        {
                            HorizontalRecViewModel obj = d.toObject(HorizontalRecViewModel.class);
                            datalist.add(obj);
                        }
                        horizontalRecyclerAdapter.notifyDataSetChanged();
                    }
                });


         */









        /*

        adapter = new FirestoreRecyclerAdapter<model, ViewHolder>(options) {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position, model model) {
            holder.bind(model);
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item, group, false);

                return new ViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);

         */


        //Bottom sheet menu

        dbMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetMenu.show(getSupportFragmentManager(), "bottomsheet");
            }
        });





    }




    /*private void setOnClickListener() {
        listener = new RecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(Dashboard.this, exam_page_layout.class);
                intent.putExtra("model", usermodel.getNAME());
                startActivity(intent);
            }
        };
    }

     */

    @Override
    protected void onStart() {
        super.onStart();




        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference;

        documentReference = firebaseFirestore.collection("users").document(currentUser);
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(!task.getResult().exists())
                        {
                            Intent intent = new Intent(Dashboard.this, phoneverification.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });



        adapter.startListening();
        horizontalRecyclerAdapter.startListening();

    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        horizontalRecyclerAdapter.stopListening();
    }

/*
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, link;
        model usermodel;

         public ViewHolder(@NonNull View itemView) {
             super(itemView);
             name = itemView.findViewById(R.id.tv1);
             link = itemView.findViewById(R.id.tv2);
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
             link.setText(mdl.getLink());
         }
     }

 */




/*
    // menu displaying code
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.logout)
        {firebaseAuth.signOut();
        startActivity(new Intent(Dashboard.this, phoneverification.class));
        finish();
        }

        if(item.getItemId() == R.id.profile)
        {
            startActivity(new Intent(Dashboard.this, Profile.class));
        }

        if(item.getItemId() == R.id.saved)
        {
            startActivity(new Intent(Dashboard.this, SavedItems.class));
        }
        return true;
    }

 */






}

