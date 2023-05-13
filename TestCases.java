import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestCases {
    private WebDriver driver;
    @BeforeClass
    public void setUp() {
        System.setProperty(
                "webdriver.chrome.driver", "C:\\Users\\SravyaKanamarlapudi\\Downloads\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    @AfterClass
    public void tearDown() {
        driver.quit();
    }
    @Test(priority = 1)
    public void testPass1() {
        Assert.assertTrue(true);
    }

    @Test(priority = 2)
    public void testFailException1() {
        throw new RuntimeException("This is a test exception.");
    }

    @Test(priority = 3)
    public void testFailTimeout1() throws InterruptedException {
        Thread.sleep(60000); // Wait for 1 minute to cause a timeout
    }

    @Test(priority = 4)
    public void testPass2() {
        Assert.assertTrue(true);
    }

    @Test(priority = 5)
    public void testFailAssert1() {
        Assert.assertEquals(1, 2);
    }

    @Test(priority = 6)
    public void testFailTimeout2() throws InterruptedException {
        Thread.sleep(60000); // Wait for 1 minute to cause a timeout
    }

    @Test(priority = 7)
    public void testFailException2() {
        String str = null;
        System.out.println(str.length()); // This will throw a NullPointerException
    }
    @Test(priority = 8)
    public void testFailTimeout3() throws InterruptedException {
        Thread.sleep(60000); // Wait for 1 minute to cause a timeout
    }

    @Test(priority = 9)
    public void testSkip1() {
        throw new SkipException("This test case is skipped.");
    }

    @Test(priority = 10)
    public void testSkip2() {
        throw new SkipException("This test case is skipped.");
    }

}