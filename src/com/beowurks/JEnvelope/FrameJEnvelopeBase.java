/*
 * JEnvelope: A program that prints envelopes from iCloud contacts.
 *
 * Copyright(c) 2012-2018, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html)
 *
 *
 */

package com.beowurks.JEnvelope;

import com.beowurks.BeoCommon.BaseFrame;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
public class FrameJEnvelopeBase extends BaseFrame
{
  private FrameMain foMainFrame;

  private boolean flHaveMenus = true;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------------------------------------------------
  public FrameJEnvelopeBase()
  {
    this.foMainFrame = (FrameMain) this;
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public FrameJEnvelopeBase(final FrameMain toMainFrame, final String tcTitle, final boolean tlPackFrame,
                            final boolean tlHaveMenus)
  {
    super(tcTitle, tlPackFrame);

    this.foMainFrame = toMainFrame;
    this.flHaveMenus = tlHaveMenus;
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public FrameMain getMainFrame()
  {
    return (this.foMainFrame);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // Component initialization
  @Override
  protected void jbInit() throws Exception
  {
    super.jbInit();

    if (this.flHaveMenus)
    {
      if (this.foMainFrame != this)
      {
        this.createDuplicateTopMenu(this.foMainFrame.getJMenuBar());
        this.setJMenuBar(this.foDuplicateMenuBar);
      }
    }

    final URL loURLIcon = FrameJEnvelopeBase.class.getResource("/com/beowurks/JEnvelope/images/JEnvelope.png");

    if (loURLIcon != null)
    {
      final Image loImage = Toolkit.getDefaultToolkit().createImage(loURLIcon);

      // If you don't getScaledInstance, then the Window seems to resize
      // according to Image.SCALE_DEFAULT and doesn't look quite right.
      this.setIconImage(loImage.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    }
  }

  // ---------------------------------------------------------------------------------------------------------------------
  @Override
  protected void releasePointers()
  {
    this.foMainFrame = null;

    super.releasePointers();
  }
  // ---------------------------------------------------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
