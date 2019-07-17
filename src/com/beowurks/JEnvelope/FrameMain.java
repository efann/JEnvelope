/*
 * JEnvelope: A program that prints envelopes from iCloud contacts.
 *
 * Copyright(c) 2012-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html)
 *
 *
 */

package com.beowurks.JEnvelope;

import com.beowurks.BeoCommon.BaseFrame;
import com.beowurks.BeoCommon.Dialogs.About.AboutAdapter;
import com.beowurks.BeoCommon.Dialogs.About.DialogAbout;
import com.beowurks.BeoCommon.Dialogs.About.IAbout;
import com.beowurks.BeoCommon.Dialogs.Credits.CreditAdapter;
import com.beowurks.BeoCommon.Dialogs.Credits.DialogCredits;
import com.beowurks.BeoCommon.Dialogs.Credits.ICredit;
import com.beowurks.BeoCommon.GridBagLayoutHelper;
import com.beowurks.BeoCommon.Util;
import com.beowurks.BeoLookFeel.LFDialog;
import com.beowurks.JEnvelope.forms.DialogPrinterInfo1;
import com.beowurks.JEnvelope.jasperreports.DialogPrintEnvelope;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Vector;

// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------

public class FrameMain extends FrameJEnvelopeBase implements ActionListener, MouseMotionListener, MouseListener
{
  private final static String FONT_NAME = "Arial";
  private final static int DISPLAY_BORDER = 70;

  private final AppProperties foAppProperties;

  final private JLabel lblStatusBar1 = new JLabel();

  final private JPanel pnlCombinedl = new JPanel();
  final private JPanel pnlEnvelopel = new JPanel();

  private final JCheckBox chkAddressee1ManualEntry = new JCheckBox();

  private final JComboBox cboReturn1 = new JComboBox();
  private final JComboBox cboAddressee1 = new JComboBox();

  private final JEditorPane txtReturn1 = new JEditorPane();
  private final JEditorPane txtAddressee1 = new JEditorPane();
  private final JScrollPane scrReturn1 = new JScrollPane();
  private final JScrollPane scrAddressee1 = new JScrollPane();

  private final JLabel lblStamp1 = new JLabel();
  private final JLabel lblMessages = new JLabel();

  private Address foCurrentReturn;
  private Address foCurrentAddressee;
  private Address foCurrentManualEntry;

  private final AddressesXML foAddressesXML = new AddressesXML(Global.LOCAL_PATH, "Addresses.xml", this);

  private final CompoundBorder foStandardBorder = BorderFactory.createCompoundBorder(
      BorderFactory.createRaisedBevelBorder(),
      BorderFactory.createLoweredBevelBorder());

