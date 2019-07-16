/*
 * JEnvelope: A program that prints envelopes from iCloud contacts.
 *
 * Copyright(c) 2012-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html)
 *
 *
 */

package com.beowurks.JEnvelope;

import com.beowurks.BeoCommon.BaseDialog;

// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
public class DialogJEnvelopeBase extends BaseDialog
{
  private final FrameMain foMainFrame;

  // Gets rid of the following error:
  // serializable class has no definition of serialVersionUID
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------------------------------------------------------------------------------
  public DialogJEnvelopeBase(final FrameMain toMainFrame, final String tcTitle)
  {
    super(toMainFrame, tcTitle);

    this.foMainFrame = toMainFrame;
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public FrameMain getMainFrame()
  {
    return (this.foMainFrame);
  }
  // ---------------------------------------------------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
