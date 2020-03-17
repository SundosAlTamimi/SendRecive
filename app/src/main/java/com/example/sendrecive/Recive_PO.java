package com.example.sendrecive;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.sendrecive.Models.ItemInfo;
import com.example.sendrecive.Models.ReciveDetail;
import com.example.sendrecive.Models.ReciveMaster;
import com.example.sendrecive.Models.Setting;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Recive_PO extends AppCompatActivity {
    EditText  transaction_no, item_no, recived_qty, voucher_no;
    TextView supplier_name, item_name, qty_required, free_qty, total_category_qty, reciver_category_qty,date;
    ScrollView scroll_table;
    public static DataBaseHandler dataBaseHandler;
    ImageView calenderdialog_image;
    SQLiteDatabase database;
    public static List<ReciveMaster> reciveListMaster = new ArrayList<>();
    public static List<ItemInfo> itemInfoList = new ArrayList<>();
    double pricevalue = 0.0;
    Context context;
    String itemNo_text, transNo_text, reciverQty_text, dateExpire_text, reciverCategory_text, voucherNo_text;
    FloatingActionButton addQty;
    private TableLayout tableCheckData;
    double qty_total = 0, bonusQty = 0, qty = 0;
    Calendar myCalendar;
    String today;
    public static int position = 1;
    public static boolean continueAdd = false;
    Button save, cancel;
    public static List<ReciveDetail> reciveDetailList=new ArrayList<>();
    JSONObject jsonObjectMASTER=null;
    JSONObject jsonObjectDetail = null;
    JSONObject finalObject=null;
    JSONArray j;
    int maxSerial = 0,counter = 0,codeState=-1,currentYear=0;
    public  static  String ipAddres="";
    boolean notFountItem=false;
    String description="";
    AlphaAnimation buttonClick;
    Animation animation;
    EditText day_editText,month_editText,year_editText;
    Button saveButton;
    Dialog dialog;
    String day="",month="",year="";
    int intday=0,intmonth=0,intyeay=0;
    boolean dayCorrect=false,monthCorrect=false,yearCorrect=false,leepYear=false,monthTow=false;
    String fullDate="";
    LinearLayout linearDate;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recive__po);
        context = getApplicationContext();
        buttonClick = new AlphaAnimation(1F, 0.2F);
        animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in);

        dataBaseHandler = new DataBaseHandler(this);
        database = dataBaseHandler.getWritableDatabase();
        ipAddres=dataBaseHandler.getIP();
        Log.e("ipAddres-PO",""+ipAddres);
        initView();
        transaction_no.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:

                            getData(transaction_no.getText().toString());

                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        date.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            linearDate.setBackground(getResources().getDrawable(R.drawable.edittext_white_border));

                            recived_qty.requestFocus();

                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        recived_qty.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            boolean result = chechQtyCategory();
                            continueAdd = false;
                            if (result) {
                                addItemTotable();
                            }

                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        item_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                itemNo_text = item_no.getText().toString().trim();
                transNo_text = transaction_no.getText().toString().trim();
                if ((!TextUtils.isEmpty(itemNo_text)) && (!TextUtils.isEmpty(transNo_text))) {
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        switch (i) {
                            case KeyEvent.KEYCODE_DPAD_CENTER:
                            case KeyEvent.KEYCODE_ENTER:
                                if ((!item_no.getText().equals("")) && (!transaction_no.getText().equals(""))) {
                                    itemNo_text = item_no.getText().toString();
                                    transNo_text = transaction_no.getText().toString();
                                    getItemInfo(transNo_text, itemNo_text);
                                            // getItemInfo("1001", "6251088000206");


                                }
                                return true;
                            default:
                                break;
                        }
                    }
                } else {
                    if (TextUtils.isEmpty(itemNo_text))
                        item_no.setError("Required");

                    if (TextUtils.isEmpty(transNo_text))
                        transaction_no.setError("Required");
                }

                return false;
            }
        });
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        today = df.format(currentTimeAndDate);
        date.setText(convertToEnglish(today));
       date.setOnClickListener(onClickListener);


    }

    private void displayErrorDialog(String mesage) {
        notFountItem=true;

      new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
               .setTitleText("عذرا ...")
                .setContentText(mesage+"!").hideConfirmButton()
                .show();

    }
    private void updateGRN() {
        reciveListMaster.get(0).setVHFNO(maxSerial+"");
        for(int i=0;i<reciveDetailList.size();i++)
        {
            reciveDetailList.get(i).setVHFNO_DETAIL(maxSerial+"");
        }

    }


    private void setItemInfo(ItemInfo reciveMaster) {
        item_name.setText(reciveMaster.getDELIVERY_ItemName());
        qty_required.setText(reciveMaster.getQty());
        free_qty.setText(reciveMaster.getBonus());
        date.requestFocus();
        linearDate.setBackground(getResources().getDrawable(R.drawable.edite_fucasable));
       // recived_qty.requestFocus();

    }

    private void updateTotalqty() {
        reciver_category_qty.setText(counter + "");
        Log.e("countItems", "" + counter);
    }

    private void getItemInfo(String transNo_text, String itemNo_text) {
//        final String url = "http://10.0.0.22:8081/GetOrderInfo?ORDERNO="+transNo_text;
        final String url = "http://"+ipAddres+"/GetOrderItems?ORDERNO=" + transNo_text + "&ITEMOCODE=" + itemNo_text;

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            Log.e("onResponse: ", "" + jsonArray.getString(0));

                            itemInfoList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject itemInfo = jsonArray.getJSONObject(i);
                                ItemInfo reciveMaster = new ItemInfo();

                                reciveMaster.setDELIVERY_ItemName(itemInfo.getString("DELIVERYTERM"));
                                reciveMaster.setQty(itemInfo.getString("Qty"));
                                reciveMaster.setBonus(itemInfo.getString("Bonus"));
                                reciveMaster.setPriceL((itemInfo.getString("PriceL")));
                                reciveMaster.setNetTL(itemInfo.getString("NetTL"));
                                reciveMaster.setTaxLV((itemInfo.getString("TaxLV")));
                                reciveMaster.setDiscLV(itemInfo.getString("DiscLV"));//test
                                reciveMaster.setPriceL(itemInfo.getString("PriceL"));
                                pricevalue = reciveMaster.getPriceL();
                                setItemInfo(reciveMaster);
                                itemInfoList.add(reciveMaster);
//                                addToDB();
                            }
                        } catch (Exception e) {
                            Log.e("Exception", "" + e.getMessage());
                        }
                    }


                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.getCause() instanceof JSONException)
                {
                    displayErrorDialog("رقم المادة غير موجود ");
                    item_no.setSelection(item_no.getText().length() , 0);

                }
                else
                if ((error instanceof TimeoutError) || (error instanceof NoConnectionError)) {
                    Toast.makeText(context,
                            "تأكد من اتصال الانترنت",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    Toast.makeText(context,
                            "تأكد من اتصال الانترنت",
                            Toast.LENGTH_SHORT).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context,
                            "تأكد من اتصال الانترنت",
                            Toast.LENGTH_SHORT).show();
                    //TODO
                } else if (error instanceof ParseError) {
                    if(error.equals("EXPORTED")){

                    }
//                    displayErrorDialog("رقم المورد  خاطئ");
                    //TODO
                }

                Log.e("onErrorResponse: ", "" + error.getMessage());
                 notFountItem = true;
