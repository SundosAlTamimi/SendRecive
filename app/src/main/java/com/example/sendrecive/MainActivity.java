package com.example.sendrecive;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.example.sendrecive.Models.Setting;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    SliderLayout sliderLayout;
    FloatingActionButton addSetting,fab_add_po,fab_print_po,fab_add_DSD,fab_print_DSD;
    LinearLayout first_linear,second_linear;
    public static DataBaseHandler dataBaseHandler;
    SQLiteDatabase database;
    public  static  String last_ipAddres="";
    String ip_str="",tel_str="",name_str="";
    int sameqty_value=0;
    Bitmap itemBitmapPic = null;
    public static final int PICK_IMAGE = 1;
     ImageView companeyLogo;
    Animation animation,anim_move_to_right,anim_move_to_left;
     AlphaAnimation buttonClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBaseHandler = new DataBaseHandler(this);
        database = dataBaseHandler.getWritableDatabase();
        initView();
        last_ipAddres = dataBaseHandler.getIP();
        buttonClick = new AlphaAnimation(1F, 0.2F);
        animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in);
        anim_move_to_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_to_right);
        anim_move_to_left = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_to_left);
        first_linear.startAnimation(anim_move_to_right);
        second_linear.startAnimation(anim_move_to_left);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        sliderLayout.setScrollTimeInSec(2); //set scroll delay in seconds :
        setSliderViews();
        setSliderViews();

        addSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                showSettingDialog();
            }
        });
    }

    private void initView() {
        second_linear = findViewById(R.id.second_linear);
        sliderLayout = findViewById(R.id.imageSlider_2);
        first_linear = findViewById(R.id.first_linear);
        addSetting=(FloatingActionButton) findViewById(R.id.fab_add_setting);

        fab_add_po=(FloatingActionButton) findViewById(R.id.fab_add_po);
        fab_add_po.setOnClickListener(onClickListener);
        fab_print_po=(FloatingActionButton) findViewById(R.id.fab_print_po);
        fab_print_po.setOnClickListener(onClickListener);
        fab_add_DSD=(FloatingActionButton) findViewById(R.id.fab_add_DSD);
        fab_add_DSD.setOnClickListener(onClickListener);
        fab_print_DSD=(FloatingActionButton) findViewById(R.id.fab_print_DSD);
        fab_print_DSD.setOnClickListener(onClickListener);

    }

    private void printLastVoucher() {
        Intent i = new Intent(getApplicationContext(), BluetoothConnectMenu2.class);
        i.putExtra("printKey", "1");
        startActivity(i);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                itemBitmapPic = bitmap;
                companeyLogo.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    private void showSettingDialog() {

        last_ipAddres=dataBaseHandler.getIP();
        Setting mySetting=new Setting();
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.setting_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);
        final Button saveButton = dialog.findViewById(R.id.save_setting);
        final Button cancelButton = dialog.findViewById(R.id.cancel_btn);
        final EditText companeyName = dialog.findViewById(R.id.companeyName);
        final EditText companeyTEL = dialog.findViewById(R.id.companeyTEL);
       companeyLogo = dialog.findViewById(R.id.companeyLogo);
        final EditText ipText = dialog.findViewById(R.id.ip_textview);
        final CheckBox sameQTY=dialog.findViewById(R.id.sameQTY);
        companeyLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);

            }
        });


        if(dataBaseHandler.getSetting()!=null)
        {
            mySetting=dataBaseHandler.getSetting();
            companeyName.setText(mySetting.getCOMPANEY_NAME());
            companeyTEL.setText(mySetting.getTEL());
            if(mySetting.getLOGO()!= null)
            {
                companeyLogo.setImageBitmap(mySetting.getLOGO());
            }
            else
            {companeyLogo.setImageDrawable(getResources().getDrawable(R.drawable.icon));}
            try{
                if( mySetting.getSAME_QUANTITY().equals("1"))
                {
                    sameQTY.setChecked(true);
                }
            }
            catch (Exception e)
            {
                sameQTY.setChecked(false);

            }

        }




        ip_str =ipText.getText().toString();
        if(!last_ipAddres.equals(""))
        {
            ipText.setText(last_ipAddres);

        }


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                ip_str =ipText.getText().toString();
                name_str=companeyName.getText().toString();
                tel_str=companeyTEL.getText().toString();
                if(sameQTY.isChecked())
                {
                    sameqty_value=1;

                }
                else {
                    sameqty_value=0;
                }


                if(!ip_str.equals("")&& !name_str.equals(""))
                {
                    Setting newSetting=new Setting();
                    newSetting.setIP(ip_str);
                    newSetting.setCOMPANEY_NAME(name_str);
                    newSetting.setTEL(tel_str);
                    newSetting.setSAME_QUANTITY(sameqty_value+"");
                    newSetting.setIP(ip_str);
                    newSetting.setLOGO(itemBitmapPic);

                    dataBaseHandler.deleteSetting();
                    dataBaseHandler.addSetting(newSetting);
                    dialog.dismiss();

                }
                else
                {   if(ip_str.equals(""))
                    ipText.setError("Required");

                    if(name_str.equals(""))
                    {companeyName.setError("Required");}
                }


            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setSliderViews() {
        for (int i = 0; i < 3; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(this);
//            sliderView.setImageScaleType(ImageView.ScaleType.FIT_END);
            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.first);


                    break;
                case 1:   sliderView.setImageDrawable(R.drawable.second);

                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.thered);
                    break;
                case 3:

                    sliderView.setImageDrawable(R.drawable.four);
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderView.setDescription("Recive System  by Falcons Soft " ) ;
            final int finalI = i;
//
//            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
//                @Override
//                public void onSliderClick(SliderView sliderView) {
//                    Toast.makeText(MainActivity.this, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
//
//                }
//            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }
    }
    private View.OnClickListener onClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.fab_add_po:
                    view.startAnimation(animation);
                    Intent i=new Intent(MainActivity.this,Recive_PO.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);

                    break;
                case R.id.fab_print_po:
                    view.startAnimation(animation);
                    Intent print=new Intent(MainActivity.this,PrintPO.class);
                    print.putExtra("Key", "PO");
                    startActivity(print);
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                    break;
                case R.id.fab_add_DSD:
                    view.startAnimation(animation);
                    Intent iDSD=new Intent(MainActivity.this,Recive_Direct.class);
                    startActivity(iDSD);
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);

                    break;
                case R.id.fab_print_DSD:
                    view.startAnimation(animation);
                    Intent print_DSD=new Intent(MainActivity.this,PrintPO.class);
                    print_DSD.putExtra("Key", "DSD");
                    startActivity(print_DSD);
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                    break;

            }

        }
    };
}
