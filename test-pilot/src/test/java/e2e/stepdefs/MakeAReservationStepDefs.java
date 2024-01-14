package e2e.stepdefs;

import e2e.WebDrivers;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

import static e2e.E2EConfig.RBP_PROXY_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class MakeAReservationStepDefs {
    WebDriver driver = WebDrivers.driver;

    @Given("There is a free room")
    @And("I am on the homepage")
    public void i_am_on_the_homepage() {
        driver.manage()
            .timeouts()
            .implicitlyWait(Duration.ofSeconds(2));

        driver.get(RBP_PROXY_URL);
    }

    @When("I make a booking")
    public void when_i_make_a_booking() {
        WebElement openBookingButton = driver.findElement(By.className("openBooking"));
        openBookingButton.click();

        List<WebElement> webElements = driver.findElements(By.className("rbc-day-bg"));
        WebElement start = null;
        WebElement end = webElements.get(webElements.size() - 1);

        for (WebElement webElement: webElements) {
            if (webElement.getAttribute("class").equals("rbc-day-bg rbc-today")) {
                start = webElement;
                break;
            }
        }

        Actions actions = new Actions(driver)
            .clickAndHold(start)
            .moveToElement(end)
            .release();
        actions.perform();

        WebElement firstnameForm = driver.findElement(By.name("firstname"));
        WebElement lastnameForm = driver.findElement(By.name("lastname"));
        WebElement emailForm = driver.findElement(By.name("email"));
        WebElement phoneForm = driver.findElement(By.name("phone"));

        firstnameForm.sendKeys("Vincenzo");
        lastnameForm.sendKeys("Tramo");
        emailForm.sendKeys("vv.tramo@gmail.com");
        phoneForm.sendKeys("35122259422");

        WebElement bookButton = driver.findElement(By.className("btn-outline-primary"));
        bookButton.click();
    }

    @Then("I will see \"Booking Successful\" on the screen")
    public void i_will_see_booking_successful_on_the_screen() {
        WebElement confirmationModal = driver.findElement(By.className("confirmation-modal"));
        WebElement successfulMessage = confirmationModal.findElement(By.tagName("h3"));
        WebElement detailedSuccessfulMessage = confirmationModal.findElement(By.tagName("p"));
        assertThat(successfulMessage.getText(), is(equalTo("Booking Successful!")));
        assertThat(detailedSuccessfulMessage.getText(), is(equalTo("Congratulations! Your booking has been confirmed for:")));
    }

    @Then("I will see \"Unavailable\" on newly booked calendar days")
    public void i_will_see_unavailable() {
        WebElement confirmationModal = driver.findElement(By.className("confirmation-modal"));
        WebElement closeButton = confirmationModal.findElement(By.className("btn-outline-primary"));
        closeButton.click();

        WebElement openBookingButton = driver.findElement(By.className("openBooking"));
        openBookingButton.click();

        WebElement unavailable = driver.findElement(By.className("rbc-event-content"));
        assertThat(unavailable.getText(), is(equalTo("Unavailable")));
        driver.close();
    }
}