package com.buyagent;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

public class ReporterTabWidget  extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
	   	
	 super.onCreate(savedInstanceState);
	   
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	   
	    setContentView(R.layout.reporter);

	    
	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, ShopInfoActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("shop").setIndicator("Obchod",
	                      res.getDrawable(R.drawable.iconidea))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, ProductListActivity.class);
	    spec = tabHost.newTabSpec("item").setIndicator("Nákup",
	                      res.getDrawable(R.drawable.iconmsg))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, CommentActivity.class);
	    spec = tabHost.newTabSpec("comment").setIndicator("Komentáø",
	                      res.getDrawable(R.drawable.iconads))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	}
	
}
