/*
 * JEnvelope: A program that prints envelopes from iCloud contacts.
 *
 * Copyright(c) 2012-2018, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html)
 *
 *
 */

package com.beowurks.JEnvelope;

import com.beowurks.BeoCommon.BaseProperties;
import com.beowurks.BeoCommon.Util;
import com.beowurks.BeoLookFeel.LFCommon;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
public class AppProperties extends BaseProperties
{
  private final static String PROPERTY_HEADER = "JEnvelope\u00a9 Parameters - DO NOT EDIT . . . please";

  private final static String LOOKANDFEEL = "Application LookAndFeel";
  private final static String METALTHEME = "Application MetalTheme";

  private final static String USER_FILENAME = System.getProperty("user.name") + ".Properties";

  private final static String COMBOBOX_ADDRESSEE = "Combobox Addressee";
  private final static String COMBOBOX_RETURN = "Combobox Return";

  private final static String MANUAL_NAME = "Manual Name";
  private final static String MANUAL_STREET = "Manual Street";
  private final static String MANUAL_CITY = "Manual City";
  private final static String MANUAL_STATE = "Manual State";
  private final static String MANUAL_ZIP = "Manual Zip";

  private final static String ADDRESSEE_MANUAL = "Addressee Manual Entry";

  private final static String SUDO_KEY = "Sudo Key";

  // ---------------------------------------------------------------------------------------------------------------------
  public AppProperties()
  {
    super(Global.LOCAL_PATH, AppProperties.USER_FILENAME, AppProperties.PROPERTY_HEADER, AppProperties.getDefaultMasterKey());
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public String getLookAndFeel()
  {
    return (this.getProperty(AppProperties.LOOKANDFEEL, "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public String getMetalTheme()
  {
    return (this.getProperty(AppProperties.METALTHEME, "javax.swing.plaf.metal.OceanTheme"));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public String getComboboxReturn()
  {
    return (this.getProperty(AppProperties.COMBOBOX_RETURN, ""));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public String getComboboxAddressee()
  {
    return (this.getProperty(AppProperties.COMBOBOX_ADDRESSEE, ""));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public boolean getAddresseeManualEntry()
  {
    return (this.getProperty(AppProperties.ADDRESSEE_MANUAL, false));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public String getManualName()
  {
    return (this.getProperty(AppProperties.MANUAL_NAME, ""));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public String getManualStreet()
  {
    // You need to use MANUAL_CR: otherwise, you'll have problems with the properties file wrapping lines
    // in OS X.
    return (this.getProperty(AppProperties.MANUAL_STREET, ""));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public String getManualCity()
  {
    return (this.getProperty(AppProperties.MANUAL_CITY, ""));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public String getManualState()
  {
    return (this.getProperty(AppProperties.MANUAL_STATE, ""));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public String getManualZipcode()
  {
    return (this.getProperty(AppProperties.MANUAL_ZIP, ""));
  }

  // -----------------------------------------------------------------------------------------------------------------------
  public String getSudoKey()
  {
    return (this.getProperty(AppProperties.SUDO_KEY, ""));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // ---------------------------------------------------------------------------------------------------------------------
  public void setLookAndFeel()
  {
    this.setProperty(AppProperties.LOOKANDFEEL, LFCommon.getCurrentLookAndFeel());
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public void setMetalTheme()
  {
    this.setProperty(AppProperties.METALTHEME, LFCommon.getCurrentMetalTheme());
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public void setComboboxReturn(final String tcValue)
  {
    this.setProperty(AppProperties.COMBOBOX_RETURN, tcValue);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public void setComboboxAddressee(final String tcValue)
  {
    this.setProperty(AppProperties.COMBOBOX_ADDRESSEE, tcValue);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public void setAddresseeManualEntry(final boolean tlValue)
  {
    this.setProperty(AppProperties.ADDRESSEE_MANUAL, tlValue);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public void setManualName(final String tcValue)
  {

    this.setProperty(AppProperties.MANUAL_NAME, tcValue);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public void setManualStreet(final String tcValue)
  {
    // You need to use CR_STRING: otherwise, you'll have problems with the properties file wrapping lines
    // in OS X.
    this.setProperty(AppProperties.MANUAL_STREET, tcValue.replaceAll("\\\\n", Global.CR_STRING).replaceAll(Global.HTML_LINE_BREAK, Global.CR_STRING));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public void setManualCity(final String tcValue)
  {

    this.setProperty(AppProperties.MANUAL_CITY, tcValue);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public void setManualState(final String tcValue)
  {
    this.setProperty(AppProperties.MANUAL_STATE, tcValue);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public void setManualZipcode(final String tcValue)
  {
    this.setProperty(AppProperties.MANUAL_ZIP, tcValue);
  }

  // -----------------------------------------------------------------------------------------------------------------------
  public void setSudoKey()
  {
    final JPasswordField loSudoKey = new JPasswordField();
    final Object[] laInputs =
        {
            loSudoKey
        };

    final Object loReturn = Util.showInputDialog(null, laInputs, loSudoKey, "JEnvelope",
        JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);

    if ((loReturn instanceof Integer) && ((Integer) loReturn == JOptionPane.OK_OPTION))
    {
      this.setProperty(AppProperties.SUDO_KEY, new String(loSudoKey.getPassword()));
    }

  }

  // -----------------------------------------------------------------------------------------------------------------------
  public static String getDefaultMasterKey()
  {
    final StringBuilder loBuilder = new StringBuilder(AppProperties.USER_FILENAME);

    return ("*" + loBuilder.reverse().append("*").toString());
  }
  // ---------------------------------------------------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
