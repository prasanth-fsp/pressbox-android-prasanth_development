package com.usepressbox.pressbox.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.adapter.TermsAndConditionAdapter;
import com.usepressbox.pressbox.models.TermsAndConditionModel;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by kruno on 11.05.16..
 *  This activity is used to show the terms and conditions for user
 */
public class TermsAndConditions extends AppCompatActivity {

    private Toolbar toolbar;
    private String[] header;
    private String[] description;
    @BindView(R.id.lw_terms_and_conditions) ListView lw_terms_and_conditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions_screen);
        ButterKnife.bind(this);

        setToolbarTitle();

        header = getResources().getStringArray(R.array.TermsHeader);
        description = getResources().getStringArray(R.array.Termsdescription);

        ArrayList<TermsAndConditionModel>termsAndConditionModelList = new ArrayList<>();

        for (int i = 0; i< header.length; i++){

            TermsAndConditionModel termsAndConditionModel = new TermsAndConditionModel();
            termsAndConditionModel.setHeader(header[i]);
            termsAndConditionModel.setDescription(description[i]);

            termsAndConditionModelList.add(termsAndConditionModel);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lw_terms_and_conditions.setAdapter(new TermsAndConditionAdapter(termsAndConditionModelList, this));
    }

    public void setToolbarTitle(){

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Terms and Conditions");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
