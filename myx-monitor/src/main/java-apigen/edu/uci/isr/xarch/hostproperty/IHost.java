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
 * Host <code>xsi:type</code> in the
 * hostproperty namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface IHost extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"hostproperty", "Host", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("id", "instance", "Identifier", null, null),
			XArchPropertyMetadata.createElement("description", "instance", "Description", 1, 1),
			XArchPropertyMetadata.createElement("hostProperty", "hostproperty", "Property", 0, XArchPropertyMetadata.UNBOUNDED),
			XArchPropertyMetadata.createElement("subhost", "hostproperty", "Host", 0, XArchPropertyMetadata.UNBOUNDED),
			XArchPropertyMetadata.createElement("hostsComponent", "hostproperty", "ElementRef", 0, XArchPropertyMetadata.UNBOUNDED),
			XArchPropertyMetadata.createElement("hostsConnector", "hostproperty", "ElementRef", 0, XArchPropertyMetadata.UNBOUNDED),
			XArchPropertyMetadata.createElement("hostsGroup", "hostproperty", "ElementRef", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Set the id attribute on this Host.
	 * @param id id
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setId(String id);

	/**
	 * Remove the id attribute from this Host.
	 */
	public void clearId();

	/**
	 * Get the id attribute from this Host.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return id on this Host
	 */
	public String getId();

	/**
	 * Determine if the id attribute on this Host
	 * has the given value.
	 * @param id Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasId(String id);


	/**
	 * Set the description for this Host.
	 * @param value new description
	 */
	public void setDescription(edu.uci.isr.xarch.instance.IDescription value);

	/**
	 * Clear the description from this Host.
	 */
	public void clearDescription();

	/**
	 * Get the description from this Host.
	 * @return description
	 */
	public edu.uci.isr.xarch.instance.IDescription getDescription();

	/**
	 * Determine if this Host has the given description
	 * @param descriptionToCheck description to compare
	 * @return <code>true</code> if the descriptions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasDescription(edu.uci.isr.xarch.instance.IDescription descriptionToCheck);

	/**
	 * Add a hostProperty to this Host.
	 * @param newHostProperty hostProperty to add.
	 */
	public void addHostProperty(IProperty newHostProperty);

	/**
	 * Add a collection of hostPropertys to this Host.
	 * @param hostPropertys hostPropertys to add.
	 */
	public void addHostPropertys(Collection hostPropertys);

	/**
	 * Remove all hostPropertys from this Host.
	 */
	public void clearHostPropertys();

	/**
	 * Remove the given hostProperty from this Host.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param hostPropertyToRemove hostProperty to remove.
	 */
	public void removeHostProperty(IProperty hostPropertyToRemove);

	/**
	 * Remove all the given hostPropertys from this Host.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param hostPropertys hostProperty to remove.
	 */
	public void removeHostPropertys(Collection hostPropertys);

	/**
	 * Get all the hostPropertys from this Host.
	 * @return all hostPropertys in this Host.
	 */
	public Collection getAllHostPropertys();

	/**
	 * Determine if this Host contains a given hostProperty.
	 * @return <code>true</code> if this Host contains the given
	 * hostPropertyToCheck, <code>false</code> otherwise.
	 */
	public boolean hasHostProperty(IProperty hostPropertyToCheck);

	/**
	 * Determine if this Host contains the given set of hostPropertys.
	 * @param hostPropertysToCheck hostPropertys to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>hostPropertys</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasHostPropertys(Collection hostPropertysToCheck);

	/**
	 * Determine if this Host contains each element in the 
	 * given set of hostPropertys.
	 * @param hostPropertysToCheck hostPropertys to check for.
	 * @return <code>true</code> if every element in
	 * <code>hostPropertys</code> is found in this Host,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllHostPropertys(Collection hostPropertysToCheck);


	/**
	 * Add a subhost to this Host.
	 * @param newSubhost subhost to add.
	 */
	public void addSubhost(IHost newSubhost);

	/**
	 * Add a collection of subhosts to this Host.
	 * @param subhosts subhosts to add.
	 */
	public void addSubhosts(Collection subhosts);

	/**
	 * Remove all subhosts from this Host.
	 */
	public void clearSubhosts();

	/**
	 * Remove the given subhost from this Host.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param subhostToRemove subhost to remove.
	 */
	public void removeSubhost(IHost subhostToRemove);

	/**
	 * Remove all the given subhosts from this Host.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param subhosts subhost to remove.
	 */
	public void removeSubhosts(Collection subhosts);

	/**
	 * Get all the subhosts from this Host.
	 * @return all subhosts in this Host.
	 */
	public Collection getAllSubhosts();

	/**
	 * Determine if this Host contains a given subhost.
	 * @return <code>true</code> if this Host contains the given
	 * subhostToCheck, <code>false</code> otherwise.
	 */
	public boolean hasSubhost(IHost subhostToCheck);

	/**
	 * Determine if this Host contains the given set of subhosts.
	 * @param subhostsToCheck subhosts to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>subhosts</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasSubhosts(Collection subhostsToCheck);

	/**
	 * Determine if this Host contains each element in the 
	 * given set of subhosts.
	 * @param subhostsToCheck subhosts to check for.
	 * @return <code>true</code> if every element in
	 * <code>subhosts</code> is found in this Host,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllSubhosts(Collection subhostsToCheck);

	/**
	 * Gets the subhost from this Host with the given
	 * id.
	 * @param id ID to look for.
	 * @return subhost with the given ID, or <code>null</code> if not found.
	 */
	public IHost getSubhost(String id);

	/**
	 * Gets the subhosts from this Host with the given
	 * ids.
	 * @param ids ID to look for.
	 * @return subhosts with the given IDs.  If an element with a given
	 * ID was not found, that ID is ignored.
	 */
	public Collection getSubhosts(Collection ids);


	/**
	 * Add a hostsComponent to this Host.
	 * @param newHostsComponent hostsComponent to add.
	 */
	public void addHostsComponent(IElementRef newHostsComponent);

	/**
	 * Add a collection of hostsComponents to this Host.
	 * @param hostsComponents hostsComponents to add.
	 */
	public void addHostsComponents(Collection hostsComponents);

	/**
	 * Remove all hostsComponents from this Host.
	 */
	public void clearHostsComponents();

	/**
	 * Remove the given hostsComponent from this Host.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param hostsComponentToRemove hostsComponent to remove.
	 */
	public void removeHostsComponent(IElementRef hostsComponentToRemove);

	/**
	 * Remove all the given hostsComponents from this Host.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param hostsComponents hostsComponent to remove.
	 */
	public void removeHostsComponents(Collection hostsComponents);

	/**
	 * Get all the hostsComponents from this Host.
	 * @return all hostsComponents in this Host.
	 */
	public Collection getAllHostsComponents();

	/**
	 * Determine if this Host contains a given hostsComponent.
	 * @return <code>true</code> if this Host contains the given
	 * hostsComponentToCheck, <code>false</code> otherwise.
	 */
	public boolean hasHostsComponent(IElementRef hostsComponentToCheck);

	/**
	 * Determine if this Host contains the given set of hostsComponents.
	 * @param hostsComponentsToCheck hostsComponents to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>hostsComponents</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasHostsComponents(Collection hostsComponentsToCheck);

	/**
	 * Determine if this Host contains each element in the 
	 * given set of hostsComponents.
	 * @param hostsComponentsToCheck hostsComponents to check for.
	 * @return <code>true</code> if every element in
	 * <code>hostsComponents</code> is found in this Host,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllHostsComponents(Collection hostsComponentsToCheck);


	/**
	 * Add a hostsConnector to this Host.
	 * @param newHostsConnector hostsConnector to add.
	 */
	public void addHostsConnector(IElementRef newHostsConnector);

	/**
	 * Add a collection of hostsConnectors to this Host.
	 * @param hostsConnectors hostsConnectors to add.
	 */
	public void addHostsConnectors(Collection hostsConnectors);

	/**
	 * Remove all hostsConnectors from this Host.
	 */
	public void clearHostsConnectors();

	/**
	 * Remove the given hostsConnector from this Host.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param hostsConnectorToRemove hostsConnector to remove.
	 */
	public void removeHostsConnector(IElementRef hostsConnectorToRemove);

	/**
	 * Remove all the given hostsConnectors from this Host.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param hostsConnectors hostsConnector to remove.
	 */
	public void removeHostsConnectors(Collection hostsConnectors);

	/**
	 * Get all the hostsConnectors from this Host.
	 * @return all hostsConnectors in this Host.
	 */
	public Collection getAllHostsConnectors();

	/**
	 * Determine if this Host contains a given hostsConnector.
	 * @return <code>true</code> if this Host contains the given
	 * hostsConnectorToCheck, <code>false</code> otherwise.
	 */
	public boolean hasHostsConnector(IElementRef hostsConnectorToCheck);

	/**
	 * Determine if this Host contains the given set of hostsConnectors.
	 * @param hostsConnectorsToCheck hostsConnectors to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>hostsConnectors</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasHostsConnectors(Collection hostsConnectorsToCheck);

	/**
	 * Determine if this Host contains each element in the 
	 * given set of hostsConnectors.
	 * @param hostsConnectorsToCheck hostsConnectors to check for.
	 * @return <code>true</code> if every element in
	 * <code>hostsConnectors</code> is found in this Host,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllHostsConnectors(Collection hostsConnectorsToCheck);


	/**
	 * Add a hostsGroup to this Host.
	 * @param newHostsGroup hostsGroup to add.
	 */
	public void addHostsGroup(IElementRef newHostsGroup);

	/**
	 * Add a collection of hostsGroups to this Host.
	 * @param hostsGroups hostsGroups to add.
	 */
	public void addHostsGroups(Collection hostsGroups);

	/**
	 * Remove all hostsGroups from this Host.
	 */
	public void clearHostsGroups();

	/**
	 * Remove the given hostsGroup from this Host.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param hostsGroupToRemove hostsGroup to remove.
	 */
	public void removeHostsGroup(IElementRef hostsGroupToRemove);

	/**
	 * Remove all the given hostsGroups from this Host.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param hostsGroups hostsGroup to remove.
	 */
	public void removeHostsGroups(Collection hostsGroups);

	/**
	 * Get all the hostsGroups from this Host.
	 * @return all hostsGroups in this Host.
	 */
	public Collection getAllHostsGroups();

	/**
	 * Determine if this Host contains a given hostsGroup.
	 * @return <code>true</code> if this Host contains the given
	 * hostsGroupToCheck, <code>false</code> otherwise.
	 */
	public boolean hasHostsGroup(IElementRef hostsGroupToCheck);

	/**
	 * Determine if this Host contains the given set of hostsGroups.
	 * @param hostsGroupsToCheck hostsGroups to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>hostsGroups</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasHostsGroups(Collection hostsGroupsToCheck);

	/**
	 * Determine if this Host contains each element in the 
	 * given set of hostsGroups.
	 * @param hostsGroupsToCheck hostsGroups to check for.
	 * @return <code>true</code> if every element in
	 * <code>hostsGroups</code> is found in this Host,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllHostsGroups(Collection hostsGroupsToCheck);

	/**
	 * Determine if another Host has the same
	 * id as this one.
	 * @param HostToCheck Host to compare with this
	 * one.
	 */
	public boolean isEqual(IHost HostToCheck);
	/**
	 * Determine if another Host is equivalent to this one, ignoring
	 * ID's.
	 * @param HostToCheck Host to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * Host are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IHost HostToCheck);

}
