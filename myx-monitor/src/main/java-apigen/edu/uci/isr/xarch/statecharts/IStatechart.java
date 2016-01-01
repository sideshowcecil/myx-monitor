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
package edu.uci.isr.xarch.statecharts;

import java.util.Collection;
import edu.uci.isr.xarch.XArchActionMetadata;
import edu.uci.isr.xarch.XArchTypeMetadata;
import edu.uci.isr.xarch.XArchPropertyMetadata;

/**
 * Interface for accessing objects of the
 * Statechart <code>xsi:type</code> in the
 * statecharts namespace.
 * 
 * @author Automatically generated by xArch apigen
 */
public interface IStatechart extends edu.uci.isr.xarch.IXArchElement{

	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_ELEMENT,
		"statecharts", "Statechart", edu.uci.isr.xarch.IXArchElement.TYPE_METADATA,
		new XArchPropertyMetadata[]{
			XArchPropertyMetadata.createAttribute("id", "instance", "Identifier", null, null),
			XArchPropertyMetadata.createElement("description", "instance", "Description", 1, 1),
			XArchPropertyMetadata.createElement("linkedComp", "instance", "XMLLink", 0, 1),
			XArchPropertyMetadata.createElement("state", "statecharts", "State", 0, XArchPropertyMetadata.UNBOUNDED),
			XArchPropertyMetadata.createElement("transition", "statecharts", "Transition", 0, XArchPropertyMetadata.UNBOUNDED)},
		new XArchActionMetadata[]{});

	/**
	 * Set the id attribute on this Statechart.
	 * @param id id
	 * @exception FixedValueException if the attribute has a fixed value
	 * and the value passed is not the fixed value.
	*/
	public void setId(String id);

	/**
	 * Remove the id attribute from this Statechart.
	 */
	public void clearId();

	/**
	 * Get the id attribute from this Statechart.
	 * if the attribute has a fixed value, this function will
	 * return that fixed value, even if it is not actually present
	 * in the XML document.
	 * @return id on this Statechart
	 */
	public String getId();

	/**
	 * Determine if the id attribute on this Statechart
	 * has the given value.
	 * @param id Attribute value to compare
	 * @return <code>true</code> if they match; <code>false</code>
	 * otherwise.
	 */
	public boolean hasId(String id);


	/**
	 * Set the description for this Statechart.
	 * @param value new description
	 */
	public void setDescription(edu.uci.isr.xarch.instance.IDescription value);

	/**
	 * Clear the description from this Statechart.
	 */
	public void clearDescription();

	/**
	 * Get the description from this Statechart.
	 * @return description
	 */
	public edu.uci.isr.xarch.instance.IDescription getDescription();

	/**
	 * Determine if this Statechart has the given description
	 * @param descriptionToCheck description to compare
	 * @return <code>true</code> if the descriptions are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasDescription(edu.uci.isr.xarch.instance.IDescription descriptionToCheck);

	/**
	 * Set the linkedComp for this Statechart.
	 * @param value new linkedComp
	 */
	public void setLinkedComp(edu.uci.isr.xarch.instance.IXMLLink value);

	/**
	 * Clear the linkedComp from this Statechart.
	 */
	public void clearLinkedComp();

	/**
	 * Get the linkedComp from this Statechart.
	 * @return linkedComp
	 */
	public edu.uci.isr.xarch.instance.IXMLLink getLinkedComp();

	/**
	 * Determine if this Statechart has the given linkedComp
	 * @param linkedCompToCheck linkedComp to compare
	 * @return <code>true</code> if the linkedComps are equivalent,
	 * <code>false</code> otherwise
	 */
	public boolean hasLinkedComp(edu.uci.isr.xarch.instance.IXMLLink linkedCompToCheck);

	/**
	 * Add a state to this Statechart.
	 * @param newState state to add.
	 */
	public void addState(IState newState);

	/**
	 * Add a collection of states to this Statechart.
	 * @param states states to add.
	 */
	public void addStates(Collection states);

	/**
	 * Remove all states from this Statechart.
	 */
	public void clearStates();

	/**
	 * Remove the given state from this Statechart.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param stateToRemove state to remove.
	 */
	public void removeState(IState stateToRemove);

	/**
	 * Remove all the given states from this Statechart.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param states state to remove.
	 */
	public void removeStates(Collection states);

	/**
	 * Get all the states from this Statechart.
	 * @return all states in this Statechart.
	 */
	public Collection getAllStates();

	/**
	 * Determine if this Statechart contains a given state.
	 * @return <code>true</code> if this Statechart contains the given
	 * stateToCheck, <code>false</code> otherwise.
	 */
	public boolean hasState(IState stateToCheck);

	/**
	 * Determine if this Statechart contains the given set of states.
	 * @param statesToCheck states to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>states</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasStates(Collection statesToCheck);

	/**
	 * Determine if this Statechart contains each element in the 
	 * given set of states.
	 * @param statesToCheck states to check for.
	 * @return <code>true</code> if every element in
	 * <code>states</code> is found in this Statechart,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllStates(Collection statesToCheck);

	/**
	 * Gets the state from this Statechart with the given
	 * id.
	 * @param id ID to look for.
	 * @return state with the given ID, or <code>null</code> if not found.
	 */
	public IState getState(String id);

	/**
	 * Gets the states from this Statechart with the given
	 * ids.
	 * @param ids ID to look for.
	 * @return states with the given IDs.  If an element with a given
	 * ID was not found, that ID is ignored.
	 */
	public Collection getStates(Collection ids);


	/**
	 * Add a transition to this Statechart.
	 * @param newTransition transition to add.
	 */
	public void addTransition(ITransition newTransition);

	/**
	 * Add a collection of transitions to this Statechart.
	 * @param transitions transitions to add.
	 */
	public void addTransitions(Collection transitions);

	/**
	 * Remove all transitions from this Statechart.
	 */
	public void clearTransitions();

	/**
	 * Remove the given transition from this Statechart.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param transitionToRemove transition to remove.
	 */
	public void removeTransition(ITransition transitionToRemove);

	/**
	 * Remove all the given transitions from this Statechart.
	 * Matching is done by the <code>isEquivalent(...)</code> function.
	 * @param transitions transition to remove.
	 */
	public void removeTransitions(Collection transitions);

	/**
	 * Get all the transitions from this Statechart.
	 * @return all transitions in this Statechart.
	 */
	public Collection getAllTransitions();

	/**
	 * Determine if this Statechart contains a given transition.
	 * @return <code>true</code> if this Statechart contains the given
	 * transitionToCheck, <code>false</code> otherwise.
	 */
	public boolean hasTransition(ITransition transitionToCheck);

	/**
	 * Determine if this Statechart contains the given set of transitions.
	 * @param transitionsToCheck transitions to check for.
	 * @return Collection of <code>java.lang.Boolean</code>.  If the i<sup>th</sup>
	 * element in <code>transitions</code> was found, then the i<sup>th</sup>
	 * element of the collection will be set to <code>true</code>, otherwise it
	 * will be set to <code>false</code>.  Matching is done with the
	 * <code>isEquivalent(...)</code> method.
	 */
	public Collection hasTransitions(Collection transitionsToCheck);

	/**
	 * Determine if this Statechart contains each element in the 
	 * given set of transitions.
	 * @param transitionsToCheck transitions to check for.
	 * @return <code>true</code> if every element in
	 * <code>transitions</code> is found in this Statechart,
	 * <code>false</code> otherwise.
	 */
	public boolean hasAllTransitions(Collection transitionsToCheck);

	/**
	 * Gets the transition from this Statechart with the given
	 * id.
	 * @param id ID to look for.
	 * @return transition with the given ID, or <code>null</code> if not found.
	 */
	public ITransition getTransition(String id);

	/**
	 * Gets the transitions from this Statechart with the given
	 * ids.
	 * @param ids ID to look for.
	 * @return transitions with the given IDs.  If an element with a given
	 * ID was not found, that ID is ignored.
	 */
	public Collection getTransitions(Collection ids);

	/**
	 * Determine if another Statechart has the same
	 * id as this one.
	 * @param StatechartToCheck Statechart to compare with this
	 * one.
	 */
	public boolean isEqual(IStatechart StatechartToCheck);
	/**
	 * Determine if another Statechart is equivalent to this one, ignoring
	 * ID's.
	 * @param StatechartToCheck Statechart to compare to this one.
	 * @return <code>true</code> if all the child elements of this
	 * Statechart are equivalent, <code>false</code> otherwise.
	 */
	public boolean isEquivalent(IStatechart StatechartToCheck);

}
