package d.wmcapp;

import java.io.Serializable;

/**
 * Created by Josh on 23/03/2016.
 */
//abstact is only the super class
    //need to have for polymorphism, as it enforces the implementation of a function that inherits from it
    //abstract methods are the ones that are implemented differently in child classes (poly)
public abstract class Person implements Serializable{
    //declare general properties to be inherited by child classes
    protected Integer id;
    protected Integer role;
    protected String name;
    protected String email;
    protected String org;
    protected String description;
    protected String address;
    protected String lat;
    protected String lng;

    //default person contructor (for blank objects)
    Person(){
        id = 0;
        role = 0;
        name = "";
        email = "";
        description = "";
        org = "";
        address = "";
        lat = "";
        lng = "";
    }

    //override constructor
    Person (Integer i, Integer r, String n, String e, String d, String o, String a, String la, String ln){
        id = i;
        role = r;
        name = n;
        email = e;
        description = d;
        org = o;
        address = a;
        lat = la;
        lng = ln;
    }

    //mutators sets, accessors gets (getting/setting is main factor of encapusalation)
    //getters
    public Integer getId(){ return id; }
    public Integer getRole(){ return role; }
    public String getName(){ return name; }
    public String getEmail(){ return email; }
    public String getDescription(){ return description; }
    public String getOrg(){ return org; }
    public String getAddress(){ return address; }
    public String getLat(){ return lat; }
    public String getLng(){ return lng; }

    //setters
    public void setId(Integer id){this.id = id;}
    public void setRole(Integer role) { this.role = role; }
    public void setName(String name) { this.name = name; }
    public void setOrg(String org) { this.org = org; }
    public void setEmail(String email) { this.email = email; }
    public void setDescription(String description) { this.description = description; }
    public void setAddress(String address) { this.address = address; }
    public void setLat(String lat) { this.lat = lat; }
    public void setLng(String lng) { this.lng = lng; }

    //declaring abstract polymorphic function
    public abstract String viewStats();

}
