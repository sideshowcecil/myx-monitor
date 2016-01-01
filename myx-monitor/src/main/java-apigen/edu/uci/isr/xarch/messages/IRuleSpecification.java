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
package edu.uci.isr.xarch.messages;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * RuleSpecification <code>xsi:type</code> in the
 * messages namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface IRuleSpecification extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"messages", "RuleSpecification", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("id", "instance", "Identifier", null, null),
			XArchPropertyMetadata.createElement("description", "instance", "Description", 0, 1),
			XArchPropertyMetadata.createElement("rule", "messages", "ProductionRule", 1, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Set the id attribute on this RuleSpecification.
	 * @param id id
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setId(String id);

	/**
	 * Remove the id attribute from this RuleSpecification.
	 */
	public void clearId();

	/**
	 * Get the id attribute from this RuleSpecification.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return id on this RuleSpecification
	 */
	public String getId();

	/**
	 * Determine if the id attribute on this RuleSpecification
	 * has the given value.
	 * @param id Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasId(String id);


	/**
	 * Set the description for this RuleSpecification.
	 * @param value new description
	 */
	public void setDescription(edu.uci.isr.xarch.instance.IDescription value);

	/**
	 * Clear the description from this RuleSpecification.
	 */
	public void clearDescription();

	/**
	 * Get the description from this RuleSpecification.
	 * @return description
	 */
	public edu.uci.isr.xarch.instance.IDescription getDescription();

	/**
	 * Determine if this RuleSpecification has the given description
	 * @param descriptionToCheck description to compare
	 * @return <code>true</code> if the descriptions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasDescription(edu.uci.isr.xarch.instance.IDescription descriptionToCheck);

	/**
	 * Add a rule to this RuleSpecification.
	 * @param newRule rule to add.
	 */
	public void addRule(IProductionRule newRule);

	/**
	 * Add a collection of rules to this RuleSpecification.
	 * @param rules rules to add.
	 */
	public void addRules(Collection rules);

	/**
	 * Remove all rules from this RuleSpecification.
	 */
	public void clearRules();

	/**
	 * Remove the given rule from this RuleSpecification.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param ruleToRemove rule to remove.
	 */
	public void removeRule(IProductionRule ruleToRemove);

	/**
	 * Remove all the given rules from this RuleSpecification.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param rules rule to remove.
	 */
	public void removeRules(Collection rules);

	/**
	 * Get all the rules from this RuleSpecification.
	 * @return all rules in this RuleSpecification.
	 */
	public Collection getAllRules();

	/**
	 * Determine if this RuleSpecification contains a given rule.
	 * @return <code>true</code> if this RuleSpecification contains the given
	 * ruleToCheck, <code>false</code> otherwise.
	 */
	public boolean hasRule(IProductionRule ruleToCheck);

	/**
	 * Determine if this RuleSpecification contains the given set of rules.
	 * @param rulesToCheck rules to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>rules</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasRules(Collection rulesToCheck);

	/**
	 * Determine if this RuleSpecification contains each element in the 
	 * given set of rules.
	 * @param rulesToCheck rules to check for.
	 * @return <code>true</code> if every element in
	 * <code>rules</code> is found in this RuleSpecification,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllRules(Collection rulesToCheck);

	/**
	 * Gets the rule from this RuleSpecification with the given
	 * id.
	 * @param id ID to look for.
	 * @return rule with the given ID, or <code>null</code> if not found.
	 */
	public IProductionRule getRule(String id);

	/**
	 * Gets the rules from this RuleSpecification with the given
	 * ids.
	 * @param ids ID to look for.
	 * @return rules with the given IDs.  If an element with a given
	 * ID was not found, that ID is ignored.
	 */
	public Collection getRules(Collection ids);

	/**
	 * Determine if another RuleSpecification has the same
	 * id as this one.
	 * @param RuleSpecificationToCheck RuleSpecification to compare with this
	 * one.
	 */
	public boolean isEqual(IRuleSpecification RuleSpecificationToCheck);
	/**
	 * Determine if another RuleSpecification is equivalent to this one, ignoring
	 * ID's.
	 * @param RuleSpecificationToCheck RuleSpecification to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * RuleSpecification are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IRuleSpecification RuleSpecificationToCheck);

}
