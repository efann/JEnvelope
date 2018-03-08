/*
 * JEnvelope: A program that prints envelopes from iCloud contacts.
 *
 * Copyright(c) 2012-2018, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html)
 *
 *
 */

package com.beowurks.JEnvelope;

import com.beowurks.BeoCommon.BaseButton;
import com.beowurks.BeoCommon.Util;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.Vector;

// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
public class MenusAndButtons
{
  final public BaseButton btnPrint1 = new BaseButton(72, 72);
  final public BaseButton btnRefreshAddresses1 = new BaseButton(72, 72);
  final public BaseButton btnContactsApp1 = new BaseButton(72, 72);

  final public JMenuBar menuBar1 = new JMenuBar();

  final public JMenu menuFile1 = new JMenu();
  final public JMenu menuTools1 = new JMenu();
  final public JMenu menuHelp1 = new JMenu();

  final public JMenuItem menuSudoKey1 = new JMenuItem();
  final public JMenuItem menuPrint1 = new JMenuItem();
  final public JMenuItem menuExit1 = new JMenuItem();

  final public JMenuItem menuLookFeel1 = new JMenuItem();
  final public JMenuItem menuRefreshAddresses1 = new JMenuItem();
  final public JMenuItem menuCheckSystem1 = new JMenuItem();

  final public JMenuItem menuContactsApp1 = new JMenuItem();

  public final JMenuItem menuDocumentation1 = new JMenuItem();
  public final JMenuItem menuPrinterInfo1 = new JMenuItem();
  public final JMenuItem menuCredits1 = new JMenuItem();

  public final JMenuItem menuAbout1 = new JMenuItem();

  final public JToolBar tlbJEnvelope1 = new JToolBar("JEnvelope Tool Bar");

  final private FrameMain foFrameMain;

  // ---------------------------------------------------------------------------------------------------------------------
  public MenusAndButtons(final FrameMain toFrameMain)
  {
    this.foFrameMain = toFrameMain;

    this.setupMenu();

    try
    {
      this.setupButtons();
    }
    catch (final Exception loErr)
    {
      Util.showStackTraceInMessage(toFrameMain, loErr, "Unfortunate Exception");
    }

    this.setupToolBars();

    this.foFrameMain.setJMenuBar(this.menuBar1);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void setupMenu()
  {
    // ----
    this.menuFile1.setText("File");
    this.menuFile1.setMnemonic('F');

    this.menuSudoKey1.setText("Sudo Key");
    this.menuSudoKey1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/JEnvelope/images/kgpg_key232.png")));
    this.menuSudoKey1.setToolTipText("Set the Sudo password");

    this.menuPrint1.setText("Print");
    this.menuPrint1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/JEnvelope/images/printer32.png")));
    this.menuPrint1.setMnemonic('P');
    this.menuPrint1.setToolTipText("Print the envelope");

    this.menuExit1.setText("Exit");
    this.menuExit1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/JEnvelope/images/exit32.png")));
    this.menuExit1.setMnemonic('X');
    this.menuExit1.setToolTipText("Exit JEnvelope");

    // ----
    this.menuTools1.setText("Tools");
    this.menuTools1.setMnemonic('T');

    this.menuLookFeel1.setText("Look & Feel...");
    this.menuLookFeel1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/JEnvelope/images/looknfeel32.png")));
    this.menuLookFeel1.setMnemonic('L');
    this.menuLookFeel1.setToolTipText("Customize the look & feel of this application");

    this.menuRefreshAddresses1.setText("Refresh Addresses");
    this.menuRefreshAddresses1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/JEnvelope/images/refresh32.png")));
    this.menuRefreshAddresses1.setMnemonic('C');
    this.menuRefreshAddresses1.setToolTipText("Refresh the list of contacts");

    this.menuCheckSystem1.setText("Check System");
    this.menuCheckSystem1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/JEnvelope/images/check32.png")));
    this.menuCheckSystem1.setToolTipText("Check that the application is running well");

    this.menuContactsApp1.setText("Contacts App...");
    this.menuContactsApp1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/JEnvelope/images/contacts32.png")));
    this.menuContactsApp1.setToolTipText("Launch the Apple Contacts App");

    // ----
    this.menuHelp1.setText("Help");
    this.menuHelp1.setMnemonic('H');

    this.menuPrinterInfo1.setText("Printer Info");
    this.menuPrinterInfo1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/JEnvelope/images/printer_info32.png")));
    this.menuPrinterInfo1.setToolTipText("Information about the printers connected to this system");

    this.menuCredits1.setText("Credits");
    this.menuCredits1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/JEnvelope/images/credits32.png")));
    this.menuCredits1.setMnemonic('C');
    this.menuCredits1.setToolTipText("Credits for pieces parts used in this program");

    this.menuDocumentation1.setText("Help Documentation");
    this.menuDocumentation1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/JEnvelope/images/help32.png")));
    this.menuDocumentation1.setMnemonic('D');
    this.menuDocumentation1.setToolTipText("Documentation on how to use this program");
    this.menuDocumentation1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));

