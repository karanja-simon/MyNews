/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mynews.utils;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author RESEARCH2
 */
public class Commons {
        public static String timeFromTimestamp(String ts) {
        long mili = Long.parseLong(ts);
        Timestamp time = new Timestamp(mili);
        Date date = new Date(time.getTime());
        return date.toString();
    }
    
}
