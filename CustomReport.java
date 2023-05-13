import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;
public class CustomReport implements IReporter {
    private static final String OUTPUT_DIRECTORY = "test-output/custom-report/";
    private static final String FILE_NAME = "index.html";

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
// Implement the report generation logic here
        String outputDir = outputDirectory + OUTPUT_DIRECTORY;
        new File(outputDir).mkdirs();
        String filePath = outputDir + FILE_NAME;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("<html>");
            writer.write("<head>");
            writer.write("<title>Custom Report</title>");
            writer.write("</head>");
            writer.write("<body>");
            writer.write("<h1>Custom Report</h1>");
            writer.write("<p>Generated on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</p>");
            writer.write("</body>");
            writer.write("</html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, List<ITestResult>> failedTests = new HashMap<>();
        Map<String, List<ITestResult>> skippedTests = new HashMap<>();
        List<ITestResult> passedTests = new ArrayList<>();

        for (ISuite suite : suites) {
            ITestContext testContext = null;
            for (ISuiteResult suiteResult : suite.getResults().values()) {
                testContext = suiteResult.getTestContext();
                for (ITestResult testResult : testContext.getFailedTests().getAllResults()) {
                    String testName = testResult.getMethod().getMethodName();
                    if (testResult.getThrowable() instanceof AssertionError) {
                        failedTests.computeIfAbsent("Assertion Failures", k -> new ArrayList<>()).add(testResult);
                    } else if (testResult.getThrowable() instanceof TimeoutException) {
                        failedTests.computeIfAbsent("Test failed due to Timeouts", k -> new ArrayList<>()).add(testResult);
                    } else {
                        failedTests.computeIfAbsent("Test failure due to Exceptions", k -> new ArrayList<>()).add(testResult);
                    }
                }
            }
            for (ITestResult testResult : testContext.getSkippedTests().getAllResults()) {
                skippedTests.computeIfAbsent(testResult.getThrowable().getMessage(), k -> new ArrayList<>()).add(testResult);
            }

            passedTests.addAll(testContext.getPassedTests().getAllResults());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("<html>");
            writer.write("<head>");
            writer.write("<title>Custom Report</title>");
            writer.write("</head>");
            writer.write("<body>");
            writer.write("<h1>Custom Report</h1>");
            writer.write("<p>Generated on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</p>");
            // Passed Tests
            if (!passedTests.isEmpty()) {
                writer.write("<h2>Passed Tests</h2>");
                writer.write("<ul>");
                for (ITestResult testResult : passedTests) {
                    writer.write("<li>" + testResult.getMethod().getMethodName() + "</li>");
                }
                writer.write("</ul>");
            }
            // Failed Tests
            if (!failedTests.isEmpty()) {
                writer.write("<h2>Failed Tests</h2>");
                for (Map.Entry<String, List<ITestResult>> entry : failedTests.entrySet()) {
                    writer.write("<h3>" + entry.getKey() + "</h3>");
                    writer.write("<ul>");
                    for (ITestResult testResult : entry.getValue()) {
                        writer.write("<li>" + testResult.getMethod().getMethodName() + "</li>");
                    }
                    writer.write("</ul>");
                }
            }

// Skipped Tests
            if (!skippedTests.isEmpty()) {
                writer.write("<h2>Skipped Tests</h2>");
                for (Map.Entry<String, List<ITestResult>> entry : skippedTests.entrySet()) {
                    writer.write("<h3>" + entry.getKey() + "</h3>");
                    writer.write("<ul>");
                    for (ITestResult testResult : entry.getValue()) {
                        writer.write("<li>" + testResult.getMethod().getMethodName() + "</li>");
                    }
                    writer.write("</ul>");
                }
            }
            writer.write("</body>");
            writer.write("</html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("<html>");
            writer.write("<head>");
            writer.write("<title>Custom Report</title>");
            writer.write("<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>");
            writer.write("<script type=\"text/javascript\">");
            writer.write("google.charts.load('current', {'packages':['corechart']});");
            writer.write("google.charts.setOnLoadCallback(drawChart);");
            writer.write("function drawChart() {");
            writer.write("var data = google.visualization.arrayToDataTable([");
            writer.write("['Test Results', 'Count'],");
            writer.write("['Passed Tests', " + passedTests.size() + "],");
            writer.write("['Failed Tests', " + failedTests.values().stream().mapToInt(List::size).sum() + "],");
            writer.write("['Skipped Tests', " + skippedTests.values().stream().mapToInt(List::size).sum() + "]");
            writer.write("]);");
            writer.write("var options = {");
            writer.write("title: 'Test Results',");
            writer.write("pieHole: 0.4");
            writer.write("};");
            writer.write("var chart = new google.visualization.PieChart(document.getElementById('chart_div'));");
            writer.write("chart.draw(data, options);");
            writer.write("}");
            writer.write("</script>");
            writer.write("</head>");
            writer.write("<body>");
            writer.write("<h1>Custom Report</h1>");
            writer.write("<p>Generated on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</p>");
            writer.write("<div id=\"chart_div\" style=\"width: 900px; height: 500px;\"></div>");
            // Passed Tests
            if (!passedTests.isEmpty()) {
                writer.write("<h2>Passed Tests</h2>");
                writer.write("<ul>");
                for (ITestResult testResult : passedTests) {
                    writer.write("<li>" + testResult.getMethod().getMethodName() + "</li>");
                }
                writer.write("</ul>");
            }
            // Failed Tests
            if (!failedTests.isEmpty()) {
                writer.write("<h2>Failed Tests</h2>");
                for (Map.Entry<String, List<ITestResult>> entry : failedTests.entrySet()) {
                    writer.write("<h3>" + entry.getKey() + "</h3>");
                    writer.write("<ul>");
                    for (ITestResult testResult : entry.getValue()) {
                        writer.write("<li>" + testResult.getMethod().getMethodName() + "</li>");
                    }
                    writer.write("</ul>");
                }
            }
            // Skipped Tests
            if (!skippedTests.isEmpty()) {
                writer.write("<h2>Skipped Tests</h2>");
                for (Map.Entry<String, List<ITestResult>> entry : skippedTests.entrySet()) {
                    writer.write("<h3>" + entry.getKey() + "</h3>");
                    writer.write("<ul>");
                    for (ITestResult testResult : entry.getValue()) {
                        writer.write("<li>" + testResult.getMethod().getMethodName() + "</li>");
                    }
                    writer.write("</ul>");
                }
            }
            writer.write("</body>");
            writer.write("</html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}