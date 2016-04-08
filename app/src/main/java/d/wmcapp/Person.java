package d.wmcapp;

import java.io.Serializable;

/**
 * Created by Josh on 23/03/2016.
 */
//abstact is only the super class
    //need to have for polymorphism, as it enforces the implementation of a function that inherits from it
    //abstract methods are the ones that are implemented differently in child classes (poly)
public abstract class Person implements Serializable{
    //declare properties
    //these properties are what are inherited by child classes
    protected Integer id;
    protected String name;
    protected String email;
    protected String description;
    protected Integer role;
    protected String org;

    //default person constuctor (for blank objects)
    Person(){
        id = 0;
        name = "";
        email = "";
        description = "";
        role = 0;
        org = "";
    }

    //override constructor
//    Person ( String n, String e, String d,Integer r){
//        name = n;
//        email = e;
//        description = d;
//        role = r;
//    }

    Person (Integer i, String n, String e, String d,Integer r, String o){
        id = i;
        name = n;
        email = e;
        description = d;
        role = r;
        org = o;
    }

    //mutators sets, accessors gets (getting/setting is main factor of encapusalation)
    //getters
    public String getName(){ return name; }
    public String getEmail(){ return email; }
    public String getDescription(){ return email; }
    public Integer getRole(){ return role; }

    //setters
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setDescription(String description) { this.description = description; }
    public void setRole(Integer role) { this.role = role; }

    public abstract void addContent();

}
