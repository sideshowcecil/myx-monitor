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
package edu.uci.isr.xarch.implementation;

import java.util.*;

import edu.uci.isr.xarch.*;

import org.w3c.dom.*;

import edu.uci.isr.xarch.IXArch;
import edu.uci.isr.xarch.IXArchContext;

/**
 * The context interface for the implementation package.
 * This interface is used to create objects that are used
 * in the implementation namespace.
 *
 * @author Automatically Generated by xArch apigen
 */
public interface IImplementationContext extends IXArchContext{

	/**
	 * Create an IImplementation object in this namespace.
	 * @return New IImplementation object.
	 */
	public IImplementation createImplementation();

	/**
	 * Brings an IImplementation object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IImplementation recontextualizeImplementation(IImplementation value);

	/**
	 * Create an IVariantComponentTypeImpl object in this namespace.
	 * @return New IVariantComponentTypeImpl object.
	 */
	public IVariantComponentTypeImpl createVariantComponentTypeImpl();

	/**
	 * Brings an IVariantComponentTypeImpl object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IVariantComponentTypeImpl recontextualizeVariantComponentTypeImpl(IVariantComponentTypeImpl value);

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.variants.IVariantComponentType</code>
	 * to one of type <code>IVariantComponentTypeImpl</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.variants.IVariantComponentType</code>
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
	public IVariantComponentTypeImpl promoteToVariantComponentTypeImpl(
	edu.uci.isr.xarch.variants.IVariantComponentType value);

	/**
	 * Create an IVariantConnectorTypeImpl object in this namespace.
	 * @return New IVariantConnectorTypeImpl object.
	 */
	public IVariantConnectorTypeImpl createVariantConnectorTypeImpl();

	/**
	 * Brings an IVariantConnectorTypeImpl object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IVariantConnectorTypeImpl recontextualizeVariantConnectorTypeImpl(IVariantConnectorTypeImpl value);

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.variants.IVariantConnectorType</code>
	 * to one of type <code>IVariantConnectorTypeImpl</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.variants.IVariantConnectorType</code>
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
	public IVariantConnectorTypeImpl promoteToVariantConnectorTypeImpl(
	edu.uci.isr.xarch.variants.IVariantConnectorType value);

	/**
	 * Create an IInterfaceTypeImpl object in this namespace.
	 * @return New IInterfaceTypeImpl object.
	 */
	public IInterfaceTypeImpl createInterfaceTypeImpl();

	/**
	 * Brings an IInterfaceTypeImpl object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IInterfaceTypeImpl recontextualizeInterfaceTypeImpl(IInterfaceTypeImpl value);

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.types.IInterfaceType</code>
	 * to one of type <code>IInterfaceTypeImpl</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.types.IInterfaceType</code>
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
	public IInterfaceTypeImpl promoteToInterfaceTypeImpl(
	edu.uci.isr.xarch.types.IInterfaceType value);

	/**
	 * Create an ISignatureImpl object in this namespace.
	 * @return New ISignatureImpl object.
	 */
	public ISignatureImpl createSignatureImpl();

	/**
	 * Brings an ISignatureImpl object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public ISignatureImpl recontextualizeSignatureImpl(ISignatureImpl value);

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.types.ISignature</code>
	 * to one of type <code>ISignatureImpl</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.types.ISignature</code>
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
	public ISignatureImpl promoteToSignatureImpl(
	edu.uci.isr.xarch.types.ISignature value);


	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_CONTEXT,
		"implementation", null, null,
		new XArchPropertyMetadata[]{},
		new XArchActionMetadata[]{
			new XArchActionMetadata(XArchActionMetadata.CREATE, null, IImplementation.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.RECONTEXTUALIZE, IImplementation.TYPE_METADATA, IImplementation.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.CREATE, null, IVariantComponentTypeImpl.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.RECONTEXTUALIZE, IVariantComponentTypeImpl.TYPE_METADATA, IVariantComponentTypeImpl.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.PROMOTE, edu.uci.isr.xarch.variants.IVariantComponentType.TYPE_METADATA, IVariantComponentTypeImpl.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.CREATE, null, IVariantConnectorTypeImpl.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.RECONTEXTUALIZE, IVariantConnectorTypeImpl.TYPE_METADATA, IVariantConnectorTypeImpl.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.PROMOTE, edu.uci.isr.xarch.variants.IVariantConnectorType.TYPE_METADATA, IVariantConnectorTypeImpl.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.CREATE, null, IInterfaceTypeImpl.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.RECONTEXTUALIZE, IInterfaceTypeImpl.TYPE_METADATA, IInterfaceTypeImpl.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.PROMOTE, edu.uci.isr.xarch.types.IInterfaceType.TYPE_METADATA, IInterfaceTypeImpl.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.CREATE, null, ISignatureImpl.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.RECONTEXTUALIZE, ISignatureImpl.TYPE_METADATA, ISignatureImpl.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.PROMOTE, edu.uci.isr.xarch.types.ISignature.TYPE_METADATA, ISignatureImpl.TYPE_METADATA)});

}

