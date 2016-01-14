package uk.andrewgorton.HtmlValidatorWithSelenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

public class MainModule {
    private final Logger LOGGER = LoggerFactory.getLogger(MainModule.class);

    public static void main(String[] args) {
        System.exit(new MainModule().run(args));
    }

    public int run(String[] urls) {
        WebDriver driver = null;

        try {
            // Get the page content using Selenium
            DesiredCapabilities dCaps = new DesiredCapabilities();
            dCaps.setJavascriptEnabled(true);
            dCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
                    new String[]{"--web-security=no",
                            "--ignore-ssl-errors=yes"});
            dCaps.setJavascriptEnabled(true);

            driver = new PhantomJSDriver(dCaps);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.manage().window().maximize();

            int errorsTotal = 0;
            int warningsTotal = 0;
            for (String singleUrl : urls) {
                driver.get(singleUrl);
                LOGGER.info("Page title is: " + driver.getTitle());
                final String pageSource = driver.getPageSource();

                // Validate the page source
                int errorCount = 0;
                int warningCount = 0;
                ValidationResults vr = validateContent(pageSource);
                for (Message m : vr.getMessages()) {
                    if (m.getType().compareTo("error") == 0) {
                        ++errorCount;
                    }
                    if (m.getType().compareTo("warning") == 0) {
                        ++warningCount;
                    }
                }
                LOGGER.info(String.format("URL '%s' has %d errors, %d warnings", singleUrl, errorCount, warningCount));
                errorsTotal += errorCount;
                warningsTotal += warningCount;
            }

            LOGGER.info(String.format("Totals: %d errors, %d warnings", errorsTotal, warningsTotal));

            if (errorsTotal > 0) {
                return 1;
            }
            return 0;
        } catch (Exception e) {
            LOGGER.error(getException(e));
            if (driver != null) {
                LOGGER.debug(driver.getPageSource());
            }
        } finally {
            if (driver != null) driver.quit();
        }
        return -100;
    }

    public static ValidationResults validateContent(String pageSource) {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget wt = client
                    .target("http://validator.w3.org/nu/")
                    .queryParam("out", "json");

            Response r = wt
                    .request()
                    .header("Content-Type", "text/html; charset=utf-8")
                    .post(Entity.entity(pageSource, MediaType.TEXT_HTML));
            if (r.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
                throw new RuntimeException(String.format("%d: %s", r.getStatus(), r.getEntity()));
            }
            ValidationResults vr = r.readEntity(ValidationResults.class);
            return vr;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getException(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

}
