package com.buyagent;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.Calendar;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.util.*;

public class BuyAgentActivity extends Activity implements RateUpdateInterface,WebUpdateInterface,DataReadInterface {
	
	
	private static final String TAG = "BuyAgentActivity";
	
	private BuyAgentApplication app = null;
	
	Dialog rateDialog = null;
	Dialog cartDialog = null;
	
	Dialog wDialog = null;
	
	Dialog manualDialog = null;
	
	TweetDialog td;
	
	ProgressDialog onlineDialog = null;
	
	Vibrator vibrator = null; 
	
	
	private void vibrate() {
		if (vibrator != null) {
			  vibrator.vibrate(100);
		}
	}
  

	
	
	public void changeRate(String rate, String date) {
		
		app.setcDate(date);
		app.setcValue(rate);
		
		Log.i(TAG, "rate: " + rate);
		Log.i(TAG, "date: " + date);
		
		app.computeValues();
		renderValues();
		
	}
	
	public void updateRateDialog(Dialog rateDialog) {
		TextView cPair = (TextView)rateDialog.findViewById(R.id.currency_pair_label);
    	cPair.setText( app.getFcCode() + " / " + app.getLcCode());
    	
    	TextView cRate = (TextView)rateDialog.findViewById(R.id.current_rate_label);
    	
    	BigDecimal cr = new BigDecimal(app.getcValue());
		NumberFormat crnf = NumberFormat.getInstance();
		crnf.setMinimumFractionDigits(2);
		crnf.setMaximumFractionDigits(2);
		String crstr = crnf.format(cr);
		
		cRate.setText(crstr);    
		
		TextView rDate = (TextView)rateDialog.findViewById(R.id.rate_date_label);
		rDate.setText(app.getcDate());
	}
	
