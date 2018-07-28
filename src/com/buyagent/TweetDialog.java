package com.buyagent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.parse.ParseFile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class TweetDialog extends Dialog {

	
	private int selectedIndex = -1;
	private Context ctx;
	
	private ProductTweet prefs;
	private String userEmail;
	
	public TweetDialog(String title,
					   String price,
					   Context context,
					   RateUpdateInterface rInt,
					   String userEmail) {
		
		super(context);
		
		this.userEmail = userEmail;
		
		this.ctx = context;
		
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
	    setContentView(R.layout.tweet);
	    setTitle(title);
	    setCancelable(true);
	    setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.icon);
	
	    Spinner sSpin = (Spinner) findViewById(R.id.countryName1);
	    
	    prefs = new ProductTweet(context);
	    
		TextView townNameTV = (TextView)findViewById(R.id.townNameText1);
		TextView storeBrandTV = (TextView)findViewById(R.id.storeBrandText1);
	    
	    sSpin.setSelection(prefs.getCountryIndex());
	    townNameTV.setText(prefs.getTownName());
	    storeBrandTV.setText(prefs.getStoreBrand());
	    
	    
	    sSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
	        public void onItemSelected(AdapterView parent,
	                View view, int pos, long id) {
	                selectedIndex= pos;
	            }
	        public void onNothingSelected(AdapterView arg0) { }
	        });
	    
	   
	    
	    ((TextView)findViewById(R.id.euroLabel1)).setText(price);
	    
	    
	 
	   
	    
	 
	  //  final View.OnClickListener cmd = listener;
	    
	//    final String rUp = rateInput.getText().toString();
	//    final String dUp = dateLabel.getText().toString();
	    final RateUpdateInterface r = rInt;
	    
	
	    
	   
	    
	}
	
	
	public void setYesListener(final View.OnClickListener listener2) {
		
		
		   Button yesLabel = (Button)findViewById(R.id.tweet_yes_label);
		 yesLabel.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	if (checkProductTweet()) {   
	            		
	            		if (listener2 != null) {
	            			listener2.onClick(v);
	            		}
	            	} else {
	            		
	            		AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
	            		alertDialog.setTitle("Info");
	            		alertDialog.setMessage("Vyplòte prosím název produktu, prodejnu a mìsto.");
	            		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	            		   public void onClick(DialogInterface dialog, int which) {
	            		      // here you can add functions
	            		   }
	            		});
	            		alertDialog.setIcon(R.drawable.icon);
	            		alertDialog.show();
	            		
	            		
	            	}
	            }
	        });
	}
	
	public void setNoListener(final View.OnClickListener listener) {
		 Button noLabel = (Button)findViewById(R.id.tweet_no_label);
		   noLabel.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	if (listener != null) {
	            		listener.onClick(v);
	            	}
	            	
	            	dismiss();
	            }
	        });
		    
	}
	
	
	
	public boolean checkProductTweet() {
		
		String productName = ((TextView)findViewById(R.id.productNameText1)).getText().toString();
		String townName = ((TextView)findViewById(R.id.townNameText1)).getText().toString();
		String storeBrand = ((TextView)findViewById(R.id.storeBrandText1)).getText().toString();
		
		if (!productName.equals("") && !townName.equals("") && !storeBrand.equals("")) {
		} else {
			return false;
		}
	
		return true;
	}
	
	
	public ProductTweet getProductTweet(ParseFile image) {
		
		String euroPrice = ((TextView)findViewById(R.id.euroLabel1)).getText().toString();
		String productName = ((TextView)findViewById(R.id.productNameText1)).getText().toString();
		
		
		
		String comment = ((TextView)findViewById(R.id.commentText1)).getText().toString();
		String townName = ((TextView)findViewById(R.id.townNameText1)).getText().toString();
		String storeBrand = ((TextView)findViewById(R.id.storeBrandText1)).getText().toString();
		
		
	
		
		Spinner cn = ((Spinner)findViewById(R.id.countryName1));
		String countryName = cn.getSelectedItem().toString();
		
		String photoId = "null";
		
    	ProductTweet pt = new ProductTweet(euroPrice,  //euroPrice
    									   productName,  //productName
    									   comment,  //comments
    									   townName,  //townName
    									   storeBrand,  //storeBrand
    									   countryName,  //countryName
    									   image, //photoId 
    									   userEmail,
    									   selectedIndex,ctx);  
    	
    	pt.savePreferences();
   // 	pt.saveInBackground();
    	
    	return pt;
		
		
	}
	
	
	public boolean sendProductTweet() {
		
		
		String euroPrice = ((TextView)findViewById(R.id.euroLabel1)).getText().toString();
		String productName = ((TextView)findViewById(R.id.productNameText1)).getText().toString();
		
		
		
		String comment = ((TextView)findViewById(R.id.commentText1)).getText().toString();
		String townName = ((TextView)findViewById(R.id.townNameText1)).getText().toString();
		String storeBrand = ((TextView)findViewById(R.id.storeBrandText1)).getText().toString();
		
		
		if (!productName.equals("") && !townName.equals("") && !storeBrand.equals("")) {
		} else {
			return false;
		}
 		
		
		Spinner cn = ((Spinner)findViewById(R.id.countryName1));
		String countryName = cn.getSelectedItem().toString();
		
		String photoId = "null";
		
    	ProductTweet pt = new ProductTweet(euroPrice,  //euroPrice
    									   productName,  //productName
    									   comment,  //comments
    									   townName,  //townName
    									   storeBrand,  //storeBrand
    									   countryName,  //countryName
    									   null, //photoId 
    									   userEmail,
    									   selectedIndex,ctx);  
    	
    	pt.savePreferences();
    	pt.saveInBackground();
    	
    	return true;
	}

	
public boolean sendProductTweet(ParseFile image) {
		
		
		String euroPrice = ((TextView)findViewById(R.id.euroLabel1)).getText().toString();
		String productName = ((TextView)findViewById(R.id.productNameText1)).getText().toString();
		
		
		
		String comment = ((TextView)findViewById(R.id.commentText1)).getText().toString();
		String townName = ((TextView)findViewById(R.id.townNameText1)).getText().toString();
		String storeBrand = ((TextView)findViewById(R.id.storeBrandText1)).getText().toString();
		
		
		if (!productName.equals("") && !townName.equals("") && !storeBrand.equals("")) {
		} else {
			return false;
		}
 		
		
		Spinner cn = ((Spinner)findViewById(R.id.countryName1));
		String countryName = cn.getSelectedItem().toString();
		
		String photoId = "null";
		
    	ProductTweet pt = new ProductTweet(euroPrice,  //euroPrice
    									   productName,  //productName
    									   comment,  //comments
    									   townName,  //townName
    									   storeBrand,  //storeBrand
    									   countryName,  //countryName
    									   image, //photoId 
    									   userEmail,
    									   selectedIndex,ctx);  
    	
    	pt.savePreferences();
    	pt.saveInBackground();
    	
    	return true;
	}

	
	
	public boolean isPhotoRequired() {
		
		CheckBox cb = (CheckBox)findViewById(R.id.tweetPhotoCheck1);
		
		if (cb.isChecked()) {
			return true;
		}
		
		return false;
	}


	public void resetValues() {
		
		((TextView)findViewById(R.id.productNameText1)).setText("");
		((TextView)findViewById(R.id.townNameText1)).setText("");
		((TextView)findViewById(R.id.storeBrandText1)).setText("");
		
	}



	


}