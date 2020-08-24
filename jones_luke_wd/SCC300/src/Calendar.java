import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Calendar {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Calendar() throws IOException {
    }

    public String getCurrDate(){

        return LocalDate.now().toString();
    }

    public long getDays(String first, String second){

        final LocalDate firstDate = LocalDate.parse(first, formatter);
        final LocalDate secondDate = LocalDate.parse(second, formatter);
        final long days = ChronoUnit.DAYS.between(firstDate, secondDate);
        System.out.println("Days between: " + days);
        return days;
    }

}
