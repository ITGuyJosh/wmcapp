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
import android.widget.Button;
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

public class ViewJobs extends AppCompatActivity {

    //declare variables
    DataConn dataConn;
    ListView listJobs;
    Integer userid;
    ProgressBar pbbar;
    Toolbar toolbar;
    EditText searchJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_jobs);

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

        //get jobs
        ListJobs getAllJobs = new ListJobs();
        getAllJobs.execute();

    }

    // comparator for use in comparing the data for sorting
    // selecting that  the status is the one that should be sorted
    public Comparator<Map<String, String>> mapComparator = new Comparator<Map<String, String>>() {
        public int compare(Map<String, String> m1, Map<String, String> m2) {
            return m1.get("B").compareTo(m2.get("B"));
        }
    };

    //asynctask for getting a list of jobs
    public class ListJobs extends AsyncTask<String, String, String> {
        //declare local variables
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
            Toast.makeText(ViewJobs.this, r, Toast.LENGTH_SHORT).show();
            // An array of strings we use to reference the map.
            String[] from={"A","B"};

            // sorting algorithm to sort by
            Collections.sort(joblist, mapComparator);

            //an array of insts that reference the ids for the labels.
            int[] views = {R.id.lbljobid,R.id.lbljobtitle};
            final SimpleAdapter ADA = new SimpleAdapter(ViewJobs.this,joblist,R.layout.all_jobs_list,from,views);
            // The list adapter we're going to use to convert our arrays into the listview.
            listJobs.setAdapter(ADA);
            listJobs.setOnItemClickListener(new AdapterView.OnItemClickListener() { //When the buttons is clicked.
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Hashmap links strings with objects
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA.getItem(position);
                    String jobid = (String) obj.get("A");
                    String jobTitle = (String) obj.get("B");

                    //loadup only that jobs information through passing id
                    Intent intent = new Intent(ViewJobs.this, ApplyForJob.class);
                    intent.putExtra("jobid", jobid);
                    intent.putExtra("userid", userid);
                    intent.putExtra("manage", 2);
                    startActivity(intent);
                }
            });

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
        //background function
        protected String doInBackground(String... params) {
            try{
                //verifying database connection
                Connection conn = dataConn.CONN();
                if (conn == null) {
                    z = "Error in connection with SQL server";
                }else{
                    //query db to retrieve id and title for jobs that are active
                    String query = "SELECT id, title FROM jobs WHERE active = 1";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ResultSet rs= ps.executeQuery();

                    //evaluating resultset into hashmap and arraylist
                    while (rs.next()){
                        Map<String,String> datanum = new HashMap<String,String>();
                        datanum.put("A",rs.getString("id"));
                        datanum.put("B",rs.getString("title"));
                        joblist.add(datanum);
                    }

                    z = "All jobs loaded.";
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
