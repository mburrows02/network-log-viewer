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

            try {
            	Process process = Runtime.getRuntime().exec("logcat -d | grep HelloWorld!");
            	 BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            	 StringBuilder log = new StringBuilder();
            	 String line = "";
            	 while ((line = br.readLine()) != null) {
            		 log.append(line);
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
