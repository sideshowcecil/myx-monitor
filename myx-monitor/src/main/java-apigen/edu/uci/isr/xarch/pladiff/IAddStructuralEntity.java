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
 * AddStructuralEntity <code>xsi:type</code> in the
 * pladiff namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface IAddStructuralEntity extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"pladiff", "AddStructuralEntity", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("component", "types", "Component", 1, 1),
			XArchPropertyMetadata.createElement("connector", "types", "Connector", 1, 1),
			XArchPropertyMetadata.createElement("addLink", "pladiff", "AddLink", 1, 1),
			XArchPropertyMetadata.createElement("addInterface", "pladiff", "AddInterface", 1, 1),
			XArchPropertyMetadata.createElement("addOptional", "pladiff", "AddOptional", 1, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the component for this AddStructuralEntity.
	 * @param value new component
	 */
	public void setComponent(edu.uci.isr.xarch.types.IComponent value);

	/**
	 * Clear the component from this AddStructuralEntity.
	 */
	public void clearComponent();

	/**
	 * Get the component from this AddStructuralEntity.
	 * @return component
	 */
	public edu.uci.isr.xarch.types.IComponent getComponent();

	/**
	 * Determine if this AddStructuralEntity has the given component
	 * @param componentToCheck component to compare
	 * @return <code>true</code> if the components are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasComponent(edu.uci.isr.xarch.types.IComponent componentToCheck);

	/**
	 * Set the connector for this AddStructuralEntity.
	 * @param value new connector
	 */
	public void setConnector(edu.uci.isr.xarch.types.IConnector value);

	/**
	 * Clear the connector from this AddStructuralEntity.
	 */
	public void clearConnector();

	/**
	 * Get the connector from this AddStructuralEntity.
	 * @return connector
	 */
	public edu.uci.isr.xarch.types.IConnector getConnector();

	/**
	 * Determine if this AddStructuralEntity has the given connector
	 * @param connectorToCheck connector to compare
	 * @return <code>true</code> if the connectors are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasConnector(edu.uci.isr.xarch.types.IConnector connectorToCheck);

	/**
	 * Set the addLink for this AddStructuralEntity.
	 * @param value new addLink
	 */
	public void setAddLink(IAddLink value);

	/**
	 * Clear the addLink from this AddStructuralEntity.
	 */
	public void clearAddLink();

	/**
	 * Get the addLink from this AddStructuralEntity.
	 * @return addLink
	 */
	public IAddLink getAddLink();

	/**
	 * Determine if this AddStructuralEntity has the given addLink
	 * @param addLinkToCheck addLink to compare
	 * @return <code>true</code> if the addLinks are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasAddLink(IAddLink addLinkToCheck);

	/**
	 * Set the addInterface for this AddStructuralEntity.
	 * @param value new addInterface
	 */
	public void setAddInterface(IAddInterface value);

	/**
	 * Clear the addInterface from this AddStructuralEntity.
	 */
	public void clearAddInterface();

	/**
	 * Get the addInterface from this AddStructuralEntity.
	 * @return addInterface
	 */
	public IAddInterface getAddInterface();

	/**
	 * Determine if this AddStructuralEntity has the given addInterface
	 * @param addInterfaceToCheck addInterface to compare
	 * @return <code>true</code> if the addInterfaces are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasAddInterface(IAddInterface addInterfaceToCheck);

	/**
	 * Set the addOptional for this AddStructuralEntity.
	 * @param value new addOptional
	 */
	public void setAddOptional(IAddOptional value);

	/**
	 * Clear the addOptional from this AddStructuralEntity.
	 */
	public void clearAddOptional();

	/**
	 * Get the addOptional from this AddStructuralEntity.
	 * @return addOptional
	 */
	public IAddOptional getAddOptional();

	/**
	 * Determine if this AddStructuralEntity has the given addOptional
	 * @param addOptionalToCheck addOptional to compare
	 * @return <code>true</code> if the addOptionals are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasAddOptional(IAddOptional addOptionalToCheck);
	/**
	 * Determine if another AddStructuralEntity is equivalent to this one, ignoring
	 * ID's.
	 * @param AddStructuralEntityToCheck AddStructuralEntity to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * AddStructuralEntity are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IAddStructuralEntity AddStructuralEntityToCheck);

}
