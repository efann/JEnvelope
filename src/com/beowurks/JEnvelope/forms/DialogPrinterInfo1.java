/*
 * JEnvelope: A program that prints envelopes from iCloud contacts.
 *
 * Copyright(c) 2012-2018, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html)
 *
 *
 */

package com.beowurks.JEnvelope.forms;

import com.beowurks.BeoCommon.BaseButton;
import com.beowurks.BeoCommon.GridBagLayoutHelper;
import com.beowurks.BeoCommon.Util;
import com.beowurks.JEnvelope.DialogJEnvelopeBase;
import com.beowurks.JEnvelope.FrameMain;

import javax.print.PrintService;
import javax.print.attribute.standard.Media;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterJob;

// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
public class DialogPrinterInfo1 extends DialogJEnvelopeBase implements ActionListener, HyperlinkListener
{
  private final BaseButton btnClose1 = new BaseButton(76, 30);

  private final Box boxButtons1 = Box.createHorizontalBox();

  private final JScrollPane scrCredits1 = new JScrollPane();
  private final JEditorPane txtCredits1 = new JEditorPane();

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------------------------------------------------
  public DialogPrinterInfo1(final FrameMain toMainFrame)
  {
    super(toMainFrame, "Printer Info");

    try
    {
      this.jbInit();
      this.makeVisible(true);
    }
    catch (final Exception e)
    {
      e.printStackTrace();
    }
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // Component initialization
  // By the way, if you call this.setResizable(false), the application
  // icon will not appear in the upper left corner.
  @Override
  protected void jbInit() throws Exception
  {
    super.jbInit();

    this.setupButtons();
    this.setupTextBoxes();
    this.setupListeners();
    this.setupLayouts();

    this.setupPrinterInfo();

    Util.addEscapeListener(this);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void setupPrinterInfo()
  {
    final StringBuilder loHtml = new StringBuilder();

    loHtml.append("<html><body style='background-color: #C0D9D9; border: none; padding: 10px;'>"
        + "<p style='text-align: center; font-weight: bold;'>Information about this computer printers:</p>");

    loHtml.append("<ul>");

    final PrintService[] laServices = PrinterJob.lookupPrintServices();

    for (final PrintService loService : laServices)
    {
      final String lcPrinter = loService.getName();
      loHtml.append("<li>").append(lcPrinter).append("</li>");

      loHtml.append("<ul style='list-style-type: circle;'>");
      final Media[] laMedias = (Media[]) loService.getSupportedAttributeValues(Media.class, null, null);

      for (final Media loMedia : laMedias)
      {
        loHtml.append("<li>").append(loMedia.toString()).append("</li>");
      }
      loHtml.append("</ul>");
    }

    loHtml.append("</ul>");

    loHtml.append("</body></html>");

    this.txtCredits1.setText(loHtml.toString());
    this.txtCredits1.setBorder(null);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void setupButtons()
  {
    this.btnClose1.setText("Close");
    this.btnClose1.setMnemonic('C');
    this.btnClose1.setToolTipText("Close this dialog");

    this.btnClose1.setIcon(new ImageIcon(this.getClass().getResource("/com/beowurks/JEnvelope/images/exit22.png")));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void setupLayouts()
  {
    this.boxButtons1.add(this.btnClose1, null);

    // --------------------
    final GridBagLayoutHelper loGrid = new GridBagLayoutHelper();

    this.getContentPane().setLayout(loGrid);

    loGrid.setInsets(10, 4, 10, 4);

    this.getContentPane().add(this.scrCredits1,
        loGrid.getConstraint(0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL));
    this.getContentPane().add(this.boxButtons1,
        loGrid.getConstraint(0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void setupTextBoxes()
  {
    this.scrCredits1.setViewportView(this.txtCredits1);

    // setEnabled makes the text barely readable.
    this.txtCredits1.setEditable(false);

    this.txtCredits1.setContentType("text/html");

    final Dimension ldScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

    final int lnWidth = (int) (ldScreenSize.width * 0.4);
    final int lnHeight = (int) (ldScreenSize.height * 0.4);

    this.scrCredits1.setPreferredSize(new Dimension(lnWidth, lnHeight));
    this.scrCredits1.setMinimumSize(new Dimension(lnWidth, lnHeight));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void setupListeners()
  {
    this.btnClose1.addActionListener(this);
    this.txtCredits1.addHyperlinkListener(this);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  @Override
  public void removeListeners()
  {
    super.removeListeners();

    this.btnClose1.removeActionListener(this);
    this.txtCredits1.removeHyperlinkListener(this);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // ---------------------------------------------------------------------------------------------------------------------
  // Interface HyperlinkListener
  @Override
  public void hyperlinkUpdate(final HyperlinkEvent toHyperlinkEvent)
  {
    if (toHyperlinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
    {
      Util.launchBrowser(toHyperlinkEvent.getURL().toString());
    }
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // ---------------------------------------------------------------------------------------------------------------------
  // Interface ActionListener
  @Override
  public void actionPerformed(final ActionEvent toActionEvent)
  {
    final Object loSource = toActionEvent.getSource();

    if (loSource instanceof JButton)
    {
      if (loSource == this.btnClose1)
      {
        this.closeWindow();
      }
    }
  }
  // ---------------------------------------------------------------------------------------------------------------------
  // ---------------------------------------------------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
