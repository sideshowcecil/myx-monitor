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
package edu.uci.isr.xarch.typesmapping;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * MappedConnector <code>xsi:type</code> in the
 * typesmapping namespace.  Extends and
 * inherits the properties of the
 * Connector <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IMappedConnector extends edu.uci.isr.xarch.types.IConnector, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"typesmapping", "MappedConnector", edu.uci.isr.xarch.types.IConnector.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("blueprint", "instance", "XMLLink", 1, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the blueprint for this MappedConnector.
	 * @param value new blueprint
	 */
	public void setBlueprint(edu.uci.isr.xarch.instance.IXMLLink value);

	/**
	 * Clear the blueprint from this MappedConnector.
	 */
	public void clearBlueprint();

	/**
	 * Get the blueprint from this MappedConnector.
	 * @return blueprint
	 */
	public edu.uci.isr.xarch.instance.IXMLLink getBlueprint();

	/**
	 * Determine if this MappedConnector has the given blueprint
	 * @param blueprintToCheck blueprint to compare
	 * @return <code>true</code> if the blueprints are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasBlueprint(edu.uci.isr.xarch.instance.IXMLLink blueprintToCheck);
	/**
	 * Determine if another MappedConnector is equivalent to this one, ignoring
	 * ID's.
	 * @param MappedConnectorToCheck MappedConnector to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * MappedConnector are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IMappedConnector MappedConnectorToCheck);

}
