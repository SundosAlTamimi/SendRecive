package com.example.sendrecive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.example.sendrecive.Models.ReciveDetail;
import com.example.sendrecive.Models.ReciveMaster;
import com.example.sendrecive.Port.AlertView;
import com.sewoo.port.android.BluetoothPort;
import com.sewoo.request.android.RequestHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import static com.example.sendrecive.Recive_Direct.reciveDetailList_DSD;
import static com.example.sendrecive.Recive_Direct.reciveListMaster_DSD;
import static com.example.sendrecive.Recive_PO.reciveDetailList;
import static com.example.sendrecive.Recive_PO.reciveListMaster;

public class BluetoothConnectMenu2 extends Activity  {
    private static final String TAG = "BluetoothConnectMenu2";
    private static final int REQUEST_ENABLE_BT = 2;
    ArrayAdapter<String> adapter;

    private Vector<BluetoothDevice> remoteDevices;
    private BroadcastReceiver searchFinish;
    private BroadcastReceiver searchStart;
    private BroadcastReceiver discoveryResult;
    private BroadcastReceiver disconnectReceiver;
    private Thread hThread;
    private Context context;
    private EditText btAddrBox;
    private Button connectButton;
    private Button searchButton;
    private ListView list;
    private BluetoothPort bluetoothPort;
    private CheckBox chkDisconnect;
    private static final String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "//temp";
    private static final String fileName;
    private String lastConnAddr;
    static String idname;
    DataBaseHandler obj;
    String getData;
    DecimalFormat decimalFormat;
    private BluetoothAdapter mBluetoothAdapter;
    public static List<ReciveMaster> reciveListMaster_forPrint = new ArrayList<>();
    public static List<ReciveDetail> reciveDetailList_forPrint;
//    List<Item> long_listItems;

public  static  int valueCheckHidPrice=0;


    static {
        fileName = dir + "//BTPrinter";
    }

    public BluetoothConnectMenu2() {

    }
    private void bluetoothSetup() {
        this.clearBtDevData();
        this.bluetoothPort = BluetoothPort.getInstance();
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.mBluetoothAdapter != null) {
            if (!this.mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
                startActivityForResult(enableBtIntent, 2);
            }

        }
    }

    private void loadSettingFile() {
//        int rin = false;
        char[] buf = new char[128];

        try {
            FileReader fReader = new FileReader(fileName);
            int rin = fReader.read(buf);
            if (rin > 0) {
                this.lastConnAddr = new String(buf, 0, rin);
                this.btAddrBox.setText(this.lastConnAddr);
            }

            fReader.close();
        } catch (FileNotFoundException var4) {
            Log.i("BluetoothConnectMenu2", "Connection history not exists.");
        } catch (IOException var5) {
            Log.e("BluetoothConnectMenu2", var5.getMessage(), var5);
        }

    }

    private void saveSettingFile() {
        try {
            File tempDir = new File(dir);
            if (!tempDir.exists()) {
                tempDir.mkdir();
            }

            FileWriter fWriter = new FileWriter(fileName);//test
            if (this.lastConnAddr != null) {
                fWriter.write(this.lastConnAddr);
            }

            fWriter.close();
        } catch (FileNotFoundException var3) {
            Log.e("BluetoothConnectMenu2", var3.getMessage(), var3);
        } catch (IOException var4) {
            Log.e("BluetoothConnectMenu2", var4.getMessage(), var4);
        }

    }

    private void clearBtDevData() {
        this.remoteDevices = new Vector();
    }

    private void addPairedDevices() {
        Iterator iter = this.mBluetoothAdapter.getBondedDevices().iterator();

        while (iter.hasNext()) {
            BluetoothDevice pairedDevice = (BluetoothDevice) iter.next();
            if (this.bluetoothPort.isValidAddress(pairedDevice.getAddress())) {
                this.remoteDevices.add(pairedDevice);
                this.adapter.add(pairedDevice.getName() + "\n[" + pairedDevice.getAddress() + "] [Paired]");
            }
        }

    }

    double size_subList = 0;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.blutooth);
        this.btAddrBox = (EditText) this.findViewById(R.id.EditTextAddressBT);
        this.connectButton = (Button) this.findViewById(R.id.ButtonConnectBT);
        BluetoothConnectMenu2.this.connectButton.setEnabled(true);
        this.searchButton = (Button) this.findViewById(R.id.ButtonSearchBT);
        this.list = (ListView) this.findViewById(R.id.BtAddrListView);
        this.chkDisconnect = (CheckBox) this.findViewById(R.id.check_disconnect);
        this.chkDisconnect.setChecked(true);
        this.context = this;
        obj = new DataBaseHandler(BluetoothConnectMenu2.this);

        decimalFormat = new DecimalFormat("##.000");


