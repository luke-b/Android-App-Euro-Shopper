package com.buyagent;

import java.math.BigDecimal;
import java.text.NumberFormat;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class YesNoDialog extends Dialog {

	public YesNoDialog(String title,
					   String message,
					   String yesButton,
					   String noButton,
					   Context context,
					   View.OnClickListener listener) {
		
		super(context);
		
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
	    setContentView(R.layout.yesno);
	    setTitle(title);
	    setCancelable(true);
	    setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.icon);
		
	    TextView messageLabel = (TextView)findViewById(R.id.yesno_message_label);
	    Button yesLabel = (Button)findViewById(R.id.yesno_yes_label);
	    Button noLabel = (Button)findViewById(R.id.yesno_no_label);
	    
	    messageLabel.setText(message);
	    yesLabel.setText(yesButton);
	    noLabel.setText(noButton);
	    
	    show();
	    
	    noLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	dismiss();
            }
        });
	    
	    final View.OnClickListener cmd = listener;
	    
	    yesLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	cmd.onClick(v);
            	dismiss();
            }
        });
	  
	}

}