  final MenusAndButtons foMenusAndButtons = new MenusAndButtons(this);

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------------------------------------------------
  public FrameMain(final Application toApplication)
  {
    this.foAppProperties = toApplication.foAppProperties;

    try
    {
      Global.makeAllTemporaryDirectories();
      // Just in case there were files leftover previously.

      this.jbInit();

      this.updateMenusAndToolBars();

    }
    catch (final Exception loErr)
    {
      Util.showStackTraceInMessage(this, loErr, "Exception");
    }
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // Component initialization
  @Override
  protected void jbInit() throws Exception
  {
    super.jbInit();

    final Dimension ldScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.setSize(new Dimension((int) (ldScreenSize.width * 0.75), (int) (ldScreenSize.height * 0.80)));

    this.setTitle(Util.getTitle());

    this.setupTextBoxes();
    this.setupComboBoxes();
    this.setupCheckBoxes();
    this.setupPanels();
    this.setupLabels();
    this.setupListeners();
    this.setupLayouts();

    this.readProperties();
    this.updateManualEntry(true);

    this.checkSystem(false);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  protected void updateMenusAndToolBars()
  {
    // It doesn't matter if loActiveFrame is null or not.
    final MenusAndButtons loMenuButton = this.foMenusAndButtons;

    loMenuButton.menuLookFeel1.setEnabled(!Util.isMacintosh());
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public void setupLayouts()
  {
    if ((this.cboReturn1 == null) || (this.cboAddressee1 == null))
    {
      System.err.println("The comboboxes have yet to be initialized.");
      return;
    }

    // I've added these extra panels to give a bordered look to the envelope
    // section.
    final BorderLayout loBorderLayout = new BorderLayout(0, FrameMain.DISPLAY_BORDER);
    this.getContentPane().setLayout(loBorderLayout);
    this.getContentPane().add(this.foMenusAndButtons.tlbJEnvelope1, BorderLayout.PAGE_START);

    final JPanel loPanel1 = new JPanel();
    loPanel1.setPreferredSize(new Dimension(FrameMain.DISPLAY_BORDER, FrameMain.DISPLAY_BORDER));
    this.getContentPane().add(loPanel1, BorderLayout.LINE_START);
    this.getContentPane().add(this.pnlCombinedl, BorderLayout.CENTER);
    final JPanel loPanel2 = new JPanel();
    loPanel2.setPreferredSize(new Dimension(FrameMain.DISPLAY_BORDER, FrameMain.DISPLAY_BORDER));
    this.getContentPane().add(loPanel2, BorderLayout.LINE_END);

    this.lblStatusBar1.setText("Welcome to JEnvelope");
    this.getContentPane().add(this.lblStatusBar1, BorderLayout.PAGE_END);

    final GridBagLayoutHelper loGridBag = new GridBagLayoutHelper();
    loGridBag.setInsets(5, 5, 5, 5);

    // ----------------------------------------------------------
    this.pnlCombinedl.setLayout(new GridBagLayout());
    this.pnlCombinedl.add(this.lblMessages,
        loGridBag.getConstraint(0, 0, GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL));
    this.pnlCombinedl.add(this.pnlEnvelopel,
        loGridBag.getConstraint(0, 2, 2, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH));

    // ----------------------------------------------------------
    // Envelope
    this.pnlEnvelopel.setLayout(new GridBagLayout());

    this.pnlEnvelopel.add(this.cboReturn1,
        loGridBag.getConstraint(0, 0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE));
    this.pnlEnvelopel.add(this.scrReturn1,
        loGridBag.getConstraint(0, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH));

    this.pnlEnvelopel.add(this.lblStamp1,
        loGridBag.getConstraint(1, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE));

    this.pnlEnvelopel.add(this.chkAddressee1ManualEntry,
        loGridBag.getConstraint(1, 2, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE));
    this.pnlEnvelopel.add(this.cboAddressee1,
        loGridBag.getConstraint(1, 3, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE));
    this.pnlEnvelopel.add(this.scrAddressee1,
        loGridBag.getConstraint(1, 4, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public void setupLabels()
  {
    final URL loURLIcon = FrameJEnvelopeBase.class.getResource("/com/beowurks/JEnvelope/images/Penny_black.jpg");
    if (loURLIcon != null)
    {
      final Image loImage = Toolkit.getDefaultToolkit().createImage(loURLIcon);
      this.lblStamp1.setIcon(new ImageIcon(loImage));
    }

    this.lblStamp1.setCursor(new Cursor(Cursor.HAND_CURSOR));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void setupCheckBoxes()
  {
    this.chkAddressee1ManualEntry.setText("Manual Entry for Addressee");
    this.chkAddressee1ManualEntry.setCursor(new Cursor(Cursor.HAND_CURSOR));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public void setupPanels()
  {
    this.pnlEnvelopel.setBorder(BorderFactory.createRaisedBevelBorder());
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public void setupTextBoxes()
  {
    this.scrReturn1.setViewportView(this.txtReturn1);
    this.scrAddressee1.setViewportView(this.txtAddressee1);

    // setEnabled makes the text barely readable.
    this.txtReturn1.setEditable(false);
    this.txtAddressee1.setEditable(false);

    this.txtReturn1.setContentType("text/html");
    this.txtAddressee1.setContentType("text/html");

    this.txtReturn1.setBorder(null);
    this.scrReturn1.setBorder(this.foStandardBorder);

    // You don't need to set any sizes for the text boxes and their
    // scroll panels. The layout manager handles it with
    // GridBagConstraints.BOTH).
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public void setupComboBoxes()
  {
    this.cboReturn1.setCursor(new Cursor(Cursor.HAND_CURSOR));
    this.cboAddressee1.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // By setting the preferred, minimum and maximum sizes,
    // the cell widths will not change.
    final int lnComboHeight = (int) this.cboReturn1.getPreferredSize().getHeight();

    this.cboReturn1.setPreferredSize(new Dimension(300, lnComboHeight));
    this.cboReturn1.setMinimumSize(new Dimension(300, lnComboHeight));
    this.cboReturn1.setMaximumSize(new Dimension(300, lnComboHeight));

    this.cboAddressee1.setPreferredSize(new Dimension(300, lnComboHeight));
    this.cboAddressee1.setMinimumSize(new Dimension(300, lnComboHeight));
    this.cboAddressee1.setMaximumSize(new Dimension(300, lnComboHeight));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void setupListeners()
  {
    this.foMenusAndButtons.btnPrint1.addActionListener(this);
    this.foMenusAndButtons.btnRefreshAddresses1.addActionListener(this);
    this.foMenusAndButtons.btnContactsApp1.addActionListener(this);

    this.lblStamp1.addMouseListener(this);

    this.foMenusAndButtons.manipulateMenuListeners(true);

    this.cboReturn1.addActionListener(this);
    this.cboAddressee1.addActionListener(this);

    this.chkAddressee1ManualEntry.addActionListener(this);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public AppProperties getAppProperties()
  {
    return (this.foAppProperties);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void performButtonAction(final ActionEvent toEvent)
  {
    final Object loButton = toEvent.getSource();

    if (loButton == this.foMenusAndButtons.btnPrint1)
    {
      this.printEnvelope();
    }
    else if (loButton == this.foMenusAndButtons.btnRefreshAddresses1)
    {
      this.refreshAddressComponents();
    }
    else if (loButton == this.foMenusAndButtons.btnContactsApp1)
    {
      final String[] laArguments = new String[]{"open", "/Applications/Contacts.app"};

      Global.launchApplication(this.foAppProperties.getSudoKey(), laArguments, true);
    }
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void performMenuAction(final ActionEvent toEvent)
  {
    final Object loMenuItem = toEvent.getSource();

    if (loMenuItem == this.foMenusAndButtons.menuExit1)
    {
      this.performShutdownMaintenance();
    }
    else if (loMenuItem == this.foMenusAndButtons.menuPrinterInfo1)
    {
      new DialogPrinterInfo1(this);
    }
    else if (loMenuItem == this.foMenusAndButtons.menuCredits1)
    {
      this.showCredits();
    }
    else if (loMenuItem == this.foMenusAndButtons.menuDocumentation1)
    {
      Util.launchBrowser("http://www.beowurks.com/content/jprinte-help");
    }
    else if (loMenuItem == this.foMenusAndButtons.menuAbout1)
    {
      this.showAbout();
    }
    else if (loMenuItem == this.foMenusAndButtons.menuLookFeel1)
    {
      new LFDialog(BaseFrame.getActiveFrame());
    }
    else if (loMenuItem == this.foMenusAndButtons.menuRefreshAddresses1)
    {
      this.foMenusAndButtons.btnRefreshAddresses1.doClick();
    }
    else if (loMenuItem == this.foMenusAndButtons.menuCheckSystem1)
    {
      this.setBusy(true);
      this.checkSystem(true);
      this.setBusy(false);
    }
    else if (loMenuItem == this.foMenusAndButtons.menuSudoKey1)
    {
      this.foAppProperties.setSudoKey();
    }
    else if (loMenuItem == this.foMenusAndButtons.menuPrint1)
    {
      this.foMenusAndButtons.btnPrint1.doClick();
    }
    else if (loMenuItem == this.foMenusAndButtons.menuContactsApp1)
    {
      this.foMenusAndButtons.btnContactsApp1.doClick();
    }

  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void showAbout()
  {
    try
    {
      final BufferedImage loLogo = ImageIO.read(this.getClass().getResource("/com/beowurks/JEnvelope/images/JEnvelope.png"));
      final String lcTitleURL = "https://www.beowurks.com/applications/single/JConsult-Suite";

      final IAbout loAbout = new AboutAdapter(Util.getTitle(), lcTitleURL,
          loLogo, lcTitleURL,
          "Eclipse Public License 2.0", "https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html",
          1990, "Beowurks", "https://www.beowurks.com/");

      new DialogAbout(BaseFrame.getActiveFrame(), loAbout);
    }
    catch (final Exception loError)
    {
      Util.showStackTraceInMessage(this, loError, "Oops!");
    }
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void showCredits()
  {
    final Vector<ICredit> loVectorLinks = new Vector<ICredit>();

    loVectorLinks.add(new CreditAdapter("The image used for the stamp can be found at the following url. This link also includes licensing information.",
        "http://en.wikipedia.org/wiki/File:Penny_black.jpg"));

    loVectorLinks.add(new CreditAdapter("All other images, save for JEnvelope icon, came from the <em>Nuvola</em> icon theme for KDE 3.x by David Vignoni.",
        "http://commons.wikimedia.org/wiki/Nuvola"));

    loVectorLinks.add(new CreditAdapter("JasperReports Library was used to generate the envelopes.",
        "http://community.jaspersoft.com/"));

    loVectorLinks.add(new CreditAdapter("iReport Designer was used to create and design the envelopes.",
        "http://community.jaspersoft.com/project/ireport-designer"));

    new DialogCredits(this, loVectorLinks);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void readProperties()
  {
    final AppProperties loProp = this.getAppProperties();

    this.updateAddressComponents();
    this.cboReturn1.setSelectedItem(new Address(loProp.getComboboxReturn()));
    this.cboAddressee1.setSelectedItem(new Address(loProp.getComboboxAddressee()));

    this.chkAddressee1ManualEntry.setSelected(loProp.getAddresseeManualEntry());

    final Address loManual = new Address();
    loManual.fcName = loProp.getManualName();
    loManual.fcStreet = loProp.getManualStreet();
    loManual.fcCity = loProp.getManualCity();
    loManual.fcState = loProp.getManualState();
    loManual.fcZipcode = loProp.getManualZipcode();

    this.foCurrentManualEntry = loManual;
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void writeProperties()
  {
    final AppProperties loProp = this.getAppProperties();

    loProp.setComboboxReturn((this.foCurrentReturn != null) ? this.foCurrentReturn.toString() : "");
    loProp.setComboboxAddressee((this.foCurrentAddressee != null) ? this.foCurrentAddressee.toString() : "");

    loProp.setAddresseeManualEntry(this.isManualEntry());

    // This looks weird. Realize, you only want to decipher the manual entry when isManualEntry
    // is true. Otherwise the current text box's value comes from a list and the Manuel Entry is stored in foCurrentManualEntry.
    final Address loManual = this.isManualEntry() ? this.decipherAddress(this.txtAddressee1) : this.foCurrentManualEntry;
    loProp.setManualName(loManual.fcName);
    loProp.setManualStreet(loManual.fcStreet);
    loProp.setManualCity(loManual.fcCity);
    loProp.setManualState(loManual.fcState);
    loProp.setManualZipcode(loManual.fcZipcode);

  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void performShutdownMaintenance()
  {
    this.runCleanupOnAllFrames();

    // The property savings needs to come after runCleanupOnAllFrames.
    final AppProperties loProp = this.foAppProperties;

    loProp.setLookAndFeel();
    loProp.setMetalTheme();

    this.writeProperties();

    loProp.writeProperties();

    Global.cleanFilesFromTemporaryDirectories();

    System.exit(0);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // Overridden so we can exit when window is closed
  @Override
  protected void processWindowEvent(final WindowEvent e)
  {
    super.processWindowEvent(e);

    switch (e.getID())
    {
      case WindowEvent.WINDOW_CLOSING:
        this.performShutdownMaintenance();
        break;
    }
  }

  // ---------------------------------------------------------------------------------------------------------------------
  @Override
  protected void removeListeners()
  {
    this.foMenusAndButtons.manipulateMenuListeners(false);

    super.removeListeners();
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void runCleanupOnAllFrames()
  {
    final Frame[] laFrames = Frame.getFrames();

    for (final Frame laXFrame : laFrames)
    {
      if (laXFrame instanceof BaseFrame)
      {
        final BaseFrame loFrame = (BaseFrame) laXFrame;
        if (loFrame.isVisible())
        {
          loFrame.cleanUp();
        }
      }
    }

  }

  // ---------------------------------------------------------------------------------------------------------------------
  public void setMessageBarText(final String tcMessage)
  {
    this.lblStatusBar1.setText(tcMessage);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // jsoup removes all HTML. Unfortunately, it also shrinks many spaces
  // to one and removes link breaks (\n characters).
  private Address decipherAddress(final JEditorPane toTxtAddress)
  {
    // Replace with the new line character.
    String lcAddress = toTxtAddress.getText().trim()
        .replaceAll("<b>", "")
        .replaceAll("</b>", "")
        .replaceAll("<i>", "")
        .replaceAll("</i>", "")
        .replaceAll("<em>", "")
        .replaceAll("</em>", "")
        .replaceAll(Global.HTML_LINE_BREAK, Global.CR_STRING)
        .replaceAll("<br>", Global.CR_STRING)
        .replaceAll(Global.LINE_SEP, Global.CR_STRING)
        .replaceAll("\\<.*?>", Global.CR_STRING);

    if (Util.isWindows())
    {
      lcAddress = lcAddress.replaceAll("\n", Global.CR_STRING);
    }

    // Actual new line character.
    final String[] laText = lcAddress.split(Global.CR_STRING);

    final Vector<String> loText = new Vector<String>();

    for (final String lcText : laText)
    {
      final String lcLine = lcText.trim();
      if (!lcLine.isEmpty())
      {
        loText.add(lcLine);
      }
    }

    // ************************
    final int lnLines = loText.size();
    final Address loAddress = new Address();

    loAddress.fcName = (lnLines > 0) ? loText.get(0) : "";
    loAddress.fcStreet = "";
    for (int x = 1; x < (lnLines - 1); ++x)
    {
      loAddress.fcStreet += loText.get(x);
      // If less than the last one.
      if (x < (lnLines - 2))
      {
        // Actual "\n" string, not the new line character.
        loAddress.fcStreet += "\\n";
      }
    }

    // ************************
    final String lcCityStateZip = (lnLines > 2) ? loText.get(lnLines - 1) : "";

    // Just want the first comma
    final int lnComma1 = lcCityStateZip.indexOf(',');
    loAddress.fcCity = (lnComma1 != -1) ? lcCityStateZip.substring(0, lnComma1).trim() : "";

    // ************************
    // Just want the first number of the last number set.
    int lnNumber1 = 0;
    boolean llLastDigitFound = false;
    for (int i = lcCityStateZip.length() - 1; i >= 0; i--)
    {
      final char lcChar = lcCityStateZip.charAt(i);

      if (llLastDigitFound)
      {
        if ((!Character.isDigit(lcChar)) && (lcChar != '-'))
        {
          lnNumber1 = i;
          break;
        }
      }
      else if ((Character.isDigit(lcChar)) && (!llLastDigitFound))
      {
        llLastDigitFound = true;
      }
    }

    // ************************
    if (lnComma1 == -1)
    {
      loAddress.fcZipcode = "";
      loAddress.fcState = "";
    }
    else
    {
      loAddress.fcZipcode = (lnNumber1 > 0) ? lcCityStateZip.substring(lnNumber1).trim() : "";
      loAddress.fcState = (lnNumber1 > 0) ? lcCityStateZip.substring(lnComma1 + 1, lnNumber1).trim() : lcCityStateZip.substring(lnComma1 + 1).trim();
    }

    return (loAddress);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private boolean verifyAddress(final Address loAddress, final boolean tlShowMessage)
  {
    final boolean llError = (loAddress.fcName.isEmpty() || loAddress.fcStreet.isEmpty() || loAddress.fcCity.isEmpty()
        || loAddress.fcState.isEmpty() || loAddress.fcZipcode.isEmpty());

    if ((llError) && (tlShowMessage))
    {
      final String lcAddress = loAddress.fcName + "\n" + loAddress.fcStreet + "\n" + loAddress.fcCity + ", "
          + loAddress.fcState + " " + loAddress.fcZipcode;

      Util.errorMessage(this, "Addressee must have the format of\n\nName\nStreet Address\nCity, State Zipcode\n\n"
          + lcAddress);
    }

    return (!llError);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void printEnvelope()
  {
    final Address loReturn = this.decipherAddress(this.txtReturn1);

    if (!this.verifyAddress(loReturn, true))
    {
      return;
    }

    final Address loAddressee = this.decipherAddress(this.txtAddressee1);

    if (!this.verifyAddress(loAddressee, true))
    {
      return;
    }

    final DialogPrintEnvelope loFrame = new DialogPrintEnvelope(this, loReturn, loAddressee);
    loFrame.makeVisible(true);

  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void updateManualEntry(final boolean tlInitializing)
  {
    final boolean llSelected = this.isManualEntry();

    this.cboAddressee1.setEnabled(!llSelected);
    this.txtAddressee1.setEditable(llSelected);

    if (llSelected)
    {
      final Color loColor = new Color(0xC0D9D9);
      this.txtAddressee1.setBorder(BorderFactory.createLineBorder(loColor));
      this.scrAddressee1.setBorder(BorderFactory.createLineBorder(loColor));
    }
    else
    {
      if (!tlInitializing)
      {
        // Remember the current manual entry.
        this.foCurrentManualEntry = this.decipherAddress(this.txtAddressee1);
      }

      this.txtAddressee1.setBorder(null);
      this.scrAddressee1.setBorder(this.foStandardBorder);
    }

    this.updateEditorPane(this.txtAddressee1);
  }


  // ---------------------------------------------------------------------------------------------------------------------
  private void refreshAddressComponents()
  {
    this.setBusy(true);

    final Thread loThread = new Thread(this.foAddressesXML);
    loThread.start();

    while (loThread.isAlive())
    {
      try
      {
        Thread.sleep(500);
      }
      catch (final InterruptedException ignored)
      {
      }
    }
    this.updateAddressComponents();

    this.setBusy(false);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void updateAddressComponents()
  {
    final String lcCurrentReturn = (this.cboReturn1.getSelectedItem() != null) ? this.cboReturn1.getSelectedItem()
        .toString() : "";
    final String lcCurrentAddressee = (this.cboAddressee1.getSelectedItem() != null) ? this.cboAddressee1
        .getSelectedItem().toString() : "";

    final Vector<Address> loData = this.foAddressesXML.getData();

    this.cboReturn1.removeAllItems();
    this.cboAddressee1.removeAllItems();

    for (final Address loXAddress : loData)
    {
      final Address loAddress = new Address(loXAddress.fcSortName);
      this.cboReturn1.addItem(loAddress);
      this.cboAddressee1.addItem(loAddress);
    }

    if (!lcCurrentReturn.isEmpty())
    {
      this.cboReturn1.setSelectedItem(new Address(lcCurrentReturn));
    }
    if (!lcCurrentAddressee.isEmpty())
    {
      this.cboAddressee1.setSelectedItem(new Address(lcCurrentAddressee));
    }
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public void setMessage(final String tcMessage)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        FrameMain.this.lblMessages.setText(tcMessage);
      }
    });
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void checkSystem(final boolean tlDisplayOkay)
  {
    final StringBuilder loMessage = new StringBuilder();

    if (!this.foAddressesXML.exists())
    {
      loMessage.append("You need to run <b>Refresh Addresses</b>." + Global.HTML_LINE_BREAK);
    }

    if (loMessage.length() > 0)
    {
      this.setMessage("<html><div style='font-size: 12px; color: red; padding-left: 20px;'>" + loMessage.toString()
          + "</div></html>");
    }
    else
    {
      this.setMessage((tlDisplayOkay) ? "<html><div style='font-size: 12px; color: black; padding-left: 100px;'><b>All is well. . . .</b></div></html>"
          : "<html></html>");
    }

  }

  // ---------------------------------------------------------------------------------------------------------------------
  public Address getReturnAddress()
  {
    return (this.foCurrentReturn);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public Address getAddressee()
  {
    return (this.foCurrentAddressee);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public Address getManualAddressee()
  {
    // Realize, you only want to decipher the manual entry when isManualEntry
    // is true. Otherwise the current text box's value comes from a list.
    if (this.isManualEntry())
    {
      this.foCurrentManualEntry = this.decipherAddress(this.txtAddressee1);
    }

    return (this.foCurrentManualEntry);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public boolean isManualEntry()
  {
    return (this.chkAddressee1ManualEntry.isSelected());
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void updateEditorPane(final JEditorPane toEditorPane)
  {
    boolean llCenter = false;
    boolean llBoldFirstLine = false;

    String lcFontName = null;
    String lcFontSizeFirstLine = null;
    String lcFontSize = null;

    Address loAddress = null;

    boolean llReadOnly = false;

    if (toEditorPane == this.txtReturn1)
    {
      llReadOnly = true;
      llCenter = true;
      llBoldFirstLine = true;

      lcFontName = FrameMain.FONT_NAME;
      lcFontSizeFirstLine = "10";
      lcFontSize = "9";
      loAddress = this.foCurrentReturn;
    }
    else if (toEditorPane == this.txtAddressee1)
    {
      lcFontName = FrameMain.FONT_NAME;
      lcFontSizeFirstLine = "12";
      lcFontSize = "12";
      loAddress = (this.isManualEntry()) ? this.foCurrentManualEntry : this.foCurrentAddressee;

      llReadOnly = (!this.isManualEntry());
    }

    if (loAddress != null)
    {
      final StringBuilder loText = new StringBuilder("<div style='font-family: " + lcFontName + "; font-size: " + lcFontSizeFirstLine + "; "
          + (llCenter ? "text-align: center; " : "") + "'>" + (llBoldFirstLine ? "<b>" : "") + loAddress.fcName
          + (llBoldFirstLine ? "</b>" : "") + "</div>").append(Global.LINE_SEP);
      loText.append("<div style='font-family: ").append(lcFontName).append("; font-size: ").append(lcFontSize).append("; ").append(llCenter ? "text-align: center; " : "").append("'>");
      loText.append("<div>").append(loAddress.fcStreet.replaceAll("\\\\n", Global.HTML_LINE_BREAK).replaceAll(Global.CR_STRING, Global.HTML_LINE_BREAK)).append("</div>");
      loText.append("<div>").append(loAddress.fcCity);

      // Now there won't be an out-of-place comma when the city, state and zip code are in the city field.
      if ((!loAddress.fcState.isEmpty()) || (!loAddress.fcZipcode.isEmpty()))
      {
        loText.append(", ");
      }
      loText.append(loAddress.fcState).append(" ").append(loAddress.fcZipcode).append("</div>").append("</div>");
      final String lcBody = "<body style='" + (llReadOnly ? "background-color: #C0D9D9;" : "") + " padding: 5px;'>";

      toEditorPane.setText(lcBody + loText.toString() + "</body>");
    }
    else
    {
      toEditorPane.setText("Null address value. . . .");
    }
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // ---------------------------------------------------------------------------------------------------------------------
  // Interface MouseListener
  // ---------------------------------------------------------------------------------------------------------------------
  @Override
  public void mouseClicked(final MouseEvent teMouseEvent)
  {
    if (teMouseEvent.getComponent() == this.lblStamp1)
    {
      Util.launchBrowser("http://en.wikipedia.org/wiki/File:Penny_black.jpg");
    }
  }

  // ---------------------------------------------------------------------------------------------------------------------
  @Override
  public void mouseEntered(final MouseEvent teMouseEvent)
  {
  }

  // ---------------------------------------------------------------------------------------------------------------------
  @Override
  public void mouseExited(final MouseEvent teMouseEvent)
  {
  }

  // ---------------------------------------------------------------------------------------------------------------------
  @Override
  public void mousePressed(final MouseEvent teMouseEvent)
  {
  }

  // ---------------------------------------------------------------------------------------------------------------------
  @Override
  public void mouseReleased(final MouseEvent teMouseEvent)
  {
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // ---------------------------------------------------------------------------------------------------------------------
  // Interface ActionListener
  // ---------------------------------------------------------------------------------------------------------------------
  @Override
  public void actionPerformed(final ActionEvent toEvent)
  {
    final Object loSource = toEvent.getSource();

    if (loSource instanceof JMenuItem)
    {
      this.performMenuAction(toEvent);
    }
    else if (loSource instanceof JButton)
    {
      this.performButtonAction(toEvent);
    }
    else if (loSource instanceof JCheckBox)
    {
      final JCheckBox loCheck = (JCheckBox) loSource;
      if (loCheck.equals(this.chkAddressee1ManualEntry))
      {
        this.updateManualEntry(false);
      }
    }
    else if (loSource instanceof JComboBox)
    {
      final JComboBox loCombo = (JComboBox) loSource;

      if (loCombo.equals(this.cboReturn1))
      {
        if (loCombo.getSelectedItem() != null)
        {
          this.foCurrentReturn = this.foAddressesXML.getAddress(loCombo.getSelectedItem().toString());

          this.updateEditorPane(this.txtReturn1);
        }
      }
      else if (loCombo.equals(this.cboAddressee1))
      {
        if (loCombo.getSelectedItem() != null)
        {
          this.foCurrentAddressee = this.foAddressesXML.getAddress(loCombo.getSelectedItem().toString());

          this.updateEditorPane(this.txtAddressee1);
        }
      }
    }
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // ---------------------------------------------------------------------------------------------------------------------
  // Interface MouseMotionListener
  // ---------------------------------------------------------------------------------------------------------------------
  @Override
  public void mouseDragged(final MouseEvent e)
  {
  }

  // ---------------------------------------------------------------------------------------------------------------------
  @Override
  public void mouseMoved(final MouseEvent e)
  {
    final Object loSource = e.getSource();
    if (loSource instanceof JMenuItem)
    {
      this.setMessageBarText(((JMenuItem) loSource).getToolTipText());
    }
  }
  // ---------------------------------------------------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
