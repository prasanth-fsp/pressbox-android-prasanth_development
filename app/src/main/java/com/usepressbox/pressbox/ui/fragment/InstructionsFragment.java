package com.usepressbox.pressbox.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.asyntasks.BackgroundTask;
import com.usepressbox.pressbox.utils.SessionManager;
import com.usepressbox.pressbox.utils.UtilityClass;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kruno on 22.04.16..
 * This fragment is used to update the instuctions when order is placed by user
 */
public class InstructionsFragment extends Fragment{

    private View v;
    private Toolbar toolbar;
    @BindView(R.id.toolbar_right) TextView next;
    @BindView(R.id.toolbar_title) TextView title;
    @BindView(R.id.et_order_instruction) EditText order_instruction;
    ArrayList<String> shoecarelist;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }
        UtilityClass.hideKeyboard(getActivity());
        v = inflater.inflate(R.layout.fragment_new_order_instructions, container, false);
        ButterKnife.bind(this, v);


        toolbar = (Toolbar) v.findViewById(R.id.tool_bar);
        Bundle args = getArguments();
        if (args  != null ) {

                    if (args.containsKey("shoecarelist"))

                        shoecarelist = args.getStringArrayList("shoecarelist");
        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().getBackStackEntryCount() > 0 ){
                    getFragmentManager().popBackStack();
                }else {
                    Fragment fragment = new ShoeOrderType();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

            }
        });

        setToolbarTitle();
        return v;
    }

    @OnClick(R.id.toolbar_right) void submitOrder() {
        Log.e("Place order","clicked");

        if (order_instruction.getText().toString().length()>0){
            if(shoecarelist !=null){
                String order_notes= getOrderNoteText(shoecarelist,order_instruction.getText().toString());
                SessionManager.ORDER.setOrderNotes(order_notes);
            }else {
                SessionManager.ORDER.setOrderNotes(order_instruction.getText().toString());
            }
        }else {
            SessionManager.ORDER.setOrderNotes("");
        }
        new BackgroundTask(getActivity(), SessionManager.ORDER.claimsCreate(getActivity()),"claims");
    }

    public void setToolbarTitle(){
        toolbar.setNavigationIcon(R.drawable.back);
        next.setText("Place Order");
        title.setText("Instructions");
    }
    private String getOrderNoteText(ArrayList<String> shoecarelist, String ordernotes){
        String shoecaretext = TextUtils.join(", ", shoecarelist);
        String shoecareformat = shoecaretext.toUpperCase();
        String order_Notes = shoecareformat +", "+ ordernotes ;
        return order_Notes;
    }

}
