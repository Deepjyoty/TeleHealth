package com.gnrc.telehealth.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnrc.telehealth.FamilyHeadActivity;
import com.gnrc.telehealth.R;


public class SurveyFragment extends Fragment {

    CardView survey,wallet;
    View view;
    public SurveyFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SurveyFragment newInstance() {
        SurveyFragment fragment = new SurveyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // on below line we are initializing variables with ids.

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_survey, container, false);
        survey = view.findViewById(R.id.cardView_survey);
        wallet = view.findViewById(R.id.cardView_wallet);
        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FamilyHeadActivity.class);
                startActivity(intent);
            }
        });
        return view;


    }

}