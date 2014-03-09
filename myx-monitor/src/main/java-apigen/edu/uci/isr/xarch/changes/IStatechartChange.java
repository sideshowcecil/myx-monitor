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
 * StatechartChange <code>xsi:type</code> in the
 * changes namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface IStatechartChange extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"changes", "StatechartChange", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("id", "instance", "Identifier", null, null),
			XArchPropertyMetadata.createAttribute("type", "changes", "ChangeType", null, null),
			XArchPropertyMetadata.createElement("description", "instance", "Description", 1, 1),
			XArchPropertyMetadata.createElement("statechart", "instance", "XMLLink", 1, 1),
			XArchPropertyMetadata.createElement("copyOfRemovedStatechart", "statecharts", "Statechart", 0, 1)},
		new XArchActionMetadata[]{});

	/**
	 * Set the id attribute on this StatechartChange.
	 * @param id id
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setId(String id);

	/**
	 * Remove the id attribute from this StatechartChange.
	 */
	public void clearId();

	/**
	 * Get the id attribute from this StatechartChange.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return id on this StatechartChange
	 */
	public String getId();

	/**
	 * Determine if the id attribute on this StatechartChange
	 * has the given value.
	 * @param id Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasId(String id);


	/**
	 * Set the type attribute on this StatechartChange.
	 * @param type type
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setType(String type);

	/**
	 * Remove the type attribute from this StatechartChange.
	 */
	public void clearType();

	/**
	 * Get the type attribute from this StatechartChange.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return type on this StatechartChange
	 */
	public String getType();

	/**
	 * Determine if the type attribute on this StatechartChange
	 * has the given value.
	 * @param type Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasType(String type);


	/**
	 * Set the description for this StatechartChange.
	 * @param value new description
	 */
	public void setDescription(edu.uci.isr.xarch.instance.IDescription value);

	/**
	 * Clear the description from this StatechartChange.
	 */
	public void clearDescription();

	/**
	 * Get the description from this StatechartChange.
	 * @return description
	 */
	public edu.uci.isr.xarch.instance.IDescription getDescription();

	/**
	 * Determine if this StatechartChange has the given description
	 * @param descriptionToCheck description to compare
	 * @return <code>true</code> if the descriptions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasDescription(edu.uci.isr.xarch.instance.IDescription descriptionToCheck);

	/**
	 * Set the statechart for this StatechartChange.
	 * @param value new statechart
	 */
	public void setStatechart(edu.uci.isr.xarch.instance.IXMLLink value);

	/**
	 * Clear the statechart from this StatechartChange.
	 */
	public void clearStatechart();

	/**
	 * Get the statechart from this StatechartChange.
	 * @return statechart
	 */
	public edu.uci.isr.xarch.instance.IXMLLink getStatechart();

	/**
	 * Determine if this StatechartChange has the given statechart
	 * @param statechartToCheck statechart to compare
	 * @return <code>true</code> if the statecharts are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasStatechart(edu.uci.isr.xarch.instance.IXMLLink statechartToCheck);

	/**
	 * Set the copyOfRemovedStatechart for this StatechartChange.
	 * @param value new copyOfRemovedStatechart
	 */
	public void setCopyOfRemovedStatechart(edu.uci.isr.xarch.statecharts.IStatechart value);

	/**
	 * Clear the copyOfRemovedStatechart from this StatechartChange.
	 */
	public void clearCopyOfRemovedStatechart();

	/**
	 * Get the copyOfRemovedStatechart from this StatechartChange.
	 * @return copyOfRemovedStatechart
	 */
	public edu.uci.isr.xarch.statecharts.IStatechart getCopyOfRemovedStatechart();

	/**
	 * Determine if this StatechartChange has the given copyOfRemovedStatechart
	 * @param copyOfRemovedStatechartToCheck copyOfRemovedStatechart to compare
	 * @return <code>true</code> if the copyOfRemovedStatecharts are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasCopyOfRemovedStatechart(edu.uci.isr.xarch.statecharts.IStatechart copyOfRemovedStatechartToCheck);
	/**
	 * Determine if another StatechartChange has the same
	 * id as this one.
	 * @param StatechartChangeToCheck StatechartChange to compare with this
	 * one.
	 */
	public boolean isEqual(IStatechartChange StatechartChangeToCheck);
	/**
	 * Determine if another StatechartChange is equivalent to this one, ignoring
	 * ID's.
	 * @param StatechartChangeToCheck StatechartChange to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * StatechartChange are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IStatechartChange StatechartChangeToCheck);

}
