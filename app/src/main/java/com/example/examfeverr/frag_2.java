package com.example.examfeverr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class frag_2 extends Fragment {

    TextView link, officialSite, apply, notification;

    String  link_os, link_apply, link_nf;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        model userm = (model) getActivity().getIntent().getSerializableExtra("model");

        View view = inflater.inflate(R.layout.fragment_frag_2, container, false);


        link = view.findViewById(R.id.tv_link);
        officialSite = view.findViewById(R.id.tv_officialWebsiteLink);
        apply = view.findViewById(R.id.tv_apply);
        notification = view.findViewById(R.id.tv_notification);

        link_os = userm.getLink1();
        link_nf = userm.getLink2();
        link_apply = userm.getLink3();

        if(link_os ==null)
        {
            link_os = "N/A";
            officialSite.setClickable(false);
        }
        if(link_nf ==null)
        {
            link_nf = "N/A";
            notification.setClickable(false);
        }

        if(link_apply ==null)
        {
            link_apply = "N/A";
            apply.setClickable(false);
        }

        officialSite.setText(link_os);
        notification.setText(link_nf);
        apply.setText(link_apply);





        link.setText(userm.getTitle());
        link.setMovementMethod(LinkMovementMethod.getInstance());

        officialSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String url   = link_os;
            if(url == "N/A")
                url = "https://www.google.com";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url   = link_apply;
                if(url == "N/A")
                    url = "https://www.google.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url   = link_nf;
                if(url == "N/A")
                    url = "https://www.google.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        // Inflate the layout for this fragment
        return view;


    }
}