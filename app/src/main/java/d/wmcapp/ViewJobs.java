package d.wmcapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewJobs extends AppCompatActivity {

    //declare variables
    DataConn dataConn;
    ListView listJobs;
    Integer userid;
    String jobid;
    ProgressBar pbbar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_jobs);

        //instaniate & initalise
        dataConn = new DataConn();
        listJobs = (ListView)findViewById(R.id.listAllJobs);
        userid = getIntent().getExtras().getInt("userid");
        pbbar = (ProgressBar)findViewById(R.id.pbbar);

        // Set pbar to invisible
        pbbar.setVisibility(View.GONE);

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");
        setSupportActionBar(toolbar);

        //run functions
        ListJobs getAllJobs = new ListJobs();
        getAllJobs.execute();

    }

    //THIS NEEDS REFACTORING
    public class ListJobs extends AsyncTask<String, String, String> {

        //to refactor make new class
        //Person person;
        //checl
        //View view;
        //sql helper
        //need to pass list view too



        String z = "";
        List<Map<String, String>> joblist = new ArrayList<Map<String,String>>();

        protected void onPreExecute(){
            pbbar.setVisibility(View.VISIBLE); // Set the progress bar to visible to tell the user something is happening.
        }

        protected void onPostExecute(String r){
            pbbar.setVisibility(View.GONE);  // Once everything is done set the visibility of the progress bar to invisible
            Toast.makeText(ViewJobs.this, r, Toast.LENGTH_SHORT).show(); //Post the string r which contains info about what has happened.
            String[] from={"A","B"}; // An array of strings we use to reference our map.
            int[] views = {R.id.lbljobid,R.id.lbljobtitle}; //an array of insts that reference the ids for our labels.
            final SimpleAdapter ADA = new SimpleAdapter(ViewJobs.this,joblist,R.layout.all_jobs_list,from,views);
            listJobs.setAdapter(ADA); // The list adapter we're going to use to convert our arrays into the listview.
            listJobs.setOnItemClickListener(new AdapterView.OnItemClickListener() { //When the buttons is clicked.
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA.getItem(position); //Hashmap links strings with objectss
                    String jobid = (String) obj.get("A");
                    String jobTitle = (String) obj.get("B");

                    //loadup only that jobs information through passing id
                    Intent intent = new Intent(ViewJobs.this, ApplyForJob.class);
                    intent.putExtra("jobid", jobid);
                    intent.putExtra("userid", userid);
                    startActivity(intent);
                }
            });
        }
        protected String doInBackground(String... params) {
            try{
                Connection conn = dataConn.CONN();
                if (conn == null) {
                    z = "Error in connection with SQL server";
                }else{
                    //filter by only those that are currently open (when chester remote is open)
                    String query = "SELECT id, title FROM jobs WHERE active = 1";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ResultSet rs= ps.executeQuery();

                    //is this array list used??
                    ArrayList data1 = new ArrayList();
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
