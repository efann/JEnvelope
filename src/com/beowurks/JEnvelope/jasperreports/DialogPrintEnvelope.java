/*
 * JEnvelope: A program that prints envelopes from iCloud contacts.
 *
 * Copyright(c) 2012-2018, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html)
 *
 *
 */

package com.beowurks.JEnvelope.jasperreports;

import com.beowurks.BeoCommon.GridBagLayoutHelper;
import com.beowurks.BeoCommon.Util;
import com.beowurks.JEnvelope.Address;
import com.beowurks.JEnvelope.AppProperties;
import com.beowurks.JEnvelope.DialogJEnvelopeBase;
import com.beowurks.JEnvelope.FrameMain;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.sql.Connection;
import java.util.HashMap;

// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// If you want to use BaseInternalFrame, then you have to define
// a JDesktopPane which is not needed for this application.
public class DialogPrintEnvelope extends DialogJEnvelopeBase
{
  private final AppProperties foAppProperties;

  private JRViewerBase foJRViewerEnvelope1;
  private final JPanel pnlViewer1 = new JPanel();

  private final Address foReturn;
  private final Address foAddressee;

  // ---------------------------------------------------------------------------------------------------------------------
  public DialogPrintEnvelope(final FrameMain toMainFrame, final Address toReturn, final Address toAddressee)
  {
    super(toMainFrame, "Envelope");

    this.foAppProperties = toMainFrame.getAppProperties();
    this.foReturn = toReturn;
    this.foAddressee = toAddressee;

    try
    {
      this.jbInit();
      this.setModal(false);
    }
    catch (final Exception loErr)
    {
      Util.showStackTraceInMessage(this, loErr, "Unfortunate Exception");
    }

  }

  // ---------------------------------------------------------------------------------------------------------------------
  // Component initialization
  @Override
  protected void jbInit() throws Exception
  {
    super.jbInit();

    this.generateEnvelope();
  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void generateEnvelope()
  {
    try
    {
      final JasperReport loJasperReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/com/beowurks/JEnvelope/jasperreports/Envelope.jasper"));


      final HashMap<String, Object> loHashMap = new HashMap<String, Object>();

      loHashMap.put("parName", this.foReturn.fcName);
      // Sometimes the street address can have 2 lines which iContacts separates with a \n.
      loHashMap.put("parStreet", this.foReturn.fcStreet.replaceAll("\\\\n", "<br />"));
      loHashMap.put("parCityStateZip", this.foReturn.fcCity + ", " + this.foReturn.fcState + " " + this.foReturn.fcZipcode);
      loHashMap.put("parAddrName", this.foAddressee.fcName);
      // Sometimes the street address can have 2 lines which iContacts separates with a \n.
      loHashMap.put("parAddrAddress", this.foAddressee.fcStreet.replaceAll("\\\\n", "<br />"));
      loHashMap.put("parAddrCity", this.foAddressee.fcCity);
      loHashMap.put("parAddrState", this.foAddressee.fcState);
      loHashMap.put("parAddrZipcode", this.foAddressee.fcZipcode);

      final JasperPrint loJPEnvelope = JasperFillManager.fillReport(loJasperReport, loHashMap, (Connection) null);

      if (this.foJRViewerEnvelope1 == null)
      {
        this.foJRViewerEnvelope1 = new JRViewerBase(loJPEnvelope);
        this.initLayout();
      }
      else
      {
        this.foJRViewerEnvelope1.loadReport(loJPEnvelope);
      }

      SwingUtilities.invokeLater(new Runnable()
      {
        @Override
        public void run()
        {
          try
          {
            JasperPrintManager.printReport(loJPEnvelope, true);
            DialogPrintEnvelope.this.closeWindow();
          }
          catch (final JRException loErr)
          {
            Util.showStackTraceInMessage(DialogPrintEnvelope.this.getMainFrame(), loErr, "Error in printing. . . .");
          }
        }
      });

    }
    catch (final JRException loErr)
    {
      loErr.printStackTrace();
    }

  }

  // ---------------------------------------------------------------------------------------------------------------------
  private void initLayout()
  {
    GridBagLayoutHelper loGrid;
    final Dimension ldMainFormSize = this.getMainFrame().getSize();

    // --------------------

    loGrid = new GridBagLayoutHelper();

    this.pnlViewer1.setLayout(loGrid);
    this.pnlViewer1.add(this.foJRViewerEnvelope1, loGrid.getConstraint(0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH));
    this.pnlViewer1.setPreferredSize(new Dimension((int) (ldMainFormSize.width * 0.9), (int) (ldMainFormSize.height * 0.9)));

    // --------------------

    loGrid = new GridBagLayoutHelper();
    this.getContentPane().setLayout(loGrid);

    this.getContentPane().add(this.pnlViewer1, loGrid.getConstraint(0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH));

  }
// ---------------------------------------------------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
