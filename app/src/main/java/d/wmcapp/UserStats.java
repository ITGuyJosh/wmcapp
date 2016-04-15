package d.wmcapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserStats extends AppCompatActivity {

    //declare variables
    TextView txtStats;
    DataConn dataConn;
    Person user;
    Student stu;
    Employer emp;
    ProgressBar pbbar;
    Toolbar toolbar;
    Integer userid, uRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stats);

        //initialising and instantiating
        dataConn = new DataConn();
        txtStats = (TextView)findViewById(R.id.txtStats);
        pbbar = (ProgressBar)findViewById(R.id.pbbar);
        uRole = getIntent().getExtras().getInt("uRole");

        //setting user object depending on role
        if(uRole == 2){
            stu = (Student) getIntent().getSerializableExtra("user");
            userid = stu.getId();
        } else {
            emp = (Employer) getIntent().getSerializableExtra("user");
            userid = emp.getId();
        }

        // Set pbar to invisible
        pbbar.setVisibility(View.GONE);

        //setting custom toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setLogo(R.mipmap.wmc_icon);
        toolbar.setTitle("WMC EXP Helper");
        setSupportActionBar(toolbar);

        //get user stats and set to page
        ViewStats getStats = new ViewStats();
        getStats.execute();
    }

    //Async task for adding that the student applied for the job
    public class ViewStats extends AsyncTask<String, String, String> {
        String message = "";
        Boolean isSuccess = false;
        String stats;

        @Override
        protected  void onPreExecute(){
            //set progress bar to visible
            pbbar.setVisibility(View.VISIBLE);
        }

        //post background task
        @Override
        protected void onPostExecute(String r){
            // display message, set progress bar to off
            pbbar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_LONG).show();

            //set polymorphic text
            txtStats.setText(stats);
        }

        // background task to get specific user stats depending on role via custom polymorphic class function
        @Override
        protected String doInBackground(String... params) {

            try{
                Connection conn = dataConn.CONN();
                if(conn == null) {
                    message = "Error with server connection.";
                }else {
                    //get student information else get employer information
                    if(uRole == 2){
                        String query = "SELECT DISTINCT\n" +
                                "    (SELECT COUNT(*) FROM job_applications WHERE user_id = '" + userid + "') AS AppliedJobsNo,\n" +
                                "    (SELECT COUNT(*) FROM job_applications WHERE user_id = '" + userid + "' AND status = 1) AS PendingNo,\n" +
                                "    (SELECT COUNT(*) FROM job_applications WHERE user_id = '" + userid + "' AND status = 2) AS AcceptedNo,\n" +
                                "\t(SELECT COUNT(*) FROM job_applications WHERE user_id = '" + userid + "' AND status = 3) AS RejectedNo\n" +
                                "FROM job_applications;";
                        PreparedStatement myQuery = conn.prepareStatement(query);
                        ResultSet rs = myQuery.executeQuery();

                        if(rs.next()){
                            //set result set to object model
                            stu.setAppliedJobsNo(rs.getInt("AppliedJobsNo"));
                            stu.setPendingNo(rs.getInt("PendingNo"));
                            stu.setAcceptedNo(rs.getInt("AcceptedNo"));
                            stu.setRejectedNo(rs.getInt("RejectedNo"));

                            //run polymorphic function
                            stats = stu.viewStats();
                        }

                    } else {
                        String query = "SELECT DISTINCT\n" +
                                "\t(SELECT COUNT(*) FROM jobs WHERE user_id = '" + userid + "') AS PostedJobsNo,\n" +
                                "    (SELECT COUNT(*) FROM job_applications LEFT JOIN jobs AS J ON JA.job_id = J.id WHERE  J.user_id = '" + userid + "') AS AppsNo,\n" +
                                "\t(SELECT TOP 1 job_id FROM job_applications LEFT JOIN jobs AS J ON JA.job_id = J.id WHERE  J.user_id = '" + userid + "' GROUP BY job_id ORDER BY COUNT(job_id) DESC) AS MostPopJob,\n" +
                                "\t(SELECT TOP 1 job_id FROM job_applications LEFT JOIN jobs AS J ON JA.job_id = J.id WHERE  J.user_id = '" + userid + "' GROUP BY job_id ORDER BY COUNT(job_id) ASC) AS LeastPopJob\n" +
                                "FROM job_applications AS JA\n" +
                                "LEFT JOIN jobs AS J ON JA.job_id = J.id;";
                        PreparedStatement myQuery = conn.prepareStatement(query);
                        ResultSet rs = myQuery.executeQuery();

                        if(rs.next()){
                            //set result set to object model
                            emp.setPostedJobsNo(rs.getInt("PostedJobsNo"));
                            emp.setAppsNo(rs.getInt("AppsNo"));
                            emp.setMostPopJob(rs.getInt("MostPopJob"));
                            emp.setLeastPopJob(rs.getInt("LeastPopJob"));

                            //run polymorphic function
                            stats = emp.viewStats();
                        }
                    }

                    message = "User stats loaded.";
                    isSuccess = true;
                }
            }catch (Exception ex){
                isSuccess = false;
                message = "Exceptions Generated";
            }

            return message;
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
