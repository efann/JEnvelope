/*
 * JEnvelope: A program that prints envelopes from iCloud contacts.
 *
 * Copyright(c) 2012-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html)
 *
 *
 */

package com.beowurks.JEnvelope;

import com.beowurks.BeoCommon.Util;
import com.beowurks.BeoLookFeel.LFCommon;

// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
public class Application
{
  protected AppProperties foAppProperties;

  // This needs to be false, just in case the run time on a user's machine accesses
  // this variable before this variable is initialized.
  private static boolean APPLICATION_DEVELOPMENT = false;

  private static String APPLICATION_TITLE = "JEnvelope";
  private static String APPLICATION_VERSION = "(Development Version)";

  // ---------------------------------------------------------------------------------------------------------------------
  // Construct the application
  public Application()
  {
    // Needs to be first: otherwise the Global variables are initialized before
    // APPLICATION_DEVELOPMENT is set.
    Application.initializeEnvironment(this);

    if (!Global.isValidPC())
    {
      Util.errorMessage(null, "Sorry " + Global.USER_NAME + ": invalid environment. . . .");
      return;
    }

    // Turns off the security sandbox which should improve performance somewhat.
    System.setSecurityManager(null);

    this.foAppProperties = new AppProperties();

    Util.setTitle(Application.getApplicationFullName());

    this.setLookAndFeel();

    final FrameMain loFrame = new FrameMain(this);
    loFrame.makeVisible(true);
  }

  // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
  private static void initializeEnvironment(final Object toObject)
  {
    // If you use Application.class.getClass instead of toObject in a static method,
    // you'll get something like Java Runtime Environment 1.8.0_144
    final String lcTitle = toObject.getClass().getPackage().getImplementationTitle();
    final String lcVersion = toObject.getClass().getPackage().getImplementationVersion();

    Application.APPLICATION_DEVELOPMENT = ((lcTitle == null) || (lcVersion == null));

    if (!Application.APPLICATION_DEVELOPMENT)
    {
      Application.APPLICATION_TITLE = lcTitle;
      Application.APPLICATION_VERSION = lcVersion;
    }

  }

  // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
  public static String getApplicationFullName()
  {
    return (String.format("%s %s", Application.getApplicationName(), Application.getApplicationVersion()));
  }

  // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
  public static String getApplicationName()
  {
    return (Application.APPLICATION_TITLE);
  }

  // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
  public static String getApplicationVersion()
  {
    return (Application.APPLICATION_VERSION);
  }

  // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
  public static boolean isDevelopmentEnvironment()
  {
    return (Application.APPLICATION_DEVELOPMENT);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void setLookAndFeel()
  {
    Util.setUIManagerAppearances();

    final String lcLookAndFeel = this.foAppProperties.getLookAndFeel();
    final String lcMetalTheme = this.foAppProperties.getMetalTheme();

    LFCommon.setLookFeel(lcLookAndFeel, lcMetalTheme, null, true);

    // You can a list of the defaults by doing the following:
    // Util.listDefaultSettings();
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // Main method
  public static void main(final String[] args)
  {
    new Application();
  }
  // ---------------------------------------------------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