//                displayErrorDialog("رقم المادةغير موجود");

//

            }

        });
        MySingeltone.getmInstance(Recive_PO.this).addToRequestQueue(stringRequest);

    }

    private void getData(final String transNo) {
//       String URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php";
        final String url = "http://"+ipAddres+"/GetOrderInfo?ORDERNO=" + transNo;

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            String s = "";
                            Log.e("onResponse: ", "" + jsonArray.getString(0));
                            Log.e("onResponse2: ", "" + jsonArray.getJSONObject(0).getString("CoYear"));
                            reciveListMaster.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject transactioRecive = jsonArray.getJSONObject(i);
                                ReciveMaster reciveMaster = new ReciveMaster();
                                reciveMaster.setORDERNO(transNo);
                                reciveMaster.setVENDOR_VHFDATE(today);
                                reciveMaster.setSUBTOTAL(transactioRecive.getString("TotalL"));
                                reciveMaster.setTAX(Double.parseDouble(transactioRecive.getString("Tax")));
                                reciveMaster.setNETTOTAL(transactioRecive.getString("NetTotL"));
                                reciveMaster.setIS_POSTED("0");
                                reciveMaster.setAccName(transactioRecive.getString("AccName"));
                                reciveMaster.setCOUNTX(transactioRecive.getString("COUNTX"));
                                reciveMaster.setDISC(Double.parseDouble(transactioRecive.getString("Disc_1")));
                                reciveMaster.setTAXKIND("0");//test
                                reciveListMaster.add(reciveMaster);
                                supplier_name.setText(reciveMaster.getAccName());
                                total_category_qty.setText(reciveMaster.getCOUNTX());
                                item_no.requestFocus();
                            }
                        } catch (Exception e) {
                            Log.e("Exception", "" + e.getMessage());
                        }
                    }


                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                notFountItem = true;
