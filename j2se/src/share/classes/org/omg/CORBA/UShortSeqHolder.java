/*
 * @(#)UShortSeqHolder.java	1.11 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.omg.CORBA;


/**
* The Holder for <tt>UShortSeq</tt>.  For more information on 
* Holder files, see <a href="doc-files/generatedfiles.html#holder">
* "Generated Files: Holder Files"</a>.<P>
* org/omg/CORBA/UShortSeqHolder.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from streams.idl
* 13 May 1999 22:41:36 o'clock GMT+00:00
*/

public final class UShortSeqHolder implements org.omg.CORBA.portable.Streamable
{
    public short value[] = null;

    public UShortSeqHolder ()
    {
    }

    public UShortSeqHolder (short[] initialValue)
    {
	value = initialValue;
    }

    public void _read (org.omg.CORBA.portable.InputStream i)
    {
	value = org.omg.CORBA.UShortSeqHelper.read (i);
    }

    public void _write (org.omg.CORBA.portable.OutputStream o)
    {
	org.omg.CORBA.UShortSeqHelper.write (o, value);
    }

    public org.omg.CORBA.TypeCode _type ()
    {
	return org.omg.CORBA.UShortSeqHelper.type ();
    }

}
