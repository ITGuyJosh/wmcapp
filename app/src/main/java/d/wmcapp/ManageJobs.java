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

public class ManageJobs extends AppCompatActivity {

    //declare variables
    DataConn dataConn;
    Button btnAddJob;
    ListView listJobs;
    Integer userid;
    ProgressBar pbbar;
    Toolbar toolbar;
    EditText searchJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_jobs);

        //instaniate & initalise
        dataConn = new DataConn();
        btnAddJob = (Button)findViewById(R.id.btnAddJob);
        listJobs = (ListView)findViewById(R.id.listAllJobs);
        userid = getIntent().getExtras().getInt("userid");
        pbbar = (ProgressBar)findViewById(R.id.pbbar);
        searchJobs = (EditText) findViewById(R.id.searchJobs);

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");

        //intiate getAllJobs asynctask
        ListJobs getAllJobs = new ListJobs();
        getAllJobs.execute();

        //set onclick listener
        btnAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening Add Job activity
                Intent i = new Intent(ManageJobs.this, AddJobs.class);
                i.putExtra("userid", userid);
                startActivity(i);
                finish();
            }
        });

    }


    // comparator for use in comparing the data for sorting
    // selecting that  the status is the one that should be sorted
    public Comparator<Map<String, String>> mapComparator = new Comparator<Map<String, String>>() {
        public int compare(Map<String, String> m1, Map<String, String> m2) {
            return m1.get("B").compareTo(m2.get("B"));
        }
    };

    //async task for getting and setting dynamic database information to listview
    public class ListJobs extends AsyncTask<String, String, String> {

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
            Toast.makeText(ManageJobs.this, r, Toast.LENGTH_SHORT).show();
            // An array of strings we use to reference our map.
            String[] from={"A","B", "C", "D"};

            // sorting algorithm to sort by
            Collections.sort(joblist, mapComparator);

            //an array of insts that reference the ids for our labels.
            int[] views = {R.id.lbljobid,R.id.lbljobtitle};
            final SimpleAdapter ADA = new SimpleAdapter(ManageJobs.this,joblist,R.layout.all_jobs_list,from,views);
            // The list adapter we're going to use to convert our arrays into the listview.
            listJobs.setAdapter(ADA);
            listJobs.setOnItemClickListener(new AdapterView.OnItemClickListener() { //When the buttons is clicked.
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Hashmap links strings with objectss
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA.getItem(position);
                    String jobID = (String) obj.get("A");
                    String jobTitle = (String) obj.get("B");
                    String jobSkills = (String) obj.get("C");
                    String jobDesc = (String) obj.get("D");

                    //loadup only that jobs information for editing
                    Intent intent = new Intent(getApplicationContext(), EditJobs.class);
                    intent.putExtra("userid", userid);
                    intent.putExtra("jobID", jobID);
                    intent.putExtra("jobTitle", jobTitle);
                    intent.putExtra("jobSkills", jobSkills);
                    intent.putExtra("jobDesc", jobDesc);
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
        //background task to query db
        protected String doInBackground(String... params) {
            //verify db conn and set it
            try{
                Connection conn = dataConn.CONN();
                if (conn == null) {
                    z = "Error in connection with SQL server";
                }else{
                    //run query
                    String query = "SELECT * FROM jobs WHERE user_id = '" + userid + "'";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ResultSet rs= ps.executeQuery();

                    //evaluating result set into hashmap and arraylist
                    while (rs.next()){
                        Map<String,String> datanum = new HashMap<String,String>();
                        datanum.put("A",rs.getString("id"));
                        datanum.put("B",rs.getString("title"));
                        datanum.put("C",rs.getString("skills"));
                        datanum.put("D",rs.getString("description"));
                        joblist.add(datanum);
                    }
                    z="Success";


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
