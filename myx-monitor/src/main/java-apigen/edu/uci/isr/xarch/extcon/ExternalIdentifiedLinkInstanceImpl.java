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
package edu.uci.isr.xarch.extcon;

import org.w3c.dom.*;

import edu.uci.isr.xarch.*;

import java.util.*;

/**
 * DOM-Based implementation of the IExternalIdentifiedLinkInstance interface.
 *
 * @author Automatically generated by xArch apigen.
 */
public class ExternalIdentifiedLinkInstanceImpl extends edu.uci.isr.xarch.instance.LinkInstanceImpl implements
IExternalIdentifiedLinkInstance, edu.uci.isr.xarch.instance.ILinkInstance, DOMBased{
	
	public static final String XSD_TYPE_NSURI = ExtconConstants.NS_URI;
	public static final String XSD_TYPE_NAME = "ExternalIdentifiedLinkInstance";

	/** Tag name for extIds in this object. */
	public static final String EXT_ID_ATTR_NAME = "extId";

	
	private static SequenceOrder seqOrderAppend = new SequenceOrder(
		new QName[]{
		}
	);
	
	public ExternalIdentifiedLinkInstanceImpl(Element elt){
		super(elt);
	}
	
	protected static SequenceOrder getSequenceOrder(){
		return new SequenceOrder(edu.uci.isr.xarch.instance.LinkInstanceImpl.getSequenceOrder(), seqOrderAppend);
	}

	public IXArchElement cloneElement(int depth){
		synchronized(DOMUtils.getDOMLock(elt)){
			Document doc = elt.getOwnerDocument();
			if(depth == 0){
				Element cloneElt = (Element)elt.cloneNode(false);
				cloneElt = (Element)doc.importNode(cloneElt, true);
				ExternalIdentifiedLinkInstanceImpl cloneImpl = new ExternalIdentifiedLinkInstanceImpl(cloneElt);
				cloneImpl.setXArch(getXArch());
				return cloneImpl;
			}
			else if(depth == 1){
				Element cloneElt = (Element)elt.cloneNode(false);
				cloneElt = (Element)doc.importNode(cloneElt, true);
				ExternalIdentifiedLinkInstanceImpl cloneImpl = new ExternalIdentifiedLinkInstanceImpl(cloneElt);
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
				ExternalIdentifiedLinkInstanceImpl cloneImpl = new ExternalIdentifiedLinkInstanceImpl(cloneElt);
				cloneImpl.setXArch(getXArch());
				return cloneImpl;
			}
		}
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
				if(!DOMUtils.hasXSIType(elt, "http://www.ics.uci.edu/pub/arch/xArch/extcon.xsd", baseTypeName)){
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
		return IExternalIdentifiedLinkInstance.TYPE_METADATA;
	}

	public XArchInstanceMetadata getInstanceMetadata(){
		return new XArchInstanceMetadata(XArchUtils.getPackageTitle(elt.getNamespaceURI()));
	}
	/**
	 * Set the extId attribute on this object.
	 * @param extId attribute value.
	 */
	public void setExtId(String extId){
		{
			String oldValue = getExtId();
			if(oldValue == null ? extId == null : oldValue.equals(extId))
				return;
			DOMUtils.removeAttribute(elt, ExtconConstants.NS_URI, EXT_ID_ATTR_NAME);
			IXArch _x = getXArch();
			if(_x != null){
				_x.fireXArchEvent(
					new XArchEvent(this, 
					XArchEvent.CLEAR_EVENT,
					XArchEvent.ATTRIBUTE_CHANGED,
					"extId", oldValue,
					XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this), true)
				);
			}
		}
		DOMUtils.setAttribute(elt, ExtconConstants.NS_URI, EXT_ID_ATTR_NAME, extId);
		IXArch _x = getXArch();
		if(_x != null){
			_x.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.SET_EVENT,
				XArchEvent.ATTRIBUTE_CHANGED,
				"extId", extId,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
		
	/**
	 * Removes the extId attribute from this object.
	 */
	public void clearExtId(){
		String oldValue = getExtId();
		if(oldValue == null)
			return;
		DOMUtils.removeAttribute(elt, ExtconConstants.NS_URI, EXT_ID_ATTR_NAME);
		IXArch _x = getXArch();
		if(_x != null){
			_x.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.CLEAR_EVENT,
				XArchEvent.ATTRIBUTE_CHANGED,
				"extId", oldValue,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
	
	/**
	 * Gets the value of the extId attribute on this object.
	 * @return extId attribute's value or <code>null</code> if that
	 * attribute is not set.
	 */
	public String getExtId(){
		return DOMUtils.getAttributeValue(elt, ExtconConstants.NS_URI, EXT_ID_ATTR_NAME);
	}
	
	/**
	 * Determines if this object's extId attribute has the
	 * given value.
	 * @param extId value to test.
	 * @return <code>true</code> if the values match, <code>false</code> otherwise.
	 * Matching is done by string-matching.
	 */
	public boolean hasExtId(String extId){
		return DOMUtils.objNullEq(getExtId(), extId);
	}

	public boolean isEquivalent(IExternalIdentifiedLinkInstance c){
		return
			super.isEquivalent(c) &&
		hasExtId(c.getExtId()) ;
	}

}
