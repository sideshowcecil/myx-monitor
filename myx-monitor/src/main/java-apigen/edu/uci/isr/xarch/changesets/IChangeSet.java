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
package edu.uci.isr.xarch.changesets;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * ChangeSet <code>xsi:type</code> in the
 * changesets namespace.  Extends and
 * inherits the properties of the
 * AbstractChangeSet <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IChangeSet extends IAbstractChangeSet, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"changesets", "ChangeSet", IAbstractChangeSet.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("id", "instance", "Identifier", null, null),
			XArchPropertyMetadata.createAttribute("childrenChangeSetOrder", "http://www.w3.org/2001/XMLSchema", "string", null, null),
			XArchPropertyMetadata.createAttribute("lockStatus", "http://www.w3.org/2001/XMLSchema", "string", null, null),
			XArchPropertyMetadata.createElement("description", "instance", "Description", 1, 1),
			XArchPropertyMetadata.createElement("xArchElement", "changesets", "ElementSegment", 1, 1),
			XArchPropertyMetadata.createElement("childChangeSet", "instance", "XMLLink", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Set the id attribute on this ChangeSet.
	 * @param id id
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setId(String id);

	/**
	 * Remove the id attribute from this ChangeSet.
	 */
	public void clearId();

	/**
	 * Get the id attribute from this ChangeSet.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return id on this ChangeSet
	 */
	public String getId();

	/**
	 * Determine if the id attribute on this ChangeSet
	 * has the given value.
	 * @param id Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasId(String id);


	/**
	 * Set the childrenChangeSetOrder attribute on this ChangeSet.
	 * @param childrenChangeSetOrder childrenChangeSetOrder
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setChildrenChangeSetOrder(String childrenChangeSetOrder);

	/**
	 * Remove the childrenChangeSetOrder attribute from this ChangeSet.
	 */
	public void clearChildrenChangeSetOrder();

	/**
	 * Get the childrenChangeSetOrder attribute from this ChangeSet.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return childrenChangeSetOrder on this ChangeSet
	 */
	public String getChildrenChangeSetOrder();

	/**
	 * Determine if the childrenChangeSetOrder attribute on this ChangeSet
	 * has the given value.
	 * @param childrenChangeSetOrder Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasChildrenChangeSetOrder(String childrenChangeSetOrder);


	/**
	 * Set the lockStatus attribute on this ChangeSet.
	 * @param lockStatus lockStatus
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setLockStatus(String lockStatus);

	/**
	 * Remove the lockStatus attribute from this ChangeSet.
	 */
	public void clearLockStatus();

	/**
	 * Get the lockStatus attribute from this ChangeSet.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return lockStatus on this ChangeSet
	 */
	public String getLockStatus();

	/**
	 * Determine if the lockStatus attribute on this ChangeSet
	 * has the given value.
	 * @param lockStatus Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasLockStatus(String lockStatus);


	/**
	 * Set the description for this ChangeSet.
	 * @param value new description
	 */
	public void setDescription(edu.uci.isr.xarch.instance.IDescription value);

	/**
	 * Clear the description from this ChangeSet.
	 */
	public void clearDescription();

	/**
	 * Get the description from this ChangeSet.
	 * @return description
	 */
	public edu.uci.isr.xarch.instance.IDescription getDescription();

	/**
	 * Determine if this ChangeSet has the given description
	 * @param descriptionToCheck description to compare
	 * @return <code>true</code> if the descriptions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasDescription(edu.uci.isr.xarch.instance.IDescription descriptionToCheck);

	/**
	 * Set the xArchElement for this ChangeSet.
	 * @param value new xArchElement
	 */
	public void setXArchElement(IElementSegment value);

	/**
	 * Clear the xArchElement from this ChangeSet.
	 */
	public void clearXArchElement();

	/**
	 * Get the xArchElement from this ChangeSet.
	 * @return xArchElement
	 */
	public IElementSegment getXArchElement();

	/**
	 * Determine if this ChangeSet has the given xArchElement
	 * @param xArchElementToCheck xArchElement to compare
	 * @return <code>true</code> if the xArchElements are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasXArchElement(IElementSegment xArchElementToCheck);

	/**
	 * Add a childChangeSet to this ChangeSet.
	 * @param newChildChangeSet childChangeSet to add.
	 */
	public void addChildChangeSet(edu.uci.isr.xarch.instance.IXMLLink newChildChangeSet);

	/**
	 * Add a collection of childChangeSets to this ChangeSet.
	 * @param childChangeSets childChangeSets to add.
	 */
	public void addChildChangeSets(Collection childChangeSets);

	/**
	 * Remove all childChangeSets from this ChangeSet.
	 */
	public void clearChildChangeSets();

	/**
	 * Remove the given childChangeSet from this ChangeSet.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param childChangeSetToRemove childChangeSet to remove.
	 */
	public void removeChildChangeSet(edu.uci.isr.xarch.instance.IXMLLink childChangeSetToRemove);

	/**
	 * Remove all the given childChangeSets from this ChangeSet.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param childChangeSets childChangeSet to remove.
	 */
	public void removeChildChangeSets(Collection childChangeSets);

	/**
	 * Get all the childChangeSets from this ChangeSet.
	 * @return all childChangeSets in this ChangeSet.
	 */
	public Collection getAllChildChangeSets();

	/**
	 * Determine if this ChangeSet contains a given childChangeSet.
	 * @return <code>true</code> if this ChangeSet contains the given
	 * childChangeSetToCheck, <code>false</code> otherwise.
	 */
	public boolean hasChildChangeSet(edu.uci.isr.xarch.instance.IXMLLink childChangeSetToCheck);

	/**
	 * Determine if this ChangeSet contains the given set of childChangeSets.
	 * @param childChangeSetsToCheck childChangeSets to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>childChangeSets</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasChildChangeSets(Collection childChangeSetsToCheck);

	/**
	 * Determine if this ChangeSet contains each element in the 
	 * given set of childChangeSets.
	 * @param childChangeSetsToCheck childChangeSets to check for.
	 * @return <code>true</code> if every element in
	 * <code>childChangeSets</code> is found in this ChangeSet,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllChildChangeSets(Collection childChangeSetsToCheck);

	/**
	 * Determine if another ChangeSet is equivalent to this one, ignoring
	 * ID's.
	 * @param ChangeSetToCheck ChangeSet to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * ChangeSet are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IChangeSet ChangeSetToCheck);

}
