/*
 * JEnvelope: A program that prints envelopes from iCloud contacts.
 *
 * Copyright(c) 2012-2018, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html)
 *
 *
 */

package com.beowurks.JEnvelope;

import com.beowurks.BeoCommon.Util;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import javax.swing.JTextField;
import java.awt.Window;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
public final class Global
{
  protected final static String LOCAL_PATH = Global.getLocalPath();

  protected final static String TEMPORARY_PATH = Util.includeTrailingBackslash(Util.includeTrailingBackslash(Util
      .includeTrailingBackslash(System.getProperty("java.io.tmpdir"))
      + "Beowurks") + "JEnvelope");

  protected static final String USER_NAME = System.getProperty("user.name");

  protected final static String CR_STRING = "XXXcarriage_returnXXX";
  protected final static String LINE_SEP = System.getProperty("line.separator");
  protected final static String HTML_LINE_BREAK = "<br />";

  private static final StringBuilder fcExceptionError = new StringBuilder(256);

  // ---------------------------------------------------------------------------------------------------------------------
  private Global()
  {
  }

  // ---------------------------------------------------------------------------------------------------------------------
  static protected void errorException(final Window toWindow, final String tcException)
  {
    Global.errorException(toWindow, "Please notify support@beowurks.com of the following error:", tcException);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  static protected void errorException(final Window toWindow, final String tcMessage, final String tcException)
  {
    Global.setExceptionError(tcMessage, tcException);

    Util.errorMessage(toWindow, Global.fcExceptionError.toString());
  }

  // ---------------------------------------------------------------------------------------------------------------------
  static protected String getLocalPath()
  {
    String lcPath = "";

    if (Application.isDevelopmentEnvironment())
    {
      if (Util.isMacintosh())
      {
        lcPath = Util.includeTrailingBackslash(System.getProperty("user.home") + "/IntelliJ/JEnvelope");
      }
      else
      {
        lcPath = Util.includeTrailingBackslash("\\IntelliJ\\JEnvelope");
      }
    }
    else
    {
      lcPath = Util.includeTrailingBackslash(Util.includeTrailingBackslash(Util.includeTrailingBackslash(System.getProperty("user.home"))
          + "Beowurks") + "JEnvelope");
    }

    return (lcPath);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // The default height for text fields changes with different L&F due to border
  // and margins, etc.
  // For instance, using the value of 21 will look fine with Metal, but the
  // Nimbus text fields
  // will be too small.
  public static int getDefaultTextHeight()
  {
    // As the L&F could change, the JTextField needs to be dynamically created.
    // Otherwise it would use the settings of the L&F when it was first created.
    final JTextField loField = new JTextField();

    return ((int) loField.getPreferredSize().getHeight());
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // Let's set this application to only run on certain PCs.
  public static boolean isValidPC()
  {
    if (Application.isDevelopmentEnvironment() || (Global.USER_NAME.toLowerCase().contains("fann")) || (Global.USER_NAME.toLowerCase().contains("eddie")))
    {
      return (true);
    }

    return (false);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // Cause deleteonexit doesn't always work. . . .
  // http://www.devx.com/Java/Article/22018/0/page/2 describes the problem.
  public static void cleanFilesFromTemporaryDirectories()
  {
    Global.deleteFilesInFolder(Global.TEMPORARY_PATH);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public static void deleteFilesInFolder(final String tcFolder)
  {
    final File loTemp = new File(tcFolder);
    final File[] laFiles = loTemp.listFiles();

    if (laFiles == null)
    {
      return;
    }

    for (final File loFile : laFiles)
    {
      if (loFile.isFile())
      {
        try
        {
          loFile.delete();
        }
        catch (final SecurityException ignored)
        {
        }
      }
    }
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public static boolean makeAllTemporaryDirectories()
  {
    if (Global.makeDirectory(Global.TEMPORARY_PATH))
    {
      return (true);
    }

    return (false);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private static boolean makeDirectory(final String tcDirectory)
  {
    if (!Util.makeDirectory(tcDirectory))
    {
      Util.errorMessageInThread(null, "Unable to create the directory of " + tcDirectory + ".");
      return (false);
    }

    return (true);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  static private void setExceptionError(final String tcMessage, final String tcException)
  {
    Util.clearStringBuilder(Global.fcExceptionError);

    Global.fcExceptionError.append("<html><font face=\"Arial\">");
    Global.fcExceptionError.append(tcMessage);
    Global.fcExceptionError.append(" " + Global.HTML_LINE_BREAK + Global.HTML_LINE_BREAK + "<i> ");
    Global.fcExceptionError.append(tcException);
    Global.fcExceptionError.append(" </i><p></p></font></html>");
  }

  // ---------------------------------------------------------------------------------------------------------------------
  static public boolean launchApplication(final String tcPassword, final String[] taArguments, final boolean tlDisplayMessage)
  {
    boolean llOkay = true;

    // You must run with sudo: the JRE does not have full permissions to execute
    // an app, and the app will crash with weird "Quit Unexpectedly" errors.
    final CommandLine loCommandLine = new CommandLine("sudo");
    loCommandLine.addArgument("-S");
    // So that any generated files will be owned by the current user.
    loCommandLine.addArgument("-u");
    loCommandLine.addArgument(Global.USER_NAME);

    final int lnLength = taArguments.length;
    for (int i = 0; i < lnLength; ++i)
    {
      loCommandLine.addArgument(taArguments[i]);
    }

    final DefaultExecutor loExecutor = new DefaultExecutor();
    loExecutor.setExitValues(new int[]{0, 1});

    // By the way, when piping to sudo, you must include a carriage return.
    final String lcPassword = tcPassword + "\n";
    try
    {
      // From http://stackoverflow.com/questions/4695664/how-to-pipe-a-string-argument-to-an-executable-launched-with-apache-commons-exec/4751955#4751955
      final ByteArrayInputStream loInputStream = new ByteArrayInputStream(lcPassword.getBytes("ISO-8859-1"));
      final ByteArrayOutputStream loOutputStream = new ByteArrayOutputStream();

      loExecutor.setStreamHandler(new PumpStreamHandler(loOutputStream, null, loInputStream));
      loExecutor.execute(loCommandLine);
    }
    catch (final IOException loErr)
    {
      llOkay = false;
      if (tlDisplayMessage)
      {
        Util.errorMessageInThread(null, loErr.getMessage());
      }
    }

    return (llOkay);
  }

  // ---------------------------------------------------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
