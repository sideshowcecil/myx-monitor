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
package edu.uci.isr.xarch.typesmapping;

import java.util.*;

import edu.uci.isr.xarch.*;

import org.w3c.dom.*;

import edu.uci.isr.xarch.IXArch;
import edu.uci.isr.xarch.IXArchContext;

/**
 * The context object for the typesmapping package.
 * This object is used to create objects that are used
 * in the typesmapping namespace.
 *
 * @author Automatically Generated by xArch apigen
 */
public class TypesmappingContext implements ITypesmappingContext {

	protected static final String DEFAULT_ELT_NAME = "anonymousInstanceTag";
	protected Document doc;
	protected IXArch xArch;

	/**
	 * Create a new TypesmappingContext for the given
	 * IXArch object.
	 * @param xArch XArch object to contextualize in this namespace.
	 */
	public TypesmappingContext(IXArch xArch){
		if(!(xArch instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Node docRootNode = ((DOMBased)xArch).getDOMNode();
		synchronized(DOMUtils.getDOMLock(docRootNode)){
			this.doc = docRootNode.getOwnerDocument();
			xArch.addSchemaLocation("http://www.ics.uci.edu/pub/arch/xArch/typesmapping.xsd", "http://www.ics.uci.edu/pub/arch/xArch/typesmapping.xsd");
			this.xArch = xArch;
		}
	}

	public IXArch getXArch(){
		return xArch;
	}
	
	protected Element createElement(String name){
		synchronized(DOMUtils.getDOMLock(doc)){
			return doc.createElementNS(TypesmappingConstants.NS_URI, name);
		}
	}

	public XArchTypeMetadata getTypeMetadata(){
		return ITypesmappingContext.TYPE_METADATA;
	}
	/**
	 * Create an IMappedComponent object in this namespace.
	 * @return New IMappedComponent object.
	 */
	public IMappedComponent createMappedComponent(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, MappedComponentImpl.XSD_TYPE_NSURI, MappedComponentImpl.XSD_TYPE_NAME);
		MappedComponentImpl newElt = new MappedComponentImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IMappedComponent object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IMappedComponent recontextualizeMappedComponent(IMappedComponent value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, TypesmappingConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, TypesmappingConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.types.IComponent</code>
	 * to one of type <code>IMappedComponent</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.types.IComponent</code>
	 * object wraps a DOM element that is the base type, then the 
	 * <code>xsi:type</code> of the base element is promoted.  Otherwise, 
	 * it is left unchanged.
	 *
	 * This function also emits an <CODE>XArchEvent</CODE> with type
	 * PROMOTE_TYPE.  The source for this events is the pre-promoted
	 * IXArchElement object (should no longer be used), and the
	 * target is the post-promotion object.  The target name is
	 * the name of the interface class that was the target of the
	 * promotion.
	 * 
	 * @param value Object to promote.
	 * @return Promoted object.
	 */
	public IMappedComponent promoteToMappedComponent(
	edu.uci.isr.xarch.types.IComponent value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		if(DOMUtils.hasXSIType(elt, 
			edu.uci.isr.xarch.types.ComponentImpl.XSD_TYPE_NSURI,
			edu.uci.isr.xarch.types.ComponentImpl.XSD_TYPE_NAME)){

				DOMUtils.addXSIType(elt, MappedComponentImpl.XSD_TYPE_NSURI, 
					MappedComponentImpl.XSD_TYPE_NAME);
		}
		MappedComponentImpl newElt = new MappedComponentImpl(elt);
		newElt.setXArch(this.getXArch());

		xArch.fireXArchEvent(
			new XArchEvent(value, 
			XArchEvent.PROMOTE_EVENT,
			XArchEvent.ELEMENT_CHANGED,
			IMappedComponent.class.getName(), newElt,
			XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, newElt))
		);

		return newElt;
	}

	/**
	 * Create an edu.uci.isr.xarch.instance.IXMLLink object in this namespace.
	 * @return New edu.uci.isr.xarch.instance.IXMLLink object.
	 */
	public edu.uci.isr.xarch.instance.IXMLLink createXMLLink(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, edu.uci.isr.xarch.instance.XMLLinkImpl.XSD_TYPE_NSURI, edu.uci.isr.xarch.instance.XMLLinkImpl.XSD_TYPE_NAME);
		edu.uci.isr.xarch.instance.XMLLinkImpl newElt = new edu.uci.isr.xarch.instance.XMLLinkImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an edu.uci.isr.xarch.instance.IXMLLink object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public edu.uci.isr.xarch.instance.IXMLLink recontextualizeXMLLink(edu.uci.isr.xarch.instance.IXMLLink value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, TypesmappingConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, TypesmappingConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IMappedConnector object in this namespace.
	 * @return New IMappedConnector object.
	 */
	public IMappedConnector createMappedConnector(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, MappedConnectorImpl.XSD_TYPE_NSURI, MappedConnectorImpl.XSD_TYPE_NAME);
		MappedConnectorImpl newElt = new MappedConnectorImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IMappedConnector object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IMappedConnector recontextualizeMappedConnector(IMappedConnector value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, TypesmappingConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, TypesmappingConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.types.IConnector</code>
	 * to one of type <code>IMappedConnector</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.types.IConnector</code>
	 * object wraps a DOM element that is the base type, then the 
	 * <code>xsi:type</code> of the base element is promoted.  Otherwise, 
	 * it is left unchanged.
	 *
	 * This function also emits an <CODE>XArchEvent</CODE> with type
	 * PROMOTE_TYPE.  The source for this events is the pre-promoted
	 * IXArchElement object (should no longer be used), and the
	 * target is the post-promotion object.  The target name is
	 * the name of the interface class that was the target of the
	 * promotion.
	 * 
	 * @param value Object to promote.
	 * @return Promoted object.
	 */
	public IMappedConnector promoteToMappedConnector(
	edu.uci.isr.xarch.types.IConnector value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		if(DOMUtils.hasXSIType(elt, 
			edu.uci.isr.xarch.types.ConnectorImpl.XSD_TYPE_NSURI,
			edu.uci.isr.xarch.types.ConnectorImpl.XSD_TYPE_NAME)){

				DOMUtils.addXSIType(elt, MappedConnectorImpl.XSD_TYPE_NSURI, 
					MappedConnectorImpl.XSD_TYPE_NAME);
		}
		MappedConnectorImpl newElt = new MappedConnectorImpl(elt);
		newElt.setXArch(this.getXArch());

		xArch.fireXArchEvent(
			new XArchEvent(value, 
			XArchEvent.PROMOTE_EVENT,
			XArchEvent.ELEMENT_CHANGED,
			IMappedConnector.class.getName(), newElt,
			XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, newElt))
		);

		return newElt;
	}

	/**
	 * Create an IMappedInterface object in this namespace.
	 * @return New IMappedInterface object.
	 */
	public IMappedInterface createMappedInterface(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, MappedInterfaceImpl.XSD_TYPE_NSURI, MappedInterfaceImpl.XSD_TYPE_NAME);
		MappedInterfaceImpl newElt = new MappedInterfaceImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IMappedInterface object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IMappedInterface recontextualizeMappedInterface(IMappedInterface value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, TypesmappingConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, TypesmappingConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.types.IInterface</code>
	 * to one of type <code>IMappedInterface</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.types.IInterface</code>
	 * object wraps a DOM element that is the base type, then the 
	 * <code>xsi:type</code> of the base element is promoted.  Otherwise, 
	 * it is left unchanged.
	 *
	 * This function also emits an <CODE>XArchEvent</CODE> with type
	 * PROMOTE_TYPE.  The source for this events is the pre-promoted
	 * IXArchElement object (should no longer be used), and the
	 * target is the post-promotion object.  The target name is
	 * the name of the interface class that was the target of the
	 * promotion.
	 * 
	 * @param value Object to promote.
	 * @return Promoted object.
	 */
	public IMappedInterface promoteToMappedInterface(
	edu.uci.isr.xarch.types.IInterface value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		if(DOMUtils.hasXSIType(elt, 
			edu.uci.isr.xarch.types.InterfaceImpl.XSD_TYPE_NSURI,
			edu.uci.isr.xarch.types.InterfaceImpl.XSD_TYPE_NAME)){

				DOMUtils.addXSIType(elt, MappedInterfaceImpl.XSD_TYPE_NSURI, 
					MappedInterfaceImpl.XSD_TYPE_NAME);
		}
		MappedInterfaceImpl newElt = new MappedInterfaceImpl(elt);
		newElt.setXArch(this.getXArch());

		xArch.fireXArchEvent(
			new XArchEvent(value, 
			XArchEvent.PROMOTE_EVENT,
			XArchEvent.ELEMENT_CHANGED,
			IMappedInterface.class.getName(), newElt,
			XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, newElt))
		);

		return newElt;
	}


}

