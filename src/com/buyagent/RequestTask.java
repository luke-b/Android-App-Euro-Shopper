package com.buyagent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

class RequestTask extends AsyncTask<String, String, String>{

	private boolean errored = false;
	private WebUpdateInterface report = null;
	
	
	
    public WebUpdateInterface getReport() {
		return report;
	}

	public void setReport(WebUpdateInterface report) {
		this.report = report;
	}

	@Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        
        errored = false;
        
        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            	
            	System.out.println("HTTP STATUS OK");
            	
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                               
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        	System.out.println("HTTP CLIENT PROTOCOL EXCEPTION ");
        	e.printStackTrace(System.out);
        	errored = true;
        } catch (IOException e) {
            //TODO Handle problems..
        	System.out.println("HTTP IO EXCEPTION " + e.getMessage());
        	errored = true;
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String responseBody) {
        super.onPostExecute(responseBody);
       
        if (report != null) {
        	
        	if (responseBody != null) {
        	
        		int pos = responseBody.indexOf("EUR") + 3 + 1;
        		int end = responseBody.substring(pos).indexOf('\n');
        	
        		String value = responseBody.substring(pos, pos+end);
        		String date = responseBody.substring(0,10);
        	
        		value = value.replace(',', '.');
        		
        		report.webUpdateFinished(false, "", value, date);
        	} else {
        		
        		report.webUpdateFinished(true, "", null, null);
        	}
        }
        
    }
    
    public boolean isErrored() {
    	return errored;
    }
}