import org.openqa.selenium.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;


public class Home {
    /*

     * @startuml

     * Home - wheel

     * @enduml

     */
    private long startTime, startTime1, endTime, elapsed, totalElapsed = 0;
    private int seconds, minutes, timeLimit  = 10;
    private String displayMin, displaySec;
    private boolean chosen = false;

    private JLabel timeLabel = new JLabel(" ", SwingConstants.CENTER);
    private JProgressBar bar = new JProgressBar(SwingConstants.VERTICAL);
    private JButton[] buttons = new JButton[3];
    private JPanel backPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();

    private FirefoxProfile profile = new FirefoxProfile();
    private WebDriver driver = new FirefoxDriver();

    private Reader r = new Reader();
    private Calendar c = new Calendar();

    public Home() throws IOException {

        //System.setProperty("webdriver.gecko.driver","C:\\geckodriver.exe");

        JFrame f = new JFrame("Social Diet");
        f.setSize(250,400);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        backPanel.setLayout(new GridLayout(0, 2));
        buttonPanel.setLayout(new GridLayout(4,0));

        bar.setMinimum(0);
        bar.setStringPainted(true);
        bar.setForeground(Color.green);

        backPanel.add(bar);                                         //TODO Time finished notification
        buttonPanel.add(timeLabel);

        for (int x = 0; x < buttons.length; x++) {
            buttons[x] = new JButton("");
            buttonPanel.add(buttons[x]);
            buttons[x].setVisible(false);
        }

        buttons[0].setText("Easy");
        buttons[1].setText("Medium");
        buttons[2].setText("Hard");

        if(r.getStarted()) {
            buttons[2].setText("Reset Diet");
            buttons[2].setVisible(true);
            chosen = true;

            timeLimit = r.getLevel();
            setup();

        }else{
            timeLabel.setText("Pick a diet difficulty");

            for (int x = 0; x < buttons.length; x++) {
                buttons[x].setVisible(true);
            }
        }

        backPanel.add(buttonPanel);
        f.add(backPanel);
        f.setVisible(true);

        //Easy
        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!r.getStarted()) {
                    chosen = true;
                    timeLimit = 30;
                    minutes = timeLimit-1;
                    setup();
                }
            }
        });

        //Medium
        buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!r.getStarted()) {
                    chosen = true;
                    timeLimit = 20;
                    minutes = timeLimit-1;
                    setup();
                }
            }
        });

        //Hard
        buttons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!r.getStarted()) {
                    chosen = true;
                    timeLimit = 10;
                    minutes = timeLimit-1;
                    setup();

                }else{
                    chosen = false;
                    totalElapsed = 0;
                    seconds = 0;
                    r.setStarted(false);
                    r.setProgress(0);
                    r.setWeekCount(0);
                    r.write();

                    buttons[0].setVisible(true);
                    buttons[1].setVisible(true);

                    timeLabel.setText("Diet Reset");
                    bar.setValue(0);

                    buttons[2].setText("Hard");
                }
            }
        });


        while(!driver.getCurrentUrl().isEmpty()) {
            if (scan()&&chosen) {

                buttons[0].setVisible(false);
                buttons[1].setVisible(false);
                buttons[2].setText("Reset Diet");

                r.setStarted(true);
                r.write();
                startTime = System.nanoTime();
                startTime1 = System.nanoTime();

                while (scan()&&chosen) {

                    endTime = System.nanoTime();

                        elapsed = (endTime - startTime) / 1000000;

                        seconds = (int) ((elapsed/1000) + totalElapsed);
                        r.setProgress(seconds);
                        r.setMin(minutes);
                        r.write();

                        if (seconds > 59){
                            minutes--;
                            startTime = System.nanoTime();
                            totalElapsed = 0;
                        }

                        if(minutes<10){
                            displayMin = "0" + minutes;

                        }if(minutes<0){

                            r.setStarted(true); //Stop the timer from being able to run
                            r.write();
                            chosen = false;
                            timeLabel.setText("Time's Up!");
                            driver.quit();
                            break;

                        }else{
                            displayMin = Integer.toString(minutes);
                        }

                        if(seconds>49){
                            displaySec = "0" + (59-seconds);
                        }else{
                            displaySec = Integer.toString(59-seconds);
                        }

                        bar.setValue((minutes*60)+(59-seconds));

                        long barStatus = bar.getValue()*100/bar.getMaximum();
                        if(barStatus>60){
                            bar.setForeground(Color.green);
                        }else if (barStatus>30){
                            bar.setForeground(Color.yellow);
                        }else{
                            bar.setForeground(Color.red);
                     }

                    timeLabel.setText("<html><div style='text-align: center;'>" + displayMin +" : "+ displaySec + "</div></html>");
                }
                totalElapsed = seconds;

            }

        }
    }

    public void setup(){
        r.setLevel(timeLimit);
        r.write();

        if(r.getStarted()){
            int dietDays = (int) c.getDays(r.getStartDate(),c.getCurrDate());

            if((int) c.getDays(r.getCurrDate(),c.getCurrDate()) > 0){ //Past current day


                if((dietDays > 28)&&(r.getWeekCount() < 4)) { //Four weeks past

                    r.setWeekCount(r.getWeekCount() + 1);
                    r.setLevel((int) (r.getLevel() *0.5));

                }else if((dietDays > 21)&&(r.getWeekCount() < 3)) { //Three weeks past

                    r.setWeekCount(r.getWeekCount() + 1);
                    r.setLevel((int) (r.getLevel() *0.7));


                }else if((dietDays > 14)&&(r.getWeekCount() < 2)){ //Two weeks past

                    r.setWeekCount(r.getWeekCount()+1);
                    r.setLevel((int) (r.getLevel() *0.8));

                }else if ((dietDays > 7)&&(r.getWeekCount() < 1)){ //One week past

                    r.setWeekCount(r.getWeekCount()+1);
                    r.setLevel((int) (r.getLevel() *0.9));

                }

                r.setProgress(60);
                r.setMin(r.getLevel());
                r.setCurrDate(c.getCurrDate());
                r.write();

            }
            timeLimit = r.getLevel();
            bar.setMaximum(timeLimit * 60);

            totalElapsed = r.getProgress();
            minutes = r.getMin();

            int tempSecDisp = 60-r.getProgress();
            if (tempSecDisp < 10){
                timeLabel.setText(r.getMin() + " : 0" + tempSecDisp);
            }else {
                timeLabel.setText(r.getMin() + " : " + tempSecDisp);
            }

            int temp1 = 60-r.getProgress();
            int temp = r.getMin()*60;
            temp = temp1+temp;
            System.out.println(temp);
            bar.setValue(temp);
        }else {
            //Setting current date to the start of the diet
            r.setCurrDate(c.getCurrDate());
            r.setStartDate(c.getCurrDate());
            r.write();
            bar.setMaximum(timeLimit * 60);
            timeLabel.setText(timeLimit + " : 00");
            timeLabel.setText("<html><div style='text-align: center;'>" + timeLimit + " : 00" + "</div></html>");
            bar.setValue(bar.getMaximum());
        }
    }

    public boolean scan(){

        String currentUrl = "";
        ArrayList<String> blackList = r.getBlackList();

        try {
            currentUrl = driver.getCurrentUrl();
        } catch (NoSuchWindowException e){
            driver.quit();
            System.out.println("Session Ended");
            throw(e);

        } catch(WebDriverException e){
            System.out.println("WebDriver Exception");
            throw(e);

        }

        for (String s : blackList) {
            if (currentUrl.contains(s)) {
                return true;
            }
        }
        return false;
    }

}
