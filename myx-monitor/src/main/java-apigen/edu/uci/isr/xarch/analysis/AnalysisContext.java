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
package edu.uci.isr.xarch.analysis;

import java.util.*;

import edu.uci.isr.xarch.*;

import org.w3c.dom.*;

import edu.uci.isr.xarch.IXArch;
import edu.uci.isr.xarch.IXArchContext;

/**
 * The context object for the analysis package.
 * This object is used to create objects that are used
 * in the analysis namespace.
 *
 * @author Automatically Generated by xArch apigen
 */
public class AnalysisContext implements IAnalysisContext {

	protected static final String DEFAULT_ELT_NAME = "anonymousInstanceTag";
	protected Document doc;
	protected IXArch xArch;

	/**
	 * Create a new AnalysisContext for the given
	 * IXArch object.
	 * @param xArch XArch object to contextualize in this namespace.
	 */
	public AnalysisContext(IXArch xArch){
		if(!(xArch instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Node docRootNode = ((DOMBased)xArch).getDOMNode();
		synchronized(DOMUtils.getDOMLock(docRootNode)){
			this.doc = docRootNode.getOwnerDocument();
			xArch.addSchemaLocation("http://www.ics.uci.edu/pub/arch/xArch/analysis.xsd", "http://www.isr.uci.edu/projects/xarchuci/ext/analysis.xsd");
			this.xArch = xArch;
		}
	}

	public IXArch getXArch(){
		return xArch;
	}
	
	protected Element createElement(String name){
		synchronized(DOMUtils.getDOMLock(doc)){
			return doc.createElementNS(AnalysisConstants.NS_URI, name);
		}
	}

	public XArchTypeMetadata getTypeMetadata(){
		return IAnalysisContext.TYPE_METADATA;
	}
	/**
	 * Create an IArchAnalysis object in this namespace.
	 * @return New IArchAnalysis object.
	 */
	public IArchAnalysis createArchAnalysis(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, ArchAnalysisImpl.XSD_TYPE_NSURI, ArchAnalysisImpl.XSD_TYPE_NAME);
		ArchAnalysisImpl newElt = new ArchAnalysisImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IArchAnalysis object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IArchAnalysis recontextualizeArchAnalysis(IArchAnalysis value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, AnalysisConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, AnalysisConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create an IAnalysis object in this namespace.
	 * @return New IAnalysis object.
	 */
	public IAnalysis createAnalysis(){
		Element elt = createElement(DEFAULT_ELT_NAME);
		DOMUtils.addXSIType(elt, AnalysisImpl.XSD_TYPE_NSURI, AnalysisImpl.XSD_TYPE_NAME);
		AnalysisImpl newElt = new AnalysisImpl(elt);
		newElt.setXArch(this.getXArch());
		return newElt;
	}

	/**
	 * Brings an IAnalysis object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IAnalysis recontextualizeAnalysis(IAnalysis value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Element elt = (Element)((DOMBased)value).getDOMNode();

		elt = DOMUtils.cloneAndRename(elt, doc, AnalysisConstants.NS_URI, elt.getLocalName());
		//elt = DOMUtils.cloneAndRename(elt, AnalysisConstants.NS_URI, elt.getTagName());

		//Removed next line because it causes an illegal character DOM exception
		//elt.setPrefix(null);

		((DOMBased)value).setDOMNode(elt);
		((IXArchElement)value).setXArch(this.getXArch());
		return value;
	}

	/**
	 * Create a top-level element of type <code>IArchAnalysis</code>.
	 * This function should be used in lieu of <code>createArchAnalysis</code>
	 * if the element is to be added as a sub-object of <code>IXArch</code>.
	 * @return new IArchAnalysis suitable for adding
	 * as a child of xArch.
	 */
	public IArchAnalysis createArchAnalysisElement(){
		Element elt = createElement("archAnalysis");
		DOMUtils.addXSIType(elt, ArchAnalysisImpl.XSD_TYPE_NSURI, 
			ArchAnalysisImpl.XSD_TYPE_NAME);
		ArchAnalysisImpl newElt = new ArchAnalysisImpl(elt);

		IXArch de = getXArch();
		newElt.setXArch(de);
		return newElt;
	}

	/**
	 * Gets the IArchAnalysis child from the given <code>IXArch</code>
	 * element.  If there are multiple matching children, this returns the first one.
	 * @param xArch <code>IXArch</code> object from which to get the child.
	 * @return <code>IArchAnalysis</code> that is the child
	 * of <code>xArch</code> or <code>null</code> if no such object exists.
	 */
	public IArchAnalysis getArchAnalysis(IXArch xArch){
		if(!(xArch instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Collection elementCollection = xArch.getAllObjects();
		for(Iterator en = elementCollection.iterator(); en.hasNext(); ){
			Object o = en.next();
			if(o instanceof IArchAnalysis){
				return (IArchAnalysis)o;
			}
			else if(o instanceof Element){
				Element elt = (Element)o;
				synchronized(DOMUtils.getDOMLock(elt)){
					String nsURI = elt.getNamespaceURI();
					String localName = elt.getLocalName();
					if((nsURI != null) && (nsURI.equals(AnalysisConstants.NS_URI))){
						if((localName != null) && (localName.equals("archAnalysis"))){
							ArchAnalysisImpl newElt = new ArchAnalysisImpl(elt);
							newElt.setXArch(this.getXArch());
							return newElt;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets all the IArchAnalysis children from the given 
	 * <code>IXArch</code> element.
	 * @param xArch <code>IXArch</code> object from which to get the children.
	 * @return Collection of <code>IArchAnalysis</code> that are 	
	 * the children of <code>xArch</code> or an empty collection if no such object exists.
	 */
	public Collection getAllArchAnalysiss(IXArch xArch){
		if(!(xArch instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot process non-DOM based xArch entities.");
		}
		Collection elementCollection = xArch.getAllObjects();
		Vector v = new Vector();

		for(Iterator en = elementCollection.iterator(); en.hasNext(); ){
			Object o = en.next();
			if(o instanceof IArchAnalysis){
				v.addElement(o);
			}
			else if(o instanceof Element){
				Element elt = (Element)o;
				synchronized(DOMUtils.getDOMLock(elt)){
					String nsURI = elt.getNamespaceURI();
					String localName = elt.getLocalName();
					if((nsURI != null) && (nsURI.equals(AnalysisConstants.NS_URI))){
						if((localName != null) && (localName.equals("archAnalysis"))){
							ArchAnalysisImpl newElt = new ArchAnalysisImpl(elt);
							newElt.setXArch(this.getXArch());
							v.addElement(newElt);
						}
					}
				}
			}
		}
		return v;
	}

}

