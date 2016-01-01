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
package edu.uci.isr.xarch.javainitparams;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * InitializationParameter <code>xsi:type</code> in the
 * javainitparams namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface IInitializationParameter extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"javainitparams", "InitializationParameter", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("name", "http://www.w3.org/2001/XMLSchema", "string", null, null),
			XArchPropertyMetadata.createAttribute("value", "http://www.w3.org/2001/XMLSchema", "string", null, null)},
		new XArchActionMetadata[]{});

	/**
	 * Set the name attribute on this InitializationParameter.
	 * @param name name
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setName(String name);

	/**
	 * Remove the name attribute from this InitializationParameter.
	 */
	public void clearName();

	/**
	 * Get the name attribute from this InitializationParameter.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return name on this InitializationParameter
	 */
	public String getName();

	/**
	 * Determine if the name attribute on this InitializationParameter
	 * has the given value.
	 * @param name Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasName(String name);


	/**
	 * Set the value attribute on this InitializationParameter.
	 * @param value value
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setValue(String value);

	/**
	 * Remove the value attribute from this InitializationParameter.
	 */
	public void clearValue();

	/**
	 * Get the value attribute from this InitializationParameter.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return value on this InitializationParameter
	 */
	public String getValue();

	/**
	 * Determine if the value attribute on this InitializationParameter
	 * has the given value.
	 * @param value Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasValue(String value);

	/**
	 * Determine if another InitializationParameter is equivalent to this one, ignoring
	 * ID's.
	 * @param InitializationParameterToCheck InitializationParameter to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * InitializationParameter are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IInitializationParameter InitializationParameterToCheck);

}
