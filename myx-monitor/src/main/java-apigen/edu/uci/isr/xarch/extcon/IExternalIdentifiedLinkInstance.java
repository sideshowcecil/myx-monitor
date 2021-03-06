/*
 * Copyright (c) 2000-2005 Regents of the University of California.
 * All rights reserved.
 *
 * This software was developed at the University of California, Irvine.
 *
 * Redistribution and use in source and binary forms are permitted
 * provided that the above copyright notice and this paragraph are
 * duplicated in all such forms and that any documentation,
 * advertising materials, and other materials related to such
 * distribution and use acknowledge that the software was developed
 * by the University of California, Irvine.  The name of the
 * University may not be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package edu.uci.isr.xarch.extcon;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * ExternalIdentifiedLinkInstance <code>xsi:type</code> in the
 * extcon namespace.  Extends and
 * inherits the properties of the
 * LinkInstance <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IExternalIdentifiedLinkInstance extends edu.uci.isr.xarch.instance.ILinkInstance, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"extcon", "ExternalIdentifiedLinkInstance", edu.uci.isr.xarch.instance.ILinkInstance.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("extId", "instance", "Identifier", null, null)},
		new XArchActionMetadata[]{});

	/**
	 * Set the extId attribute on this ExternalIdentifiedLinkInstance.
	 * @param extId extId
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setExtId(String extId);

	/**
	 * Remove the extId attribute from this ExternalIdentifiedLinkInstance.
	 */
	public void clearExtId();

	/**
	 * Get the extId attribute from this ExternalIdentifiedLinkInstance.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return extId on this ExternalIdentifiedLinkInstance
	 */
	public String getExtId();

	/**
	 * Determine if the extId attribute on this ExternalIdentifiedLinkInstance
	 * has the given value.
	 * @param extId Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasExtId(String extId);

	/**
	 * Determine if another ExternalIdentifiedLinkInstance is equivalent to this one, ignoring
	 * ID's.
	 * @param ExternalIdentifiedLinkInstanceToCheck ExternalIdentifiedLinkInstance to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * ExternalIdentifiedLinkInstance are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IExternalIdentifiedLinkInstance ExternalIdentifiedLinkInstanceToCheck);

}