//                displayErrorDialog("الرقم خاطئ");
                Log.e("onErrorResponse: ", "" + error);
                if(error.getCause() instanceof JSONException)
                {
                    if (error.getMessage().contains("6")){
                        displayErrorDialog("تم ترحيل المادة");
                    }
                    if (error.getMessage().contains("3")){
                        displayErrorDialog("الرقم خاطئ");
                    }

//                    displayErrorDialog("رقم المورد  خاطئ");
                }
                else
                if ((error instanceof TimeoutError) || (error instanceof NoConnectionError)) {
                    Toast.makeText(context,
                            "تأكد من اتصال الانترنت",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    Toast.makeText(context,
                            "تأكد من اتصال الانترنت",
                            Toast.LENGTH_SHORT).show();
                    //TODO
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context,
                            "تأكد من اتصال الانترنت",
                            Toast.LENGTH_SHORT).show();
                    //TODO
                } else if (error instanceof ParseError) {

                    //TODO
                }
                Log.e("onErrorResponse: ", "" + error);

            }

        });
        MySingeltone.getmInstance(Recive_PO.this).addToRequestQueue(stringRequest);


    }

    void addMasterToDB() {

        for (int i = 0; i < reciveListMaster.size(); i++) {
            dataBaseHandler.add_RECIVE_MASTER(reciveListMaster.get(i));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        date = (TextView) findViewById(R.id.date_expire);
        calenderdialog_image=findViewById(R.id.calenderdialog_image);
        calenderdialog_image.setOnClickListener(onClickListener);
        transaction_no = (EditText) findViewById(R.id.transaction_no);
        item_no = (EditText) findViewById(R.id.item_no);
        recived_qty = (EditText) findViewById(R.id.recived_qty);
        reciver_category_qty = (TextView) findViewById(R.id.reciver_category_qty);
        voucher_no = (EditText) findViewById(R.id.voucher_no);
        supplier_name = (TextView) findViewById(R.id.supplier_name);
        item_name = (TextView) findViewById(R.id.item_name);
        qty_required = (TextView) findViewById(R.id.qty_required);
        free_qty = (TextView) findViewById(R.id.free_qty);
        total_category_qty = (TextView) findViewById(R.id.total_category_qty);
        addQty = (FloatingActionButton) findViewById(R.id.add_qty);
        addQty.setOnClickListener(onClickListener);
        tableCheckData = (TableLayout) findViewById(R.id.tableData);
        linearDate = (LinearLayout) findViewById(R.id.linearDate);
//        scroll_table=(ScrollView) findViewById(R.id.scroll_table);

        tableCheckData.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        myCalendar = Calendar.getInstance();
//        row=new TableRow(Recive_PO.this);
        save = findViewById(R.id.save);
        save.setOnClickListener(onClickListener);
        cancel = findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(onClickListener);
        reciveDetailList = new ArrayList<>();

    }
    private void askForPrint() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(Recive_PO.this);
////        builder.setTitle("طباعة الفاتورة");
////        builder.setCancelable(false);
////        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialogInterface, int i) {
////                printLastVoucher();
////                clearAllData();
////
////            }
////        });
////
////
////        builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialogInterface, int i) {
////                dialogInterface.dismiss();
////                clearAllData();
////                clearLists();
////            }
////        });
////        builder.setMessage("هل تريد الطباعة ؟");
////        AlertDialog alertDialog = builder.create();
////        alertDialog.show();
////

        new SweetAlertDialog(Recive_PO.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("طباعة الفاتورة")
                .setContentText("هل تريد الطباعة ؟")
                .setConfirmText("نعم!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        printLastVoucher();
                        clearAllData();
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelButton("لا", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        clearAllData();
                        clearLists();
                    }
                })
                .show();


    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.add_qty:
                    view.startAnimation(animation);
                    addItemTotable();

                    break;
                case R.id.date_expire:

                    new DatePickerDialog(Recive_PO.this, openDatePickerDialog(date), myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    break;
                case R.id.save:
                    view.startAnimation(buttonClick);
                    if (tableCheckData.getChildCount() > 0)
                    {
                        voucherNo_text = voucher_no.getText().toString().trim();
                        if (TextUtils.isEmpty(voucherNo_text)) {
                            voucher_no.setError("Required");
                            voucher_no.requestFocus();
                        } else {
                            setVoucherNoToDetailList(voucherNo_text);
                            reciveListMaster.get(0).setVENDOR_VHFNO(voucherNo_text);
                            saveDataSendtoURL();
                            addMasterToDB();
                            addDetailToDB();
                        }

                    }

                    else {
                        Toast.makeText(context, "املى  بيانات الجدول ", Toast.LENGTH_SHORT).show();
                    }



                    break;
                case R.id.cancel_btn:
                    view.startAnimation(buttonClick);

                    new SweetAlertDialog(Recive_PO.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("تأكيد")
                            .setContentText("هل تريد الخروج  ؟")
                            .setConfirmText("نعم")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    Intent intent = new Intent(Recive_PO.this, MainActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .setCancelButton("لا", null)
                            .show();

                    break;
                case R.id.calenderdialog_image:
                    dialogEditDate();

                    break;

            }
        }
    };


    private void dialogEditDate() {

        dialog = new Dialog(Recive_PO.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.edit_date_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);
         saveButton = dialog.findViewById(R.id.save);
        final Button cancelButton = dialog.findViewById(R.id.cancel_btn);

        day_editText = dialog.findViewById(R.id.day_editText);
        month_editText = dialog.findViewById(R.id.month_editText);
        year_editText = dialog.findViewById(R.id.year_editText);
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        today = df.format(currentTimeAndDate);
        day_editText.setText(convertToEnglish(today.substring(0,2)));
        month_editText.setText(convertToEnglish(today.substring(3,5)));
        year_editText.setText(convertToEnglish(today.substring(6,today.length())));

        day_editText.setOnClickListener(onclickListenerDate);
        month_editText.setOnClickListener(onclickListenerDate);
        year_editText.setOnClickListener(onclickListenerDate);
        saveButton.setOnClickListener(onclickListenerDate);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    View.OnClickListener onclickListenerDate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.day_editText:
                    if(!day_editText.getText().equals(""))
                    {
                       day_editText.setSelection(day_editText.getText().length(),0);
                    }
                    break;
                case R.id.month_editText:
                    if(!month_editText.getText().equals(""))
                    {
                        month_editText.setSelection(month_editText.getText().length(),0);
                    }
                    break;
                case R.id.year_editText:
                    if(!year_editText.getText().equals(""))
                    {
                        year_editText.setSelection(year_editText.getText().length(),0);
                    }
                    break;
                case R.id.save:
                    day=day_editText.getText().toString();
                    month=month_editText.getText().toString();
                    year=year_editText.getText().toString();
                    checkdataDateDialog();
                    break;



            }
        }
    };

    private void checkdataDateDialog() {
       currentYear=Integer.parseInt(today.substring(6,today.length()));
        dayCorrect=false;monthCorrect=false;yearCorrect=false;leepYear=false;monthTow=false;

        if ((!TextUtils.isEmpty(day)) && (!TextUtils.isEmpty(month))&& (!TextUtils.isEmpty(year)))
        {
            chechYear();
            checkMonth();
            checkDay();




            if(  dayCorrect && monthCorrect && yearCorrect)
            {
                date.setText(day+"/"+month+"/"+year);
                recived_qty.requestFocus();
                dialog.dismiss();

            }
            else{
                Log.e("date.setText",""+ dayCorrect +""+ monthCorrect +""+ yearCorrect);

            }

        }
        else{
            if(TextUtils.isEmpty(day))
            {
                day_editText.setError("Required");
            }
            if(TextUtils.isEmpty(month))
            {
                month_editText.setError("Required");
            }
            if(TextUtils.isEmpty(year))
            {
                year_editText.setError("Required");
            }

        }


    }

    private void checkDay() {
        if(day.length()<=2)
        {
            intday=Integer.parseInt(day);

            if(leepYear)
            {
                Log.e("towwwwwwwwwww",""+monthTow);
                if(monthTow)
                {
                    if(intday>0&&intday<=29)
                    {
                        dayCorrect=true;
                        fullDate+=day+"/";
                    }
                    else{
                        day_editText.setError("Invalid Number of day ");
                    }
                }
                else{// all month without 2
                    if(intday>0&&intday<=31) {
                        dayCorrect=true;
                        fullDate+=day+"/";
                    }
                    else{
                        day_editText.setError("Invalid Number of day ");
                    }

                }

            }else{
                Log.e("towwwwwwwwwww",""+monthTow);

                if(monthTow)
                {
                    if(intday>0&&intday<=28)
                    {
                        dayCorrect=true;
                        fullDate+=day+"/";
                    }
                    else{
                        day_editText.setError("Invalid Number of day ");
                    }
                }
                else{// all month without 2
                    if(intday>0&&intday<=31) {
                        dayCorrect=true;
                        fullDate+=day+"/";
                    }
                    else{
                        day_editText.setError("Invalid Number of day ");
                    }

                }

            }

        }


        else {
            day_editText.setError("Only tow Digit");

        }
    }

    private void checkMonth() {
        if(month.length()<=2)
        {
            intmonth=Integer.parseInt(month);
            if(intmonth>0&&intmonth<=12)
            {
                monthCorrect=true;
                fullDate+=month+"/";
                Log.e("towwwwwwwwwww",""+intmonth);
                if(intmonth==2)
                {
                    monthTow=true;
                    Log.e("towwwwwwwwwww",""+monthTow);
                }

            }
            else {
                month_editText.setError("Invalid Number of Month ");
            }


        }

        else {
            month_editText.setError("Only tow Digit");


        }
    }

    private void chechYear() {
        if(year.length()<=4)
        {    intyeay=Integer.parseInt(year);
            if(intyeay>=currentYear)
            {
                GregorianCalendar cal = new GregorianCalendar();

                if(cal.isLeapYear(intyeay))
                {
                    leepYear=true;

                }
                else
                {
                    leepYear=false;

                }


                yearCorrect=true;
                fullDate+=year;
            }
            else {
                year_editText.setError("Invalid Number of year ");
            }




        }

        else {

            year_editText.setError("Only four Digit");
        }
    }

    private void addDetailToDB() {
        for (int i = 0; i < reciveDetailList.size(); i++) {
            dataBaseHandler.add_RECIVE_DETAIL(reciveDetailList.get(i));

        }
    }

    private void addItemTotable() {
        if (checkFildesRequired()) {

            final TableRow row = new TableRow(Recive_PO.this);
            qty_total = calckQty();
            row.setPadding(1, 7, 1, 5);
            row.setTag(position);
            for (int i = 0; i < 3; i++) {

                String[] record = {item_name.getText().toString(), qty_total + "",
                        free_qty.getText().toString()};

                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                TextView textView = new TextView(Recive_PO.this);
                textView.setHint(record[i]);
                textView.setTextColor(ContextCompat.getColor(Recive_PO.this, R.color.darkblue_));
                textView.setHintTextColor(ContextCompat.getColor(Recive_PO.this, R.color.darkblue_));
                if (i == 0) {
                    textView.setGravity(Gravity.CENTER);
                } else {
                    textView.setGravity(Gravity.CENTER);
                }
                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                textView.setLayoutParams(lp2);
                row.addView(textView);
            }
            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    row.setBackgroundColor(getResources().getColor(R.color.layer4));
                    new SweetAlertDialog(Recive_PO.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("تحذير")
                            .setContentText("هل تريد الحذف بالتأكيد ؟")
                            .setConfirmText("نعم")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    int tag = Integer.parseInt(row.getTag().toString());
                                    tableCheckData.removeView(row);
                                    reciveDetailList.remove(tag - 1);
                                    position--;
                                    counter--;
                                    updateTotalqty();
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .setCancelButton("لا", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();


                    return true;
                }
            });//
            adddataToList(row.getTag().toString());
            tableCheckData.addView(row);
            position++;
            counter = tableCheckData.getChildCount();
            updateTotalqty();
            item_no.requestFocus();

            clearData();
        } else {
            recived_qty.requestFocus();

        }
    }

    private void setVoucherNoToDetailList(String voucherNo_text) {
        for(int i=0;i<reciveDetailList.size();i++)
        {
            reciveDetailList.get(i).setVHFNO_DETAIL(maxSerial+"");
        }
    }


    private boolean chechQtyCategory() {
        int requiredQty = 0;
        int freeQty = 0;
        int reciverQty = 0;
        int samQty=0;
        Setting mySetting=new Setting();



        try {
            mySetting=dataBaseHandler.getSetting();
            samQty=Integer.parseInt(mySetting.getSAME_QUANTITY());
            requiredQty = Integer.parseInt(qty_required.getText().toString());
            freeQty = Integer.parseInt(free_qty.getText().toString());
            reciverQty = Integer.parseInt(recived_qty.getText().toString());
        } catch (Exception e) {
            Log.e("chechQtyCategory", "" + e.getMessage());
            return true;

        }
        if(samQty==1)
        {
            if(reciverQty != (freeQty + requiredQty))
            {
                new SweetAlertDialog(Recive_PO.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("تحذير")
                        .setContentText("يجب ان يكون عدد المواد المستلمة مساوي لاجمالي المواد")
                        .setConfirmText("نعم").hideConfirmButton()
                        .show();






                recived_qty.setText("");
                return  false;

            }
            else {
                return true;
            }


        }
        else {

            if (reciverQty > freeQty + requiredQty) {

//
//                AlertDialog.Builder builder = new AlertDialog.Builder(Recive_PO.this);
//                builder.setTitle("تحذير");
//                builder.setCancelable(false);
//                builder.setPositiveButton("استمرار", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        continueAdd = true;
//                        voucher_no.requestFocus();
//                    }
//                });
//
//
//                builder.setNegativeButton("تراجع", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        continueAdd = false;
//                        recived_qty.setText("");
//
//                    }
//                });
//                builder.setMessage("عدد المواد المستلمة اكبر من  اجمالي المواد");
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
                //////////////////////////////
                new SweetAlertDialog(Recive_PO.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("تحذير")
                        .setContentText("عدد المواد المستلمة اكبر من  اجمالي المواد")
                        .setConfirmText("استمرار")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                continueAdd = true;
                                voucher_no.requestFocus();
                                sDialog.dismissWithAnimation();

                            }
                        })
                        .setCancelButton("تراجع", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                continueAdd = false;
                                recived_qty.setText("");
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();

                return continueAdd;
            } else {
                return true;
            }
        }


    }

    private void adddataToList(String rowNo) {
        ReciveDetail data = new ReciveDetail();
        data.setORDERNUMBER(transaction_no.getText().toString());
        data.setVSERIAL(rowNo);
        data.setITEMOCODE(item_no.getText().toString());
        data.setORDER_QTY(qty_required.getText().toString());
        data.setORDER_BONUS(free_qty.getText().toString());
        data.setBONUS(  itemInfoList.get(0).getBonus());
        data.setTAXDETAIL(  itemInfoList.get(0).getTaxLV());
        data.setTOTAL( itemInfoList.get(0).getNetTL());
        data.setDISCL(  itemInfoList.get(0).getDiscLV());
        data.setRECEIVED_QTY(calckQty() + "");
        data.setPRICE(  itemInfoList.get(0).getPriceL().toString());//test
        data.setINDATE(today);
        data.setEXPDATE(date.getText().toString());
        data.setITEM_NAME(itemInfoList.get(0).getDELIVERY_ItemName());

        reciveDetailList.add(data);
        Log.e("listSize", "" + reciveDetailList.size() + "/t" + data.getRECEIVED_QTY());
    }

    private void saveDataSendtoURL() {
        getDataJSON();
        new JSONTask().execute();

    }

    private void getDataJSON() {
         jsonObjectMASTER = new JSONObject();
        try {
            jsonObjectMASTER.put("ORDERNO", reciveListMaster.get(0).getORDERNO());
            jsonObjectMASTER.put("VHFNO","0");
            Log.e("getDataJSON",""+maxSerial);
            jsonObjectMASTER.put("VHFDATE",  convertToEnglish(date.getText().toString()));
            jsonObjectMASTER.put("VENDOR_VHFNO", reciveListMaster.get(0).getVENDOR_VHFNO());
            jsonObjectMASTER.put("VENDOR_VHFDATE",convertToEnglish( reciveListMaster.get(0).getVENDOR_VHFDATE()));
            jsonObjectMASTER.put("SUBTOTAL", reciveListMaster.get(0).getSUBTOTAL());
            jsonObjectMASTER.put("TAX", reciveListMaster.get(0).getTAX()+"");
            jsonObjectMASTER.put("NETTOTAL", reciveListMaster.get(0).getNETTOTAL());
            jsonObjectMASTER.put("IS_POSTED", reciveListMaster.get(0).getIS_POSTED()+"");
            jsonObjectMASTER.put("TAXKIND", reciveListMaster.get(0).getTAXKIND());
            jsonObjectMASTER.put("DISC", reciveListMaster.get(0).getDISC());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        jsonObjectDetail = null;
        j = new JSONArray();
        for (int i = 0; i < reciveDetailList.size(); i++) {
            jsonObjectDetail = new JSONObject();
            try {
                jsonObjectDetail.put("ORDERNO", reciveDetailList.get(i).getORDERNUMBER());

                jsonObjectDetail.put("VHFNO", "0");
                jsonObjectDetail.put("ITEMOCODE", reciveDetailList.get(i).getITEMOCODE());
                jsonObjectDetail.put("VSERIAL", reciveDetailList.get(i).getVSERIAL());
                jsonObjectDetail.put("ORDER_QTY", reciveDetailList.get(i).getORDER_QTY());
                jsonObjectDetail.put("ORDER_BONUS", reciveDetailList.get(i).getBONUS());
                jsonObjectDetail.put("VHFDATE", convertToEnglish(date.getText().toString()));
                jsonObjectDetail.put("RECEIVED_QTY", reciveDetailList.get(i).getRECEIVED_QTY());
                jsonObjectDetail.put("BONUS", reciveDetailList.get(i).getBONUS());
                jsonObjectDetail.put("PRICE", reciveDetailList.get(i).getPRICE());
                jsonObjectDetail.put("TOTAL", reciveDetailList.get(i).getTOTAL());
                jsonObjectDetail.put("TAX", reciveDetailList.get(i).getTAXDETAIL());
                jsonObjectDetail.put("INDATE",convertToEnglish(reciveDetailList.get(i).getINDATE()));
                jsonObjectDetail.put("DISCL", reciveDetailList.get(i).getDISCL());
                jsonObjectDetail.put("EXPDATE", convertToEnglish(reciveDetailList.get(i).getEXPDATE()));
                j.put(jsonObjectDetail);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



         finalObject=new JSONObject();
        try {
            finalObject.put("MASTER", jsonObjectMASTER);
            Log.e("finalObjectMas",""+finalObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            finalObject.put("DETAIL", j);
            Log.e("finalObjectDETAIL",""+finalObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void sendData() throws JSONException {
        Map<String, String> params = new HashMap<>();
        JSONObject jsonObjectMASTER = new JSONObject();
        try {
            jsonObjectMASTER.put("ORDERNO", reciveListMaster.get(0).getORDERNO());
            jsonObjectMASTER.put("VHFNO", reciveListMaster.get(0).getVHFNO());
            jsonObjectMASTER.put("VHFDATE", reciveListMaster.get(0).getVHFDATE());
            jsonObjectMASTER.put("VENDOR_VHFNO", reciveListMaster.get(0).getVENDOR_VHFNO());
            jsonObjectMASTER.put("VENDOR_VHFDATE", reciveListMaster.get(0).getVENDOR_VHFDATE());
            jsonObjectMASTER.put("SUBTOTAL", reciveListMaster.get(0).getSUBTOTAL());
            jsonObjectMASTER.put("TAX", reciveListMaster.get(0).getTAX());
            jsonObjectMASTER.put("NETTOTAL", reciveListMaster.get(0).getNETTOTAL());
            jsonObjectMASTER.put("IS_POSTED", reciveListMaster.get(0).getIS_POSTED());
            jsonObjectMASTER.put("TAXKIND", reciveListMaster.get(0).getTAXKIND());
            jsonObjectMASTER.put("DISC", reciveListMaster.get(0).getDISC());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonObjectDetail = null;
        for (int i = 0; i < reciveDetailList.size(); i++) {
            jsonObjectDetail = new JSONObject();
            try {
                jsonObjectDetail.put("ORDERNO", reciveDetailList.get(i).getORDERNUMBER());

                jsonObjectDetail.put("VHFNO", reciveDetailList.get(i).getVHFNO_DETAIL());
                jsonObjectDetail.put("ITEMOCODE", reciveDetailList.get(i).getITEMOCODE());
                jsonObjectDetail.put("VSERIAL", reciveDetailList.get(i).getVSERIAL());
                jsonObjectDetail.put("ORDER_QTY", reciveDetailList.get(i).getORDER_QTY());
                jsonObjectDetail.put("ORDER_BONUS", reciveDetailList.get(i).getBONUS());
                jsonObjectDetail.put("VHFDATE", reciveDetailList.get(i).getVHFDATE_DETAIL());
                jsonObjectDetail.put("RECEIVED_QTY", reciveDetailList.get(i).getRECEIVED_QTY());
                jsonObjectDetail.put("BONUS", reciveDetailList.get(i).getBONUS());
                jsonObjectDetail.put("PRICE", reciveDetailList.get(i).getPRICE());
                jsonObjectDetail.put("TOTAL", "100");
                jsonObjectDetail.put("TAX", reciveDetailList.get(i).getTAXDETAIL());
                jsonObjectDetail.put("INDATE", reciveDetailList.get(i).getINDATE());
                jsonObjectDetail.put("DISCL", reciveDetailList.get(i).getDISCL());
                jsonObjectDetail.put("EXPDATE", reciveDetailList.get(i).getEXPDATE());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        JSONArray j = new JSONArray();
        j.put(jsonObjectDetail);

        JSONObject finalObject=new JSONObject();
        try {
            finalObject.put("MASTER", jsonObjectMASTER.toString());
            Log.e("finalObjectMas",""+finalObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            finalObject.put("DETAIL", j.toString());
            Log.e("finalObjectDETAIL",""+finalObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        params.put("JSONSTR", finalObject.toString());
//        JSONObject allData=new JSONObject();
//        allData.put("JSONSTR", finalObject.toString());

//        Log.e("allData",""+allData);


        Log.e("finalObject",""+finalObject.toString());
        String url = "http://"+ipAddres+"/SaveOrder?JSONSTR=" ;
        JsonObjectRequest request = null;
        request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //                    String code = jsonObject.getString("responceFamily_build");
                Log.e("respoCode", "" + jsonObject);
//                    if (code.equals("faild")) {
//                        Toast.makeText(CompalaintActivity.this, "Family Number  and  build Number not exist ", Toast.LENGTH_SHORT).show();
//                    }
//                    if (code.equals("Sucsses"))
//                    {
//                        Toast.makeText(CompalaintActivity.this, "thank  ", Toast.LENGTH_SHORT).show();
//
//                    }
//                    if (code.equals("faild_FamilyId")) {
//                        Toast.makeText(CompalaintActivity.this, "Family number not exist ", Toast.LENGTH_SHORT).show();
//                    }
//                    if (code.equals("faild_BuildId")) {
//                        Toast.makeText(CompalaintActivity.this, "Building number not exist", Toast.LENGTH_SHORT).show();
//                    }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error ", "onErrorResponse: " + error);
                Toast.makeText(Recive_PO.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }
        );
        MySingeltone.getmInstance(Recive_PO.this).addToRequestQueue(request);

    }

    private void sndToUrl() {
        String url = "http://10.0.0.22:8081/SaveOrder?JSONSTR=" ;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("responsesndToUrl", "" + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                JSONObject jsonObjectMASTER = new JSONObject();
                try {
                    jsonObjectMASTER.put("ORDERNO", reciveListMaster.get(0).getORDERNO());
                    jsonObjectMASTER.put("VHFNO", reciveListMaster.get(0).getVHFNO());
                    jsonObjectMASTER.put("VHFDATE", reciveListMaster.get(0).getVHFDATE());
                    jsonObjectMASTER.put("VENDOR_VHFNO", reciveListMaster.get(0).getVENDOR_VHFNO());
                    jsonObjectMASTER.put("VENDOR_VHFDATE", reciveListMaster.get(0).getVENDOR_VHFDATE());
                    jsonObjectMASTER.put("SUBTOTAL", reciveListMaster.get(0).getSUBTOTAL());
                    jsonObjectMASTER.put("TAX", reciveListMaster.get(0).getTAX());
                    jsonObjectMASTER.put("NETTOTAL", reciveListMaster.get(0).getNETTOTAL());
                    jsonObjectMASTER.put("IS_POSTED", reciveListMaster.get(0).getIS_POSTED());
                    jsonObjectMASTER.put("TAXKIND", reciveListMaster.get(0).getTAXKIND());
                    jsonObjectMASTER.put("DISC", reciveListMaster.get(0).getDISC());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject jsonObjectDetail = null;
                for (int i = 0; i < reciveDetailList.size(); i++) {
                    jsonObjectDetail = new JSONObject();
                    try {
                        jsonObjectDetail.put("ORDERNO", reciveDetailList.get(i).getORDERNUMBER());

                        jsonObjectDetail.put("VHFNO", reciveDetailList.get(i).getVHFNO_DETAIL());
                        jsonObjectDetail.put("ITEMOCODE", reciveDetailList.get(i).getITEMOCODE());
                        jsonObjectDetail.put("VSERIAL", reciveDetailList.get(i).getVSERIAL());
                        jsonObjectDetail.put("ORDER_QTY", reciveDetailList.get(i).getORDER_QTY());
                        jsonObjectDetail.put("ORDER_BONUS", reciveDetailList.get(i).getBONUS());
                        jsonObjectDetail.put("VHFDATE", reciveDetailList.get(i).getVHFDATE_DETAIL());
                        jsonObjectDetail.put("RECEIVED_QTY", reciveDetailList.get(i).getRECEIVED_QTY());
                        jsonObjectDetail.put("BONUS", reciveDetailList.get(i).getBONUS());
                        jsonObjectDetail.put("PRICE", reciveDetailList.get(i).getPRICE());
                        jsonObjectDetail.put("TOTAL", "100");
                        jsonObjectDetail.put("TAX", reciveDetailList.get(i).getTAXDETAIL());
                        jsonObjectDetail.put("INDATE", reciveDetailList.get(i).getINDATE());
                        jsonObjectDetail.put("DISCL", reciveDetailList.get(i).getDISCL());
                        jsonObjectDetail.put("EXPDATE", reciveDetailList.get(i).getEXPDATE());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                JSONArray j = new JSONArray();
                j.put(jsonObjectDetail);
                JSONObject finalObject=new JSONObject();
                try {
                    finalObject.put("MASTER", jsonObjectMASTER.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    finalObject.put("DETAIL", j.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                params.put("JSONSTR",finalObject.toString());
//                params.put("JSONSTR",finalObject.toString());

                //returning parameter
                return params;
            }
        };
        MySingeltone.getmInstance(Recive_PO.this).addToRequestQueue(stringRequest);

    }

    public DatePickerDialog.OnDateSetListener openDatePickerDialog(final TextView editText) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(editText);
            }

        };
        return date;
    }

    private void updateLabel(TextView editText) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));

    }

    private void clearData() {

        item_no.setText("");
        qty_required.setText("");
        free_qty.setText("");
        recived_qty.setText("");
        date.setText(convertToEnglish(today));
        voucher_no.setText("");
        item_name.setText("");
    }

    void clearAllData() {
        clearData();
        total_category_qty.setText("");
        supplier_name.setText("");
        transaction_no.setText("");
        reciver_category_qty.setText("");
        tableCheckData.removeAllViews();

        pricevalue = 0;
        itemInfoList.clear();
        date.setText(convertToEnglish(today));
        pricevalue=0;
        counter=0;
        position=1;
        transaction_no.requestFocus();

    }
    void  clearLists(){
        reciveDetailList.clear();

        reciveListMaster.clear();
    }

    private int calckQty() {
        reciverQty_text = recived_qty.getText().toString();
        bonusQty = Double.parseDouble(free_qty.getText().toString());

        qty = Double.parseDouble(reciverQty_text);
        if (qty > 0 && qty > bonusQty) {
            qty_total = qty - bonusQty;
        } else {
            qty_total = qty;
        }
        int totalQty=(int)qty_total;


        return totalQty;

    }
    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }
    private Boolean checkFildesRequired() {
        boolean allFull = true;
        itemNo_text = item_no.getText().toString().trim();
        transNo_text = transaction_no.getText().toString().trim();
        reciverQty_text = recived_qty.getText().toString().trim();
        dateExpire_text = date.getText().toString().trim();
        reciverCategory_text = reciver_category_qty.getText().toString().trim();
        voucherNo_text = voucher_no.getText().toString().trim();

        if (TextUtils.isEmpty(transNo_text)) {
            allFull = false;
            transaction_no.setError("Required");
        } else if (TextUtils.isEmpty(itemNo_text)) {
            allFull = false;
            item_no.setError("Required");
        } else if (TextUtils.isEmpty(reciverQty_text)) {
            allFull = false;
            recived_qty.setError("Required");
        } else if (TextUtils.isEmpty(dateExpire_text)) {
            allFull = false;
            date.setError("Required");
        } else if (reciverQty_text.equals("0")) {
            allFull = false;
            recived_qty.setError("Error value Zero");

        }

        if (allFull) {
            return true;
        }


        return false;
    }



//    int getMaxSerial(String type) {
//        if (type.equals("PO") || type.equals("DSD")) {
//            final String url = "http://"+ipAddres+"/GetMaxSerial?MAXTYPE=" + type;
//
//            JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
//                    new Response.Listener<JSONArray>() {
//
//                        @Override
//                        public void onResponse(JSONArray jsonArray) {
//                            try {
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject transactioRecive = jsonArray.getJSONObject(i);
//
//                                    maxSerial = Integer.parseInt(transactioRecive.getString("VSERIAL"));
//                                    Log.e("maxSerial", "" + maxSerial);
//
//
//
//                                }
//                            } catch (Exception e) {
//                                Log.e("Exception", "" + e.getMessage());
//                            }
//                        }
//
//
//                    }
//
//                    , new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e("onErrorResponse: ", "" + error);
//                }
//
//            });
//            MySingeltone.getmInstance(Recive_PO.this).addToRequestQueue(stringRequest);
//        }
//        return maxSerial;
//
//    }
    private ProgressDialog progressDialog;

    // resturant way
    public   class JSONTaskSenddata extends AsyncTask<String, String, String> {

        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setCancelable(false);
//            progressDialog.setMessage("Loading...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setProgress(0);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
//                String link = "http://10.0.0.16:8080/WSKitchenScreen/FSAppServiceDLL.dll/RestSaveKitchenScreen";

                String ss="";
                String link = "http://10.0.0.22:8081/" + "SaveOrder";
                String data = "JSONSTR" + URLEncoder.encode(finalObject.toString().trim(), "UTF-8") ;
                Log.e("data",""+data);


                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                wr.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("onPostExecute",""+s);
            Toast.makeText(Recive_PO.this , ""+s , Toast.LENGTH_LONG).show();

            if (s != null && s.contains("Saved Successfully")) {

                Toast.makeText(Recive_PO.this , "Success" , Toast.LENGTH_SHORT).show();
//
                Log.e("tag", "****Success");
            } else {
//                Toast.makeText(ExportJason.this, "Failed to export data", Toast.LENGTH_SHORT).show();
                Log.e("tag", "****Failed to export data");
            }
        }
    }
    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String s="";

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI("http://"  +ipAddres+"/" + "SaveOrder"));

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("JSONSTR", finalObject.toString().trim()));

                request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = client.execute(request);

                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();

                JsonResponse = sb.toString();
                Log.e("tag", "" + JsonResponse);

                return JsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("onPostExecute", "" + s);

            if (s != null ) {
                if (s.contains("ErrorCode")) {

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        codeState = Integer.parseInt(jsonObject.getString("ErrorCode"));
                        description = (jsonObject.getString("ErrorDesc"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
                    if(codeState==0)
                    {

                        maxSerial=Integer.parseInt(description);
                        updateGRN();
                        Log.e("POSTEXECmaxSerial",""+maxSerial);
                        savedDialog();
                        askForPrint();
                        addDetailToDB();
                        addMasterToDB();
//                        clearAllData();

                    }
                    else Log.e("tag", "****Failed to export data Please check internet connection");


            }
        }

    private void printLastVoucher() {
        Intent i = new Intent(Recive_PO.this, BluetoothConnectMenu.class);
        i.putExtra("printKey", "1");
        startActivity(i);

    }


    private void savedDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(Recive_PO.this);
//        builder.setTitle("GRN");
//        builder.setCancelable(true);
//        builder.setMessage(""+maxSerial);
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();

        new SweetAlertDialog(Recive_PO.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("تم الحفظ بنجاح")
                .setContentText("  GRN \t " +maxSerial)
                .hideConfirmButton()
                .show();



    }

}
