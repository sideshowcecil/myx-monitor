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
package edu.uci.isr.xarch.hostproperty;

import org.w3c.dom.*;

import edu.uci.isr.xarch.*;

import java.util.*;

/**
 * DOM-Based implementation of the IHostedArchStructure interface.
 *
 * @author Automatically generated by xArch apigen.
 */
public class HostedArchStructureImpl extends edu.uci.isr.xarch.types.ArchStructureImpl implements
IHostedArchStructure, edu.uci.isr.xarch.types.IArchStructure, DOMBased{
	
	public static final String XSD_TYPE_NSURI = HostpropertyConstants.NS_URI;
	public static final String XSD_TYPE_NAME = "HostedArchStructure";

	/** Tag name for hosts in this object. */
	public static final String HOST_ELT_NAME = "host";

	
	private static SequenceOrder seqOrderAppend = new SequenceOrder(
		new QName[]{
			new QName(HostpropertyConstants.NS_URI, HOST_ELT_NAME)
		}
	);
	
	public HostedArchStructureImpl(Element elt){
		super(elt);
	}
	
	protected static SequenceOrder getSequenceOrder(){
		return new SequenceOrder(edu.uci.isr.xarch.types.ArchStructureImpl.getSequenceOrder(), seqOrderAppend);
	}

	public IXArchElement cloneElement(int depth){
		synchronized(DOMUtils.getDOMLock(elt)){
			Document doc = elt.getOwnerDocument();
			if(depth == 0){
				Element cloneElt = (Element)elt.cloneNode(false);
				cloneElt = (Element)doc.importNode(cloneElt, true);
				HostedArchStructureImpl cloneImpl = new HostedArchStructureImpl(cloneElt);
				cloneImpl.setXArch(getXArch());
				return cloneImpl;
			}
			else if(depth == 1){
				Element cloneElt = (Element)elt.cloneNode(false);
				cloneElt = (Element)doc.importNode(cloneElt, true);
				HostedArchStructureImpl cloneImpl = new HostedArchStructureImpl(cloneElt);
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
				HostedArchStructureImpl cloneImpl = new HostedArchStructureImpl(cloneElt);
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
				if(!DOMUtils.hasXSIType(elt, "http://www.ics.uci.edu/pub/arch/xArch/hostproperty.xsd", baseTypeName)){
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
		return IHostedArchStructure.TYPE_METADATA;
	}

	public XArchInstanceMetadata getInstanceMetadata(){
		return new XArchInstanceMetadata(XArchUtils.getPackageTitle(elt.getNamespaceURI()));
	}
	public void addHost(IHost newHost){
		if(!(newHost instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		Element newChildElt = (Element)(((DOMBased)newHost).getDOMNode());
		newChildElt = DOMUtils.cloneAndRename(newChildElt, HostpropertyConstants.NS_URI, HOST_ELT_NAME);
		((DOMBased)newHost).setDOMNode(newChildElt);

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
				"host", newHost,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
		
	public void addHosts(Collection hosts){
		for(Iterator en = hosts.iterator(); en.hasNext(); ){
			IHost elt = (IHost)en.next();
			addHost(elt);
		}
	}		
		
	public void clearHosts(){
		//DOMUtils.removeChildren(elt, HostpropertyConstants.NS_URI, HOST_ELT_NAME);
		Collection coll = getAllHosts();
		removeHosts(coll);
	}
	
	public void removeHost(IHost hostToRemove){
		if(!(hostToRemove instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		NodeList nl = DOMUtils.getChildren(elt, HostpropertyConstants.NS_URI, HOST_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			if(n == ((DOMBased)hostToRemove).getDOMNode()){
				synchronized(DOMUtils.getDOMLock(elt)){
					elt.removeChild(n);
				}

				IXArch context = getXArch();
				if(context != null){
					context.fireXArchEvent(
						new XArchEvent(this, 
						XArchEvent.REMOVE_EVENT,
						XArchEvent.ELEMENT_CHANGED,
						"host", hostToRemove,
						XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
					);
				}

				return;
			}
		}
	}
	
	public void removeHosts(Collection hosts){
		for(Iterator en = hosts.iterator(); en.hasNext(); ){
			IHost elt = (IHost)en.next();
			removeHost(elt);
		}
	}
	
	public Collection getAllHosts(){
		NodeList nl = DOMUtils.getChildren(elt, HostpropertyConstants.NS_URI, HOST_ELT_NAME);
		int nlLength = nl.getLength();
		ArrayList v = new ArrayList(nlLength);
		IXArch de = getXArch();
		for(int i = 0; i < nlLength; i++){
			Element el = (Element)nl.item(i);
			boolean found = false;
			if(de != null){
				IXArchElement cachedXArchElt = de.getWrapper(el);
				if(cachedXArchElt != null){
					v.add((IHost)cachedXArchElt);
					found = true;
				}
			}
			if(!found){
				Object o = makeDerivedWrapper(el, "Host");	
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)o));
						}
						v.add((IHost)o);
					}
					catch(Exception e){
						HostImpl eltImpl = new HostImpl((Element)nl.item(i));
						eltImpl.setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
						}
						v.add(eltImpl);
					}
				}
				else{
					HostImpl eltImpl = new HostImpl((Element)nl.item(i));
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

	public boolean hasHost(IHost hostToCheck){
		Collection c = getAllHosts();
		
		for(Iterator en = c.iterator(); en.hasNext(); ){
			IHost elt = (IHost)en.next();
			if(elt.isEquivalent(hostToCheck)){
				return true;
			}
		}
		return false;
	}
	
	public Collection hasHosts(Collection hostsToCheck){
		Vector v = new Vector();
		for(Iterator en = hostsToCheck.iterator(); en.hasNext(); ){
			IHost elt = (IHost)en.next();
			v.addElement(new Boolean(hasHost(elt)));
		}
		return v;
	}
		
	public boolean hasAllHosts(Collection hostsToCheck){
		for(Iterator en = hostsToCheck.iterator(); en.hasNext(); ){
			IHost elt = (IHost)en.next();
			if(!hasHost(elt)){
				return false;
			}
		}
		return true;
	}
	public IHost getHost(String id){
		NodeList nl = DOMUtils.getChildren(elt, HostpropertyConstants.NS_URI, HOST_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			
			IHost el = new HostImpl((Element)nl.item(i));
			if(DOMUtils.objNullEq(id, el.getId())){
				Element domElt = (Element)nl.item(i);
				Object o = makeDerivedWrapper(domElt, "Host");
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						return (IHost)o;
					}
					catch(Exception e){}
				}
				el.setXArch(getXArch());
				return el;
			}
		}
		return null;
	}
	
	public Collection getHosts(Collection ids){
		//If there is an ID that does not exist, it is simply ignored.
		//You can tell if this happened if ids.size() != returned collection.size().
		Vector v = new Vector();
		for(Iterator en = ids.iterator(); en.hasNext(); ){
			String elt = (String)en.next();
			IHost retElt = getHost(elt);
			if(retElt != null){
				v.addElement(retElt);
			}
		}
		return v;
	}	
	
	public boolean isEquivalent(IHostedArchStructure c){
		return
			super.isEquivalent(c) &&
			hasAllHosts(c.getAllHosts()) &&
			c.hasAllHosts(getAllHosts()) ;
	}

}
