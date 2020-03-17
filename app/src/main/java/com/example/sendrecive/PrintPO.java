package com.example.sendrecive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sendrecive.Models.ReciveDetail;
import com.example.sendrecive.Models.ReciveMaster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PrintPO extends AppCompatActivity {
    public static DataBaseHandler dataBaseHandler;
   public static List<ReciveMaster>   MasterListPrintPo;
    public static  List<ReciveDetail> DetailListPrintPo;
    EditText grn_txt;
    String grn_value;
    private TableLayout tableCheckData;
    Button print,cancel_btn;
    String keyPrint="";
    LinearLayout lineartransaction;
    AlphaAnimation buttonClick;
    Animation animation;
    TextView supplier_name, item_name, qty_required, free_qty, total_category_qty, reciver_category_qty,date, transaction_no, item_no, recived_qty, voucher_no,variableText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_po);
        keyPrint = getIntent().getStringExtra("Key");
        buttonClick = new AlphaAnimation(1F, 0.2F);
        animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in);
        Log.e("keyPrint",""+keyPrint);
        initView();
        dataBaseHandler=new DataBaseHandler(PrintPO.this);
        MasterListPrintPo=new ArrayList<>();
        DetailListPrintPo=new ArrayList<>();
        grn_txt=findViewById(R.id.grn_no);
        grn_value=grn_txt.getText().toString();
        grn_txt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                grn_value=grn_txt.getText().toString().trim();
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if(!TextUtils.isEmpty(grn_value))
                        {
                            if(keyPrint.equals("PO"))
                            {
                                MasterListPrintPo=dataBaseHandler.getReciveMaster(grn_value,"1");
                                DetailListPrintPo=dataBaseHandler.getReciveDETAIL(grn_value,"1");
                            }
                            else if(keyPrint.equals("DSD"))
                            {
                                MasterListPrintPo=dataBaseHandler.getReciveMaster(grn_value,"2");
                                DetailListPrintPo=dataBaseHandler.getReciveDETAIL(grn_value,"2");


                            }

                            fillData();
                            Log.e("MasterListPrintPo",""+MasterListPrintPo.size()+"\t"+DetailListPrintPo.size());
                        }
                           else{
                                grn_txt.setError("Required");
                            }

                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });



    }

    private void fillData() {
        if(DetailListPrintPo.size()!=0 && MasterListPrintPo.size()!=0)
        {

            transaction_no.setText(MasterListPrintPo.get(0).getORDERNO());
            supplier_name.setText(MasterListPrintPo.get(0).getAccName());
            total_category_qty.setText(MasterListPrintPo.get(0).getCOUNTX());
            date.setText(MasterListPrintPo.get(0).getVHFDATE());
            voucher_no.setText(MasterListPrintPo.get(0).getVENDOR_VHFNO());
            reciver_category_qty.setText(DetailListPrintPo.size()+"");
            for(int i=0;i<DetailListPrintPo.size();i++)
            {
                fillTable(i);
            }


        }
        else{
            alertdialog();
            // not  Exist Order
        }
    }


    private void fillTable(int position) {

            final TableRow row = new TableRow(PrintPO.this);

            row.setPadding(1, 7, 1, 5);
//            row.setTag(position);

            for (int i = 0; i < 3; i++) {

                String[] record = {DetailListPrintPo.get(position).getITEM_NAME(), DetailListPrintPo.get(position).getRECEIVED_QTY() + "",
                      DetailListPrintPo.get(position).getBONUS()};

                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                TextView textView = new TextView(PrintPO.this);
                textView.setHint(record[i]);
                textView.setTextColor(ContextCompat.getColor(PrintPO.this, R.color.darkblue_));
                textView.setHintTextColor(ContextCompat.getColor(PrintPO.this, R.color.darkblue_));
                if (i == 0) {
                    textView.setGravity(Gravity.CENTER);
                } else {
                    textView.setGravity(Gravity.CENTER);
                }
                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                textView.setLayoutParams(lp2);
                row.addView(textView);
            }
        tableCheckData.addView(row);





    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.print:
                    view.startAnimation(buttonClick);
                    if(tableCheckData.getChildCount()>0)
                    {
                        printLastVoucher();
                    }
                    else {
                        Toast.makeText(PrintPO.this, "املى  بيانات الجدول ", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case R.id.cancel_btn:
                    view.startAnimation(buttonClick);
                   Intent i= new Intent(PrintPO.this,MainActivity.class);
                   startActivity(i);
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                    break;

            }
        }
    };

    private void alertdialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(PrintPO.this);
//        builder.setTitle("تحذير ");
//        builder.setCancelable(true);
//
//        builder.setMessage("الرقم غير موجود");
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();

        new SweetAlertDialog(PrintPO.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("تحذير")
                .setContentText("الرقم غير موجود")
                .hideConfirmButton()
                .show();


    }
    private void initView() {
        date = (TextView) findViewById(R.id.date_expire);
        transaction_no = (TextView) findViewById(R.id.transaction_no);
        item_no = (TextView) findViewById(R.id.item_no);
        recived_qty = (TextView) findViewById(R.id.recived_qty);
        reciver_category_qty = (TextView) findViewById(R.id.reciver_category_qty);
        voucher_no = (TextView) findViewById(R.id.voucher_no);
        supplier_name = (TextView) findViewById(R.id.supplier_name);
        item_name = (TextView) findViewById(R.id.item_name);
        qty_required = (TextView) findViewById(R.id.qty_required);
        free_qty = (TextView) findViewById(R.id.free_qty);
        total_category_qty = (TextView) findViewById(R.id.total_category_qty);
//        addQty = (FloatingActionButton) findViewById(R.id.add_qty);
//        addQty.setOnClickListener(onClickListener);
        tableCheckData = (TableLayout) findViewById(R.id.tableData);
//        myCalendar = Calendar.getInstance();
////        row=new TableRow(Recive_PO.this);
        print = findViewById(R.id.print);
        print.setOnClickListener(onClickListener);
        cancel_btn = findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(onClickListener);
        DetailListPrintPo = new ArrayList<>();
        variableText= (TextView) findViewById(R.id.variableText);
        lineartransaction=findViewById(R.id.lineartransaction);
        if(keyPrint.equals("PO")) {
//            variableText.setText("رقم الطلبية");
            lineartransaction.setVisibility(View.VISIBLE);
        }
        else{
//            variableText.setText("رقم المورد");
            lineartransaction.setVisibility(View.GONE);
        }



    }

    private void printLastVoucher() {
        Intent i = new Intent(PrintPO.this, BluetoothConnectMenu.class);
        i.putExtra("printKey", "3");
        startActivity(i);

    }
}
