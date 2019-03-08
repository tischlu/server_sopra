package ch.uzh.ifi.seal.soprafs19.timeStamp;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class timeStamp {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    public static void main(String[] args) {

        //method 1
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);
    }
}