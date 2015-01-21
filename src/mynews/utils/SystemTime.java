/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mynews.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@SuppressWarnings("FieldMayBeFinal")
public final class SystemTime {

    private String date = null;
    private String time = null;
    private int hrs = 0;
    private int min = 0;
    private int sec = 0;
    private TimeZone tz = null;
    private GregorianCalendar gCalendar = null;
    public SystemTime()
    {
        tz      = TimeZone.getTimeZone("Africa/Nairobi");
        gCalendar = new GregorianCalendar();
        gCalendar.setTimeZone(tz);
    }
    public String getFullTime() {  
        time = "" + gCalendar.get(Calendar.HOUR_OF_DAY) + ":" + gCalendar.get(Calendar.MINUTE) + ":" + gCalendar.get(Calendar.SECOND);
        return time;
    }
    public int getHrs(){
        hrs = gCalendar.get(Calendar.HOUR_OF_DAY);
        return hrs;
    }
    public int getMin(){
        min = gCalendar.get(Calendar.MINUTE);
        return min;
    }
    public int getSec(){
        sec = gCalendar.get(Calendar.SECOND);
        return sec;
    }
    public String getFullDate() {
        date = "" + gCalendar.get(Calendar.DATE) + "-" + (gCalendar.get(Calendar.MONTH) + 1) + "-" + gCalendar.get(Calendar.YEAR);
        return date;
    }
    public String getTime() {
        time = "" + gCalendar.get(Calendar.HOUR_OF_DAY) + "." + gCalendar.get(Calendar.MINUTE);
        return time;
    } 
    public String getHumanDateTime() {
        date = "" + gCalendar.get(Calendar.DATE) + "-" + (gCalendar.get(Calendar.MONTH) + 1) + "-" + gCalendar.get(Calendar.YEAR);
        time = "" + gCalendar.get(Calendar.HOUR_OF_DAY) + ":" + gCalendar.get(Calendar.MINUTE) + ":" + gCalendar.get(Calendar.SECOND);
        return date + "  /  " + time;
    } 
    public String getTabbedFullDateTime() {
        date = "" + gCalendar.get(Calendar.DATE) + "-" + (gCalendar.get(Calendar.MONTH) + 1) + "-" + gCalendar.get(Calendar.YEAR);
        time = "" + gCalendar.get(Calendar.HOUR_OF_DAY) + "." + gCalendar.get(Calendar.MINUTE) + "." + gCalendar.get(Calendar.SECOND);
        return date + "[" + time+"]";
    }
    public int dayOfWeek() {
        return gCalendar.get(Calendar.DAY_OF_WEEK);
    }
    public String getDayOfWeek(int dayofweek){
        switch(dayofweek){
            case 1:
                return "SUN";
            case 2:
                return "MON";
            case 3:
                return "TUE";
             case 4:
                return "WED";
            case 5:
                return "THUR";
            case 6:
                return "FRI"; 
            case 7:
                return "SAT";
            default:
                return null;
              
        }
    }
    public void initialize()
    {
        
    }

}
