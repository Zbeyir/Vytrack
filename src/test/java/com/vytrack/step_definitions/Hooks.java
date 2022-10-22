package com.vytrack.step_definitions;

import com.vytrack.utulities.DBUtils;
import com.vytrack.utulities.Driver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.util.concurrent.TimeUnit;

public class Hooks {

    @Before
    public void setUp(){
        System.out.println("\tthis is coming from BEFORE");
        Driver.get().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Driver.get().manage().window().maximize();

    }

    @After
    public void tearDown(Scenario scenario){
        if(scenario.isFailed()){
            final byte[] screenshot = ((TakesScreenshot) Driver.get()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot,"image/png","screenshot");
        }

        Driver.closeDriver();

    }

    /**
     * yukarudakiler normal UI DEN GELEN ACMA KAPAM
     * Assagidakiler DataBase den gele acma kapakar
     *veeee @db yazmayi unut ma veya db yerine herhangibirsey de yazabiliriz
     * AMAA BU @db runner 'a  yazmaya gerek yok sadece feature ile Hooks arasinda bir baglanti sagliyor
     */

    @Before("@db")
    public void setUpDB(){
        System.out.println("Connecting to database...");
        DBUtils.createConnection();
    }

    @After("@db")
    public void tearDownDB(){
        System.out.println("close database connection...");
        DBUtils.destroy();
    }
}
