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
 * ComponentType <code>xsi:type</code> in the
 * types namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface IComponentType extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"types", "ComponentType", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("id", "instance", "Identifier", null, null),
			XArchPropertyMetadata.createElement("description", "instance", "Description", 1, 1),
			XArchPropertyMetadata.createElement("signature", "types", "Signature", 0, XArchPropertyMetadata.UNBOUNDED),
			XArchPropertyMetadata.createElement("subArchitecture", "types", "SubArchitecture", 0, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the id attribute on this ComponentType.
	 * @param id id
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setId(String id);

	/**
	 * Remove the id attribute from this ComponentType.
	 */
	public void clearId();

	/**
	 * Get the id attribute from this ComponentType.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return id on this ComponentType
	 */
	public String getId();

	/**
	 * Determine if the id attribute on this ComponentType
	 * has the given value.
	 * @param id Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasId(String id);


	/**
	 * Set the description for this ComponentType.
	 * @param value new description
	 */
	public void setDescription(edu.uci.isr.xarch.instance.IDescription value);

	/**
	 * Clear the description from this ComponentType.
	 */
	public void clearDescription();

	/**
	 * Get the description from this ComponentType.
	 * @return description
	 */
	public edu.uci.isr.xarch.instance.IDescription getDescription();

	/**
	 * Determine if this ComponentType has the given description
	 * @param descriptionToCheck description to compare
	 * @return <code>true</code> if the descriptions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasDescription(edu.uci.isr.xarch.instance.IDescription descriptionToCheck);

	/**
	 * Add a signature to this ComponentType.
	 * @param newSignature signature to add.
	 */
	public void addSignature(ISignature newSignature);

	/**
	 * Add a collection of signatures to this ComponentType.
	 * @param signatures signatures to add.
	 */
	public void addSignatures(Collection signatures);

	/**
	 * Remove all signatures from this ComponentType.
	 */
	public void clearSignatures();

	/**
	 * Remove the given signature from this ComponentType.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param signatureToRemove signature to remove.
	 */
	public void removeSignature(ISignature signatureToRemove);

	/**
	 * Remove all the given signatures from this ComponentType.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param signatures signature to remove.
	 */
	public void removeSignatures(Collection signatures);

	/**
	 * Get all the signatures from this ComponentType.
	 * @return all signatures in this ComponentType.
	 */
	public Collection getAllSignatures();

	/**
	 * Determine if this ComponentType contains a given signature.
	 * @return <code>true</code> if this ComponentType contains the given
	 * signatureToCheck, <code>false</code> otherwise.
	 */
	public boolean hasSignature(ISignature signatureToCheck);

	/**
	 * Determine if this ComponentType contains the given set of signatures.
	 * @param signaturesToCheck signatures to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>signatures</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasSignatures(Collection signaturesToCheck);

	/**
	 * Determine if this ComponentType contains each element in the 
	 * given set of signatures.
	 * @param signaturesToCheck signatures to check for.
	 * @return <code>true</code> if every element in
	 * <code>signatures</code> is found in this ComponentType,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllSignatures(Collection signaturesToCheck);

	/**
	 * Gets the signature from this ComponentType with the given
	 * id.
	 * @param id ID to look for.
	 * @return signature with the given ID, or <code>null</code> if not found.
	 */
	public ISignature getSignature(String id);

	/**
	 * Gets the signatures from this ComponentType with the given
	 * ids.
	 * @param ids ID to look for.
	 * @return signatures with the given IDs.  If an element with a given
	 * ID was not found, that ID is ignored.
	 */
	public Collection getSignatures(Collection ids);


	/**
	 * Set the subArchitecture for this ComponentType.
	 * @param value new subArchitecture
	 */
	public void setSubArchitecture(ISubArchitecture value);

	/**
	 * Clear the subArchitecture from this ComponentType.
	 */
	public void clearSubArchitecture();

	/**
	 * Get the subArchitecture from this ComponentType.
	 * @return subArchitecture
	 */
	public ISubArchitecture getSubArchitecture();

	/**
	 * Determine if this ComponentType has the given subArchitecture
	 * @param subArchitectureToCheck subArchitecture to compare
	 * @return <code>true</code> if the subArchitectures are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasSubArchitecture(ISubArchitecture subArchitectureToCheck);
	/**
	 * Determine if another ComponentType has the same
	 * id as this one.
	 * @param ComponentTypeToCheck ComponentType to compare with this
	 * one.
	 */
	public boolean isEqual(IComponentType ComponentTypeToCheck);
	/**
	 * Determine if another ComponentType is equivalent to this one, ignoring
	 * ID's.
	 * @param ComponentTypeToCheck ComponentType to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * ComponentType are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IComponentType ComponentTypeToCheck);

}
