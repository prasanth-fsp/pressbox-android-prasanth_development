package com.usepressbox.pressbox.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.models.Order;
import com.usepressbox.pressbox.ui.activity.order.Orders;
import com.usepressbox.pressbox.ui.activity.order.Video;
import com.usepressbox.pressbox.utils.Constants;
import com.usepressbox.pressbox.utils.SessionManager;
import com.usepressbox.pressbox.utils.UtilityClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



import android.widget.MediaController;
import android.widget.VideoView;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by kruno on 22.04.16..
 * This fragment is used to place new order for the existing user
 */
public class LockerFragment extends Fragment {

    private View v;
    private Toolbar toolbar;
    @BindView(R.id.toolbar_left) TextView skip;
    @BindView(R.id.toolbar_right) TextView next;
    @BindView(R.id.toolbar_title) TextView title;
    @BindView(R.id.tw_welcome) TextView welcome;
//    @BindView(R.id.et_locker_number) EditText lockerNumber;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }
        UtilityClass.hideKeyboard(getActivity());

        v = inflater.inflate(R.layout.fragment_new_order_locker, container, false);
        ButterKnife.bind(this, v);

        setToolbarTitle();
        return v;
    }

//    @OnClick(R.id.btn_new_order_next) void makeOrder() {
//
//        if (lockerNumber.getText().toString().length()>0){
//        if(SessionManager.ORDER ==null) SessionManager.ORDER= new Order();
//        SessionManager.ORDER.setLockerName(lockerNumber.getText().toString());
//        Fragment fragment = new SelectServices();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//
//        }else {
//            Toast.makeText(getActivity(), getResources().getString(R.string.toast_locker_number), Toast.LENGTH_SHORT).show();
//        }
//    }

    @OnClick(R.id.toolbar_left) void cancel() {
        Intent toOrder = new Intent(getActivity(), Orders.class);
        startActivity(toOrder);
        getActivity().finish();

    }

    public void setToolbarTitle(){
        toolbar = (Toolbar) v.findViewById(R.id.tool_bar);
        //ActionBar actionBar = getActivity().setSupportActionBar(toolbar);
        skip.setText("Cancel");
        next.setText("");
        title.setText("");
    }

    public static String getUrlVideoRTSP(String urlYoutube)
    {
        try
        {
            String gdy = "http://gdata.youtube.com/feeds/api/videos/";
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String id = extractYoutubeId(urlYoutube);
            URL url = new URL(gdy + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Document doc = documentBuilder.parse(connection.getInputStream());
            Element el = doc.getDocumentElement();
            NodeList list = el.getElementsByTagName("media:content");///media:content
            String cursor = urlYoutube;
            for (int i = 0; i < list.getLength(); i++)
            {
                Node node = list.item(i);
                if (node != null)
                {
                    NamedNodeMap nodeMap = node.getAttributes();
                    HashMap<String, String> maps = new HashMap<String, String>();
                    for (int j = 0; j < nodeMap.getLength(); j++)
                    {
                        Attr att = (Attr) nodeMap.item(j);
                        maps.put(att.getName(), att.getValue());
                    }
                    if (maps.containsKey("yt:format"))
                    {
                        String f = maps.get("yt:format");
                        if (maps.containsKey("url"))
                        {
                            cursor = maps.get("url");
                        }
                        if (f.equals("1"))
                            return cursor;
                    }
                }
            }
            return cursor;
        }
        catch (Exception ex)
        {

        }
        return urlYoutube;

    }



    protected static String extractYoutubeId(String url) throws MalformedURLException
    {
        String id = null;
        try
        {
            String query = new URL(url).getQuery();
            if (query != null)
            {
                String[] param = query.split("&");
                for (String row : param)
                {
                    String[] param1 = row.split("=");
                    if (param1[0].equals("v"))
                    {
                        id = param1[1];
                    }
                }
            }
            else
            {
                if (url.contains("embed"))
                {
                    id = url.substring(url.lastIndexOf("/") + 1);
                }
            }
        }
        catch (Exception ex)
        {
            Log.e("Exception", ex.toString());
        }
        return id;
    }
}
