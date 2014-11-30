package ca.carleton.michelleburrows.networklogviewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PackageListActivity extends Activity {
	private static final String TAG = "NETLOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.package_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_package_list, container, false);
            return rootView;
        }
        

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
        	super.onActivityCreated(savedInstanceState);

            try {
            	
            	String pname = getActivity().getApplicationContext().getPackageName();
            	String[] CMDLINE_GRANTPERMS = { "su", "-c", null };
            	if (getActivity().getApplicationContext().getPackageManager().checkPermission(android.Manifest.permission.READ_LOGS, pname) != 0) {
            	    Log.d(TAG, "we do not have the READ_LOGS permission!");
            	    if (android.os.Build.VERSION.SDK_INT >= 16) {
            	        Log.d(TAG, "Working around JellyBeans 'feature'...");
            	        try {
            	            // format the commandline parameter
            	            CMDLINE_GRANTPERMS[2] = String.format("pm grant %s android.permission.READ_LOGS", pname);
            	            java.lang.Process p = Runtime.getRuntime().exec(CMDLINE_GRANTPERMS);
            	            int res = p.waitFor();
            	            Log.d(TAG, "exec returned: " + res);
            	            if (res != 0)
            	                throw new Exception("failed to become root");
            	        } catch (Exception e) {
            	            Log.d(TAG, "exec(): " + e);
            	        }
            	    }
            	} else
            	    Log.d(TAG, "we have the READ_LOGS permission already!");
            	
            	
            	Process process = Runtime.getRuntime().exec("logcat -c | grep HelloWorld!");
            	 BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            	 StringBuilder log = new StringBuilder();
            	 String line = "";
            	 while ((line = br.readLine()) != null) {
            		 log.append(line + "\n");
            	 }
            	 br.close();

                 TextView tv = (TextView)getView().findViewById(R.id.temp_text_view);
                 tv.setText(log.toString());
            } catch (IOException e) { 
            	Log.e(TAG, "An error occurred reading the logs", e); 
            }
            
            //TODO parse logs for pids (save all data permanently and clear logs?)
            //String[] pkgNames = this.getActivity().getApplicationContext().getPackageManager().getPackagesForUid(uid);
        }
    }

}
