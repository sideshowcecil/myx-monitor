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
package edu.uci.isr.xarch.instancemapping;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * MappedInterfaceInstance <code>xsi:type</code> in the
 * instancemapping namespace.  Extends and
 * inherits the properties of the
 * InterfaceInstance <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IMappedInterfaceInstance extends edu.uci.isr.xarch.instance.IInterfaceInstance, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"instancemapping", "MappedInterfaceInstance", edu.uci.isr.xarch.instance.IInterfaceInstance.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("type", "instance", "XMLLink", 0, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the type for this MappedInterfaceInstance.
	 * @param value new type
	 */
	public void setType(edu.uci.isr.xarch.instance.IXMLLink value);

	/**
	 * Clear the type from this MappedInterfaceInstance.
	 */
	public void clearType();

	/**
	 * Get the type from this MappedInterfaceInstance.
	 * @return type
	 */
	public edu.uci.isr.xarch.instance.IXMLLink getType();

	/**
	 * Determine if this MappedInterfaceInstance has the given type
	 * @param typeToCheck type to compare
	 * @return <code>true</code> if the types are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasType(edu.uci.isr.xarch.instance.IXMLLink typeToCheck);
	/**
	 * Determine if another MappedInterfaceInstance is equivalent to this one, ignoring
	 * ID's.
	 * @param MappedInterfaceInstanceToCheck MappedInterfaceInstance to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * MappedInterfaceInstance are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IMappedInterfaceInstance MappedInterfaceInstanceToCheck);

}
