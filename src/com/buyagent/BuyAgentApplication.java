/**
 * 
 */
package com.buyagent;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import rsg.mailchimp.api.MailChimpApiException;
import rsg.mailchimp.api.lists.ListMethods;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.text.InputFilter;
import android.util.*;

/**
 * @author Lzu
 *
 */
public class BuyAgentApplication extends Application {
	
	private static final String TAG = "BuyAgentApp";

	private static final String RATE_UPDATE_URL = "http://www.cnb.cz/cs/financni_trhy/devizovy_trh/kurzy_devizoveho_trhu/denni_kurz.txt";
	
	private static final String DATA_FILE = "runcount.txt";
	
	private static final String DATA_FILENAME = "data.txt";
	
	private final static int CHAR_LIMIT = 9;
	
	private String fcValue = "0";
	private String lcValue = "0";
	
	private String fcCode = "EUR";
	private String lcCode = "CZK";
	
	private String fcTotal = "0";
	private String lcTotal = "0";
	
	private String cValue = "25.3";
	private String cDate = "";
	
	private boolean rateLoaded = false;

	private String userEmail;
	
	private Vector<ProductTweet> tweets = new Vector<ProductTweet>();
	private Vector<ParseFile> parseFiles = new Vector<ParseFile>();
	
	
	public boolean getIsRateLoaded() {
		return rateLoaded;
	}
	
	public void setRateLoaded() {
		this.rateLoaded = true;
	}
	
	
	/**
	 * 
	 */
	
	public BuyAgentApplication() {
		// TODO Auto-generated constructor stub
		Log.i(TAG, "BuyAgentApplication() init");
		System.out.println("BuyAgentApplication() init");
		
		
		
	}
	
	
	public boolean isZero() {
		
		if (fcValue.equals("0")) {
			return true;
		}
		
		return false;
	}
	
	
	public void loadData(DataReadInterface report) {
	  
		 FileInputStream fis;

			try {
			
				String data = cDate + "," + cValue;
				
				fis = openFileInput(DATA_FILENAME);
											
				int lenght = fis.read();
				
				String rDate = "";
				String rValue = "";
				
				if (lenght != -1) {
					int b;
					int pos = 0;
					byte buf[] = new byte[lenght];
				
					while ((b = fis.read()) != -1) {
						buf[pos] = (byte)b;
						pos++;
					}
					
					String s = new String(buf);
					String t[] = s.split(",");
					
					rDate = t[0];
					rValue = t[1];
					
					cValue = rValue;
					cDate = rDate;
					
					if (report != null) {
						report.dataReadUpdate(false,1, "", rDate, rValue);
					}
				}
								
				fis.close();
				
			} catch (FileNotFoundException e) {
				if (report != null) {
					report.dataReadUpdate(true,1, e.getLocalizedMessage(), null, null);
				}
			} catch (IOException e) {
				if (report != null) {
					report.dataReadUpdate(true,1, e.getLocalizedMessage(), null, null);
				}
			}
	
	}
	
	public void saveData(DataReadInterface report) {
		
	    FileOutputStream fos;

		try {
		
			String data = cDate + "," + cValue;
			
			fos = openFileOutput(DATA_FILENAME, Context.MODE_PRIVATE); 
			
			int b = data.getBytes().length;
			fos.write(b);
			fos.write(data.getBytes());
			fos.flush();
			fos.close();
			
			if (report != null) {
				report.dataReadUpdate(false, 0,getString(R.string.rate_was_stored), null, null);
			}
			
		} catch (FileNotFoundException e) {
			if (report != null) {
				report.dataReadUpdate(true, 0,e.getLocalizedMessage(), null, null);
			}
		} catch (IOException e) {
			if (report != null) {
				report.dataReadUpdate(true, 0,e.getLocalizedMessage(), null, null);
			}
		}
		
	}
	