	private void downloadRate() {
	
		onlineDialog = ProgressDialog.show(BuyAgentActivity.this, "", 
								getString(R.string.rate_is_being_downloaded), true);
		onlineDialog.show();
		
		app.updateRateOnline(this);
		 
		
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                      
        Log.i(TAG,"BuyAgentActivity initialized.");
        
        this.app = (BuyAgentApplication)getApplication();
        
    
   
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
       // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.main);
    
        
        Registration.check(this); // register app via Google Macro
        
        TextView tv = (TextView) findViewById(R.id.textView1);
        Button bv = (Button) findViewById(R.id.button4);
       
        TextView tv2 = (TextView) findViewById(R.id.textView2);
        
        TextView tv3 = (TextView) findViewById(R.id.textView3);
        TextView tv5 = (TextView) findViewById(R.id.textView5);
        
        TextView tv4 = (TextView) findViewById(R.id.foreign_label_total);
        TextView tv6 = (TextView) findViewById(R.id.local_label_total);
        
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/lcddot.otf");
        tv.setTypeface(tf);
        tv2.setTypeface(tf);
        tv3.setTypeface(tf);
        tv5.setTypeface(tf);
        tv4.setTypeface(tf);
        tv6.setTypeface(tf);
        bv.setTypeface(tf);
        
        
        
        
        // Number "1" button
        Button btn1 = (Button)findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.addDigit("1");
                app.computeValues();
                renderValues();
                
                // SoundManager.playSound(1, 0);
                vibrate();
              
            }
        });
        
        // Number "2" button
        Button btn2 = (Button)findViewById(R.id.button5);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.addDigit("2");
                app.computeValues();
                renderValues();
                
                // SoundManager.playSound(1, 0);
                vibrate();
            }
        });
        
        // Number "3" button
        Button btn3 = (Button)findViewById(R.id.button9);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.addDigit("3");
                app.computeValues();
                renderValues();
                
                // SoundManager.playSound(1, 0);
                vibrate();
            }
        });
        
                
        // Number "4" button
        Button btn4 = (Button)findViewById(R.id.button2);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.addDigit("4");
                app.computeValues();
                renderValues();
                
                // SoundManager.playSound(1, 0);
                vibrate();
            }
        });
        
        // Number "5" button
        Button btn5 = (Button)findViewById(R.id.button6);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.addDigit("5");
                app.computeValues();
                renderValues();
                
                // SoundManager.playSound(1, 0);
                vibrate();
            }
        });
        
        // Number "6" button
        Button btn6 = (Button)findViewById(R.id.button10);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.addDigit("6");
                app.computeValues();
                renderValues();
                
                // SoundManager.playSound(1, 0);
                vibrate();
            }
        });
        
        // Number "7" button
        Button btn7 = (Button)findViewById(R.id.button3);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.addDigit("7");
                app.computeValues();
                renderValues();
                
                // SoundManager.playSound(1, 0);
                vibrate();
            }
        });
        
        // Number "8" button
        Button btn8 = (Button)findViewById(R.id.button7);
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.addDigit("8");
                app.computeValues();
                renderValues();
                
                // SoundManager.playSound(1, 0);
                vibrate();
            }
        });
        
        // Number "9" button
        Button btn9 = (Button)findViewById(R.id.button11);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.addDigit("9");
                app.computeValues();
                renderValues();
                
                // SoundManager.playSound(1, 0);
                vibrate();
            }
        });
        
                
        // Number "0" button
        Button btn0 = (Button)findViewById(R.id.button8);
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.addDigit("0");
                app.computeValues();
                renderValues();
                
                // SoundManager.playSound(1, 0);
                vibrate();
            }
        });
        
        // Number "." button
        Button btnDot = (Button)findViewById(R.id.button12);
        btnDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.addDigit(".");
                app.computeValues();
                renderValues();
                
                // SoundManager.playSound(1, 0);
                vibrate();
            }
        });
        
        // Number "C" button
        Button btnClear = (Button)findViewById(R.id.button13);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	ToggleButton b = (ToggleButton) findViewById(R.id.tweetButton1);
            	b.setChecked(false);
            	            	
                app.clearValue();
                app.computeValues();
                renderValues();
                
                // SoundManager.playSound(1, 0);
                vibrate();
            }
        });
        
        // Number "+" button
        Button btnPlus = (Button)findViewById(R.id.button15);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	ToggleButton b = (ToggleButton) findViewById(R.id.tweetButton1);
            	b.setChecked(false);
            	
                app.addToTotal();
                app.computeValues();
                renderValues();
                
                // SoundManager.playSound(1, 0);
                vibrate();
            }
        });
        
     
          
        
        // setup "rate" info dialog
        rateDialog = new Dialog(this);
        rateDialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        rateDialog.setContentView(R.layout.euro);
        rateDialog.setTitle(R.string.current_rate);
        rateDialog.setCancelable(true);
        rateDialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.icon);
        
        // Number "rate" button
        Button btnRate = (Button)findViewById(R.id.button14);
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
                updateRateDialog(rateDialog);
            	
            	rateDialog.show();
            	
            	   // SoundManager.playSound(2, 0);
                   vibrate();
            }
        });
        
        Button rateCartCancel = (Button)rateDialog.findViewById(R.id.rate_cancel_button);
        rateCartCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	app.computeValues();
            	renderValues();
            	
            	
            	
            	   // SoundManager.playSound(2, 0);
                   vibrate();
            	
            	rateDialog.dismiss();
            }
        });
        
        Button rateManualInput = (Button)rateDialog.findViewById(R.id.foreign_manual_input);
        rateManualInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	   // SoundManager.playSound(2, 0);
                   vibrate();
            	
            	Calendar c = Calendar.getInstance(); 
            	String newDate = c.get(Calendar.DAY_OF_MONTH) + "." +
            					 ( c.get(Calendar.MONTH) + 1 ) + "." +
            					 c.get(Calendar.YEAR);
   
            	boolean changed = false;
            	
            	new RateInputDialog(getString(R.string.please_enter_value),
    					app.getFcCode() + " / " + app.getLcCode(), 
    					app.getcValue(), 
    					newDate, 
    					BuyAgentActivity.this,
    					BuyAgentActivity.this,
    					new View.OnClickListener() {
    			            @Override
    			            public void onClick(View v) {
    			            	
    			            	   // SoundManager.playSound(2, 0);
    			                   vibrate();
    			            	
    			            	// update dialog values
    			            	// changeRate();
    			            	
    			            	// store the new rate to sd card
    			            	updateRateDialog(rateDialog);
    			            	saveNewRate();
    			            	    			            	
    			            	// update Rate dialog values
    			            	
    			            }

						
        				});
            	
            	
            }
        });
        
        Button rateAutoInput = (Button)rateDialog.findViewById(R.id.foreign_auto_input);
        rateAutoInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	new YesNoDialog(getString(R.string.are_you_sure),
    					getString(R.string.download_rate_from_internet), 
    					getString(R.string.yes_button), 
    					getString(R.string.no_button), 
    					BuyAgentActivity.this, 
    					new View.OnClickListener() {
    			            @Override
    			            public void onClick(View v) {
    			            	
    			            	   // SoundManager.playSound(2, 0);
    			                   vibrate();
    			            	
    			            	// update dialog values
    			            	
    			                downloadRate();
    			            	
    			            }

						
        				});
            	
            }
        });
        
        
        // setup "cart" info dialog
        cartDialog = new Dialog(this);
        cartDialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        cartDialog.setContentView(R.layout.cart);
        cartDialog.setTitle(R.string.shopping_cart_label);
        cartDialog.setCancelable(true);
        cartDialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.icon);
       
        
        // Number "cart" button
        Button btnCart = (Button)findViewById(R.id.button4);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	   // SoundManager.playSound(2, 0);
                   vibrate();
            	
            	// foreign currency value input modified
        		BigDecimal xft2 = new BigDecimal(app.getFcValue());
        		NumberFormat xftnf2 = NumberFormat.getInstance();
        		xftnf2.setMinimumFractionDigits(2);
        		xftnf2.setMaximumFractionDigits(2);
        		String xftTotal2 = xftnf2.format(xft2);
            	
            	Button b = (Button)cartDialog.findViewById(R.id.cart_subtract_button);
            	b.setText(getString(R.string.subtract_item_label) + " (" + xftTotal2 + " " + app.getFcCode() + ")");
            
            	app.computeValues();
            	
            	TextView ft = (TextView)cartDialog.findViewById(R.id.fcTitle);
            	ft.setText(app.getFcCode() + " " + getString(R.string.sum_label) + ":");
            	
            	TextView lt = (TextView)cartDialog.findViewById(R.id.lcTitle);
            	lt.setText(app.getLcCode() + " " + getString(R.string.sum_label) + ":");
            	
            	// local currency total modified
        		BigDecimal lt2 = new BigDecimal(app.getLcTotal());
        		NumberFormat ltnf2 = NumberFormat.getInstance();
        		ltnf2.setMinimumFractionDigits(2);
        		ltnf2.setMaximumFractionDigits(2);
        		String ltTotal2 = ltnf2.format(lt2);
        		
        		
        		// foreign currency total modified
        		BigDecimal ft2 = new BigDecimal(app.getFcTotal());
        		NumberFormat ftnf2 = NumberFormat.getInstance();
        		ftnf2.setMinimumFractionDigits(2);
        		ftnf2.setMaximumFractionDigits(2);
        		String ftTotal2 = ftnf2.format(ft2);
            	
            	
            	
            	TextView ftv = (TextView)cartDialog.findViewById(R.id.cart_foreign_total);
            	ftv.setText(ftTotal2 + " " + app.getFcCode());
            	
            	TextView ltv = (TextView)cartDialog.findViewById(R.id.cart_local_total);
            	ltv.setText(ltTotal2 + " " + app.getLcCode());
            	
            	if (app.getFcValue().equals("0")) {
            		b.setEnabled(false);
            	} else {
            		b.setEnabled(true);
            	}
            	
            	cartDialog.show();
            }
        });
      
        Button btnCartCancel = (Button)cartDialog.findViewById(R.id.cart_cancel_button);
        btnCartCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	   // SoundManager.playSound(2, 0);
                   vibrate();
            	
            	cartDialog.dismiss();
            }
        });
        
        Button btnCartReset = (Button)cartDialog.findViewById(R.id.cart_reset);
        btnCartReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	   // SoundManager.playSound(2, 0);
                   vibrate();
            	
            	new YesNoDialog(getString(R.string.are_you_sure),
            					getString(R.string.ready_to_delete_shopping_cart), 
            					getString(R.string.yes_button), 
            					getString(R.string.no_button), 
            					BuyAgentActivity.this, 
            					new View.OnClickListener() {
            			            @Override
            			            public void onClick(View v) {
            			            	
            			            	app.resetValues();
            			            	app.computeValues();
            			            	renderValues();
            			            	
            			            	if (app.isZero()) {
            			            		
            			            		ToggleButton b = (ToggleButton) findViewById(R.id.tweetButton1);
            			                	b.setChecked(false);
            			            		
            			            	}
            			            	
            			            	   // SoundManager.playSound(2, 0);
            			                   vibrate();
            			            	
            			            	cartDialog.dismiss();
            			             }
                				});
             
            }
        });
        
        Button btnCartSubtract = (Button)cartDialog.findViewById(R.id.cart_subtract_button);
        btnCartSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	app.removeFromTotal();
            	app.computeValues();
            	renderValues();
            	
            	   // SoundManager.playSound(2, 0);
                   vibrate();
            	
            	cartDialog.dismiss();
            }
        });
      
        TextView forTotal = (TextView)findViewById(R.id.foreign_label_total);
        TextView locTotal = (TextView)findViewById(R.id.local_label_total);
        
        forTotal.setText("# " + app.getFcCode() + " " + getString(R.string.sum_label));
        locTotal.setText("# " + app.getLcCode() + " " + getString(R.string.sum_label));
        
        
        
        
        this.app.computeValues();
        this.renderValues();
        
        if (!app.getIsRateLoaded()) {
        
        	app.loadData(this); // load stored rate if available
        	app.setRateLoaded();
        	
        	//try {
        	//vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        	//} catch (Throwable t) {
        	//	vibrator = null;
        	//}
        	
        	 // SoundManager.getInstance();
             // SoundManager.initSounds(this);
             // SoundManager.loadSounds();
        	
        }
        
        
        // star button flashing animation
        
        final Animation animation = new AlphaAnimation(1f, 0f); // Change alpha from fully visible to invisible
        animation.setDuration(1000); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        final Button btn = (Button) findViewById(R.id.tweetButton1);
        btn.startAnimation(animation);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
            	
            	final ToggleButton b = (ToggleButton) findViewById(R.id.tweetButton1);
            	
            	b.setChecked(false);
            	showTweetInfoDialog();
            	
            }
        });
        
        //----
    }
  
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uriImage;
        InputStream inputStream = null;
        
        if (td != null) {   
	    	td.hide();
        }
        
        if ( resultCode == Activity.RESULT_OK) {
            uriImage = data.getData();
            try {
                inputStream = getContentResolver().openInputStream(uriImage);
             //   Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
             //   imageView.setImageBitmap(bitmap);
                
          
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] dat = new byte[16384];
                while ((nRead = inputStream.read(dat, 0, dat.length)) != -1) {
                  buffer.write(dat, 0, nRead);
                }
                buffer.flush();
                
                
                final ParseFile pf = new ParseFile("product-view.jpg",buffer.toByteArray());
                final ProductTweet pto = td.getProductTweet(null);
                app.addProductTweet(pto);
                app.addParseFile(pf);
                pf.saveInBackground(new SaveCallback() {
                	
                	final ProductTweet owner = pto;
                	final ParseFile file = pf;
					
					@Override
					public void done(ParseException a) {
						
						Context context = getApplicationContext();
						CharSequence text = "Fotka " + pto.getString("productName") + " uploadnuta.";
						int duration = Toast.LENGTH_SHORT;

						Toast toast = Toast.makeText(context, text, duration);
						toast.show();
						
						pto.setImageFile(file);
						
						pto.saveInBackground(new SaveCallback() {
								
								@Override
								public void done(ParseException arg0) {
					
									Context context = getApplicationContext();
									CharSequence text = "Popis k " + pto.getString("productName") + " uploadnut.";
									int duration = Toast.LENGTH_SHORT;

									Toast toast = Toast.makeText(context, text, duration);
									toast.show();
									
									app.removeParseFile(file);
									app.removeProductTweet(owner);
									
								}
							});
							
							
					    //	td.dismiss();
		            	
					}
				});
          
                
                
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
           
        }
    }
    
    
    
    
    private void renderValues() {
    	
    	if (this.app != null) {
    		
    		// main user input value - presented without modification
    		String mainValue = this.app.getFcValue();
    		
    		// converted value modified to contain currency code
    		BigDecimal sv = new BigDecimal(this.app.getLcValue());
    		NumberFormat nf = NumberFormat.getInstance();
    		nf.setMinimumFractionDigits(2);
    		nf.setMaximumFractionDigits(2);
    		String subValue = nf.format(sv) + " " + this.app.getLcCode();
    		
    		// local currency total modified
    		BigDecimal lt = new BigDecimal(this.app.getLcTotal());
    		NumberFormat ltnf = NumberFormat.getInstance();
    		ltnf.setMinimumFractionDigits(2);
    		ltnf.setMaximumFractionDigits(2);
    		String ltTotal = ltnf.format(lt);
    		
    		
    		// foreign currency total modified
    		BigDecimal ft = new BigDecimal(this.app.getFcTotal());
    		NumberFormat ftnf = NumberFormat.getInstance();
    		ftnf.setMinimumFractionDigits(2);
    		ftnf.setMaximumFractionDigits(2);
    		String ftTotal = ftnf.format(ft);
    		
    		//set UI values here
    		TextView tm = (TextView)findViewById(R.id.textView1);
    		tm.setText("" + mainValue);
    		
    		TextView tm2 = (TextView)findViewById(R.id.textView2);
    		tm2.setText("" + subValue);
    		
    		TextView tm3 = (TextView)findViewById(R.id.textView3);
    		tm3.setText("" + ftTotal);
    		
    		TextView tm4 = (TextView)findViewById(R.id.textView5);
    		tm4.setText("" + ltTotal);
    		
    	}
    	
    }

	@Override
	public void webUpdateFinished(boolean errored, String error, String rate,
			String date) {
		
			if (errored) {  // show toas with message and close progress
				
				if (onlineDialog != null) {
					if (onlineDialog.isShowing()) {
						onlineDialog.dismiss();
						onlineDialog = null;
					}
				}
				
				Context context = getApplicationContext();
				CharSequence text = error;
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				
			} else {
				
				if (onlineDialog != null) {
					if (onlineDialog.isShowing()) {
						onlineDialog.dismiss();
						onlineDialog = null;
					}
				}
					
				changeRate(rate, date);
				app.saveData(this);
				
				
				if (rateDialog != null) {
					updateRateDialog(rateDialog);
				}
				
			}
		
	}
	
	
	private void showTweetInfoDialog() {
		
		   // setup "rate" info dialog
     wDialog = new Dialog(this,R.style.myBackgroundStyle);
     wDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
     wDialog.setContentView(R.layout.tweetinfo);
     wDialog.setCancelable(true);
     wDialog.show();
      
     Button internetEntry = (Button)wDialog.findViewById(R.id.tweetInfoOk);
     internetEntry.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
        	 wDialog.dismiss();
         }
     });
     
     
     Button wantIt = (Button)wDialog.findViewById(R.id.tweetInfoMore);
     wantIt.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
           	    try {
            	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.steepmax.expenses")));
            	} catch (android.content.ActivityNotFoundException anfe) {
            	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.steepmax.expenses")));
            	}
         
         }
     });
     
     
     Button bannerClick = (Button)wDialog.findViewById(R.id.marketJump);
     bannerClick.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
        	   try {
            	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.steepmax.expenses")));
            	} catch (android.content.ActivityNotFoundException anfe) {
            	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.steepmax.expenses")));
            	}
         
         }
     });
     
     
    	
	}

	
	
	private void showWelcomeDialog() {
		
		   // setup "rate" info dialog
        wDialog = new Dialog(this,R.style.myBackgroundStyle);
        wDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wDialog.setContentView(R.layout.welcome);
        wDialog.setCancelable(false);
        wDialog.show();
    
    	Calendar c = Calendar.getInstance(); 
    	final String newDate = c.get(Calendar.DAY_OF_MONTH) + "." +
    					 ( c.get(Calendar.MONTH) + 1 ) + "." +
    					 c.get(Calendar.YEAR);
        
        
        Button internetEntry = (Button)wDialog.findViewById(R.id.internetEntry);
        internetEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	new YesNoDialog(getString(R.string.are_you_sure),
    					getString(R.string.download_rate_from_internet), 
    					getString(R.string.yes_button), 
    					getString(R.string.no_button), 
    					BuyAgentActivity.this, 
    					new View.OnClickListener() {
    			            @Override
    			            public void onClick(View v) {
    			            	
    			            	   // SoundManager.playSound(2, 0);
    			                   vibrate();
    			            	
    			            	// update dialog values
    			            	
    			                downloadRate();
    			                wDialog.dismiss();
    			            	
    			            }

						
        				});
            	
            	
            	
            }
        });
        
        
        Button manualEntry = (Button)wDialog.findViewById(R.id.manualEntry);
        manualEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	new RateInputDialog(getString(R.string.please_enter_value),
        				app.getFcCode() + " / " + app.getLcCode(), 
        				app.getcValue(), 
        				newDate, 
        				BuyAgentActivity.this,
        				BuyAgentActivity.this,
        				new View.OnClickListener() {
        		            @Override
        		            public void onClick(View v) {
        		            	// SoundManager.playSound(2, 0);
        		             	vibrate();
        		            	saveNewRate();
        		            	wDialog.dismiss();
        		            }
        				});
            	
            	
            }
        });
		
	}

	@Override
	public void dataReadUpdate(boolean errored, int type, String message,
			String date, String value) {
	
			String messageShow = null;
	
			if (type == 1) {  // load
				
				if (errored) {  // load failed
					
					//messageShow = "Aktuální kurz " + app.getFcCode() + " nebyl naèten.";
					//when load fails, then welcome dialog should be displayed
					
					messageShow = null;
					
					showWelcomeDialog();
					
				} else {  // load ok
					
					messageShow = getString(R.string.rate_label) + " " + app.getFcCode() + " = " + value + " " + getString(R.string.of_date_label) + " " + date;
					changeRate(value, date);
					
				}
								
			} else {  // save
				
				if (errored) {  // save failed
					
					messageShow = getString(R.string.unable_to_store_current_rate) + message;
					
				} else {  // save ok
				
					messageShow = getString(R.string.rate_label) + " " + app.getFcCode() + " " + getString(R.string.was_saved_label);
					
				}
				
			}
		
			if (message != null && messageShow != null) {
				Toast toast = Toast.makeText(getApplicationContext(), messageShow, Toast.LENGTH_LONG);
				toast.show();
			}
				
	}
	
	private void saveNewRate() {
		
		app.saveData(this);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	  //  MenuInflater inflater = getMenuInflater();
	  //  inflater.inflate(R.menu.optionsmenu, menu);
	    return true;
	}
	
    
}