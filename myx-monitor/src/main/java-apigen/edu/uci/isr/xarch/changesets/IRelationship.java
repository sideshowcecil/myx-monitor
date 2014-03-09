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
 * Relationship <code>xsi:type</code> in the
 * changesets namespace.  Extends and
 * inherits the properties of the
 * AbstractRelationship <code>xsi:type</code>.
 * 
 * @author xArch apigen
 */
public interface IRelationship extends IAbstractRelationship, edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"changesets", "Relationship", IAbstractRelationship.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("id", "instance", "Identifier", null, null),
			XArchPropertyMetadata.createAttribute("generated", "changesets", "BooleanValue", null, null),
			XArchPropertyMetadata.createElement("rationale", "changesets", "RelationshipRationale", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Set the id attribute on this Relationship.
	 * @param id id
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setId(String id);

	/**
	 * Remove the id attribute from this Relationship.
	 */
	public void clearId();

	/**
	 * Get the id attribute from this Relationship.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return id on this Relationship
	 */
	public String getId();

	/**
	 * Determine if the id attribute on this Relationship
	 * has the given value.
	 * @param id Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasId(String id);


	/**
	 * Set the generated attribute on this Relationship.
	 * @param generated generated
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setGenerated(String generated);

	/**
	 * Remove the generated attribute from this Relationship.
	 */
	public void clearGenerated();

	/**
	 * Get the generated attribute from this Relationship.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return generated on this Relationship
	 */
	public String getGenerated();

	/**
	 * Determine if the generated attribute on this Relationship
	 * has the given value.
	 * @param generated Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasGenerated(String generated);


	/**
	 * Add a rationale to this Relationship.
	 * @param newRationale rationale to add.
	 */
	public void addRationale(IRelationshipRationale newRationale);

	/**
	 * Add a collection of rationales to this Relationship.
	 * @param rationales rationales to add.
	 */
	public void addRationales(Collection rationales);

	/**
	 * Remove all rationales from this Relationship.
	 */
	public void clearRationales();

	/**
	 * Remove the given rationale from this Relationship.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param rationaleToRemove rationale to remove.
	 */
	public void removeRationale(IRelationshipRationale rationaleToRemove);

	/**
	 * Remove all the given rationales from this Relationship.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param rationales rationale to remove.
	 */
	public void removeRationales(Collection rationales);

	/**
	 * Get all the rationales from this Relationship.
	 * @return all rationales in this Relationship.
	 */
	public Collection getAllRationales();

	/**
	 * Determine if this Relationship contains a given rationale.
	 * @return <code>true</code> if this Relationship contains the given
	 * rationaleToCheck, <code>false</code> otherwise.
	 */
	public boolean hasRationale(IRelationshipRationale rationaleToCheck);

	/**
	 * Determine if this Relationship contains the given set of rationales.
	 * @param rationalesToCheck rationales to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>rationales</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasRationales(Collection rationalesToCheck);

	/**
	 * Determine if this Relationship contains each element in the 
	 * given set of rationales.
	 * @param rationalesToCheck rationales to check for.
	 * @return <code>true</code> if every element in
	 * <code>rationales</code> is found in this Relationship,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllRationales(Collection rationalesToCheck);

	/**
	 * Gets the rationale from this Relationship with the given
	 * id.
	 * @param id ID to look for.
	 * @return rationale with the given ID, or <code>null</code> if not found.
	 */
	public IRelationshipRationale getRationale(String id);

	/**
	 * Gets the rationales from this Relationship with the given
	 * ids.
	 * @param ids ID to look for.
	 * @return rationales with the given IDs.  If an element with a given
	 * ID was not found, that ID is ignored.
	 */
	public Collection getRationales(Collection ids);

	/**
	 * Determine if another Relationship is equivalent to this one, ignoring
	 * ID's.
	 * @param RelationshipToCheck Relationship to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * Relationship are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IRelationship RelationshipToCheck);

}
