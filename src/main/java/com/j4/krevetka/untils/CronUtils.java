package com.j4.krevetka.untils;

import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

public class CronUtils {
    public static Date getNextExecutionTime(String cronExpression) throws ParseException {
        CronExpression cron = new CronExpression(cronExpression);

        return cron.getNextValidTimeAfter(Date.from(new Date().toInstant()));
    }
}
