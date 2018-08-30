package com.usepressbox.pressbox.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.utils.UtilityClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;


/**
 * Created by kruno on 26.04.16..
 * This fragment is used as Introduction tip screen after registration
 */
public class IntroTipFragment extends Fragment {

    private View v;
    private Toolbar toolbar;
    @BindView(R.id.toolbar_left) TextView skip;
    @BindView(R.id.toolbar_right) TextView next;
    @BindView(R.id.toolbar_title) TextView title;
    @BindView(R.id.tw_finish_up) TextView finish;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UtilityClass.hideKeyboard(getActivity());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }


        v = inflater.inflate(R.layout.fragment_intro_locker, container, false);
        ButterKnife.bind(this, v);


//        InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);


        setToolbarTitle();
        return v;
    }
    @OnClick(R.id.tw_finish_up) void finishUp() {

        Fragment fragment = new IntroFinishFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void setToolbarTitle(){
        toolbar = (Toolbar) v.findViewById(R.id.tool_bar);
        skip.setText("");
        next.setText("");
        title.setText("");
    }
}
