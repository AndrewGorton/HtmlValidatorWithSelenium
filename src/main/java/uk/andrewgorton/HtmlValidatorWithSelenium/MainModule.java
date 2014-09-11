package uk.andrewgorton.HtmlValidatorWithSelenium;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.FileWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainModule {

    public static void main(String[] args) {
        WebDriver driver = null;

        try {
            // Get the page content using Selenium
            DesiredCapabilities dCaps = new DesiredCapabilities();
            dCaps.setJavascriptEnabled(true);

            driver = new PhantomJSDriver(dCaps);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.manage().window().maximize();

            driver.get("http://www.w3.org/");
            WebElement sampleElement = driver.findElement(By.id("w3c_home_talks"));

            System.out.println("Page title is: " + driver.getTitle());
            final String pageSource = driver.getPageSource();
            final String pageUrl = driver.getTitle();

            // Validate the page source
            validateContent(pageSource, pageUrl);
        } catch (Exception e) {
            e.printStackTrace();
            if (driver != null) {
                System.out.println(driver.getPageSource());
            }
        } finally {
            if (driver != null) driver.quit();
        }
    }

    public static void validateContent(String pageSource, String pageUrl) {
        try {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("ss", "1"));
            nvps.add(new BasicNameValuePair("verbose", "1"));
            nvps.add(new BasicNameValuePair("uploaded_file", pageSource));

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://validator.w3.org/check");
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));

            CloseableHttpResponse response2 = httpclient.execute(httpPost);
            System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            String filename = URLEncoder.encode(pageUrl + ".html", StandardCharsets.UTF_8.name());
            FileWriter fw = new FileWriter(new File(filename));
            IOUtils.copy(entity2.getContent(), fw);
            fw.close();
            EntityUtils.consume(entity2);
            System.out.println(String.format("Wrote validation results to '%s'", filename));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
