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
package edu.uci.isr.xarch.hostproperty;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * HostedArchStructure <code>xsi:type</code> in the
 * hostproperty namespace.  Extends and
 * inherits the properties of the
 * ArchStructure <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IHostedArchStructure extends edu.uci.isr.xarch.types.IArchStructure, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"hostproperty", "HostedArchStructure", edu.uci.isr.xarch.types.IArchStructure.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createElement("host", "hostproperty", "Host", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Add a host to this HostedArchStructure.
	 * @param newHost host to add.
	 */
	public void addHost(IHost newHost);

	/**
	 * Add a collection of hosts to this HostedArchStructure.
	 * @param hosts hosts to add.
	 */
	public void addHosts(Collection hosts);

	/**
	 * Remove all hosts from this HostedArchStructure.
	 */
	public void clearHosts();

	/**
	 * Remove the given host from this HostedArchStructure.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param hostToRemove host to remove.
	 */
	public void removeHost(IHost hostToRemove);

	/**
	 * Remove all the given hosts from this HostedArchStructure.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param hosts host to remove.
	 */
	public void removeHosts(Collection hosts);

	/**
	 * Get all the hosts from this HostedArchStructure.
	 * @return all hosts in this HostedArchStructure.
	 */
	public Collection getAllHosts();

	/**
	 * Determine if this HostedArchStructure contains a given host.
	 * @return <code>true</code> if this HostedArchStructure contains the given
	 * hostToCheck, <code>false</code> otherwise.
	 */
	public boolean hasHost(IHost hostToCheck);

	/**
	 * Determine if this HostedArchStructure contains the given set of hosts.
	 * @param hostsToCheck hosts to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>hosts</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasHosts(Collection hostsToCheck);

	/**
	 * Determine if this HostedArchStructure contains each element in the 
	 * given set of hosts.
	 * @param hostsToCheck hosts to check for.
	 * @return <code>true</code> if every element in
	 * <code>hosts</code> is found in this HostedArchStructure,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllHosts(Collection hostsToCheck);

	/**
	 * Gets the host from this HostedArchStructure with the given
	 * id.
	 * @param id ID to look for.
	 * @return host with the given ID, or <code>null</code> if not found.
	 */
	public IHost getHost(String id);

	/**
	 * Gets the hosts from this HostedArchStructure with the given
	 * ids.
	 * @param ids ID to look for.
	 * @return hosts with the given IDs.  If an element with a given
	 * ID was not found, that ID is ignored.
	 */
	public Collection getHosts(Collection ids);

	/**
	 * Determine if another HostedArchStructure is equivalent to this one, ignoring
	 * ID's.
	 * @param HostedArchStructureToCheck HostedArchStructure to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * HostedArchStructure are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IHostedArchStructure HostedArchStructureToCheck);

}
