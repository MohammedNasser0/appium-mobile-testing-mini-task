package mobile;

import com.sun.net.httpserver.HttpServer;
import org.openqa.selenium.By;
import org.testng.annotations.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import static org.testng.Assert.assertEquals;

public class ChromeWebTest extends BaseTest {
    private HttpServer site;
    @Override protected String appMode() { return "chrome"; }

    @Test public void submitsNameInChrome() throws Exception {
        // Local fixture keeps browser assertions independent of an external website.
        byte[] page = ("<!doctype html><html><head><meta name='viewport' content='width=device-width,initial-scale=1'>"
                + "<title>Appium web test</title></head><body style='font:20px Arial;padding:24px'>"
                + "<h1>Appium web test</h1><label for='name'>Your name</label><br>"
                + "<input id='name' style='font:inherit;margin:16px 0' placeholder='Enter name'><br>"
                + "<button id='submit' style='font:inherit' onclick=\"document.getElementById('result').textContent="
                + "'Hello, '+document.getElementById('name').value+'!'\">Say hello</button>"
                + "<p id='result' role='status'></p></body></html>").getBytes(StandardCharsets.UTF_8);
        site = HttpServer.create(new InetSocketAddress("127.0.0.1", 8777), 0);
        site.createContext("/", exchange -> {
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, page.length);
            try (var body = exchange.getResponseBody()) { body.write(page); }
        });
        site.start();
        driver.get("http://127.0.0.1:8777/");
        visible(By.id("name")).sendKeys("Mohammed");
        visible(By.id("submit")).click();
        wait.until(d -> d.findElement(By.id("result")).getText().equals("Hello, Mohammed!"));
        assertEquals(driver.findElement(By.id("result")).getText(), "Hello, Mohammed!");
        screenshot("chrome-web");
    }

    @AfterMethod(alwaysRun = true) public void stopSite() {
        if (site != null) site.stop(0);
    }
}