//
        getData = getIntent().getStringExtra("printKey");
//        Bundle bundle = getIntent().getExtras();
//         allStudents = (List<Item>) bundle.get("ExtraData");
//
//         Log.e("all",allStudents.get(0).getBarcode());

        Log.e("printKey", "" + getData);
        this.loadSettingFile();
        this.bluetoothSetup();
        this.connectButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!BluetoothConnectMenu2.this.bluetoothPort.isConnected()) {
                    try {
                        BluetoothConnectMenu2.this.btConn(BluetoothConnectMenu2.this.mBluetoothAdapter.getRemoteDevice(BluetoothConnectMenu2.this.btAddrBox.getText().toString()));
                    } catch (IllegalArgumentException var3) {
                        Log.e("BluetoothConnectMenu2", var3.getMessage(), var3);
                        AlertView.showAlert(var3.getMessage(), BluetoothConnectMenu2.this.context);
                        return;
                    } catch (IOException var4) {
                        Log.e("BluetoothConnectMenu2", var4.getMessage(), var4);
                        AlertView.showAlert(var4.getMessage(), BluetoothConnectMenu2.this.context);
                        return;
                    }
                } else {
                    BluetoothConnectMenu2.this.btDisconn();
                }

            }
        });
        this.searchButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!BluetoothConnectMenu2.this.mBluetoothAdapter.isDiscovering()) {
                    BluetoothConnectMenu2.this.clearBtDevData();
                    BluetoothConnectMenu2.this.adapter.clear();
                    BluetoothConnectMenu2.this.mBluetoothAdapter.startDiscovery();
                } else {
                    BluetoothConnectMenu2.this.mBluetoothAdapter.cancelDiscovery();
                }

            }
        });
        this.adapter = new ArrayAdapter(BluetoothConnectMenu2.this, R.layout.cci);

        this.list.setAdapter(this.adapter);
        this.addPairedDevices();
        this.list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                BluetoothDevice btDev = (BluetoothDevice) BluetoothConnectMenu2.this.remoteDevices.elementAt(arg2);

                try {
                    if (BluetoothConnectMenu2.this.mBluetoothAdapter.isDiscovering()) {
                        BluetoothConnectMenu2.this.mBluetoothAdapter.cancelDiscovery();
                    }

                    BluetoothConnectMenu2.this.btAddrBox.setText(btDev.getAddress());
                    BluetoothConnectMenu2.this.btConn(btDev);
                } catch (IOException var8) {
                    AlertView.showAlert(var8.getMessage(), BluetoothConnectMenu2.this.context);
                }
            }
        });
        this.discoveryResult = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                BluetoothDevice remoteDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (remoteDevice != null) {
                    String key;
                    if (remoteDevice.getBondState() != 12) {
                        key = remoteDevice.getName() + "\n[" + remoteDevice.getAddress() + "]";
                    } else {
                        key = remoteDevice.getName() + "\n[" + remoteDevice.getAddress() + "] [Paired]";
                    }

                    if (BluetoothConnectMenu2.this.bluetoothPort.isValidAddress(remoteDevice.getAddress())) {
                        BluetoothConnectMenu2.this.remoteDevices.add(remoteDevice);
                        BluetoothConnectMenu2.this.adapter.add(key);
                    }
                }

            }
        };
        this.registerReceiver(this.discoveryResult, new IntentFilter("android.bluetooth.device.action.FOUND"));
        this.searchStart = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                BluetoothConnectMenu2.this.connectButton.setEnabled(false);
                BluetoothConnectMenu2.this.btAddrBox.setEnabled(false);
