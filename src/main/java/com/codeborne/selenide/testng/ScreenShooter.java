package com.codeborne.selenide.testng;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.reporters.ExitCodeListener;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static com.codeborne.selenide.WebDriverRunner.takeScreenShot;

/**
 * Annotate your test class with <code>@Listeners({ ScreenShooter.class})</code>
 */
public class ScreenShooter extends ExitCodeListener {
  public static boolean captureFailingTests = true;
  public static boolean captureSuccessfulTests;

  @Override
  public void onTestFailure(ITestResult result) {
    super.onTestFailure(result);
    if (captureFailingTests) {
      System.err.println("Saved failed test screenshot to: " + screenShot(result));
    }
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    super.onTestSuccess(result);
    if (captureSuccessfulTests) {
      System.out.println("Saved succeeded test screenshot to: " + screenShot(result));
    }
  }

  protected String screenShot(ITestResult result) {
    String className = result.getMethod().getTestClass().getName();
    String methodName = result.getMethod().getMethodName();
    final String screenShotPath = takeScreenShot(className, methodName);
    URL screenShotURL = getFileURL(screenShotPath);
    if (screenShotURL != null) {
      Reporter.setCurrentTestResult(result);
      Reporter.log("<a href=\"" + screenShotURL + "\">Screenshot</a>");
    }
    return screenShotPath;
  }

  private URL getFileURL(String screenShotPath) {
    try {
      return new File(screenShotPath).toURI().toURL();
    } catch (MalformedURLException e) {
      System.err.println("Unable to create URL from screenShotPath " + screenShotPath + " due to: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }
}
