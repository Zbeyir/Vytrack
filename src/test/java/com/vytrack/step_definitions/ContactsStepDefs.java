package com.vytrack.step_definitions;

import com.vytrack.pages.ContactInfoPage;
import com.vytrack.pages.ContactsPage;
import com.vytrack.pages.DashboardPage;
import com.vytrack.pages.LoginPage;
import com.vytrack.utulities.BrowserUtils;
import com.vytrack.utulities.ConfigurationReader;
import com.vytrack.utulities.DBUtils;
import com.vytrack.utulities.Driver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class ContactsStepDefs {

    @Given("the user logged in as {string}")
    public void the_user_logged_in_as(String userType) {
        //go to login page
        Driver.get().get(ConfigurationReader.get("url"));
        //based on input enter that user information
        String username =null;
        String password =null;

        if(userType.equals("driver")){
            username = ConfigurationReader.get("driver_username");
            password = ConfigurationReader.get("driver_password");
        }else if(userType.equals("sales manager")){
            username = ConfigurationReader.get("sales_manager_username");
            password = ConfigurationReader.get("sales_manager_password");
        }else if(userType.equals("store manager")){
            username = ConfigurationReader.get("store_manager_username");
            password = ConfigurationReader.get("store_manager_password");
        }
        //send username and password and login
        new LoginPage().login(username,password);
    }

    @Then("the user should see following options")
    public void the_user_should_see_following_options(List<String> menuOptions) {
        BrowserUtils.waitFor(2);
        //get the list of webelement and convert them to list of string and assert
        List<String> actualOptions = BrowserUtils.getElementsText(new DashboardPage().menuOptions);

        Assert.assertEquals(menuOptions,actualOptions);
        System.out.println("menuOptions = " + menuOptions);
        System.out.println("actualOptions = " + actualOptions);
    }

    @When("the user logs in using following credentials")
    public void the_user_logs_in_using_following_credentials(Map<String,String> userInfo) {
        System.out.println(userInfo);
        //use map information to login and also verify firstname and lastname
        //login with map info
        new LoginPage().login(userInfo.get("username"),userInfo.get("password"));
        //verify firstname and lastname
        String actualName = new DashboardPage().getUserName();
        String expectedName = userInfo.get("firstname")+" "+ userInfo.get("lastname");

        Assert.assertEquals(expectedName,actualName);
        System.out.println("expectedName = " + expectedName);
        System.out.println("actualName = " + actualName);


    }

    @When("the user clicks the {string} from contacts")
    public void the_user_clicks_the_from_contacts(String email) {

        BrowserUtils.waitFor(3);
        //we have ready method to find email webelement in pom class
        //we just get the email from feature file and pass to that method and click it
        ContactsPage contactsPage = new ContactsPage();
        contactsPage.getContactEmail(email).click();

    }

    // biz cemal ile day6 da sadece burdan sonrasini yaptik

    @Then("the information should be same with database")
    public void the_information_should_be_same_with_database() {
        BrowserUtils.waitFor(4);
        //get the information from UI

        ContactInfoPage contactInfoPage = new ContactInfoPage();
        String actualFullName = contactInfoPage.contactFullName.getText();
        String actualEmail = contactInfoPage.email.getText();

        System.out.println("actualFullName = " + actualFullName);
        System.out.println("actualEmail = " + actualEmail);


        //get information from database
        String query= "select concat(first_name,' ',last_name) \"full_name\", e.email \n" +
                "from orocrm_contact c inner join orocrm_contact_email e \n" +
                "on c.id = e.owner_id \n" +
                "where e.email = 'mrjakc@mail.ru'";

        //bu yukarida kini data base den kopyalidik 6.GÜN Vytrack Qa2

        //create the connection to qa2 env
        // DBUtils.createConnection();  //------> bunu kapattik cünkü hooks ta actik haci:)
        //get the data in java collections
        Map<String, Object> rowMap = DBUtils.getRowMap(query);
        String expectedFullName = (String) rowMap.get("full_name");
        String expectedEmail = (String) rowMap.get("email");

        System.out.println("expectedFullName = " + expectedFullName);  // dataBase 'den gelen bilgi
        System.out.println("expectedEmail = " + expectedEmail);   // dataBase 'den gelen bilgi
        //close connection
        // DBUtils.destroy();  //------> bunu kapattik cünkü hooks ta actik haci:)

        //assertion, compare UI vs DATABASE information
        Assert.assertEquals(expectedFullName,actualFullName);
        Assert.assertEquals(expectedEmail,actualEmail);







    }

    @Then("the information for {string} should be same with database")
    public void the_information_for_should_be_same_with_database(String email) {
        BrowserUtils.waitFor(4);
        //get the information from UI
        ContactInfoPage contactInfoPage = new ContactInfoPage();
        String actualFullname = contactInfoPage.contactFullName.getText();
        String actualEmail = contactInfoPage.email.getText();

        System.out.println("actualFullname = " + actualFullname);
        System.out.println("actualEmail = " + actualEmail);


        //get information from database
        String query= "select concat(first_name,' ',last_name) \"full_name\", e.email \n" +
                "from orocrm_contact c inner join orocrm_contact_email e \n" +
                "on c.id = e.owner_id \n" +
                "where e.email = '"+email+"'";
        // burada yukari da kin den farkli olarak email 'i data base den geleni dynamic yaptik
        // artik feature 'da email olark ne girersek burayada o yazilacak#
        // bir önceki ile bunun farki sadece bu ve mantikli olani di bu hersey dynamic olmali


        //create the connection to qa2 env
        // DBUtils.createConnection();
        //get the data in java collections
        Map<String, Object> rowMap = DBUtils.getRowMap(query);
        String expectedFullname = (String) rowMap.get("full_name");
        String expectedEmail = (String) rowMap.get("email");

        System.out.println("expectedFullname = " + expectedFullname);
        System.out.println("expectedEmail = " + expectedEmail);
        //close connection
        // DBUtils.destroy();

        //assertion, compare UI vs DATABASE information
        Assert.assertEquals(expectedFullname,actualFullname);
        Assert.assertEquals(expectedEmail,actualEmail);
    }

}
