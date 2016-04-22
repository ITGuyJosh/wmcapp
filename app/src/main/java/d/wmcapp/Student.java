package d.wmcapp;

/**
 * Created by Josh on 23/03/2016.
 */
public class Student extends Person {
    //declaring additional student properties
    protected Integer AppliedJobsNo;
    protected Integer AcceptedNo;
    protected Integer PendingNo;
    protected Integer RejectedNo;
    protected Float AcceptenceRatio;

    //default constructor
    Student() {
        super();
        AppliedJobsNo = 0;
        AcceptedNo = 0;
        PendingNo = 0;
        RejectedNo = 0;
        AcceptenceRatio = 0f;
    }

    //base override constructor
    Student (Integer i, Integer r, String n, String e, String d, String o, String a, String lt, String ln){
        super(i, r, n, e, d, o, a, lt, ln);
        AppliedJobsNo = 0;
        AcceptedNo = 0;
        PendingNo = 0;
        RejectedNo = 0;
        AcceptenceRatio = 0f;
    }

    //full override constructor
    Student (Integer i, Integer r, String n, String e, String d, String o, String a, String lt, String ln,
             Integer ajno, Integer ano, Integer pno, Integer rno, Float ar){
        super(i, r, n, e, d, o, a, lt, ln);
        AppliedJobsNo = ajno;
        AcceptedNo = ano;
        PendingNo = pno;
        RejectedNo = rno;
        AcceptenceRatio = ar;
    }



    //getters
    public Integer getAppliedJobsNo(){return AppliedJobsNo;}
    public Integer getAcceptedNo(){return AcceptedNo;}
    public Integer getPendingNo(){return PendingNo;}
    public Integer getRejectedNo(){return RejectedNo;}
    public Float getAcceptenceRatio(){return AcceptenceRatio;}

    //setters
    public void setAppliedJobsNo(Integer AppliedJobsNo){this.AppliedJobsNo = AppliedJobsNo;}
    public void setAcceptedNo(Integer AcceptedNo){this.AcceptedNo = AcceptedNo;}
    public void setPendingNo(Integer PendingNo){this.PendingNo = PendingNo;}
    public void setRejectedNo(Integer RejectedNo){this.RejectedNo = RejectedNo;}
    public void setAcceptenceRatio(Float AcceptenceRatio){this.AcceptenceRatio = AcceptenceRatio;}

    //polymorphic methods
    @Override
    public String viewStats() {
        //declaring empty string
        String stats = "";
        //calculating acceptence ratio
        AcceptenceRatio = ((float)AcceptedNo / (float)AppliedJobsNo) * 100;

        //adding object data to string
        stats = "The total number of jobs applied for is: " + AppliedJobsNo +".\n" +
                "The number of jobs you have been accepted for is: "+ AcceptedNo +".\n" +
                "The number of jobs you have been rejected for is: "+ RejectedNo +".\n" +
                "The number of jobs still pending is: "+ PendingNo +".\n" +
                "Your overall acceptence ratio is: " + AcceptenceRatio +"%.";

        //returning polymorphic string to view
        return stats;
    }
}
