import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Check {

    public Check() {

    }

    public boolean start (String name) throws IOException {

        String line;
        String pidInfo ="";

        Process p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");

        BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));

        while ((line = input.readLine()) != null) {
            pidInfo+=line;
        }

        input.close();

        return pidInfo.contains(name);


    }

}
