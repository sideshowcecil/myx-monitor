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

import org.w3c.dom.*;

import edu.uci.isr.xarch.*;

import java.util.*;

/**
 * DOM-Based implementation of the IChanges interface.
 *
 * @author Automatically generated by xArch apigen.
 */
public class ChangesImpl implements IChanges, DOMBased{
	
	public static final String XSD_TYPE_NSURI = ChangesConstants.NS_URI;
	public static final String XSD_TYPE_NAME = "Changes";
	
	protected IXArch xArch;
	
	/** Tag name for ids in this object. */
	public static final String ID_ATTR_NAME = "id";
	/** Tag name for statuss in this object. */
	public static final String STATUS_ATTR_NAME = "status";
	/** Tag name for descriptions in this object. */
	public static final String DESCRIPTION_ELT_NAME = "description";
	/** Tag name for componentChanges in this object. */
	public static final String COMPONENT_CHANGE_ELT_NAME = "componentChange";
	/** Tag name for linkChanges in this object. */
	public static final String LINK_CHANGE_ELT_NAME = "linkChange";
	/** Tag name for interactionChanges in this object. */
	public static final String INTERACTION_CHANGE_ELT_NAME = "interactionChange";
	/** Tag name for statechartChanges in this object. */
	public static final String STATECHART_CHANGE_ELT_NAME = "statechartChange";

	
	protected Element elt;
	
	private static SequenceOrder seqOrd = new SequenceOrder(
		new QName[]{
			new QName(ChangesConstants.NS_URI, DESCRIPTION_ELT_NAME),
			new QName(ChangesConstants.NS_URI, COMPONENT_CHANGE_ELT_NAME),
			new QName(ChangesConstants.NS_URI, LINK_CHANGE_ELT_NAME),
			new QName(ChangesConstants.NS_URI, INTERACTION_CHANGE_ELT_NAME),
			new QName(ChangesConstants.NS_URI, STATECHART_CHANGE_ELT_NAME)
		}
	);
	
	public ChangesImpl(Element elt){
		if(elt == null){
			throw new IllegalArgumentException("Element cannot be null.");
		}
		this.elt = elt;
	}

	public Node getDOMNode(){
		return elt;
	}
	
	public void setDOMNode(Node node){
		if(node.getNodeType() != Node.ELEMENT_NODE){
			throw new IllegalArgumentException("Base DOM node of this type must be an Element.");
		}
		elt = (Element)node;
	}
	
	protected static SequenceOrder getSequenceOrder(){
		return seqOrd;
	}
	
	public void setXArch(IXArch xArch){
		this.xArch = xArch;
	}
	
	public IXArch getXArch(){
		return this.xArch;
	}

	public IXArchElement cloneElement(int depth){
		synchronized(DOMUtils.getDOMLock(elt)){
			Document doc = elt.getOwnerDocument();
			if(depth == 0){
				Element cloneElt = (Element)elt.cloneNode(false);
				cloneElt = (Element)doc.importNode(cloneElt, true);
				ChangesImpl cloneImpl = new ChangesImpl(cloneElt);
				cloneImpl.setXArch(getXArch());
				return cloneImpl;
			}
			else if(depth == 1){
				Element cloneElt = (Element)elt.cloneNode(false);
				cloneElt = (Element)doc.importNode(cloneElt, true);
				ChangesImpl cloneImpl = new ChangesImpl(cloneElt);
				cloneImpl.setXArch(getXArch());
				
				NodeList nl = elt.getChildNodes();
				int size = nl.getLength();
				for(int i = 0; i < size; i++){
					Node n = nl.item(i);
					Node cloneNode = (Node)n.cloneNode(false);
					cloneNode = doc.importNode(cloneNode, true);
					cloneElt.appendChild(cloneNode);
				}
				return cloneImpl;
			}
			else /* depth = infinity */{
				Element cloneElt = (Element)elt.cloneNode(true);
				cloneElt = (Element)doc.importNode(cloneElt, true);
				ChangesImpl cloneImpl = new ChangesImpl(cloneElt);
				cloneImpl.setXArch(getXArch());
				return cloneImpl;
			}
		}
	}