//                BluetoothConnectMenu2.this.searchButton.setText(BluetoothConnectMenu2.this.getResources().getString(2131034114));

                BluetoothConnectMenu2.this.searchButton.setText("stop ");
            }
        };
        this.registerReceiver(this.searchStart, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_STARTED"));
        this.searchFinish = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                BluetoothConnectMenu2.this.connectButton.setEnabled(true);
                BluetoothConnectMenu2.this.btAddrBox.setEnabled(true);
//                BluetoothConnectMenu2.this.searchButton.setText(BluetoothConnectMenu2.this.getResources().getString(2131034113));
                BluetoothConnectMenu2.this.searchButton.setText("search");

            }
        };
        this.registerReceiver(this.searchFinish, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED"));
        if (this.chkDisconnect.isChecked()) {
            this.disconnectReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                    if (!"android.bluetooth.device.action.ACL_CONNECTED".equals(action) && "android.bluetooth.device.action.ACL_DISCONNECTED".equals(action)) {
                        BluetoothConnectMenu2.this.DialogReconnectionOption();
                    }

                }
            };
        }

    }

    protected void onDestroy() {
        try {
            if (this.bluetoothPort.isConnected() && this.chkDisconnect.isChecked()) {
                this.unregisterReceiver(this.disconnectReceiver);
            }

            this.saveSettingFile();//test
            this.bluetoothPort.disconnect();
        } catch (IOException var2) {
            Log.e("BluetoothConnectMenu2", var2.getMessage(), var2);
        } catch (InterruptedException var3) {
            Log.e("BluetoothConnectMenu2", var3.getMessage(), var3);
        }

        if (this.hThread != null && this.hThread.isAlive()) {
            this.hThread.interrupt();
            this.hThread = null;
        }

        this.unregisterReceiver(this.searchFinish);
        this.unregisterReceiver(this.searchStart);
        this.unregisterReceiver(this.discoveryResult);
        super.onDestroy();
    }

    private void DialogReconnectionOption() {
        String[] items = new String[]{"Bluetooth printer"};
        Builder builder = new Builder(this);
        builder.setTitle("connection ...");
        builder.setSingleChoiceItems(items, 0, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).setPositiveButton("connect", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    BluetoothConnectMenu2.this.btDisconn();
                    BluetoothConnectMenu2.this.btConn(BluetoothConnectMenu2.this.mBluetoothAdapter.getRemoteDevice(BluetoothConnectMenu2.this.btAddrBox.getText().toString()));
                } catch (IllegalArgumentException var4) {
                    Log.e("BluetoothConnectMenu2", var4.getMessage(), var4);
                    AlertView.showAlert(var4.getMessage(), BluetoothConnectMenu2.this.context);
                } catch (IOException var5) {
                    Log.e("BluetoothConnectMenu2", var5.getMessage(), var5);
                    AlertView.showAlert(var5.getMessage(), BluetoothConnectMenu2.this.context);
                }
            }
        }).setNegativeButton("cancel", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                BluetoothConnectMenu2.this.btDisconn();
            }
        });
        builder.show();
    }

    private void btConn(BluetoothDevice btDev) throws IOException {
        (new BluetoothConnectMenu2.connTask()).execute(new BluetoothDevice[]{btDev});
    }

    private void btDisconn() {
        try {
            this.bluetoothPort.disconnect();
            if (this.chkDisconnect.isChecked()) {
                this.unregisterReceiver(this.disconnectReceiver);
            }
        } catch (Exception var2) {
            Log.e("BluetoothConnectMenu2", var2.getMessage(), var2);
        }

        if (this.hThread != null && this.hThread.isAlive()) {
            this.hThread.interrupt();
        }

        this.connectButton.setText("Connect");
        this.list.setEnabled(true);
        this.btAddrBox.setEnabled(true);
        this.searchButton.setEnabled(true);
        Toast toast = Toast.makeText(this.context, "disconnect", Toast.LENGTH_SHORT);
        toast.show();
    }
    class connTask extends AsyncTask<BluetoothDevice, Void, Integer> {
        private final ProgressDialog dialog = new ProgressDialog(BluetoothConnectMenu2.this);

        connTask() {
        }

        protected void onPreExecute() {
            this.dialog.setTitle(" Try Connect ");
            this.dialog.setMessage("Please Wait ....");
            this.dialog.show();
            super.onPreExecute();
        }

        protected Integer doInBackground(BluetoothDevice... params) {
            Integer retVal = null;

            try {
                BluetoothConnectMenu2.this.bluetoothPort.connect(params[0]);
                BluetoothConnectMenu2.this.lastConnAddr = params[0].getAddress();

                retVal = 0;
            } catch (IOException var4) {
                Log.e("BluetoothConnectMenu2", var4.getMessage());
                retVal = -1;
            }

            return retVal;
        }

        @SuppressLint("WrongThread")
        protected void onPostExecute(Integer result) {
            if (result == 0) {
                RequestHandler rh = new RequestHandler();
                BluetoothConnectMenu2.this.hThread = new Thread(rh);
                BluetoothConnectMenu2.this.hThread.start();
                BluetoothConnectMenu2.this.connectButton.setText("Connect");
                BluetoothConnectMenu2.this.connectButton.setEnabled(false);
                BluetoothConnectMenu2.this.list.setEnabled(false);
                BluetoothConnectMenu2.this.btAddrBox.setEnabled(false);
                BluetoothConnectMenu2.this.searchButton.setEnabled(false);
                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }
                double total_Qty=0;

               int count = Integer.parseInt(getData);
                CPCLSample2 sample = new CPCLSample2(BluetoothConnectMenu2.this);
                sample.selectContinuousPaper();
                try {
                    if(count==1)
                    {

                        reciveListMaster_forPrint=reciveListMaster;
                        reciveDetailList_forPrint=reciveDetailList;
//                        List<ReciveMaster> reciveMasterList=obj.getReciveMaster();
//                        List<ReciveDetail> reciveDetailList=obj.getReciveDETAIL();

                        Bitmap bit = convertLayoutToImage(reciveListMaster_forPrint,reciveDetailList_forPrint);
//                    Bitmap bit = convertLayoutToImage(voucherforPrint, itemforPrint);
                        try {
                            sample.imageTestArabic(1, bit);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    else if(count==2)
                    {
                        reciveListMaster_forPrint=reciveListMaster_DSD;
                        reciveDetailList_forPrint=reciveDetailList_DSD;

                        Bitmap bit = convertLayoutToImage(reciveListMaster_forPrint,reciveDetailList_forPrint);
//                    Bitmap bit = convertLayoutToImage(voucherforPrint, itemforPrint);
                        try {
                            sample.imageTestArabic(1, bit);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    reciveListMaster_forPrint.clear();
                    reciveDetailList_forPrint.clear();
                    reciveListMaster_DSD.clear();
                    reciveDetailList_DSD.clear();
                    reciveDetailList.clear();
                    reciveListMaster.clear();

                    finish();
                    Thread.sleep(1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (BluetoothConnectMenu2.this.chkDisconnect.isChecked()) {
                    BluetoothConnectMenu2.this.registerReceiver(BluetoothConnectMenu2.this.disconnectReceiver, new IntentFilter("android.bluetooth.device.action.ACL_CONNECTED"));
                    BluetoothConnectMenu2.this.registerReceiver(BluetoothConnectMenu2.this.disconnectReceiver, new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED"));
                }
            } else {
                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }

                AlertView.showAlert("Disconnect Bluetoothُ", "Try Again ,,,.", BluetoothConnectMenu2.this.context);
            }

            super.onPostExecute(result);
        }

    }

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }
    private Bitmap convertLayoutToImage(List<ReciveMaster> masterList, List<ReciveDetail> detailList) {
        Log.e("masterList",""+masterList.size()+"\t"+detailList.size());
        LinearLayout linearView = null;
        String s="";

        final Dialog dialogs = new Dialog(BluetoothConnectMenu2.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setCancelable(true);
        dialogs.setContentView(R.layout.sewo30_printer_layout);//
        TextView compname, tel, grn, vhNo, date, transaction_no, supplier_name, qty_required, recived_qty,typevoucher,compname025;
        ImageView img = (ImageView) dialogs.findViewById(R.id.img);
//
        compname = (TextView) dialogs.findViewById(R.id.compname);
        tel = (TextView) dialogs.findViewById(R.id.tel);
        grn = (TextView) dialogs.findViewById(R.id.grn);
        date = (TextView) dialogs.findViewById(R.id.date);
        transaction_no = (TextView) dialogs.findViewById(R.id.transaction_no);
        supplier_name = (TextView) dialogs.findViewById(R.id.supplier_name);
        qty_required = (TextView) dialogs.findViewById(R.id.qty_required);
        recived_qty = (TextView) dialogs.findViewById(R.id.recived_qty);
        typevoucher = (TextView) dialogs.findViewById(R.id.typevoucher);
//        compname025 = (TextView) dialogs.findViewById(R.id.compname025);
//        compname025.setVisibility(View.INVISIBLE);




        TableLayout tabLayout = (TableLayout) dialogs.findViewById(R.id.tab);
        img.setImageDrawable(getResources().getDrawable(R.drawable.thered));
//        compname.setText(companyInfo.getCompanyName());
//        if (companyInfo.getLogo()!=(null))
//        {
//            img.setImageBitmap(companyInfo.getLogo());
//        }
//        else{img.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));}
        date.setText(masterList.get(0).getVHFDATE());
        if(masterList.get(0).getORDERNO().contains("xxxxxxxxx"))
        {
            typevoucher.setText("DSD");
        }
        else{
            typevoucher.setText("PO");
        }
        grn.setText(masterList.get(0).getVHFNO());
        grn.setText(masterList.get(0).getVHFNO());
        transaction_no.setText(masterList.get(0).getORDERNO());
        supplier_name.setText(masterList.get(0).getAccName());
        qty_required.setText(masterList.get(0).getCOUNTX());
        int qtyRecive=detailList.size();
        recived_qty.setText(qtyRecive+"");

        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(100, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        lp2.setMargins(0, 7, 0, 0);
        lp3.setMargins(0, 7, 0, 0);
        for (int j = 0; j < detailList.size(); j++) {

                final TableRow row = new TableRow(BluetoothConnectMenu2.this);


                for (int i = 0; i <= 2; i++) {
//                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(500, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                    lp.setMargins(0, 10, 0, 0);
                    row.setLayoutParams(lp);

                    TextView textView = new TextView(BluetoothConnectMenu2.this);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(14);
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setTextColor(getResources().getColor(R.color.black));

                    switch (i) {
                        case 0:
                            textView.setText(detailList.get(j).getITEM_NAME());
                            textView.setLayoutParams(lp3);
                            break;


                        case 1:

                                textView.setText(detailList.get(j).getRECEIVED_QTY());
                                textView.setLayoutParams(lp2);

                            break;

                        case 2:

                                textView.setText(detailList.get(j).getORDER_BONUS());
                                textView.setLayoutParams(lp2);


                            break;


                    }
                    row.addView(textView);
                }



                tabLayout.addView(row);

        }


//        total_qty_text.setText(count+"");
//        Log.e("countItem",""+count);

//        linearView  = (LinearLayout) this.getLayoutInflater().inflate(R.layout.printdialog, null, false); //you can pass your xml layout
        linearView = (LinearLayout) dialogs.findViewById(R.id.layoutforPrint);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());

        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit =linearView.getDrawingCache();

//        dialogs.show();

//        linearView.setDrawingCacheEnabled(true);
//        linearView.buildDrawingCache();
//        Bitmap bit =linearView.getDrawingCache();

//        Bitmap bitmap = Bitmap.createBitmap(linearView.getWidth(), linearView.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        Drawable bgDrawable = linearView.getBackground();
//        if (bgDrawable != null) {
//            bgDrawable.draw(canvas);
//        } else {
//            canvas.drawColor(Color.WHITE);
//        }
//        linearView.draw(canvas);

        return bit;// creates bitmap and returns the same
    }
}
