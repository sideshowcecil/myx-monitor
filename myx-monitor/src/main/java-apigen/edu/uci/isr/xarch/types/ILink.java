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
package edu.uci.isr.xarch.types;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * Link <code>xsi:type</code> in the
 * types namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface ILink extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"types", "Link", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("id", "instance", "Identifier", null, null),
			XArchPropertyMetadata.createElement("description", "instance", "Description", 1, 1),
			XArchPropertyMetadata.createElement("point", "instance", "Point", 2, 2)},
		new XArchActionMetadata[]{});

	/**
	 * Set the id attribute on this Link.
	 * @param id id
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setId(String id);

	/**
	 * Remove the id attribute from this Link.
	 */
	public void clearId();

	/**
	 * Get the id attribute from this Link.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return id on this Link
	 */
	public String getId();

	/**
	 * Determine if the id attribute on this Link
	 * has the given value.
	 * @param id Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasId(String id);


	/**
	 * Set the description for this Link.
	 * @param value new description
	 */
	public void setDescription(edu.uci.isr.xarch.instance.IDescription value);

	/**
	 * Clear the description from this Link.
	 */
	public void clearDescription();

	/**
	 * Get the description from this Link.
	 * @return description
	 */
	public edu.uci.isr.xarch.instance.IDescription getDescription();

	/**
	 * Determine if this Link has the given description
	 * @param descriptionToCheck description to compare
	 * @return <code>true</code> if the descriptions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasDescription(edu.uci.isr.xarch.instance.IDescription descriptionToCheck);

	/**
	 * Add a point to this Link.
	 * @param newPoint point to add.
	 */
	public void addPoint(edu.uci.isr.xarch.instance.IPoint newPoint);

	/**
	 * Add a collection of points to this Link.
	 * @param points points to add.
	 */
	public void addPoints(Collection points);

	/**
	 * Remove all points from this Link.
	 */
	public void clearPoints();

	/**
	 * Remove the given point from this Link.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param pointToRemove point to remove.
	 */
	public void removePoint(edu.uci.isr.xarch.instance.IPoint pointToRemove);

	/**
	 * Remove all the given points from this Link.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param points point to remove.
	 */
	public void removePoints(Collection points);

	/**
	 * Get all the points from this Link.
	 * @return all points in this Link.
	 */
	public Collection getAllPoints();

	/**
	 * Determine if this Link contains a given point.
	 * @return <code>true</code> if this Link contains the given
	 * pointToCheck, <code>false</code> otherwise.
	 */
	public boolean hasPoint(edu.uci.isr.xarch.instance.IPoint pointToCheck);

	/**
	 * Determine if this Link contains the given set of points.
	 * @param pointsToCheck points to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>points</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasPoints(Collection pointsToCheck);

	/**
	 * Determine if this Link contains each element in the 
	 * given set of points.
	 * @param pointsToCheck points to check for.
	 * @return <code>true</code> if every element in
	 * <code>points</code> is found in this Link,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllPoints(Collection pointsToCheck);

	/**
	 * Determine if another Link has the same
	 * id as this one.
	 * @param LinkToCheck Link to compare with this
	 * one.
	 */
	public boolean isEqual(ILink LinkToCheck);
	/**
	 * Determine if another Link is equivalent to this one, ignoring
	 * ID's.
	 * @param LinkToCheck Link to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * Link are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(ILink LinkToCheck);

}
