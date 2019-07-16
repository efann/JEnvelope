/*
 * JEnvelope: A program that prints envelopes from iCloud contacts.
 *
 * Copyright(c) 2012-2019, Beowurks.
 * License: Eclipse Public License - v 2.0 (https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html)
 *
 *
 */

package com.beowurks.JEnvelope;

// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
final public class Address implements Comparable<Object>
{
  public String fcSortName = "";

  public String fcName = "";
  public String fcStreet = "";
  public String fcCity = "";
  public String fcState = "";
  public String fcZipcode = "";
  public String fcCountry = "";

  // ---------------------------------------------------------------------------------------------------------------------
  public Address()
  {
  }

  // ---------------------------------------------------------------------------------------------------------------------
  public Address(final String tcName)
  {
    this.fcSortName = tcName;
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // This way, JComboBox will display the name rather
  // than the memory address from the default toString.
  @Override
  public String toString()
  {
    return (this.fcSortName);
  }

  // ---------------------------------------------------------------------------------------------------------------------
  // According to the document, it is generally necessary to override the
  // hashCode method whenever this method is overridden,
  @Override
  public boolean equals(final Object toObject)
  {
    return ((toObject != null) && (this.fcSortName.compareTo(toObject.toString()) == 0));
  }

  // ---------------------------------------------------------------------------------------------------------------------
  @Override
  public int hashCode()
  {
    return (this.fcSortName.hashCode());
  }

  // ---------------------------------------------------------------------------------------------------------------------
  @Override
  public int compareTo(final Object toObject)
  {
    return (toObject != null ? this.fcSortName.compareTo(toObject.toString()) : -1);
  }
  // ---------------------------------------------------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
