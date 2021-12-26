package com.adt.ibp.ui_automation.Privacy.Test;


import com.adt.ibp.Utils.EmailManager;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduledActivities {

    EmailManager emailManager = new EmailManager();
    String emailBody = "Privacy Monitoring Test,"
            + "<br><br> Regards, <br>Test Automation Team<br>";
    public void taskEmailInterval(List<String> attachments){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 21);
        c.set(Calendar.MINUTE, 00);
        c.set(Calendar.SECOND, 00);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //Call your method here
                //setEmail(emailContent, subject);
                emailManager.sendEmail("smtp.gmail.com", "587", "mirzayev.mirali19@gmail.com", "rrpbskukayzcksxk",
                        "Please find Privacy Monitor Test Report below!",	emailBody, attachments);
            }
        }, c.getTime(), 86400000);
    }
}
