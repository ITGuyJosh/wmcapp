package d.wmcapp;

/**
 * Created by Josh on 05/04/2016.
 */
public class Job{
    //declaring variables
    protected Integer jobid;
    protected String title;
    protected String skills;
    protected String desc;

    //default constructor
    Job() {
        jobid = 0;
        title = "";
        skills = "";
        desc = "";
    }

    //override constructor
    Job (Integer i, String t, String s, String d){
        jobid = i;
        title = t;
        skills = s;
        desc = d;
    }

    //getters

    //setters

    //methods

}
