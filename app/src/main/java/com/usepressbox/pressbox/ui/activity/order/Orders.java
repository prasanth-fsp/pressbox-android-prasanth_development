package com.usepressbox.pressbox.ui.activity.order;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.usepressbox.pressbox.R;
import com.usepressbox.pressbox.adapter.OnboardPageAdapter;
import com.usepressbox.pressbox.adapter.OrdersAdapter;
import com.usepressbox.pressbox.asyntasks.BackgroundTask;
import com.usepressbox.pressbox.models.GetOrdersModel;
import com.usepressbox.pressbox.models.Order;
import com.usepressbox.pressbox.ui.MyAcccount;
import com.usepressbox.pressbox.ui.fragment.SelectServices;
import com.usepressbox.pressbox.utils.SessionManager;
import com.usepressbox.pressbox.utils.UtilityClass;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

/**
 * Created by kruno on 14.04.16..
 * Modified by Prasanth.S on 27/08/2018
 * This class is used to list the placed orders by the customer
 */
public class Orders extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    static Orders orders;
    private String from, code, percentage;
    public static ArrayList<GetOrdersModel> dataArray;//= new ArrayList<>();
    public static OrdersAdapter adapter;
    @BindView(R.id.lw_orders)
    ListView lw_orders;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipe_refresh_layout;
    @BindView(R.id.btn_contact_support)
    ImageView cntctSupport;
    @BindView(R.id.place_order_button)
    Button place_order_button;
    boolean doubleBackToExitPressedOnce = false;
    private ViewPager pager;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int selectedPageIndex = -1;
    private boolean exitWhenScrollNextPage = false;
    SessionManager sessionManager;
    private String From = "Default";
    private String FromScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_screen);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("From")) {
                from = extras.getString("From");
                showalert();
            } else if (extras.containsKey("percentage") && extras.containsKey("code")) {
                percentage = extras.getString("percentage");
                code = extras.getString("code");
                Log.e("orderpercent", percentage);
                Log.e("ordercod", code);
                if (!percentage.equalsIgnoreCase("null") && !code.equalsIgnoreCase("null") && !percentage.equalsIgnoreCase("") && !code.equalsIgnoreCase("")) {
                    UtilityClass.showNotificationAlert(Orders.this, percentage, code);
                }

            }

        } else {

        }
        ButterKnife.bind(this);
        place_order_button.setText("Place a New Order");

        //reference to activity
        setToolbarTitle();
        orders = this;


        sessionManager = new SessionManager(Orders.this);
        if (!sessionManager.getOnboardStatus()) {
            showOnBoardDialog();
        }


        dataArray = new ArrayList<>();

        adapter = new OrdersAdapter(dataArray, this);

        //Added by Prasanth.S
        View view = getLayoutInflater().inflate(R.layout.order_empty_view, null);
        ViewGroup viewGroup = (ViewGroup) lw_orders.getParent();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 50, 0, 0);
        view.setLayoutParams(params);
        viewGroup.addView(view);

        lw_orders.setEmptyView(view);
        lw_orders.setAdapter(adapter);


        //reference to listview
        lw_orders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LinearLayout lockerId = (LinearLayout) view.findViewById(R.id.linear_layout_locker_id);

                if (lockerId.getVisibility() == View.VISIBLE) {

                    lockerId.setVisibility(View.INVISIBLE);
                    OrdersAdapter.selectedPosition = -1;

                } else {
                    lockerId.setVisibility(View.VISIBLE);
                    OrdersAdapter.selectedPosition = position;
                }

                adapter.notifyDataSetChanged();
            }
        });

        lw_orders.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (lw_orders != null && lw_orders.getChildCount() > 0) {
                    boolean firstItemVisible = lw_orders.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = lw_orders.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipe_refresh_layout.setEnabled(enable);
            }
        });

        swipe_refresh_layout.setOnRefreshListener(this);

        if (SessionManager.ORDER == null) SessionManager.ORDER = new Order();
        new BackgroundTask(this, SessionManager.ORDER.getClaims(this), lw_orders, swipe_refresh_layout, adapter, dataArray);

    }


    public void replaceFragment(android.support.v4.app.Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment, fragment);
        transaction.commit();
    }

    private void showOnBoardDialog() {
        final Dialog dialog = new Dialog(Orders.this, R.style.Empty_dialog_theme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialogbg));
        dialog.setContentView(R.layout.onboard_dialog);

        /*Setting width and height programmatically*/
        Rect displayRectangle = new Rect();
        Window window = Orders.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() * 0.8f), (int) (displayRectangle.height() * 0.75f));
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, (int)(displayRectangle.height() * 0.8f));

        dialog.setCanceledOnTouchOutside(false);

        OnboardPageAdapter adapters = new OnboardPageAdapter(Orders.this, dialog);
        pager = (ViewPager) dialog.findViewById(R.id.viewpager);
        dotsLayout = (LinearLayout) dialog.findViewById(R.id.layoutDots);
        LinearLayout close_button = (LinearLayout) dialog.findViewById(R.id.close_button);
        // adding bottom dots
        addBottomDots(0);

        pager.setAdapter(adapters);
        pager.addOnPageChangeListener(viewPagerPageChangeListener);

        close_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (dialog != null) {
                    dialog.cancel();
                    dialog.dismiss();
                    sessionManager.SetOnboard(true);
                }
                return false;
            }
        });
        dialog.show();


    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onPageSelected(int position) {
            selectedPageIndex = position;
            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {
            if (exitWhenScrollNextPage && position == pager.getAdapter().getCount() - 1) {
                exitWhenScrollNextPage = false; // avoid call more times
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == SCROLL_STATE_IDLE) {
                exitWhenScrollNextPage = selectedPageIndex == pager.getAdapter().getCount() - 1;
            }
        }
    };

    /*Created by Prasanth.S on 24/08/2018*/
    private void addBottomDots(int currentPage) {
        dots = new TextView[3];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20, 0, 20, 0);
            dots[i].setLayoutParams(params);

            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    public void setToolbarTitle() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMyAccount = new Intent(Orders.this, MyAcccount.class);
                startActivity(toMyAccount);
                finish();
            }
        });
        ImageView toolbar_image = (ImageView) toolbar.findViewById(R.id.toolbar_image);
        toolbar_image.setVisibility(View.VISIBLE);

        int offset = (toolbar.getWidth() / 2) - (toolbar_image.getWidth() / 2);
        // set
        toolbar_image.setX(offset);

        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setVisibility(View.GONE);

        TextView toolbar_right = (TextView) toolbar.findViewById(R.id.toolbar_right);
        toolbar_right.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @OnClick(R.id.place_order_button)
    void newOrder() {
        Intent newOrder = new Intent(Orders.this, NewOrder.class);
        newOrder.putExtra("From", "Orders");
        startActivity(newOrder);
        finish();
    }

    public static Orders getInstance() {
        return orders;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_mail) {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@usepressbox.com"});
            startActivity(Intent.createChooser(intent, ""));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        Log.e("Back", "Orders");

        if (doubleBackToExitPressedOnce) {

            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, Orders.this.getResources().getString(R.string.exit_msg), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        swipe_refresh_layout.setRefreshing(true);
        if (SessionManager.ORDER == null) SessionManager.ORDER = new Order();
        new BackgroundTask(this, SessionManager.ORDER.getClaims(this), lw_orders, swipe_refresh_layout, adapter, dataArray);
    }

    public void showalert() {
        if (from.equals("IntroFinishFragment") || from.equals("claims")) {
           /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setMessage(getResources().getString(R.string.message)).setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();


                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();*/

            UtilityClass.showAlertWithOk(this, "Thanks for your Order!", "Details will be sent to your email shortly.","confirm-order");


        } else if (from.equals("nolocker")) {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                String sourceString = b.getString("Data");
                Log.e("data", sourceString);


                final TextView tx1 = new TextView(this);

                tx1.setGravity(Gravity.CENTER_HORIZONTAL);
                tx1.setPadding(0, 25, 0, 0);
                tx1.setAutoLinkMask(RESULT_OK);
                tx1.setMovementMethod(LinkMovementMethod.getInstance());

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                    }
                                })
                        .setView(tx1);

                final AlertDialog dialog = builder.create();
                sourceString = sourceString + "\n Contact Support";
                String keyWord = "Contact Support";

                Log.e("sourceString", sourceString);
                SpannableString spannableString = new SpannableString(sourceString);


                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("plain/text");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@usepressbox.com"});
                        startActivity(Intent.createChooser(intent, ""));
                        dialog.dismiss();
                    }
                };

                spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 0, tx1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(clickableSpan, sourceString.indexOf(keyWord), sourceString.indexOf(keyWord) + keyWord.length(), 0);

                spannableString.setSpan(new NonUnderlinedClickableSpan(keyWord), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                tx1.setText(spannableString);

                dialog.show();
            }
        } else {

        }
    }

    public class NonUnderlinedClickableSpan extends ClickableSpan {

        String clicked;

        public NonUnderlinedClickableSpan(String string) {
            super();
            clicked = string;
        }

        public void onClick(View tv) {

        }

        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.BLACK);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
