package com.juancoob.nanodegree.and.vegginner.ui.comingSoon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juancoob.nanodegree.and.vegginner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This fragment shows a section in development
 *
 * Created by Juan Antonio Cobos Obrero on 12/08/18.
 */
public class ComingSoonFragment extends Fragment {

    @BindView(R.id.tv_coming_soon)
    public TextView comingSoonTextView;

    public ComingSoonFragment() {
        // Required empty public constructor
    }

    public static ComingSoonFragment getInstance() {
        return new ComingSoonFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coming_soon, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        comingSoonTextView.setText(Html.fromHtml(getString(R.string.coming_soon)));
        comingSoonTextView.setLinksClickable(true);
        comingSoonTextView.setAutoLinkMask(Linkify.WEB_URLS);
    }
}
