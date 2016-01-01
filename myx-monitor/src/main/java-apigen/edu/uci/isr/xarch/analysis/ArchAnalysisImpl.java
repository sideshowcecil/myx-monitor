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

import org.w3c.dom.*;

import edu.uci.isr.xarch.*;

import java.util.*;

/**
 * DOM-Based implementation of the IArchAnalysis interface.
 *
 * @author Automatically generated by xArch apigen.
 */
public class ArchAnalysisImpl implements IArchAnalysis, DOMBased{
	
	public static final String XSD_TYPE_NSURI = AnalysisConstants.NS_URI;
	public static final String XSD_TYPE_NAME = "ArchAnalysis";
	
	protected IXArch xArch;
	
	/** Tag name for analysiss in this object. */
	public static final String ANALYSIS_ELT_NAME = "analysis";

	
	protected Element elt;
	
	private static SequenceOrder seqOrd = new SequenceOrder(
		new QName[]{
			new QName(AnalysisConstants.NS_URI, ANALYSIS_ELT_NAME)
		}
	);
	
	public ArchAnalysisImpl(Element elt){
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
				ArchAnalysisImpl cloneImpl = new ArchAnalysisImpl(cloneElt);
				cloneImpl.setXArch(getXArch());
				return cloneImpl;
			}
			else if(depth == 1){
				Element cloneElt = (Element)elt.cloneNode(false);
				cloneElt = (Element)doc.importNode(cloneElt, true);
				ArchAnalysisImpl cloneImpl = new ArchAnalysisImpl(cloneElt);
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
				ArchAnalysisImpl cloneImpl = new ArchAnalysisImpl(cloneElt);
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
				if(!DOMUtils.hasXSIType(elt, "http://www.ics.uci.edu/pub/arch/xArch/analysis.xsd", baseTypeName)){
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
		return IArchAnalysis.TYPE_METADATA;
	}

	public XArchInstanceMetadata getInstanceMetadata(){
		return new XArchInstanceMetadata(XArchUtils.getPackageTitle(elt.getNamespaceURI()));
	}
	public void addAnalysis(IAnalysis newAnalysis){
		if(!(newAnalysis instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		Element newChildElt = (Element)(((DOMBased)newAnalysis).getDOMNode());
		newChildElt = DOMUtils.cloneAndRename(newChildElt, AnalysisConstants.NS_URI, ANALYSIS_ELT_NAME);
		((DOMBased)newAnalysis).setDOMNode(newChildElt);

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
				"analysis", newAnalysis,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
		
	public void addAnalysiss(Collection analysiss){
		for(Iterator en = analysiss.iterator(); en.hasNext(); ){
			IAnalysis elt = (IAnalysis)en.next();
			addAnalysis(elt);
		}
	}		
		
	public void clearAnalysiss(){
		//DOMUtils.removeChildren(elt, AnalysisConstants.NS_URI, ANALYSIS_ELT_NAME);
		Collection coll = getAllAnalysiss();
		removeAnalysiss(coll);
	}
	
	public void removeAnalysis(IAnalysis analysisToRemove){
		if(!(analysisToRemove instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		NodeList nl = DOMUtils.getChildren(elt, AnalysisConstants.NS_URI, ANALYSIS_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			if(n == ((DOMBased)analysisToRemove).getDOMNode()){
				synchronized(DOMUtils.getDOMLock(elt)){
					elt.removeChild(n);
				}

				IXArch context = getXArch();
				if(context != null){
					context.fireXArchEvent(
						new XArchEvent(this, 
						XArchEvent.REMOVE_EVENT,
						XArchEvent.ELEMENT_CHANGED,
						"analysis", analysisToRemove,
						XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
					);
				}

				return;
			}
		}
	}
	
	public void removeAnalysiss(Collection analysiss){
		for(Iterator en = analysiss.iterator(); en.hasNext(); ){
			IAnalysis elt = (IAnalysis)en.next();
			removeAnalysis(elt);
		}
	}
	
	public Collection getAllAnalysiss(){
		NodeList nl = DOMUtils.getChildren(elt, AnalysisConstants.NS_URI, ANALYSIS_ELT_NAME);
		int nlLength = nl.getLength();
		ArrayList v = new ArrayList(nlLength);
		IXArch de = getXArch();
		for(int i = 0; i < nlLength; i++){
			Element el = (Element)nl.item(i);
			boolean found = false;
			if(de != null){
				IXArchElement cachedXArchElt = de.getWrapper(el);
				if(cachedXArchElt != null){
					v.add((IAnalysis)cachedXArchElt);
					found = true;
				}
			}
			if(!found){
				Object o = makeDerivedWrapper(el, "Analysis");	
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)o));
						}
						v.add((IAnalysis)o);
					}
					catch(Exception e){
						AnalysisImpl eltImpl = new AnalysisImpl((Element)nl.item(i));
						eltImpl.setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
						}
						v.add(eltImpl);
					}
				}
				else{
					AnalysisImpl eltImpl = new AnalysisImpl((Element)nl.item(i));
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

	public boolean hasAnalysis(IAnalysis analysisToCheck){
		Collection c = getAllAnalysiss();
		
		for(Iterator en = c.iterator(); en.hasNext(); ){
			IAnalysis elt = (IAnalysis)en.next();
			if(elt.isEquivalent(analysisToCheck)){
				return true;
			}
		}
		return false;
	}
	
	public Collection hasAnalysiss(Collection analysissToCheck){
		Vector v = new Vector();
		for(Iterator en = analysissToCheck.iterator(); en.hasNext(); ){
			IAnalysis elt = (IAnalysis)en.next();
			v.addElement(new Boolean(hasAnalysis(elt)));
		}
		return v;
	}
		
	public boolean hasAllAnalysiss(Collection analysissToCheck){
		for(Iterator en = analysissToCheck.iterator(); en.hasNext(); ){
			IAnalysis elt = (IAnalysis)en.next();
			if(!hasAnalysis(elt)){
				return false;
			}
		}
		return true;
	}
	public boolean isEquivalent(IArchAnalysis c){
		return (getClass().equals(c.getClass())) &&
			hasAllAnalysiss(c.getAllAnalysiss()) &&
			c.hasAllAnalysiss(getAllAnalysiss()) ;
	}

}
