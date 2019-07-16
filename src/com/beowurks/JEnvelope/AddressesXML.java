/*
 * JEnvelope: A program that prints envelopes from iCloud contacts.
 *
 * Copyright(c) 2012-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html)
 *
 *
 */

package com.beowurks.JEnvelope;

import com.beowurks.BeoCommon.BaseXMLData;
import com.beowurks.BeoCommon.Util;
import com.beowurks.BeoCommon.XMLTextReader;
import com.beowurks.BeoCommon.XMLTextWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Vector;

//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
public class AddressesXML extends BaseXMLData implements Runnable
{
  private static final String ELEMENT_TAG = "Address";
  private static final String ATTRIBUTE_SORT_NAME = "SortName";
  private static final String ATTRIBUTE_NAME = "Name";
  private static final String ATTRIBUTE_STREET = "Street";
  private static final String ATTRIBUTE_CITY = "City";
  private static final String ATTRIBUTE_STATE = "State";
  private static final String ATTRIBUTE_ZIPCODE = "Zipcode";
  private static final String ATTRIBUTE_COUNTRY = "Country";

  private static final String CONTACTS_VCARD = Global.LOCAL_PATH + "contacts.vcf";

  private static final String VCARD_START = "BEGIN:VCARD";
  private static final String VCARD_END = "END:VCARD";
  private static final String VCARD_SORTNAME = "N:";
  private static final String VCARD_NAME = "FN:";
  private static final String VCARD_ADDRESS = "ADR;TYPE=";
  private static final String VCARD_ADDRESS_START = "=PREF:";

  private static final int PARSE_STREET = 0;
  private static final int PARSE_CITY = 1;
  private static final int PARSE_STATE = 2;
  private static final int PARSE_ZIP = 3;
  private static final int PARSE_COUNTRY = 4;

  private final Vector<Address> foVectorData = new Vector<Address>();

  private final Vector<String> foLines = new Vector<String>();

  private final FrameMain foMainFrame;

