import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Reader {

    private URL url = getClass().getResource("data.txt");
    private File file = new File(url.getPath());
    private String line = null;
    private HashMap<String, String> saveData = new HashMap<String,String>();
    private ArrayList<String> blackList = new ArrayList<String>();

    public Reader() {
        try {
            String key;
            String value;

            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((key = br.readLine()) != null) {
                if(key.contains(".com")){
                    blackList.add(key);
                }else {
                    value = br.readLine();
                    saveData.put(key, value);
                }
            }

            System.out.println(saveData);

        br.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to locate existing data, creating new save file");
        }
        catch(IOException ex) {
            System.out.println("Error reading save data file");
        }

    }

    public void write(){
        try{
            PrintWriter out = new PrintWriter(file);

            for (String temp : saveData.keySet()) {
                out.println(temp);
                out.println(saveData.get(temp));
            }

            for (String s : blackList) {
                out.println(s);
            }

            out.close();
        }
            catch(FileNotFoundException ex) {
            System.out.println("Unable to locate existing data, creating new save file");
        }
            catch(IOException ex) {
            System.out.println("Error reading save data file");
        }
    }

    public boolean getStarted(){
        if (saveData.get("started").equals("1")){
            return true;
        }else{
            return false;
        }
    }

    public int getLevel(){
        return Integer.parseInt(saveData.get("level"));
    }

    public int getProgress(){
        return Integer.parseInt(saveData.get("progress"));
    }

    public int getMin(){
        return Integer.parseInt(saveData.get("min"));
    }

    public String getStartDate(){
        return (saveData.get("startdate"));
    }

    public String getCurrDate(){
        return (saveData.get("currdate"));
    }

    public int getWeekCount(){ return Integer.parseInt((saveData.get("weekcount"))); }

    public ArrayList getBlackList(){
        return blackList;
    }

    public void setStarted(boolean started){
        System.out.println(started);
        saveData.remove("started");
        if (started) {
            saveData.put("started", "1");
        }else{
            saveData.put("started", "0");
        }
    }

    public void setLevel(int level){
        saveData.remove("level");
        saveData.put("level", Integer.toString(level));

    }

    public void setProgress(int progress){
        saveData.remove("progress");
        saveData.put("progress", Integer.toString(progress));

    }

    public void setMin(int min){
        saveData.remove("min");
        saveData.put("min", Integer.toString(min));

    }

    public void setStartDate(String startDate){
        saveData.remove("startdate");
        saveData.put("startdate", startDate);

    }

    public void setCurrDate(String currDate){
        saveData.remove("currdate");
        saveData.put("currdate",currDate);

    }

    public void setWeekCount(int weekCount){
        saveData.remove("weekcount");
        saveData.put("weekcount",Integer.toString(weekCount));

    }


}
