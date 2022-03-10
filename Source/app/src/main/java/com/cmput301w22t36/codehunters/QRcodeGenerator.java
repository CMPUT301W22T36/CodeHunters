package com.cmput301w22t36.codehunters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRcodeGenerator extends AppCompatActivity {
    //Initialize variables
    EditText uidInput;
    Button Generate;
    ImageView CodeOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_generator);

        //Assign variables
        uidInput = findViewById(R.id.uid_input);
        Generate = findViewById(R.id.qr_generate);
        CodeOutput = findViewById(R.id.code_output);

        Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:change the input method as getting from user class instead of typing
                //now:get input from edit text
                String sText = uidInput.getText().toString().trim();
                //Initialize multi format writer
                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    //Initialize bit matrix
                    BitMatrix matrix = writer.encode(sText, BarcodeFormat.QR_CODE,350,350);
                    //Initialize barcode encoder
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    //Initialize bitmap
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    //Set bitmap on image view
                    CodeOutput.setImageBitmap(bitmap);
                    //Initialize input manager
                    InputMethodManager manager = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE
                    );
                    //Hide soft keyboard
                    manager.hideSoftInputFromWindow(uidInput.getApplicationWindowToken(),0);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
