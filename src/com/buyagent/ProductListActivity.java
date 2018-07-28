package com.buyagent;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ProductListActivity extends ListActivity {

String[] DayOfWeek = {"Sunday", "Monday", "Tuesday",
  "Wednesday", "Thursday", "Friday", "Saturday"
};

   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       //setContentView(R.layout.main);
       setListAdapter(new ArrayAdapter<String>(this,
         R.layout.row, R.id.weekofday, DayOfWeek));
   }
}