    this.menuAbout1.setText("About");
    this.menuAbout1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/JEnvelope/images/about32.png")));
    this.menuAbout1.setMnemonic('A');
    this.menuAbout1.setToolTipText("About this program");

    // ----
    this.menuBar1.add(this.menuFile1);
    this.menuBar1.add(this.menuTools1);
    this.menuBar1.add(this.menuHelp1);

    this.menuFile1.add(this.menuSudoKey1);
    this.menuFile1.addSeparator();
    this.menuFile1.add(this.menuPrint1);
    if (!Util.isMacintosh())
    {
      this.menuFile1.addSeparator();
      this.menuFile1.add(this.menuExit1);
    }

    if (!Util.isMacintosh())
    {
      this.menuTools1.add(this.menuLookFeel1);
      this.menuTools1.addSeparator();
    }

    this.menuTools1.add(this.menuRefreshAddresses1);
    this.menuTools1.addSeparator();
    this.menuTools1.add(this.menuCheckSystem1);
    this.menuTools1.add(this.menuContactsApp1);

    this.menuHelp1.add(this.menuPrinterInfo1);
    this.menuHelp1.add(this.menuCredits1);
    this.menuHelp1.add(this.menuDocumentation1);
    if (!Util.isMacintosh())
    {
      this.menuHelp1.addSeparator();
      this.menuHelp1.add(this.menuAbout1);
    }

  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void setupToolBars()
  {
    this.tlbJEnvelope1.setFloatable(false);
    this.tlbJEnvelope1.setRollover(true);

    this.tlbJEnvelope1.add(this.btnPrint1);
    this.tlbJEnvelope1.addSeparator();
    this.tlbJEnvelope1.add(this.btnRefreshAddresses1);
    this.tlbJEnvelope1.addSeparator();
    this.tlbJEnvelope1.add(this.btnContactsApp1);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void setupButtons() throws Exception
  {
    if (this.menuFile1.getText().isEmpty())
    {
      throw new Exception("The menus have not yet been set for the routine setupButtons in JEnvelope.");
    }

    MenusAndButtons.initToolBarButton(this.btnPrint1, this.menuPrint1);
    MenusAndButtons.initToolBarButton(this.btnRefreshAddresses1, this.menuRefreshAddresses1);
    MenusAndButtons.initToolBarButton(this.btnContactsApp1, this.menuContactsApp1);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private static void initToolBarButton(final JButton toButton, final JMenuItem toMenuItem)
  {
    toButton.setIcon(toMenuItem.getIcon());

    final String lcText = toMenuItem.getText();

    toButton.setText("<html><div style='text-align: center;'>" + lcText.replaceAll(" ", "<br />") + "</div></html>");
    toButton.setVerticalTextPosition(SwingConstants.BOTTOM);
    toButton.setHorizontalTextPosition(SwingConstants.CENTER);
    toButton.setMnemonic(toMenuItem.getMnemonic());
    toButton.setToolTipText(toMenuItem.getToolTipText());
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public void manipulateMenuListeners(final boolean tlAddMenuListener)
  {
    final Vector<JMenuItem> loMenuItems = new Vector<JMenuItem>();

    this.extractMenuItems(this.menuBar1, loMenuItems);

    for (final JMenuItem loMenuItem : loMenuItems)
    {
      if (tlAddMenuListener)
      {
        loMenuItem.addActionListener(this.foFrameMain);
        loMenuItem.addMouseMotionListener(this.foFrameMain);
      }
      else
      {
        loMenuItem.removeActionListener(this.foFrameMain);
        loMenuItem.removeMouseMotionListener(this.foFrameMain);
      }
    }

  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void extractMenuItems(final JComponent toMenuComponent, final Vector<JMenuItem> toMenuItems)
  {
    if (toMenuComponent instanceof JMenuBar)
    {
      final JMenuBar loBar = (JMenuBar) toMenuComponent;
      final int lnCount = loBar.getMenuCount();

      for (int i = 0; i < lnCount; ++i)
      {
        this.extractMenuItems(loBar.getMenu(i), toMenuItems);
      }
    }
    else if (toMenuComponent instanceof JMenu)
    {
      final JMenu loMenu = (JMenu) toMenuComponent;
      final int lnCount = loMenu.getMenuComponentCount();
      for (int i = 0; i < lnCount; ++i)
      {
        final Component loItem = loMenu.getMenuComponent(i);
        if (loItem instanceof JMenu)
        {
          this.extractMenuItems((JMenu) loItem, toMenuItems);
        }
        else if (loItem instanceof JMenuItem)
        {
          toMenuItems.add((JMenuItem) loItem);
        }
      }
    }
  }

// ---------------------------------------------------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
