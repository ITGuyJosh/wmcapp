package d.wmcapp;

/**
 * Created by Josh on 23/03/2016.
 */
public class Student extends Person {
    //declaring variables
    //protected because..
//    protected Integer year;
//    protected Integer age;

    //default constructor
    Student() {
        super();
//        year = 0;
//        age = 0;
    }

    //override constructor
    Student (Integer i, String n, String e, String d,Integer r, String o){
        super(i, n, e, d, r, o);
//        year = y;
//        age = a;
    }

//    Student (Integer i, String n, String e, String d,Integer r, String o){
//        super(i,n,e,d,r,o);
//    }

//    //getters
//    public Integer getYear(){ return year; }
//    public Integer getAge(){ return age; }
//
//    //setters
//    public void setYear(Integer year) { this.year = year; }
//    public void setAge(Integer age) { this.age = age; }

    //methods
    @Override
    public void addContent() {

    }

}
