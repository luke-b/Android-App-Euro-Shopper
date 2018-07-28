package com.buyagent;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


import android.content.Context;

import com.parse.ParseFile;
import com.parse.ParseObject;



public class ProductTweet extends ParseObject {

	private final static String DATA_FILE = "tweetprefs.txt";
	
	private String euroPrice;
	private String productName;
	private String comments;
	private String townName;
	private String storeBrand;
	private String countryName;
	private String photoId;	
	
	private int countryIndex;

	private Context ctx;

	private String email;
	
	public ProductTweet(Context ctx) {
		super("ProductTweet");
		this.ctx = ctx;
		loadPreferences();
	}
	
	public ProductTweet(String euroPrice,
			String productName, String comments, String townName,
			String storeBrand, String countryName, ParseFile image,String email,int countryIndex,Context ctx) {
		super("ProductTweet");
		this.euroPrice = euroPrice;
		this.productName = productName;
		this.comments = comments;
		this.townName = townName;
		this.storeBrand = storeBrand;
		this.countryName = countryName;
		//this.photoId = image;
		this.countryIndex = countryIndex;
		this.email = email;
		
	
		
		put("price",euroPrice);
		put("productName",productName);
		put("comments",comments);
		put("townName",townName);
		put("storeBrand",storeBrand);
		put("countryName",countryName);
		if (image != null) {
		put("photoId",image);
		}
		put("countryIndex",countryIndex);
		put("email",email);
		this.ctx = ctx;
	}
	
	public void loadPreferences() {
		String data = loadData(DATA_FILE);
		if (data != null) {
			String cells[] = data.trim().split(";");
			System.out.println("data: " + data);
			System.out.println("storeBrand: " + cells[0] );
			System.out.println("townName: " + cells[1]);
			System.out.println("countryIndex: " + cells[2]);
			//storeBrand + "|" + townName + "|" + countryIndex;
			
			this.storeBrand = cells[0];
			this.townName = cells[1];
			this.countryIndex = Integer.parseInt(cells[2]);
		}
	}
	
	
	
	public String getTownName() {
		return townName;
	}

	public String getStoreBrand() {
		return storeBrand;
	}

	public int getCountryIndex() {
		return countryIndex;
	}

	public void savePreferences() {
		
		  FileOutputStream fos;

			try {
			
				String data = storeBrand + ";" + townName + ";" + countryIndex;
				
				
				fos = ctx.openFileOutput(DATA_FILE, Context.MODE_PRIVATE); 
				
				int b = data.getBytes().length;
				fos.write(b);
				fos.write(data.getBytes());
				fos.flush();
				fos.close();
			
				
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		
	}
	
	
	public String loadData(String fileName) {
		  
		 FileInputStream fis;

			try {
				
				fis = ctx.openFileInput(fileName);
				
				BufferedReader input =  new BufferedReader(new InputStreamReader(fis), 1024*8);
				
				String data = input.readLine();
								
				fis.close();
				
				return data;
				
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
			
			return null;
	
	}

	public void setImageFile(ParseFile file) {
		if (file != null) {
			put("photoId",file);
		}
	}
	
	
	
	

}
