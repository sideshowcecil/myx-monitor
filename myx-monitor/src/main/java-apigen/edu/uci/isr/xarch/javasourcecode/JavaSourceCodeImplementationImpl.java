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
package edu.uci.isr.xarch.javasourcecode;

import org.w3c.dom.*;

import edu.uci.isr.xarch.*;

import java.util.*;

/**
 * DOM-Based implementation of the IJavaSourceCodeImplementation interface.
 *
 * @author Automatically generated by xArch apigen.
 */
public class JavaSourceCodeImplementationImpl extends edu.uci.isr.xarch.implementation.ImplementationImpl implements
IJavaSourceCodeImplementation, edu.uci.isr.xarch.implementation.IImplementation, DOMBased{
	
	public static final String XSD_TYPE_NSURI = JavasourcecodeConstants.NS_URI;
	public static final String XSD_TYPE_NAME = "JavaSourceCodeImplementation";

	/** Tag name for sourceCodeFiles in this object. */
	public static final String SOURCE_CODE_FILE_ELT_NAME = "sourceCodeFile";
	/** Tag name for repositoryLocations in this object. */
	public static final String REPOSITORY_LOCATION_ELT_NAME = "repositoryLocation";
	/** Tag name for sourceCodeManagers in this object. */
	public static final String SOURCE_CODE_MANAGER_ELT_NAME = "sourceCodeManager";

	
	private static SequenceOrder seqOrderAppend = new SequenceOrder(
		new QName[]{
			new QName(JavasourcecodeConstants.NS_URI, SOURCE_CODE_FILE_ELT_NAME),
			new QName(JavasourcecodeConstants.NS_URI, REPOSITORY_LOCATION_ELT_NAME),
			new QName(JavasourcecodeConstants.NS_URI, SOURCE_CODE_MANAGER_ELT_NAME)
		}
	);
	
	public JavaSourceCodeImplementationImpl(Element elt){
		super(elt);
	}
	
	protected static SequenceOrder getSequenceOrder(){
		return new SequenceOrder(edu.uci.isr.xarch.implementation.ImplementationImpl.getSequenceOrder(), seqOrderAppend);
	}

	public IXArchElement cloneElement(int depth){
		synchronized(DOMUtils.getDOMLock(elt)){
			Document doc = elt.getOwnerDocument();
			if(depth == 0){
				Element cloneElt = (Element)elt.cloneNode(false);
				cloneElt = (Element)doc.importNode(cloneElt, true);
				JavaSourceCodeImplementationImpl cloneImpl = new JavaSourceCodeImplementationImpl(cloneElt);
				cloneImpl.setXArch(getXArch());
				return cloneImpl;
			}
			else if(depth == 1){
				Element cloneElt = (Element)elt.cloneNode(false);
				cloneElt = (Element)doc.importNode(cloneElt, true);
				JavaSourceCodeImplementationImpl cloneImpl = new JavaSourceCodeImplementationImpl(cloneElt);
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
				JavaSourceCodeImplementationImpl cloneImpl = new JavaSourceCodeImplementationImpl(cloneElt);
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
				if(!DOMUtils.hasXSIType(elt, "http://www.ics.uci.edu/pub/arch/xArch/javasourcecode.xsd", baseTypeName)){
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
		return IJavaSourceCodeImplementation.TYPE_METADATA;
	}

	public XArchInstanceMetadata getInstanceMetadata(){
		return new XArchInstanceMetadata(XArchUtils.getPackageTitle(elt.getNamespaceURI()));
	}
	public void addSourceCodeFile(IJavaSourceFile newSourceCodeFile){
		if(!(newSourceCodeFile instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		Element newChildElt = (Element)(((DOMBased)newSourceCodeFile).getDOMNode());
		newChildElt = DOMUtils.cloneAndRename(newChildElt, JavasourcecodeConstants.NS_URI, SOURCE_CODE_FILE_ELT_NAME);
		((DOMBased)newSourceCodeFile).setDOMNode(newChildElt);

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
				"sourceCodeFile", newSourceCodeFile,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
		
	public void addSourceCodeFiles(Collection sourceCodeFiles){
		for(Iterator en = sourceCodeFiles.iterator(); en.hasNext(); ){
			IJavaSourceFile elt = (IJavaSourceFile)en.next();
			addSourceCodeFile(elt);
		}
	}		
		
	public void clearSourceCodeFiles(){
		//DOMUtils.removeChildren(elt, JavasourcecodeConstants.NS_URI, SOURCE_CODE_FILE_ELT_NAME);
		Collection coll = getAllSourceCodeFiles();
		removeSourceCodeFiles(coll);
	}
	
	public void removeSourceCodeFile(IJavaSourceFile sourceCodeFileToRemove){
		if(!(sourceCodeFileToRemove instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		NodeList nl = DOMUtils.getChildren(elt, JavasourcecodeConstants.NS_URI, SOURCE_CODE_FILE_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			if(n == ((DOMBased)sourceCodeFileToRemove).getDOMNode()){
				synchronized(DOMUtils.getDOMLock(elt)){
					elt.removeChild(n);
				}

				IXArch context = getXArch();
				if(context != null){
					context.fireXArchEvent(
						new XArchEvent(this, 
						XArchEvent.REMOVE_EVENT,
						XArchEvent.ELEMENT_CHANGED,
						"sourceCodeFile", sourceCodeFileToRemove,
						XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
					);
				}

				return;
			}
		}
	}
	
	public void removeSourceCodeFiles(Collection sourceCodeFiles){
		for(Iterator en = sourceCodeFiles.iterator(); en.hasNext(); ){
			IJavaSourceFile elt = (IJavaSourceFile)en.next();
			removeSourceCodeFile(elt);
		}
	}
	
	public Collection getAllSourceCodeFiles(){
		NodeList nl = DOMUtils.getChildren(elt, JavasourcecodeConstants.NS_URI, SOURCE_CODE_FILE_ELT_NAME);
		int nlLength = nl.getLength();
		ArrayList v = new ArrayList(nlLength);
		IXArch de = getXArch();
		for(int i = 0; i < nlLength; i++){
			Element el = (Element)nl.item(i);
			boolean found = false;
			if(de != null){
				IXArchElement cachedXArchElt = de.getWrapper(el);
				if(cachedXArchElt != null){
					v.add((IJavaSourceFile)cachedXArchElt);
					found = true;
				}
			}
			if(!found){
				Object o = makeDerivedWrapper(el, "JavaSourceFile");	
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)o));
						}
						v.add((IJavaSourceFile)o);
					}
					catch(Exception e){
						JavaSourceFileImpl eltImpl = new JavaSourceFileImpl((Element)nl.item(i));
						eltImpl.setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
						}
						v.add(eltImpl);
					}
				}
				else{
					JavaSourceFileImpl eltImpl = new JavaSourceFileImpl((Element)nl.item(i));
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

	public boolean hasSourceCodeFile(IJavaSourceFile sourceCodeFileToCheck){
		Collection c = getAllSourceCodeFiles();
		
		for(Iterator en = c.iterator(); en.hasNext(); ){
			IJavaSourceFile elt = (IJavaSourceFile)en.next();
			if(elt.isEquivalent(sourceCodeFileToCheck)){
				return true;
			}
		}
		return false;
	}
	
	public Collection hasSourceCodeFiles(Collection sourceCodeFilesToCheck){
		Vector v = new Vector();
		for(Iterator en = sourceCodeFilesToCheck.iterator(); en.hasNext(); ){
			IJavaSourceFile elt = (IJavaSourceFile)en.next();
			v.addElement(new Boolean(hasSourceCodeFile(elt)));
		}
		return v;
	}
		
	public boolean hasAllSourceCodeFiles(Collection sourceCodeFilesToCheck){
		for(Iterator en = sourceCodeFilesToCheck.iterator(); en.hasNext(); ){
			IJavaSourceFile elt = (IJavaSourceFile)en.next();
			if(!hasSourceCodeFile(elt)){
				return false;
			}
		}
		return true;
	}
	public void addRepositoryLocation(IRepositoryLocation newRepositoryLocation){
		if(!(newRepositoryLocation instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		Element newChildElt = (Element)(((DOMBased)newRepositoryLocation).getDOMNode());
		newChildElt = DOMUtils.cloneAndRename(newChildElt, JavasourcecodeConstants.NS_URI, REPOSITORY_LOCATION_ELT_NAME);
		((DOMBased)newRepositoryLocation).setDOMNode(newChildElt);

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
				"repositoryLocation", newRepositoryLocation,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
		
	public void addRepositoryLocations(Collection repositoryLocations){
		for(Iterator en = repositoryLocations.iterator(); en.hasNext(); ){
			IRepositoryLocation elt = (IRepositoryLocation)en.next();
			addRepositoryLocation(elt);
		}
	}		
		
	public void clearRepositoryLocations(){
		//DOMUtils.removeChildren(elt, JavasourcecodeConstants.NS_URI, REPOSITORY_LOCATION_ELT_NAME);
		Collection coll = getAllRepositoryLocations();
		removeRepositoryLocations(coll);
	}
	
	public void removeRepositoryLocation(IRepositoryLocation repositoryLocationToRemove){
		if(!(repositoryLocationToRemove instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		NodeList nl = DOMUtils.getChildren(elt, JavasourcecodeConstants.NS_URI, REPOSITORY_LOCATION_ELT_NAME);
		for(int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			if(n == ((DOMBased)repositoryLocationToRemove).getDOMNode()){
				synchronized(DOMUtils.getDOMLock(elt)){
					elt.removeChild(n);
				}

				IXArch context = getXArch();
				if(context != null){
					context.fireXArchEvent(
						new XArchEvent(this, 
						XArchEvent.REMOVE_EVENT,
						XArchEvent.ELEMENT_CHANGED,
						"repositoryLocation", repositoryLocationToRemove,
						XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
					);
				}

				return;
			}
		}
	}
	
	public void removeRepositoryLocations(Collection repositoryLocations){
		for(Iterator en = repositoryLocations.iterator(); en.hasNext(); ){
			IRepositoryLocation elt = (IRepositoryLocation)en.next();
			removeRepositoryLocation(elt);
		}
	}
	
	public Collection getAllRepositoryLocations(){
		NodeList nl = DOMUtils.getChildren(elt, JavasourcecodeConstants.NS_URI, REPOSITORY_LOCATION_ELT_NAME);
		int nlLength = nl.getLength();
		ArrayList v = new ArrayList(nlLength);
		IXArch de = getXArch();
		for(int i = 0; i < nlLength; i++){
			Element el = (Element)nl.item(i);
			boolean found = false;
			if(de != null){
				IXArchElement cachedXArchElt = de.getWrapper(el);
				if(cachedXArchElt != null){
					v.add((IRepositoryLocation)cachedXArchElt);
					found = true;
				}
			}
			if(!found){
				Object o = makeDerivedWrapper(el, "RepositoryLocation");	
				if(o != null){
					try{
						((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)o));
						}
						v.add((IRepositoryLocation)o);
					}
					catch(Exception e){
						RepositoryLocationImpl eltImpl = new RepositoryLocationImpl((Element)nl.item(i));
						eltImpl.setXArch(getXArch());
						if(de != null){
							de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
						}
						v.add(eltImpl);
					}
				}
				else{
					RepositoryLocationImpl eltImpl = new RepositoryLocationImpl((Element)nl.item(i));
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

	public boolean hasRepositoryLocation(IRepositoryLocation repositoryLocationToCheck){
		Collection c = getAllRepositoryLocations();
		
		for(Iterator en = c.iterator(); en.hasNext(); ){
			IRepositoryLocation elt = (IRepositoryLocation)en.next();
			if(elt.isEquivalent(repositoryLocationToCheck)){
				return true;
			}
		}
		return false;
	}
	
	public Collection hasRepositoryLocations(Collection repositoryLocationsToCheck){
		Vector v = new Vector();
		for(Iterator en = repositoryLocationsToCheck.iterator(); en.hasNext(); ){
			IRepositoryLocation elt = (IRepositoryLocation)en.next();
			v.addElement(new Boolean(hasRepositoryLocation(elt)));
		}
		return v;
	}
		
	public boolean hasAllRepositoryLocations(Collection repositoryLocationsToCheck){
		for(Iterator en = repositoryLocationsToCheck.iterator(); en.hasNext(); ){
			IRepositoryLocation elt = (IRepositoryLocation)en.next();
			if(!hasRepositoryLocation(elt)){
				return false;
			}
		}
		return true;
	}

	public void setSourceCodeManager(IJavaSourceCodeManager value){
		if(!(value instanceof DOMBased)){
			throw new IllegalArgumentException("Cannot handle non-DOM-based xArch entities.");
		}
		{
			IJavaSourceCodeManager oldElt = getSourceCodeManager();
			DOMUtils.removeChildren(elt, JavasourcecodeConstants.NS_URI, SOURCE_CODE_MANAGER_ELT_NAME);
			
			IXArch context = getXArch();
			if(context != null){
				context.fireXArchEvent(
					new XArchEvent(this, 
					XArchEvent.CLEAR_EVENT,
					XArchEvent.ELEMENT_CHANGED,
					"sourceCodeManager", oldElt,
					XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this), true)
				);
			}
		}
		Element newChildElt = (Element)(((DOMBased)value).getDOMNode());
		newChildElt = DOMUtils.cloneAndRename(newChildElt, JavasourcecodeConstants.NS_URI, SOURCE_CODE_MANAGER_ELT_NAME);
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
				"sourceCodeManager", value,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
	
	public void clearSourceCodeManager(){
		IJavaSourceCodeManager oldElt = getSourceCodeManager();
		DOMUtils.removeChildren(elt, JavasourcecodeConstants.NS_URI, SOURCE_CODE_MANAGER_ELT_NAME);
		
		IXArch context = getXArch();
		if(context != null){
			context.fireXArchEvent(
				new XArchEvent(this, 
				XArchEvent.CLEAR_EVENT,
				XArchEvent.ELEMENT_CHANGED,
				"sourceCodeManager", oldElt,
				XArchUtils.getDefaultXArchImplementation().isContainedIn(xArch, this))
			);
		}
	}
	
	public IJavaSourceCodeManager getSourceCodeManager(){
		NodeList nl = DOMUtils.getChildren(elt, JavasourcecodeConstants.NS_URI, SOURCE_CODE_MANAGER_ELT_NAME);
		if(nl.getLength() == 0){
			return null;
		}
		else{
			Element el = (Element)nl.item(0);
			IXArch de = getXArch();
			if(de != null){
				IXArchElement cachedXArchElt = de.getWrapper(el);
				if(cachedXArchElt != null){
					return (IJavaSourceCodeManager)cachedXArchElt;
				}
			}
			
			Object o = makeDerivedWrapper(el, "JavaSourceCodeManager");
			if(o != null){
				try{
					((edu.uci.isr.xarch.IXArchElement)o).setXArch(getXArch());
					if(de != null){
						de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)o));
					}
					return (IJavaSourceCodeManager)o;
				}
				catch(Exception e){}
			}
			JavaSourceCodeManagerImpl eltImpl = new JavaSourceCodeManagerImpl(el);
			eltImpl.setXArch(getXArch());
			if(de != null){
				de.cacheWrapper(el, ((edu.uci.isr.xarch.IXArchElement)eltImpl));
			}
			return eltImpl;
		}
	}
	
	public boolean hasSourceCodeManager(IJavaSourceCodeManager value){
		IJavaSourceCodeManager thisValue = getSourceCodeManager();
		IJavaSourceCodeManager thatValue = value;
		
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

	public boolean isEquivalent(IJavaSourceCodeImplementation c){
		return
			super.isEquivalent(c) &&
			hasAllSourceCodeFiles(c.getAllSourceCodeFiles()) &&
			c.hasAllSourceCodeFiles(getAllSourceCodeFiles()) &&
			hasAllRepositoryLocations(c.getAllRepositoryLocations()) &&
			c.hasAllRepositoryLocations(getAllRepositoryLocations()) &&
			hasSourceCodeManager(c.getSourceCodeManager()) ;
	}

}
