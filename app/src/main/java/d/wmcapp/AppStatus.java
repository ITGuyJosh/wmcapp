package d.wmcapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppStatus extends AppCompatActivity {

    DataConn dataConn;
    ListView listJobs;
    Integer userid;
    ProgressBar pbbar;
    Toolbar toolbar;
    EditText searchJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_status);

        //instaniate & initalise
        dataConn = new DataConn();
        listJobs = (ListView)findViewById(R.id.listAllJobs);
        userid = getIntent().getExtras().getInt("userid");
        pbbar = (ProgressBar)findViewById(R.id.pbbar);
        searchJobs = (EditText) findViewById(R.id.searchJobs);

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");
        setSupportActionBar(toolbar);

        //get all jobs and sort them
        ListJobStatus getJobStatus = new ListJobStatus();
        getJobStatus.execute();

    }

    // comparator for use in comparing the data for sorting
    // selecting that  the status is the one that should be sorted
    public Comparator<Map<String, String>> mapComparator = new Comparator<Map<String, String>>() {
        public int compare(Map<String, String> m1, Map<String, String> m2) {
            return m1.get("B").compareTo(m2.get("B"));
        }
    };

    //AsyncTaskfor listing job status
    public class ListJobStatus extends AsyncTask<String, String, String> {
        //declare variables
        String status;
        String z = "";
        List<Map<String, String>> joblist = new ArrayList<Map<String,String>>();

        protected void onPreExecute(){
            // Set the progress bar to visible to tell the user something is happening.
            pbbar.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(String r){
            // Once everything is done set the visibility of the progress bar to invisible
            pbbar.setVisibility(View.GONE);
            //Post the string r which contains info about what has happened.
            Toast.makeText(AppStatus.this, r, Toast.LENGTH_SHORT).show();

            // applying collections sort against arraylist & comparator benchmark
            Collections.sort(joblist, mapComparator);

            // An array of strings we use to reference our map.
            String[] from={"A","B"};
            //an array of ints that reference the ids for our labels.
            int[] views = {R.id.lblthree,R.id.lblfour};
            // The list adapter to convert arrays into the listview.
            final SimpleAdapter ADA = new SimpleAdapter(AppStatus.this,joblist,R.layout.all_display_list,from,views);
            //set adapter to the listview
            listJobs.setAdapter(ADA);

            //search functionality
            searchJobs.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    ADA.getFilter().filter(s.toString());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
        protected String doInBackground(String... params) {
            try{
                Connection conn = dataConn.CONN();
                if (conn == null) {
                    z = "Error in connection with SQL server";
                }else{
                    // Get all users jobs applied for
                    String query = "SELECT DISTINCT J.id, title, status FROM job_applications AS JA\n" +
                            "LEFT JOIN jobs AS J ON JA.job_id = J.id WHERE JA.user_id = '"+ userid +"'";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ResultSet rs= ps.executeQuery();

                    while (rs.next()){
                        // Setting users jobs to a hashmap
                        Map<String,String> datanum = new HashMap<String,String>();
                        datanum.put("A",rs.getString("title"));
                        if(rs.getInt("Status") == 1){
                            status = "Pending";
                        } else if(rs.getInt("Status") == 2){
                            status = "Accepted";
                        } else {
                            status = "Rejected";
                        }
                        datanum.put("B", status);
                        joblist.add(datanum);
                    }

                    z = "Application status loaded.";
                }
            }catch(Exception ex){
                z=" Error retrieveing data from table.";
            }
            return z;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Loading up main menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //switch case options menu
        switch (item.getItemId()) {
            case R.id.settings_about:
                // About option clicked.
                Intent i = new Intent(getApplicationContext(), HelpScreen.class);
                startActivity(i);
                return true;
            case R.id.settings_help:
                // Help option clicked.
                Intent j = new Intent(getApplicationContext(), Email.class);
                j.putExtra("helpClicked", 1);
                startActivity(j);
                return true;
            case R.id.settings_affiliates:
                // Affiliates option clicked.
                Intent k = new Intent(getApplicationContext(), Affiliates.class);
                startActivity(k);
                return true;
            case R.id.settings_logout:
                // Logout option clicked.
                Intent l = new Intent(getApplicationContext(), Login.class);
                startActivity(l);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
