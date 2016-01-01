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
package edu.uci.isr.xarch.pladiff;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * RemoveInterface <code>xsi:type</code> in the
 * pladiff namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface IRemoveInterface extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"pladiff", "RemoveInterface", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("elementDescription", "instance", "Description", 1, 1),
			XArchPropertyMetadata.createElement("interfaceDescription", "instance", "Description", 1, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the elementDescription for this RemoveInterface.
	 * @param value new elementDescription
	 */
	public void setElementDescription(edu.uci.isr.xarch.instance.IDescription value);

	/**
	 * Clear the elementDescription from this RemoveInterface.
	 */
	public void clearElementDescription();

	/**
	 * Get the elementDescription from this RemoveInterface.
	 * @return elementDescription
	 */
	public edu.uci.isr.xarch.instance.IDescription getElementDescription();

	/**
	 * Determine if this RemoveInterface has the given elementDescription
	 * @param elementDescriptionToCheck elementDescription to compare
	 * @return <code>true</code> if the elementDescriptions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasElementDescription(edu.uci.isr.xarch.instance.IDescription elementDescriptionToCheck);

	/**
	 * Set the interfaceDescription for this RemoveInterface.
	 * @param value new interfaceDescription
	 */
	public void setInterfaceDescription(edu.uci.isr.xarch.instance.IDescription value);

	/**
	 * Clear the interfaceDescription from this RemoveInterface.
	 */
	public void clearInterfaceDescription();

	/**
	 * Get the interfaceDescription from this RemoveInterface.
	 * @return interfaceDescription
	 */
	public edu.uci.isr.xarch.instance.IDescription getInterfaceDescription();

	/**
	 * Determine if this RemoveInterface has the given interfaceDescription
	 * @param interfaceDescriptionToCheck interfaceDescription to compare
	 * @return <code>true</code> if the interfaceDescriptions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasInterfaceDescription(edu.uci.isr.xarch.instance.IDescription interfaceDescriptionToCheck);
	/**
	 * Determine if another RemoveInterface is equivalent to this one, ignoring
	 * ID's.
	 * @param RemoveInterfaceToCheck RemoveInterface to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * RemoveInterface are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IRemoveInterface RemoveInterfaceToCheck);

}
