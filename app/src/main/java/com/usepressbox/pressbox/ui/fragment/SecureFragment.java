package com.usepressbox.pressbox.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.ui.activity.order.Orders;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Prasanth.S on 8/2/2018.
 */
public class SecureFragment extends Fragment {

    @BindView(R.id.toolbar_left)
    TextView left;
    @BindView(R.id.toolbar_right)
    TextView next;
    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.secure_title)
    TextView secure_title;
    @BindView(R.id.secure_message)
    TextView secure_message;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @BindView(R.id.place_order_button)   Button place_order_button;

    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        view = inflater.inflate(R.layout.fragment_secure, container, false);
        ButterKnife.bind(this, view);

        setToolbarTitle();
        place_order_button.setText("Next");
        return view;
    }

    private void setToolbarTitle() {
        left.setText("Cancel");
        next.setVisibility(View.INVISIBLE);
        title.setText("New Order");
    }

    @OnClick(R.id.toolbar_left)
    void cancel() {
        Intent toOrder = new Intent(getActivity(), Orders.class);
        startActivity(toOrder);
        getActivity().finish();
    }

    @OnClick(R.id.place_order_button) void nextScreen() {
        FragmentTransaction article_fragmentTransaction = getFragmentManager().beginTransaction();
        article_fragmentTransaction.replace(R.id.fragment, new SelectServices());
        getFragmentManager().popBackStack();
        article_fragmentTransaction.commit();
    }




}
