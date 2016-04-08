package d.wmcapp;

/**
 * Created by Josh on 23/03/2016.
 */
public class Employer extends Person {
    //declaring variables
//    private String company;
//    private String address;
    private String title;
    private String jdesc;
    private String skills;

    //default constructor
    Employer() {
        super();
        title = "";
        jdesc = "";
        skills = "";
    }

    //override constructor
//    Employer (String n, String e, String d,Integer r, String c, String a){
//        super(n,e,d,r);
////        company = c;
////        address = a;
//    }

    //override constructor
    Employer (Integer i, String n, String e, String d,Integer r, String o, String t, String jd, String s){
        super(i, n, e, d, r, o);
        title = t;
        jdesc = jd;
        skills = s;
    }

    //getters
//    public String getCompany(){ return company; }
//    public String getAddress(){ return address; }
//
//    //setters
//    public void setCompany(String company) { this.company = company; }
//    public void setAddress(String address) { this.address = address; }

    //methods
    @Override
    public void addContent() {

    }
}