  // ---------------------------------------------------------------------------------------------------------------------
  public AddressesXML(final String tcDirectory, final String tcFileName, final FrameMain toMainFrame)
  {
    super(tcDirectory, tcFileName, 2);

    this.foMainFrame = toMainFrame;

    this.parseXMLData();
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public Vector<Address> getData()
  {
    return (this.foVectorData);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public Address getAddress(final String tcSortName)
  {
    Address loAddress = null;

    for (final Address loXAddress : this.foVectorData)
    {
      if (loXAddress.fcSortName.compareTo(tcSortName) == 0)
      {
        loAddress = loXAddress;
        break;
      }
    }
    return ((loAddress != null) ? loAddress : new Address());
  }

  // ---------------------------------------------------------------------------------------------------------------------
  @Override
  public boolean parseXMLData()
  {
    final File loFile = new File(this.getFullName());
    if (!loFile.exists())
    {
      return (false);
    }

    final XMLTextReader loXMLTextReader = this.foXMLTextReader;
    final boolean llOkay = loXMLTextReader.initializeXMLDocument(new File(this.getFullName()));

    if (!llOkay)
    {
      return (false);
    }

    final Document loDoc = loXMLTextReader.getDocument();

    final NodeList loNodeList = loDoc.getElementsByTagName(AddressesXML.ELEMENT_TAG);
    final int lnLen = loNodeList.getLength();

    for (int i = 0; i < lnLen; ++i)
    {
      final Element loElement = (Element) loNodeList.item(i);

      final Address loAddress = new Address();
      loAddress.fcSortName = loXMLTextReader.getAttributeString(loElement, AddressesXML.ATTRIBUTE_SORT_NAME, "");
      loAddress.fcName = loXMLTextReader.getAttributeString(loElement, AddressesXML.ATTRIBUTE_NAME, "");
      loAddress.fcStreet = loXMLTextReader.getAttributeString(loElement, AddressesXML.ATTRIBUTE_STREET, "");
      loAddress.fcCity = loXMLTextReader.getAttributeString(loElement, AddressesXML.ATTRIBUTE_CITY, "");
      loAddress.fcState = loXMLTextReader.getAttributeString(loElement, AddressesXML.ATTRIBUTE_STATE, "");
      loAddress.fcZipcode = loXMLTextReader.getAttributeString(loElement, AddressesXML.ATTRIBUTE_ZIPCODE, "");
      loAddress.fcCountry = loXMLTextReader.getAttributeString(loElement, AddressesXML.ATTRIBUTE_COUNTRY, "");

      this.foVectorData.add(loAddress);
    }

    Collections.sort(this.foVectorData);

    return (true);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  @Override
  public boolean saveXMLData()
  {
    final boolean llOkay = true;

    this.buildLines();
    this.buildContacts();

    final XMLTextWriter loTextWriter = this.foXMLTextWriter;

    loTextWriter.initializeXMLDocument();
    loTextWriter.createRootNode("Data", null);

    for (final Address loAddress : this.foVectorData)
    {
      loTextWriter.appendNodeToRoot(AddressesXML.ELEMENT_TAG, (String) null, new Object[][]{
          {AddressesXML.ATTRIBUTE_SORT_NAME, loAddress.fcSortName},
          {AddressesXML.ATTRIBUTE_NAME, loAddress.fcName},
          {AddressesXML.ATTRIBUTE_STREET, loAddress.fcStreet},
          {AddressesXML.ATTRIBUTE_CITY, loAddress.fcCity},
          {AddressesXML.ATTRIBUTE_STATE, loAddress.fcState},
          {AddressesXML.ATTRIBUTE_ZIPCODE, loAddress.fcZipcode},
          {AddressesXML.ATTRIBUTE_COUNTRY, loAddress.fcCountry}});
    }

    loTextWriter.saveToFile(this.getFullName(), this.fnIndent);

    return (llOkay);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void buildLines()
  {
    this.foLines.clear();

    LineIterator loLineIterator = null;
    try
    {
      loLineIterator = FileUtils.lineIterator(new File(AddressesXML.CONTACTS_VCARD), "UTF-8");
      while (loLineIterator.hasNext())
      {
        final String lcLine = loLineIterator.nextLine();
        if (lcLine.startsWith(" "))
        {
          final int lnPos = this.foLines.size() - 1;
          final String lcValue = this.foLines.get(lnPos) + lcLine.trim();
          this.foLines.set(lnPos, lcValue);
        }
        else
        {
          this.foLines.add(lcLine);
        }
      }
    }
    catch (final IOException loErr)
    {
      Util.errorMessage(null, "Problem reading " + AddressesXML.CONTACTS_VCARD + "\n\n" + loErr.getMessage());
    }
    finally
    {
      if (loLineIterator != null)
      {
        loLineIterator.close();
      }
    }
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void buildContacts()
  {
    this.foVectorData.clear();

    Address loAddress = null;
    for (final String lcLine : this.foLines)
    {

      if (lcLine.indexOf(AddressesXML.VCARD_START) == 0)
      {
        loAddress = new Address();
        continue;
      }

      if (lcLine.indexOf(AddressesXML.VCARD_END) == 0)
      {
        if (loAddress.fcSortName.isEmpty())
        {
          loAddress.fcSortName = loAddress.fcName;
        }

        if (!loAddress.fcStreet.isEmpty())
        {
          this.foVectorData.addElement(loAddress);
        }

        continue;
      }

      if (lcLine.indexOf(AddressesXML.VCARD_SORTNAME) == 0)
      {
        final String lcValue = lcLine.substring(AddressesXML.VCARD_SORTNAME.length());
        loAddress.fcSortName = lcValue.replaceAll(";", " ").replaceAll("\\\\", "").trim();
        continue;
      }

      if (lcLine.indexOf(AddressesXML.VCARD_NAME) == 0)
      {
        final String lcValue = lcLine.substring(AddressesXML.VCARD_NAME.length());
        // Replace semi-colon with space and replace all double slashes with empty string.
        loAddress.fcName = lcValue.replaceAll(";", " ").replaceAll("\\\\", "").trim();
        continue;
      }

      if (lcLine.toUpperCase().contains(AddressesXML.VCARD_ADDRESS))
      {
        int lnPos = lcLine.toUpperCase().indexOf(AddressesXML.VCARD_ADDRESS_START);
        if (lnPos >= 0)
        {
          lnPos += AddressesXML.VCARD_ADDRESS_START.length();
          final String lcValue = lcLine.substring(lnPos);

          final Vector<String> laLines = this.splitLines(lcValue);

          // By the way, multiple street lines are separated by \n in vCard 3.0
          loAddress.fcStreet = this.pruneAddressElement(laLines, AddressesXML.PARSE_STREET);
          loAddress.fcCity = this.pruneAddressElement(laLines, AddressesXML.PARSE_CITY);
          loAddress.fcState = this.pruneAddressElement(laLines, AddressesXML.PARSE_STATE);
          loAddress.fcZipcode = this.pruneAddressElement(laLines, AddressesXML.PARSE_ZIP);
          loAddress.fcCountry = this.pruneAddressElement(laLines, AddressesXML.PARSE_COUNTRY);
        }
      }

    }

    Collections.sort(this.foVectorData);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private String pruneAddressElement(final Vector<String> toLines, final int tnParseType)
  {
    String lcLine = "";
    final int lnIndex;

    switch (tnParseType)
    {
      case AddressesXML.PARSE_STREET:
        lcLine = this.getStreet(toLines);
        break;

      case AddressesXML.PARSE_CITY:
        lnIndex = this.getCityIndex(toLines);
        if (lnIndex != -1)
        {
          lcLine = toLines.get(lnIndex);
        }
        break;

      case AddressesXML.PARSE_STATE:
        lnIndex = this.getStateIndex(toLines);
        if (lnIndex != -1)
        {
          lcLine = toLines.get(lnIndex);
        }
        break;

      case AddressesXML.PARSE_ZIP:
        lnIndex = this.getZipcodeIndex(toLines);
        if (lnIndex != -1)
        {
          lcLine = toLines.get(lnIndex);
        }
        break;

      case AddressesXML.PARSE_COUNTRY:
        lnIndex = this.getCountryIndex(toLines);
        if (lnIndex != -1)
        {
          lcLine = toLines.get(lnIndex);
        }
        break;
    }

    // I couldn't get String.replaceAll("/\\,/", ",") to work.
    return (Util.replaceAll(lcLine, "\\,", ",").toString());
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private String getStreet(final Vector<String> toLines)
  {
    final int lnCityIndex = this.getCityIndex(toLines);
    if (lnCityIndex != -1)
    {
      final StringBuilder loStreet = new StringBuilder();
      for (int i = 0; i < lnCityIndex; ++i)
      {
        loStreet.append(toLines.get(i));
        if (i < (lnCityIndex - 1))
        {
          // Use literal character string as that's what the contact.vcf will generate.
          loStreet.append("\\n");
        }
      }
      return (loStreet.toString());
    }

    return ("");
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private int getCityIndex(final Vector<String> toLines)
  {
    final int lnZipIndex = this.getZipcodeIndex(toLines);
    if ((lnZipIndex != -1) && (lnZipIndex > 1))
    {
      return (lnZipIndex - 2);
    }

    // Otherwise find the last element that does not start with a number but ends with a number.
    final int lnLines = toLines.size();
    for (int i = lnLines - 1; i >= 0; i--)
    {
      final String lcValue = toLines.get(i).trim();
      if (!Character.isDigit(lcValue.charAt(0)) && Character.isDigit(lcValue.charAt(lcValue.length() - 1)))
      {
        return (i);
      }
    }

    return (-1);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private int getStateIndex(final Vector<String> toLines)
  {
    final int lnZipIndex = this.getZipcodeIndex(toLines);
    if ((lnZipIndex != -1) && (lnZipIndex > 0))
    {
      return (lnZipIndex - 1);
    }

    return (-1);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private int getZipcodeIndex(final Vector<String> toLines)
  {
    final int lnLines = toLines.size();
    // Find the last element that starts and ends with a number.
    for (int i = lnLines - 1; i >= 0; i--)
    {
      final String lcValue = toLines.get(i).trim();
      if (Character.isDigit(lcValue.charAt(0)) && Character.isDigit(lcValue.charAt(lcValue.length() - 1)))
      {
        return (i);
      }
    }

    return (-1);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private int getCountryIndex(final Vector<String> toLines)
  {
    final int lnIndex = toLines.size() - 1;
    final String lcValue = toLines.get(lnIndex).trim();
    if (!Character.isDigit(lcValue.charAt(0)) && !Character.isDigit(lcValue.charAt(lcValue.length() - 1)))
    {
      return (lnIndex);
    }

    return (-1);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // There are multiple semi-colons, some of them paired. So. . . .
  private Vector<String> splitLines(final String tcValue)
  {
    final Vector<String> loFinalLines = new Vector<String>();

    final String[] laLines = tcValue.split(";");

    for (final String lcLine : laLines)
    {
      final String lcValue = lcLine.trim();
      if (!lcValue.isEmpty())
      {
        loFinalLines.add(lcValue);
      }
    }

    return (loFinalLines);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // ---------------------------------------------------------------------------------------------------------------------
  // Interface Runnable
  @Override
  public void run()
  {
    if (Util.isMacintosh())
    {
      final String lcScriptFile = Global.LOCAL_PATH + "ExportAllContacts.scpt";

      final String lcScript = "tell application \"Contacts\"" + IOUtils.LINE_SEPARATOR +
          "set the clipboard to (vcard of people) as text" + IOUtils.LINE_SEPARATOR +
          "do shell script \"pbpaste > ~/Beowurks/JEnvelope/contacts.vcf\"" + IOUtils.LINE_SEPARATOR +
          "end tell";

      boolean llOkay = true;
      /* Re-create everytime, just in case of removal or updates. */
      try
      {
        FileUtils.writeStringToFile(new File(lcScriptFile), lcScript, (String) null, false);
      }
      catch (final IOException loErr)
      {
        llOkay = false;
        Util.errorMessageInThread(null, loErr.getMessage());
      }

      final long lnLastModified = (new File(AddressesXML.CONTACTS_VCARD)).lastModified();

      if (llOkay)
      {
        final String[] laArguments = new String[]{"osascript", lcScriptFile};

        llOkay = Global.launchApplication(this.foMainFrame.getAppProperties().getSudoKey(), laArguments, true);
      }

      if (llOkay)
      {
        // Wait up till ~15 seconds for the new file to be generated.
        int lnCount = 0;
        while (lnLastModified == (new File(AddressesXML.CONTACTS_VCARD)).lastModified())
        {
          ++lnCount;
          try
          {
            Thread.sleep(1000);
          }
          catch (final InterruptedException ignored)
          {
          }

          if (lnCount > 20)
          {
            Util.errorMessageInThread(null, "This operation took too long. Try again later.");
            break;
          }
        }

        // Just give it a little time to complete flushing the system.
        try
        {
          Thread.sleep(1000);
        }
        catch (final InterruptedException loErr)
        {
          loErr.printStackTrace();
        }
      }
    }

    // If you ever have to bring the program to the forefront again (now you don't),
    // then read http://stackoverflow.com/questions/309023/how-to-bring-a-window-to-the-front
    // Execute 2 of the run routines to introduce a slight delay.

    this.saveXMLData();

    final File loDataFile = new File(AddressesXML.CONTACTS_VCARD);
    final SimpleDateFormat loFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");

    this.foMainFrame.setMessage("<html>Addresses updated from <b>" + loDataFile.getPath() + "</b> created on "
        + loFormat.format(loDataFile.lastModified()) + ".</html>");

  }
  // ---------------------------------------------------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
