package com.buyagent;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class RateInputDialog extends Dialog {

	

	public RateInputDialog(String title,
						   String currency,
						   String rate,
						   String date,
						   Context context,
						   RateUpdateInterface rInt,
						   View.OnClickListener listener) {
		
		super(context);
		
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
	    setContentView(R.layout.rate);
	    setTitle(title);
	    setCancelable(true);
	    setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.icon);
	    
	    TextView currLabel = (TextView)findViewById(R.id.rate_rate_label);
	    EditText rateInput = (EditText)findViewById(R.id.rate_rate_input);
	    TextView dateLabel = (TextView)findViewById(R.id.rate_date_label);
	    
	    currLabel.setText(currency);
	    rateInput.setText(rate);
	    dateLabel.setText(date);
	    
	    show();
	    
	    Button yesLabel = (Button)findViewById(R.id.rate_yes_label);
	    Button noLabel = (Button)findViewById(R.id.rate_no_label);
	    
	    noLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	dismiss();
            }
        });
	    
	    final View.OnClickListener cmd = listener;
	    
	    final String rUp = rateInput.getText().toString();
	    final String dUp = dateLabel.getText().toString();
	    final RateUpdateInterface r = rInt;
	    
	    yesLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            
            	r.changeRate(getRate(), dUp);
            	cmd.onClick(v);
            	dismiss();
            	
            }
        });
	    
	}
	
	private String getRate() {
		EditText rateInput = (EditText)findViewById(R.id.rate_rate_input);
		return rateInput.getText().toString();
	}

}
