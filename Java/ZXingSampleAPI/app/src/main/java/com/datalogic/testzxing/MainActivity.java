// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.testzxing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HandleClick hc = new HandleClick();
        findViewById(R.id.butQR).setOnClickListener(hc);
        findViewById(R.id.butProd).setOnClickListener(hc);
        findViewById(R.id.butOther).setOnClickListener(hc);
    }    
    private class HandleClick implements OnClickListener{
    	public void onClick(View arg0) {
	    Intent intent = new Intent("com.google.zxing.client.android.SCAN");     
	    switch(arg0.getId()){
            // Scan only QR Codes
	    	case R.id.butQR:
	            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
	            break;
            // Scan only certain barcode families
	    	case R.id.butOther:
	    	    intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR");
	    	    break;
	    }
	    startActivityForResult(intent, 0);	//Barcode scanner will scan for us
    	}
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            TextView tvStatus=(TextView)findViewById(R.id.tvStatus);
            TextView tvResult=(TextView)findViewById(R.id.tvResult);
            // Check result is correct, following ZXing specification
            if (resultCode == RESULT_OK) {
                // Get ZXing format
            	tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
                // Get the String result
            	tvResult.setText(intent.getStringExtra("SCAN_RESULT"));

				tvResult.append("\nByte Array:\n");
                // Get Byte array result
            	byte[] array = intent.getByteArrayExtra("SCAN_RESULT_BYTES");
            	if(array!=null) {
            		for(Byte b : array)
            			tvResult.append("0x"+Byte.toString(b)+" ");
            	}
            } else if (resultCode == RESULT_CANCELED) {
                // In case no result was found
            	tvStatus.setText("Press a button to start a scan.");
                tvResult.setText("Scan cancelled.");
            }
        }
    }
}