	//Override 'equals' to be DOM-based...	
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		if(!(o instanceof DOMBased)){
			return super.equals(o);
		}
		DOMBased db = (DOMBased)o;
		Node dbNode = db.getDOMNode();
		return dbNode.equals(getDOMNode());
	}

	//Override 'hashCode' to be based on the underlying node
	public int hashCode(){
		return getDOMNode().hashCode();
	}

	/**
	 * For internal use only.
	 */
	private static Object makeDerivedWrapper(Element elt, String baseTypeName){
		synchronized(DOMUtils.getDOMLock(elt)){
			QName typeName = XArchUtils.getXSIType(elt);
			if(typeName == null){
				return null;
			}
			else{
				if(!DOMUtils.hasXSIType(elt, "http://www.ics.uci.edu/pub/arch/xArch/changes.xsd", baseTypeName)){
					try{
						String packageTitle = XArchUtils.getPackageTitle(typeName.getNamespaceURI());
						String packageName = XArchUtils.getPackageName(packageTitle);
						String implName = XArchUtils.getImplName(packageName, typeName.getName());
						Class c = Class.forName(implName);
						java.lang.reflect.Constructor con = c.getConstructor(new Class[]{Element.class});
						Object o = con.newInstance(new Object[]{elt});
						return o;
					}
					catch(Exception e){
						//Lots of bad things could happen, but this
						//is OK, because this is best-effort anyway.
					}
				}
				return null;
			}
		}
	}

	public XArchTypeMetadata getTypeMetadata(){
		return IChanges.TYPE_METADATA;
	}

	public XArchInstanceMetadata getInstanceMetadata(){
		return new XArchInstanceMetadata(XArchUtils.getPackageTitle(elt.getNamespaceURI()));
	}
	/**
	 * Set the id attribute on this object.
	 * @param id attribute value.
	 */
	public void setId(String id){
		{
			String oldValue = getId();
			if(oldValue == null ? id == null : oldValue.equals(id))
				return;
			DOMUtils.removeAttribute(elt, ChangesConstants.NS_URI, ID_ATTR_NAME);
			IXArch _x = getXArch();
			if(_x != null){
				_x.fireXArchEvent(
					new XArchEvent(this, 
					XArchEvent.CLEAR_EVENT,
					XArchEvent.ATTRIBUTE_CHANGED,
					"id", oldValue,
					XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this), true)
				);
			}
		}
		DOMUtils.setAttribute(elt, ChangesConstants.NS_URI, ID_ATTR_NAME, id);
		IXArch _x = getXArch();
		if(_x != null){
			_x.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.SET_EVENT,
				XArchEvent.ATTRIBUTE_CHANGED,
				"id", id,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
		
	/**
	 * Removes the id attribute from this object.
	 */
	public void clearId(){
		String oldValue = getId();
		if(oldValue == null)
			return;
		DOMUtils.removeAttribute(elt, ChangesConstants.NS_URI, ID_ATTR_NAME);
		IXArch _x = getXArch();
		if(_x != null){
			_x.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.CLEAR_EVENT,
				XArchEvent.ATTRIBUTE_CHANGED,
				"id", oldValue,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
	
	/**
	 * Gets the value of the id attribute on this object.
	 * @return id attribute's value or <code>null</code> if that
	 * attribute is not set.
	 */
	public String getId(){
		return DOMUtils.getAttributeValue(elt, ChangesConstants.NS_URI, ID_ATTR_NAME);
	}
	
	/**
	 * Determines if this object's id attribute has the
	 * given value.
	 * @param id value to test.
	 * @return <code>true</code> if the values match, <code>false</code> otherwise.
	 * Matching is done by string-matching.
	 */
	public boolean hasId(String id){
		return DOMUtils.objNullEq(getId(), id);
	}

	/**
	 * Set the status attribute on this object.
	 * @param status attribute value.
	 */
	public void setStatus(String status){
		{
			String oldValue = getStatus();
			if(oldValue == null ? status == null : oldValue.equals(status))
				return;
			DOMUtils.removeAttribute(elt, ChangesConstants.NS_URI, STATUS_ATTR_NAME);
			IXArch _x = getXArch();
			if(_x != null){
				_x.fireXArchEvent(
					new XArchEvent(this, 
					XArchEvent.CLEAR_EVENT,
					XArchEvent.ATTRIBUTE_CHANGED,
					"status", oldValue,
					XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this), true)
				);
			}
		}
		DOMUtils.setAttribute(elt, ChangesConstants.NS_URI, STATUS_ATTR_NAME, status);
		IXArch _x = getXArch();
		if(_x != null){
			_x.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.SET_EVENT,
				XArchEvent.ATTRIBUTE_CHANGED,
				"status", status,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
		
	/**
	 * Removes the status attribute from this object.
	 */
	public void clearStatus(){
		String oldValue = getStatus();
		if(oldValue == null)
			return;
		DOMUtils.removeAttribute(elt, ChangesConstants.NS_URI, STATUS_ATTR_NAME);
		IXArch _x = getXArch();
		if(_x != null){
			_x.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.CLEAR_EVENT,
				XArchEvent.ATTRIBUTE_CHANGED,
				"status", oldValue,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
	
	/**
	 * Gets the value of the status attribute on this object.
	 * @return status attribute's value or <code>null</code> if that
	 * attribute is not set.
	 */
	public String getStatus(){
		return DOMUtils.getAttributeValue(elt, ChangesConstants.NS_URI, STATUS_ATTR_NAME);
	}
	
	/**
	 * Determines if this object's status attribute has the
	 * given value.
	 * @param status value to test.
	 * @return <code>true</code> if the values match, <code>false</code> otherwise.
	 * Matching is done by string-matching.
	 */
	public boolean hasStatus(String status){
		return DOMUtils.objNullEq(getStatus(), status);
	}


	public void setDescription(edu.uci.isr.xarch.instance.IDescription value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		{
			edu.uci.isr.xarch.instance.IDescription oldElt = getDescription();
			DOMUtils.removeChildren(elt, ChangesConstants.NS_URI, DESCRIPTION_ELT_NAME);
			
			IXArch context = getXArch();
			if(context != null){
				context.fireXArchEvent(
					new XArchEvent(this, 
					XArchEvent.CLEAR_EVENT,
					XArchEvent.ELEMENT_CHANGED,
					"description", oldElt,
					XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this), true)
				);
			}
		}
		Element newChildElt = (Element)(((DOMBased)value).getDOMNode());
		newChildElt = DOMUtils.cloneAndRename(newChildElt, ChangesConstants.NS_URI, DESCRIPTION_ELT_NAME);
		((DOMBased)value).setDOMNode(newChildElt);
		
		synchronized(DOMUtils.getDOMLock(elt)){
			elt.appendChild(newChildElt);
			DOMUtils.order(elt, getSequenceOrder());
		}
		
		IXArch context = getXArch();
		if(context != null){
			context.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.SET_EVENT,
				XArchEvent.ELEMENT_CHANGED,
				"description", value,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
	
	public void clearDescription(){
		edu.uci.isr.xarch.instance.IDescription oldElt = getDescription();
		DOMUtils.removeChildren(elt, ChangesConstants.NS_URI, DESCRIPTION_ELT_NAME);
		
		IXArch context = getXArch();
		if(context != null){
			context.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.CLEAR_EVENT,
				XArchEvent.ELEMENT_CHANGED,
				"description", oldElt,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
	
	public edu.uci.isr.xarch.instance.IDescription getDescription(){
		NodeList nl = DOMUtils.getChildren(elt, ChangesConstants.NS_URI, DESCRIPTION_ELT_NAME);
		if(nl.getLength() == 0){
			return null;
		}
		else{
			Element el = (Element)nl.item(0);
			IXArch de = getXArch();
			if(de != null){
				IXArchElement cachedXArchElt = de.getWrapper(el);
				if(cachedXArchElt != null){
					return (edu.uci.isr.xarch.instance.IDescription)cachedXArchElt;
				}
			}
			
			Object o = makeDerivedWrapper(el, "Description");
			if(o != null){
				try{
					((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
					if(de != null){
						de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)o));
					}
					return (edu.uci.isr.xarch.instance.IDescription)o;
				}
				catch(Exception e){}
			}
			edu.uci.isr.xarch.instance.DescriptionImpl eltImpl = new edu.uci.isr.xarch.instance.DescriptionImpl(el);
			eltImpl.setXArch(getXArch());
			if(de != null){
				de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
			}
			return eltImpl;
		}
	}
	
	public boolean hasDescription(edu.uci.isr.xarch.instance.IDescription value){
		edu.uci.isr.xarch.instance.IDescription thisValue = getDescription();
		edu.uci.isr.xarch.instance.IDescription thatValue = value;
		
		if((thisValue == null) && (thatValue == null)){
			return true;
		}
		else if((thisValue == null) && (thatValue != null)){
			return false;
		}
		else if((thisValue != null) && (thatValue == null)){
			return false;
		}
		return thisValue.isEquivalent(thatValue);
	}

	public void addComponentChange(IComponentChange newComponentChange){
		if(!(newComponentChange instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		Element newChildElt = (Element)(((DOMBased)newComponentChange).getDOMNode());
		newChildElt = DOMUtils.cloneAndRename(newChildElt, ChangesConstants.NS_URI, COMPONENT_CHANGE_ELT_NAME);
		((DOMBased)newComponentChange).setDOMNode(newChildElt);

		synchronized(DOMUtils.getDOMLock(elt)){
			elt.appendChild(newChildElt);
			DOMUtils.order(elt, getSequenceOrder());
		}

		IXArch context = getXArch();
		if(context != null){
			context.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.ADD_EVENT,
				XArchEvent.ELEMENT_CHANGED,
				"componentChange", newComponentChange,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
		
	public void addComponentChanges(Collection componentChanges){
		for(Iterator en = componentChanges.iterator(); en.hasNext(); ){
			IComponentChange elt = (IComponentChange)en.next();
			addComponentChange(elt);
		}
	}		
		
	public void clearComponentChanges(){
		//DOMUtils.removeChildren(elt, ChangesConstants.NS_URI, COMPONENT_CHANGE_ELT_NAME);
		Collection coll = getAllComponentChanges();
		removeComponentChanges(coll);
	}
	
	public void removeComponentChange(IComponentChange componentChangeToRemove){
		if(!(componentChangeToRemove instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		NodeList nl = DOMUtils.getChildren(elt, ChangesConstants.NS_URI, COMPONENT_CHANGE_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			if(n == ((DOMBased)componentChangeToRemove).getDOMNode()){
				synchronized(DOMUtils.getDOMLock(elt)){
					elt.removeChild(n);
				}

				IXArch context = getXArch();
				if(context != null){
					context.fireXArchEvent(
						new XArchEvent(this, 
						XArchEvent.REMOVE_EVENT,
						XArchEvent.ELEMENT_CHANGED,
						"componentChange", componentChangeToRemove,
						XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
					);
				}

				return;
			}
		}
	}
	
	public void removeComponentChanges(Collection componentChanges){
		for(Iterator en = componentChanges.iterator(); en.hasNext(); ){
			IComponentChange elt = (IComponentChange)en.next();
			removeComponentChange(elt);
		}
	}
	
	public Collection getAllComponentChanges(){
		NodeList nl = DOMUtils.getChildren(elt, ChangesConstants.NS_URI, COMPONENT_CHANGE_ELT_NAME);
		int nlLength = nl.getLength();
		ArrayList v = new ArrayList(nlLength);
		IXArch de = getXArch();
		for(int i = 0; i < nlLength; i++){
			Element el = (Element)nl.item(i);
			boolean found = false;
			if(de != null){
				IXArchElement cachedXArchElt = de.getWrapper(el);
				if(cachedXArchElt != null){
					v.add((IComponentChange)cachedXArchElt);
					found = true;
				}
			}
			if(!found){
				Object o = makeDerivedWrapper(el, "ComponentChange");	
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)o));
						}
						v.add((IComponentChange)o);
					}
					catch(Exception e){
						ComponentChangeImpl eltImpl = new ComponentChangeImpl((Element)nl.item(i));
						eltImpl.setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
						}
						v.add(eltImpl);
					}
				}
				else{
					ComponentChangeImpl eltImpl = new ComponentChangeImpl((Element)nl.item(i));
					eltImpl.setXArch(getXArch());
					if(de != null){
						de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
					}
					v.add(eltImpl);
				}
			}
		}
		return v;
	}

	public boolean hasComponentChange(IComponentChange componentChangeToCheck){
		Collection c = getAllComponentChanges();
		
		for(Iterator en = c.iterator(); en.hasNext(); ){
			IComponentChange elt = (IComponentChange)en.next();
			if(elt.isEquivalent(componentChangeToCheck)){
				return true;
			}
		}
		return false;
	}
	
	public Collection hasComponentChanges(Collection componentChangesToCheck){
		Vector v = new Vector();
		for(Iterator en = componentChangesToCheck.iterator(); en.hasNext(); ){
			IComponentChange elt = (IComponentChange)en.next();
			v.addElement(new Boolean(hasComponentChange(elt)));
		}
		return v;
	}
		
	public boolean hasAllComponentChanges(Collection componentChangesToCheck){
		for(Iterator en = componentChangesToCheck.iterator(); en.hasNext(); ){
			IComponentChange elt = (IComponentChange)en.next();
			if(!hasComponentChange(elt)){
				return false;
			}
		}
		return true;
	}
	public IComponentChange getComponentChange(String id){
		NodeList nl = DOMUtils.getChildren(elt, ChangesConstants.NS_URI, COMPONENT_CHANGE_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			
			IComponentChange el = new ComponentChangeImpl((Element)nl.item(i));
			if(DOMUtils.objNullEq(id, el.getId())){
				Element domElt = (Element)nl.item(i);
				Object o = makeDerivedWrapper(domElt, "ComponentChange");
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						return (IComponentChange)o;
					}
					catch(Exception e){}
				}
				el.setXArch(getXArch());
				return el;
			}
		}
		return null;
	}
	
	public Collection getComponentChanges(Collection ids){
		//If there is an ID that does not exist, it is simply ignored.
		//You can tell if this happened if ids.size() != returned collection.size().
		Vector v = new Vector();
		for(Iterator en = ids.iterator(); en.hasNext(); ){
			String elt = (String)en.next();
			IComponentChange retElt = getComponentChange(elt);
			if(retElt != null){
				v.addElement(retElt);
			}
		}
		return v;
	}	
	
	public void addLinkChange(ILinkChange newLinkChange){
		if(!(newLinkChange instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		Element newChildElt = (Element)(((DOMBased)newLinkChange).getDOMNode());
		newChildElt = DOMUtils.cloneAndRename(newChildElt, ChangesConstants.NS_URI, LINK_CHANGE_ELT_NAME);
		((DOMBased)newLinkChange).setDOMNode(newChildElt);

		synchronized(DOMUtils.getDOMLock(elt)){
			elt.appendChild(newChildElt);
			DOMUtils.order(elt, getSequenceOrder());
		}

		IXArch context = getXArch();
		if(context != null){
			context.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.ADD_EVENT,
				XArchEvent.ELEMENT_CHANGED,
				"linkChange", newLinkChange,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
		
	public void addLinkChanges(Collection linkChanges){
		for(Iterator en = linkChanges.iterator(); en.hasNext(); ){
			ILinkChange elt = (ILinkChange)en.next();
			addLinkChange(elt);
		}
	}		
		
	public void clearLinkChanges(){
		//DOMUtils.removeChildren(elt, ChangesConstants.NS_URI, LINK_CHANGE_ELT_NAME);
		Collection coll = getAllLinkChanges();
		removeLinkChanges(coll);
	}
	
	public void removeLinkChange(ILinkChange linkChangeToRemove){
		if(!(linkChangeToRemove instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		NodeList nl = DOMUtils.getChildren(elt, ChangesConstants.NS_URI, LINK_CHANGE_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			if(n == ((DOMBased)linkChangeToRemove).getDOMNode()){
				synchronized(DOMUtils.getDOMLock(elt)){
					elt.removeChild(n);
				}

				IXArch context = getXArch();
				if(context != null){
					context.fireXArchEvent(
						new XArchEvent(this, 
						XArchEvent.REMOVE_EVENT,
						XArchEvent.ELEMENT_CHANGED,
						"linkChange", linkChangeToRemove,
						XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
					);
				}

				return;
			}
		}
	}
	
	public void removeLinkChanges(Collection linkChanges){
		for(Iterator en = linkChanges.iterator(); en.hasNext(); ){
			ILinkChange elt = (ILinkChange)en.next();
			removeLinkChange(elt);
		}
	}
	
	public Collection getAllLinkChanges(){
		NodeList nl = DOMUtils.getChildren(elt, ChangesConstants.NS_URI, LINK_CHANGE_ELT_NAME);
		int nlLength = nl.getLength();
		ArrayList v = new ArrayList(nlLength);
		IXArch de = getXArch();
		for(int i = 0; i < nlLength; i++){
			Element el = (Element)nl.item(i);
			boolean found = false;
			if(de != null){
				IXArchElement cachedXArchElt = de.getWrapper(el);
				if(cachedXArchElt != null){
					v.add((ILinkChange)cachedXArchElt);
					found = true;
				}
			}
			if(!found){
				Object o = makeDerivedWrapper(el, "LinkChange");	
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)o));
						}
						v.add((ILinkChange)o);
					}
					catch(Exception e){
						LinkChangeImpl eltImpl = new LinkChangeImpl((Element)nl.item(i));
						eltImpl.setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
						}
						v.add(eltImpl);
					}
				}
				else{
					LinkChangeImpl eltImpl = new LinkChangeImpl((Element)nl.item(i));
					eltImpl.setXArch(getXArch());
					if(de != null){
						de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
					}
					v.add(eltImpl);
				}
			}
		}
		return v;
	}

	public boolean hasLinkChange(ILinkChange linkChangeToCheck){
		Collection c = getAllLinkChanges();
		
		for(Iterator en = c.iterator(); en.hasNext(); ){
			ILinkChange elt = (ILinkChange)en.next();
			if(elt.isEquivalent(linkChangeToCheck)){
				return true;
			}
		}
		return false;
	}
	
	public Collection hasLinkChanges(Collection linkChangesToCheck){
		Vector v = new Vector();
		for(Iterator en = linkChangesToCheck.iterator(); en.hasNext(); ){
			ILinkChange elt = (ILinkChange)en.next();
			v.addElement(new Boolean(hasLinkChange(elt)));
		}
		return v;
	}
		
	public boolean hasAllLinkChanges(Collection linkChangesToCheck){
		for(Iterator en = linkChangesToCheck.iterator(); en.hasNext(); ){
			ILinkChange elt = (ILinkChange)en.next();
			if(!hasLinkChange(elt)){
				return false;
			}
		}
		return true;
	}
	public ILinkChange getLinkChange(String id){
		NodeList nl = DOMUtils.getChildren(elt, ChangesConstants.NS_URI, LINK_CHANGE_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			
			ILinkChange el = new LinkChangeImpl((Element)nl.item(i));
			if(DOMUtils.objNullEq(id, el.getId())){
				Element domElt = (Element)nl.item(i);
				Object o = makeDerivedWrapper(domElt, "LinkChange");
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						return (ILinkChange)o;
					}
					catch(Exception e){}
				}
				el.setXArch(getXArch());
				return el;
			}
		}
		return null;
	}
	
	public Collection getLinkChanges(Collection ids){
		//If there is an ID that does not exist, it is simply ignored.
		//You can tell if this happened if ids.size() != returned collection.size().
		Vector v = new Vector();
		for(Iterator en = ids.iterator(); en.hasNext(); ){
			String elt = (String)en.next();
			ILinkChange retElt = getLinkChange(elt);
			if(retElt != null){
				v.addElement(retElt);
			}
		}
		return v;
	}	
	
	public void addInteractionChange(IInteractionChange newInteractionChange){
		if(!(newInteractionChange instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		Element newChildElt = (Element)(((DOMBased)newInteractionChange).getDOMNode());
		newChildElt = DOMUtils.cloneAndRename(newChildElt, ChangesConstants.NS_URI, INTERACTION_CHANGE_ELT_NAME);
		((DOMBased)newInteractionChange).setDOMNode(newChildElt);

		synchronized(DOMUtils.getDOMLock(elt)){
			elt.appendChild(newChildElt);
			DOMUtils.order(elt, getSequenceOrder());
		}

		IXArch context = getXArch();
		if(context != null){
			context.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.ADD_EVENT,
				XArchEvent.ELEMENT_CHANGED,
				"interactionChange", newInteractionChange,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
		
	public void addInteractionChanges(Collection interactionChanges){
		for(Iterator en = interactionChanges.iterator(); en.hasNext(); ){
			IInteractionChange elt = (IInteractionChange)en.next();
			addInteractionChange(elt);
		}
	}		
		
	public void clearInteractionChanges(){
		//DOMUtils.removeChildren(elt, ChangesConstants.NS_URI, INTERACTION_CHANGE_ELT_NAME);
		Collection coll = getAllInteractionChanges();
		removeInteractionChanges(coll);
	}
	
	public void removeInteractionChange(IInteractionChange interactionChangeToRemove){
		if(!(interactionChangeToRemove instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		NodeList nl = DOMUtils.getChildren(elt, ChangesConstants.NS_URI, INTERACTION_CHANGE_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			if(n == ((DOMBased)interactionChangeToRemove).getDOMNode()){
				synchronized(DOMUtils.getDOMLock(elt)){
					elt.removeChild(n);
				}

				IXArch context = getXArch();
				if(context != null){
					context.fireXArchEvent(
						new XArchEvent(this, 
						XArchEvent.REMOVE_EVENT,
						XArchEvent.ELEMENT_CHANGED,
						"interactionChange", interactionChangeToRemove,
						XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
					);
				}

				return;
			}
		}
	}
	
	public void removeInteractionChanges(Collection interactionChanges){
		for(Iterator en = interactionChanges.iterator(); en.hasNext(); ){
			IInteractionChange elt = (IInteractionChange)en.next();
			removeInteractionChange(elt);
		}
	}
	
	public Collection getAllInteractionChanges(){
		NodeList nl = DOMUtils.getChildren(elt, ChangesConstants.NS_URI, INTERACTION_CHANGE_ELT_NAME);
		int nlLength = nl.getLength();
		ArrayList v = new ArrayList(nlLength);
		IXArch de = getXArch();
		for(int i = 0; i < nlLength; i++){
			Element el = (Element)nl.item(i);
			boolean found = false;
			if(de != null){
				IXArchElement cachedXArchElt = de.getWrapper(el);
				if(cachedXArchElt != null){
					v.add((IInteractionChange)cachedXArchElt);
					found = true;
				}
			}
			if(!found){
				Object o = makeDerivedWrapper(el, "InteractionChange");	
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)o));
						}
						v.add((IInteractionChange)o);
					}
					catch(Exception e){
						InteractionChangeImpl eltImpl = new InteractionChangeImpl((Element)nl.item(i));
						eltImpl.setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
						}
						v.add(eltImpl);
					}
				}
				else{
					InteractionChangeImpl eltImpl = new InteractionChangeImpl((Element)nl.item(i));
					eltImpl.setXArch(getXArch());
					if(de != null){
						de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
					}
					v.add(eltImpl);
				}
			}
		}
		return v;
	}

	public boolean hasInteractionChange(IInteractionChange interactionChangeToCheck){
		Collection c = getAllInteractionChanges();
		
		for(Iterator en = c.iterator(); en.hasNext(); ){
			IInteractionChange elt = (IInteractionChange)en.next();
			if(elt.isEquivalent(interactionChangeToCheck)){
				return true;
			}
		}
		return false;
	}
	
	public Collection hasInteractionChanges(Collection interactionChangesToCheck){
		Vector v = new Vector();
		for(Iterator en = interactionChangesToCheck.iterator(); en.hasNext(); ){
			IInteractionChange elt = (IInteractionChange)en.next();
			v.addElement(new Boolean(hasInteractionChange(elt)));
		}
		return v;
	}
		
	public boolean hasAllInteractionChanges(Collection interactionChangesToCheck){
		for(Iterator en = interactionChangesToCheck.iterator(); en.hasNext(); ){
			IInteractionChange elt = (IInteractionChange)en.next();
			if(!hasInteractionChange(elt)){
				return false;
			}
		}
		return true;
	}
	public IInteractionChange getInteractionChange(String id){
		NodeList nl = DOMUtils.getChildren(elt, ChangesConstants.NS_URI, INTERACTION_CHANGE_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			
			IInteractionChange el = new InteractionChangeImpl((Element)nl.item(i));
			if(DOMUtils.objNullEq(id, el.getId())){
				Element domElt = (Element)nl.item(i);
				Object o = makeDerivedWrapper(domElt, "InteractionChange");
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						return (IInteractionChange)o;
					}
					catch(Exception e){}
				}
				el.setXArch(getXArch());
				return el;
			}
		}
		return null;
	}
	
	public Collection getInteractionChanges(Collection ids){
		//If there is an ID that does not exist, it is simply ignored.
		//You can tell if this happened if ids.size() != returned collection.size().
		Vector v = new Vector();
		for(Iterator en = ids.iterator(); en.hasNext(); ){
			String elt = (String)en.next();
			IInteractionChange retElt = getInteractionChange(elt);
			if(retElt != null){
				v.addElement(retElt);
			}
		}
		return v;
	}	
	
	public void addStatechartChange(IStatechartChange newStatechartChange){
		if(!(newStatechartChange instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		Element newChildElt = (Element)(((DOMBased)newStatechartChange).getDOMNode());
		newChildElt = DOMUtils.cloneAndRename(newChildElt, ChangesConstants.NS_URI, STATECHART_CHANGE_ELT_NAME);
		((DOMBased)newStatechartChange).setDOMNode(newChildElt);

		synchronized(DOMUtils.getDOMLock(elt)){
			elt.appendChild(newChildElt);
			DOMUtils.order(elt, getSequenceOrder());
		}

		IXArch context = getXArch();
		if(context != null){
			context.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.ADD_EVENT,
				XArchEvent.ELEMENT_CHANGED,
				"statechartChange", newStatechartChange,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
		
	public void addStatechartChanges(Collection statechartChanges){
		for(Iterator en = statechartChanges.iterator(); en.hasNext(); ){
			IStatechartChange elt = (IStatechartChange)en.next();
			addStatechartChange(elt);
		}
	}		
		
	public void clearStatechartChanges(){
		//DOMUtils.removeChildren(elt, ChangesConstants.NS_URI, STATECHART_CHANGE_ELT_NAME);
		Collection coll = getAllStatechartChanges();
		removeStatechartChanges(coll);
	}
	
	public void removeStatechartChange(IStatechartChange statechartChangeToRemove){
		if(!(statechartChangeToRemove instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		NodeList nl = DOMUtils.getChildren(elt, ChangesConstants.NS_URI, STATECHART_CHANGE_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			if(n == ((DOMBased)statechartChangeToRemove).getDOMNode()){
				synchronized(DOMUtils.getDOMLock(elt)){
					elt.removeChild(n);
				}

				IXArch context = getXArch();
				if(context != null){
					context.fireXArchEvent(
						new XArchEvent(this, 
						XArchEvent.REMOVE_EVENT,
						XArchEvent.ELEMENT_CHANGED,
						"statechartChange", statechartChangeToRemove,
						XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
					);
				}

				return;
			}
		}
	}
	
	public void removeStatechartChanges(Collection statechartChanges){
		for(Iterator en = statechartChanges.iterator(); en.hasNext(); ){
			IStatechartChange elt = (IStatechartChange)en.next();
			removeStatechartChange(elt);
		}
	}
	
	public Collection getAllStatechartChanges(){
		NodeList nl = DOMUtils.getChildren(elt, ChangesConstants.NS_URI, STATECHART_CHANGE_ELT_NAME);
		int nlLength = nl.getLength();
		ArrayList v = new ArrayList(nlLength);
		IXArch de = getXArch();
		for(int i = 0; i < nlLength; i++){
			Element el = (Element)nl.item(i);
			boolean found = false;
			if(de != null){
				IXArchElement cachedXArchElt = de.getWrapper(el);
				if(cachedXArchElt != null){
					v.add((IStatechartChange)cachedXArchElt);
					found = true;
				}
			}
			if(!found){
				Object o = makeDerivedWrapper(el, "StatechartChange");	
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)o));
						}
						v.add((IStatechartChange)o);
					}
					catch(Exception e){
						StatechartChangeImpl eltImpl = new StatechartChangeImpl((Element)nl.item(i));
						eltImpl.setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
						}
						v.add(eltImpl);
					}
				}
				else{
					StatechartChangeImpl eltImpl = new StatechartChangeImpl((Element)nl.item(i));
					eltImpl.setXArch(getXArch());
					if(de != null){
						de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
					}
					v.add(eltImpl);
				}
			}
		}
		return v;
	}

	public boolean hasStatechartChange(IStatechartChange statechartChangeToCheck){
		Collection c = getAllStatechartChanges();
		
		for(Iterator en = c.iterator(); en.hasNext(); ){
			IStatechartChange elt = (IStatechartChange)en.next();
			if(elt.isEquivalent(statechartChangeToCheck)){
				return true;
			}
		}
		return false;
	}
	
	public Collection hasStatechartChanges(Collection statechartChangesToCheck){
		Vector v = new Vector();
		for(Iterator en = statechartChangesToCheck.iterator(); en.hasNext(); ){
			IStatechartChange elt = (IStatechartChange)en.next();
			v.addElement(new Boolean(hasStatechartChange(elt)));
		}
		return v;
	}
		
	public boolean hasAllStatechartChanges(Collection statechartChangesToCheck){
		for(Iterator en = statechartChangesToCheck.iterator(); en.hasNext(); ){
			IStatechartChange elt = (IStatechartChange)en.next();
			if(!hasStatechartChange(elt)){
				return false;
			}
		}
		return true;
	}
	public IStatechartChange getStatechartChange(String id){
		NodeList nl = DOMUtils.getChildren(elt, ChangesConstants.NS_URI, STATECHART_CHANGE_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			
			IStatechartChange el = new StatechartChangeImpl((Element)nl.item(i));
			if(DOMUtils.objNullEq(id, el.getId())){
				Element domElt = (Element)nl.item(i);
				Object o = makeDerivedWrapper(domElt, "StatechartChange");
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						return (IStatechartChange)o;
					}
					catch(Exception e){}
				}
				el.setXArch(getXArch());
				return el;
			}
		}
		return null;
	}
	
	public Collection getStatechartChanges(Collection ids){
		//If there is an ID that does not exist, it is simply ignored.
		//You can tell if this happened if ids.size() != returned collection.size().
		Vector v = new Vector();
		for(Iterator en = ids.iterator(); en.hasNext(); ){
			String elt = (String)en.next();
			IStatechartChange retElt = getStatechartChange(elt);
			if(retElt != null){
				v.addElement(retElt);
			}
		}
		return v;
	}	
	
	public boolean isEqual(IChanges ChangesToCheck){
		String thisId = getId();
		String thatId = ChangesToCheck.getId();
		
		if((thisId == null) || (thatId == null)){
			throw new IllegalArgumentException("One of the arguments is missing an ID.");
		}
		return thisId.equals(thatId);
	}
	
	public boolean isEquivalent(IChanges c){
		return (getClass().equals(c.getClass())) &&
		hasStatus(c.getStatus()) &&
			hasDescription(c.getDescription()) &&
			hasAllComponentChanges(c.getAllComponentChanges()) &&
			c.hasAllComponentChanges(getAllComponentChanges()) &&
			hasAllLinkChanges(c.getAllLinkChanges()) &&
			c.hasAllLinkChanges(getAllLinkChanges()) &&
			hasAllInteractionChanges(c.getAllInteractionChanges()) &&
			c.hasAllInteractionChanges(getAllInteractionChanges()) &&
			hasAllStatechartChanges(c.getAllStatechartChanges()) &&
			c.hasAllStatechartChanges(getAllStatechartChanges()) ;
	}

}
