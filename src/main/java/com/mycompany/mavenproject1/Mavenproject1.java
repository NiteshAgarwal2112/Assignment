package com.mycompany.mavenproject1;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalTime;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 *
 * @author nitesh
 */
public class Mavenproject1 {

    public static void main(String[] args) {
        List<Person> ListofEmployees = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        boolean Eployeefound=false;
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Register Name");
        String Register=(input.nextLine());
        try (FileReader reader = new FileReader(Register+".json"))
        {
            Object obj = jsonParser.parse(reader);
            JSONArray employeeList = (JSONArray) obj;
            for (Object emp:employeeList)
            {
                Person l = new Person();
                l.Insert((JSONObject) emp);
                ListofEmployees.add(l);
            }
        } 
        catch (FileNotFoundException e) {
            System.out.println("Unable to Find Register: "+Register);
            System.exit(0);
        } 
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Enter Employee Name");
        String Pname = input.nextLine();
        System.out.println(StringUtils.rightPad("employeName", 20, " ")+
                StringUtils.rightPad("checkinTime", 20, " ")+
                StringUtils.rightPad("checkoutTime", 20, " ")+
                StringUtils.rightPad("date", 11, " ")+
                StringUtils.rightPad("dept", 15, " ")+
                StringUtils.rightPad("WorkingHrs", 10, " "));
        for(Person x:ListofEmployees)
            if(x.Search(Pname))
                Eployeefound=true;
        if(!Eployeefound)
            System.out.println("Employee: "+Pname+" is not found in the records");
    }
}
class Person
{   
    String Pname;
    String Date;
    LocalTime CheckinTime;
    LocalTime CheckoutTime;
    String DeptName;
    float whrs;
    public void Insert(JSONObject Employee)
    {
        Pname=(String) Employee.get("employeName");
        Date=(String) Employee.get("date");
        DeptName=(String) Employee.get("dept");
        CheckinTime=GetTime(Employee.get("checkinTime").toString());       
        CheckoutTime= GetTime(Employee.get("checkouttime").toString()); 
        whrs= (float) SECONDS.between(CheckinTime, CheckoutTime)/3600;
    }
    public void Read()
    {
         System.out.println(StringUtils.rightPad(Pname, 20, " ")+
                StringUtils.rightPad(CheckinTime.toString(), 20, " ")+
                StringUtils.rightPad(CheckoutTime.toString(), 20, " ")+
                StringUtils.rightPad(Date, 11, " ")+
                StringUtils.rightPad(DeptName, 15, " ")+
                StringUtils.rightPad(whrs+"", 10, " "));
    }
    public boolean Search(String Pname) 
    {
        if(this.Pname.equals(Pname))
        {
           Read();
           return true;
        } 
        return false;            
    }
    public LocalTime GetTime(String Time)
    {
        String[] result = Time.split(":");
        Time="";
        for (int i=0;i<result.length && i<3;i++)
        {
            if(i==0)
            {
                if(result[i].length()==1)
                result[i]= "0".concat(result[i]);
            }    
            else if(result[i].length()==1)
                 result[i]= result[i].concat("0");
            Time+=result[i]+":";
        }
        return(LocalTime.parse(Time.substring(0, Time.lastIndexOf(':'))));
    }
}
