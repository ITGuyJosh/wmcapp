package d.wmcapp;

/**
 * Created by Josh on 23/03/2016.
 */
public class Employer extends Person {
    //declaring additional employer properties
    protected Integer PostedJobsNo;
    protected Integer AppsNo;
    protected Integer MostPopJob;
    protected Integer LeastPopJob;

    //default constructor
    Employer() {
        super();
        PostedJobsNo = 0;
        AppsNo = 0;
        MostPopJob = 0;
        LeastPopJob = 0;
    }

    //base override constructor
    Employer (Integer i, Integer r, String n, String e, String d, String o, String a){
        super(i, r, n, e, d, o, a);
        PostedJobsNo = 0;
        AppsNo = 0;
        MostPopJob = 0;
        LeastPopJob = 0;
    }

    //full override constructor
    Employer (Integer i, Integer r, String n, String e, String d, String o, String a,
             Integer pjno, Integer apno, Integer mpj, Integer lpj){
        super(i, r, n, e, d, o, a);
        PostedJobsNo = pjno;
        AppsNo = apno;
        MostPopJob = mpj;
        LeastPopJob = lpj;
    }

    //getters
    public Integer getPostedJobsNo(){return PostedJobsNo;}
    public Integer getAppsNo(){return AppsNo;}
    public Integer getMostPopJob(){return MostPopJob;}
    public Integer getLeastPopJob(){return LeastPopJob;}

    //setters
    public void setPostedJobsNo(Integer PostedJobsNo){this.PostedJobsNo = PostedJobsNo;}
    public void setAppsNo(Integer AppsNo){this.AppsNo = AppsNo;}
    public void setMostPopJob(Integer MostPopJob){this.MostPopJob = MostPopJob;}
    public void setLeastPopJob(Integer LeastPopJob){this.LeastPopJob = LeastPopJob;}

    //methods
    @Override
    public String viewStats() {
        //declaring empty string
        String stats = "";

        //adding object data to string
        stats = "The total number of jobs you have posted is: " + PostedJobsNo +".\n" +
                "The total applications against your posted jobs is: "+ AppsNo +".\n" +
                "Your most applied for job is: JobID"+ MostPopJob +".\n" +
                "Your least applied for job is: JobID"+ LeastPopJob +".\n" +
                "(Check your Manage Job page for further information about your jobs)";

        //returning polymorphic string to view
        return stats;
    }
}
