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
package edu.uci.isr.xarch.changes;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * LinkChange <code>xsi:type</code> in the
 * changes namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface ILinkChange extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"changes", "LinkChange", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("id", "instance", "Identifier", null, null),
			XArchPropertyMetadata.createAttribute("type", "changes", "ChangeType", null, null),
			XArchPropertyMetadata.createElement("description", "instance", "Description", 1, 1),
			XArchPropertyMetadata.createElement("link", "instance", "XMLLink", 1, 1),
			XArchPropertyMetadata.createElement("copyOfRemovedLink", "types", "Link", 0, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the id attribute on this LinkChange.
	 * @param id id
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setId(String id);

	/**
	 * Remove the id attribute from this LinkChange.
	 */
	public void clearId();

	/**
	 * Get the id attribute from this LinkChange.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return id on this LinkChange
	 */
	public String getId();

	/**
	 * Determine if the id attribute on this LinkChange
	 * has the given value.
	 * @param id Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasId(String id);


	/**
	 * Set the type attribute on this LinkChange.
	 * @param type type
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setType(String type);

	/**
	 * Remove the type attribute from this LinkChange.
	 */
	public void clearType();

	/**
	 * Get the type attribute from this LinkChange.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return type on this LinkChange
	 */
	public String getType();

	/**
	 * Determine if the type attribute on this LinkChange
	 * has the given value.
	 * @param type Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasType(String type);


	/**
	 * Set the description for this LinkChange.
	 * @param value new description
	 */
	public void setDescription(edu.uci.isr.xarch.instance.IDescription value);

	/**
	 * Clear the description from this LinkChange.
	 */
	public void clearDescription();

	/**
	 * Get the description from this LinkChange.
	 * @return description
	 */
	public edu.uci.isr.xarch.instance.IDescription getDescription();

	/**
	 * Determine if this LinkChange has the given description
	 * @param descriptionToCheck description to compare
	 * @return <code>true</code> if the descriptions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasDescription(edu.uci.isr.xarch.instance.IDescription descriptionToCheck);

	/**
	 * Set the link for this LinkChange.
	 * @param value new link
	 */
	public void setLink(edu.uci.isr.xarch.instance.IXMLLink value);

	/**
	 * Clear the link from this LinkChange.
	 */
	public void clearLink();

	/**
	 * Get the link from this LinkChange.
	 * @return link
	 */
	public edu.uci.isr.xarch.instance.IXMLLink getLink();

	/**
	 * Determine if this LinkChange has the given link
	 * @param linkToCheck link to compare
	 * @return <code>true</code> if the links are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasLink(edu.uci.isr.xarch.instance.IXMLLink linkToCheck);

	/**
	 * Set the copyOfRemovedLink for this LinkChange.
	 * @param value new copyOfRemovedLink
	 */
	public void setCopyOfRemovedLink(edu.uci.isr.xarch.types.ILink value);

	/**
	 * Clear the copyOfRemovedLink from this LinkChange.
	 */
	public void clearCopyOfRemovedLink();

	/**
	 * Get the copyOfRemovedLink from this LinkChange.
	 * @return copyOfRemovedLink
	 */
	public edu.uci.isr.xarch.types.ILink getCopyOfRemovedLink();

	/**
	 * Determine if this LinkChange has the given copyOfRemovedLink
	 * @param copyOfRemovedLinkToCheck copyOfRemovedLink to compare
	 * @return <code>true</code> if the copyOfRemovedLinks are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasCopyOfRemovedLink(edu.uci.isr.xarch.types.ILink copyOfRemovedLinkToCheck);
	/**
	 * Determine if another LinkChange has the same
	 * id as this one.
	 * @param LinkChangeToCheck LinkChange to compare with this
	 * one.
	 */
	public boolean isEqual(ILinkChange LinkChangeToCheck);
	/**
	 * Determine if another LinkChange is equivalent to this one, ignoring
	 * ID's.
	 * @param LinkChangeToCheck LinkChange to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * LinkChange are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(ILinkChange LinkChangeToCheck);

}
