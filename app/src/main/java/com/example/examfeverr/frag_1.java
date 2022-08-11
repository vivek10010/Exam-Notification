package com.example.examfeverr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.libRG.CustomTextView;




public class frag_1 extends Fragment {


    TextView dates,fee, ageLimit;
    String setFee, setDate, setDesc, setAge;
    CustomTextView desc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        model userm = (model) getActivity().getIntent().getSerializableExtra("model");


        View view = inflater.inflate(R.layout.fragment_frag_1, container, false);


        setFee = userm.getFee().replace("\\n", "\n");
        setDate = userm.getDates().replace("\\n", "\n");
        setDesc = userm.getDesc().replace("\\n", "n");
        setAge = userm.getAge().replace("\\n", "\n");


        dates = view.findViewById(R.id.tv_dates);
        fee = view.findViewById(R.id.tv_fee);
        desc = view.findViewById(R.id.tv_desc);
        ageLimit = view.findViewById(R.id.tv_ageLimit);



        dates.setText(setDate);
        fee.setText(setFee);
        desc.setText(setDesc);
        ageLimit.setText(setAge);

        return view;
    }
}