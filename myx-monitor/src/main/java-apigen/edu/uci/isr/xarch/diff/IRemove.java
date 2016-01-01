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
package edu.uci.isr.xarch.diff;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * Remove <code>xsi:type</code> in the
 * diff namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface IRemove extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"diff", "Remove", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("removeId", "instance", "Identifier", null, null)},
		new XArchActionMetadata[]{});

	/**
	 * Set the removeId attribute on this Remove.
	 * @param removeId removeId
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setRemoveId(String removeId);

	/**
	 * Remove the removeId attribute from this Remove.
	 */
	public void clearRemoveId();

	/**
	 * Get the removeId attribute from this Remove.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return removeId on this Remove
	 */
	public String getRemoveId();

	/**
	 * Determine if the removeId attribute on this Remove
	 * has the given value.
	 * @param removeId Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasRemoveId(String removeId);

	/**
	 * Determine if another Remove is equivalent to this one, ignoring
	 * ID's.
	 * @param RemoveToCheck Remove to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * Remove are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IRemove RemoveToCheck);

}
