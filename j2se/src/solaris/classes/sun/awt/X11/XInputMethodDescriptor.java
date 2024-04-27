/*
 * @(#)XInputMethodDescriptor.java	1.2 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package sun.awt.X11;

import java.awt.im.spi.InputMethod;
import sun.awt.X11InputMethodDescriptor;

class XInputMethodDescriptor extends X11InputMethodDescriptor {

    /**
     * @see java.awt.im.spi.InputMethodDescriptor#createInputMethod
     */
    public InputMethod createInputMethod() throws Exception {
        return new XInputMethod();
    }
}
