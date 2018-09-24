package com.juancoob.nanodegree.and.vegginner.ui.beginning;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juancoob.nanodegree.and.vegginner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This is the main fragment which shows main information about veganism
 *
 * Created by Juan Antonio Cobos Obrero on 24/07/18.
 */
public class BeginningFragment extends Fragment {

    @BindView(R.id.tv_main_description)
    public TextView mainDescriptionTextView;

    public BeginningFragment() {
        // Required empty public constructor
    }

    public static BeginningFragment getInstance() {
        return new BeginningFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beginning, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainDescriptionTextView.setMovementMethod(new ScrollingMovementMethod());
    }
}