	public void savePreferences(String data) {
		
		  FileOutputStream fos;

			try {
			
					
				fos = getBaseContext().openFileOutput(DATA_FILE, Context.MODE_PRIVATE); 
				
				int b = data.getBytes().length;
				fos.write(b);
				fos.write(data.getBytes());
				fos.flush();
				fos.close();
			
				
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		
	}
	
	
	public String loadData() {
		  
		 FileInputStream fis;

			try {
				
				fis = getBaseContext().openFileInput(DATA_FILE);
				
				BufferedReader input =  new BufferedReader(new InputStreamReader(fis), 1024*8);
				
				String data = input.readLine();
								
				fis.close();
				
				return data;
				
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
			
			return null;
	
	}
	
	
	
	private boolean addToList(String emailAddy) {
    	
		if (emailAddy != null) {
		ListMethods listMethods = new ListMethods(getBaseContext().getResources().getText(R.string.mc_api_key));
		String message = "Signup successful!";
		try {
			listMethods.listSubscribe(getBaseContext().getText(R.string.mc_list_id).toString(), emailAddy);
			return true;
		} catch (MailChimpApiException e) {
			Log.e("MailChimp", "Exception subscribing person: " + e.getMessage());
			return false;
		} finally {
		}
		} 
		
		return false;
    }
	
	
	public void updateRateOnline2(WebUpdateInterface report) {
		
	
		  // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(RATE_UPDATE_URL);

	    try {
	        // Execute HTTP Post Request
	        ResponseHandler<String> responseHandler=new BasicResponseHandler();
	        String responseBody = httpclient.execute(httppost, responseHandler);
	    
	        if (responseBody.indexOf(fcCode) != -1) {
	        	
	        	int pos = responseBody.indexOf(fcCode) + fcCode.length() + 1;
	        	int end = responseBody.substring(pos).indexOf('\n');
	        	
	        	String value = responseBody.substring(pos, end);
	        	String date = responseBody.substring(0,9);
	        	
	        	if (report != null) {
		    		   report.webUpdateFinished(false, getString(R.string.rate_was_found) + " " + value + ", " + date + " ...", value, date);
				} 
	        	
	        } else {
	        	if (report != null) {
		    		report.webUpdateFinished(true, getString(R.string.rate_not_found), null, null);
				}
	        }
	        	        
	    	
	    } catch (ClientProtocolException e) {
	    	if (report != null) {
	    		report.webUpdateFinished(true, e.getLocalizedMessage(), null, null);
			}
	    } catch (IOException e) {
	    	if (report != null) {
	    		report.webUpdateFinished(true, e.getLocalizedMessage(), null, null);
			}
	    } catch (Throwable t) {
	    	if (report != null) {
	    		report.webUpdateFinished(true, t.getLocalizedMessage(), null, null);
			}
	    }
	    
	   
	
	}
	
	
	
	public void updateRateOnline(WebUpdateInterface repor) {
		
		RequestTask rt = new RequestTask();
		rt.setReport(repor);
		rt.execute(RATE_UPDATE_URL);
		
	}

	
		
	
	public String getcDate() {
		return cDate;
	}



	public void setcDate(String cDate) {
		this.cDate = cDate;
	}






	public String getcValue() {
		return cValue;
	}




	public void setcValue(String cValue) {
		this.cValue = cValue;
	}




	public String getFcValue() {
		return fcValue;
	}




	public String getLcValue() {
		return lcValue;
	}




	public String getFcCode() {
		return fcCode;
	}




	public String getLcCode() {
		return lcCode;
	}




	public String getFcTotal() {
		return fcTotal;
	}




	public String getLcTotal() {
		return lcTotal;
	}




	public void addDigit(String digit) {
		
		if (digit.equals(".") && fcValue.equals("0")) {
			
			fcValue = "0.";
			
		} else 			
		if (digit.equals("0") && fcValue.equals("0")) {
			
		} else {
		
			if (fcValue.equals("0")) {
				fcValue = "";
			}
						
			if (fcValue.length() + 1 < CHAR_LIMIT) {
		
				if (digit.equals(".")) {  // in case dot is beign added
			
					if (fcValue.indexOf('.') == -1) { // in case dot not present
				
						fcValue = fcValue + ".";
				
					} else {
			
						// do nothing
					}
			
				} else { // numbers
				
					fcValue = fcValue + digit;
			
				}
			}
		}
		
		computeValues();
	}
	
	public void removeDigit() {
		
		if (fcValue.length() - 1 >= 0) {
			fcValue = fcValue.substring(1);
		}
		
		computeValues();
	}
	
	public void addToTotal() {
		
		BigDecimal fcv = new BigDecimal((fcValue.equals("")?"0":fcValue));
		BigDecimal fct = new BigDecimal(fcTotal);
		fcTotal = fct.add(fcv).toPlainString();
		
		BigDecimal lcv = new BigDecimal(lcValue);
		BigDecimal lct = new BigDecimal(lcTotal);
		lcTotal = lct.add(lcv).toPlainString();
		
		clearValue();
	}
	
	public void removeFromTotal() {
		
		BigDecimal fcv = new BigDecimal((fcValue.equals("")?"0":fcValue));
		BigDecimal fct = new BigDecimal(fcTotal);
		fcTotal = fct.subtract(fcv).toPlainString();
		
		BigDecimal lcv = new BigDecimal(lcValue);
		BigDecimal lct = new BigDecimal(lcTotal);
		lcTotal = lct.subtract(lcv).toPlainString();
		
		clearValue();
	}
	
	
	public void computeValues() {
		
		BigDecimal lcv = new BigDecimal((fcValue.equals("")?"0":fcValue));
		lcv = lcv.multiply(new BigDecimal(cValue));
		
		lcValue = lcv.toPlainString();

		BigDecimal fct = new BigDecimal(fcTotal);
		BigDecimal rt = new BigDecimal(cValue);
		BigDecimal lct = fct.multiply(rt);
		
		lcTotal = lct.toPlainString();
		
		// not affected bellow
		//fcTotal = "0";
		//lcTotal = "0";  
		
	}
	
	public void clearValue() {
	
		fcValue = "0";
		lcValue = "0";
		computeValues();
	}
	
	public void resetValues() {
		
		fcValue = "0";
		lcValue = "0";
		fcTotal = "0";
		lcTotal = "0";
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		Log.i(TAG, "BuyAppAgent onCreate()");
		System.out.println("onCreate()");
		
	    Parse.initialize(this, "iOQMtaLUaX3E29wfrspjSeF46OPtNVCTkcovocAC", "Aq3BJ6L3hAHFRpMtwWxYcCLukYUUqqozeWBJ2aPG"); 
	
	   //  ParseObject testObject = new ParseObject("TestObject");
	   // testObject.put("foo", "bar");
	   // testObject.
	   // testObject.saveInBackground();
	    
	    Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");
		for (Account account : accounts) {
		 // this is where the email should be in: 
		  userEmail = account.name;
		 
		}
		
		
		String runCount = loadData();
		
		boolean save = true;
		
		if (runCount == null) {
			runCount = "1";
			
			if (userEmail != null) {
			  ParseObject po = new ParseObject("NewUser");
			  po.put("email", userEmail);
			  po.saveInBackground();
			}
			
			
			  new Thread(new Runnable() {
				    public void run() {
				    	if (addToList(userEmail)) {
				    		savePreferences("2");
						}
				    }
				  }).start();
			
			
			
			
		} else {
			
			int cnt = 1;
			
			try {
			cnt = Integer.parseInt(runCount);
			cnt++;
			} catch (NumberFormatException e) {
			}
			
			runCount = cnt +"";
		}
		
		if (save) {
			savePreferences(runCount);
		}
		
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	public void addProductTweet(ProductTweet productTweet) {
		tweets.add(productTweet);
	}
	
	public void removeProductTweet(ProductTweet productTweet) {
		tweets.remove(productTweet);
	}

	public void addParseFile(ParseFile pf) {
		 parseFiles.add(pf);
	}
	
	public void removeParseFile(ParseFile pf) {
		parseFiles.remove(pf);
	}
	
	

}
