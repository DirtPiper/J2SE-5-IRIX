/*
 * @(#)JWindow.java	1.60 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javax.swing;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.Vector;
import java.io.Serializable;

import javax.accessibility.*;

/** 
 * A <code>JWindow</code> is a container that can be displayed anywhere on the
 * user's desktop. It does not have the title bar, window-management buttons,
 * or other trimmings associated with a <code>JFrame</code>, but it is still a 
 * "first-class citizen" of the user's desktop, and can exist anywhere
 * on it.
 * <p>
 * The <code>JWindow</code> component contains a <code>JRootPane</code>
 * as its only child.  The <code>contentPane</code> should be the parent
 * of any children of the <code>JWindow</code>.
 * As a conveniance <code>add</code> and its variants, <code>remove</code> and
 * <code>setLayout</code> have been overridden to forward to the
 * <code>contentPane</code> as necessary. This means you can write:
 * <pre>
 *       window.add(child);
 * </pre>
 * And the child will be added to the contentPane.
 * The <code>contentPane</code> will always be non-<code>null</code>.
 * Attempting to set it to <code>null</code> will cause the <code>JWindow</code>
 * to throw an exception. The default <code>contentPane</code> will have a
 * <code>BorderLayout</code> manager set on it. 
 * Refer to {@link javax.swing.RootPaneContainer}
 * for details on adding, removing and setting the <code>LayoutManager</code>
 * of a <code>JWindow</code>.
 * <p>
 * Please see the {@link JRootPane} documentation for a complete description of
 * the <code>contentPane</code>, <code>glassPane</code>, and
 * <code>layeredPane</code> components.
 * <p>
 * In a multi-screen environment, you can create a <code>JWindow</code>
 * on a different screen device.  See {@link java.awt.Window} for more
 * information.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans<sup><font size="-2">TM</font></sup>
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 * @see JRootPane
 *
 * @beaninfo
 *      attribute: isContainer true
 *      attribute: containerDelegate getContentPane
 *    description: A toplevel window which has no system border or controls.
 *
 * @version 1.60 12/19/03
 * @author David Kloba
 */
public class JWindow extends Window implements Accessible, RootPaneContainer 
{
    /**
     * The <code>JRootPane</code> instance that manages the
     * <code>contentPane</code> 
     * and optional <code>menuBar</code> for this frame, as well as the 
     * <code>glassPane</code>.
     *
     * @see #getRootPane
     * @see #setRootPane
     */
    protected JRootPane rootPane;

    /**
     * If true then calls to <code>add</code> and <code>setLayout</code>
     * will be forwarded to the <code>contentPane</code>. This is initially
     * false, but is set to true when the <code>JWindow</code> is constructed.
     *
     * @see #isRootPaneCheckingEnabled
     * @see #setRootPaneCheckingEnabled
     * @see javax.swing.RootPaneContainer
     */
    protected boolean rootPaneCheckingEnabled = false;

    /**
     * Creates a window with no specified owner. This window will not be
     * focusable.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.     
     *
     * @throws HeadlessException if
     *         <code>GraphicsEnvironment.isHeadless()</code> returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see #isFocusableWindow
     * @see JComponent#getDefaultLocale
     */
    public JWindow() {
        this((Frame)null);
    }

    /**
     * Creates a window with the specified <code>GraphicsConfiguration</code>
     * of a screen device. This window will not be focusable.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.     
     * 
     * @param gc the <code>GraphicsConfiguration</code> that is used
     * 		to construct the new window with; if gc is <code>null</code>,
     * 		the system default <code>GraphicsConfiguration</code>
     *		is assumed
     * @throws HeadlessException If
     *         <code>GraphicsEnvironment.isHeadless()</code> returns true.
     * @throws IllegalArgumentException if <code>gc</code> is not from
     * 	       a screen device.
     *
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see #isFocusableWindow
     * @see JComponent#getDefaultLocale
     *
     * @since  1.3
     */
    public JWindow(GraphicsConfiguration gc) {
        this(null, gc);
        super.setFocusableWindowState(false);
    }
    
    /**
     * Creates a window with the specified owner frame.
     * If <code>owner</code> is <code>null</code>, the shared owner
     * will be used and this window will not be focusable. Also, 
     * this window will not be focusable unless its owner is showing
     * on the screen.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.     
     *
     * @param owner the frame from which the window is displayed
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     *            returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see #isFocusableWindow
     * @see JComponent#getDefaultLocale
     */
    public JWindow(Frame owner) {
        super(owner == null? SwingUtilities.getSharedOwnerFrame() : owner);
	if (owner == null) {
	    WindowListener ownerShutdownListener =
		(WindowListener)SwingUtilities.getSharedOwnerFrameShutdownListener();
 	    addWindowListener(ownerShutdownListener);
	}
        windowInit();
    }

    /**
     * Creates a window with the specified owner window. This window
     * will not be focusable unless its owner is showing on the screen.
     * If <code>owner</code> is <code>null</code>, the shared owner
     * will be used and this window will not be focusable.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.     
     *
     * @param owner the window from which the window is displayed
     * @throws HeadlessException if
     *         <code>GraphicsEnvironment.isHeadless()</code> returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see #isFocusableWindow
     * @see JComponent#getDefaultLocale
     */
    public JWindow(Window owner) {
        super(owner == null ? (Window)SwingUtilities.getSharedOwnerFrame() :
              owner);
	if (owner == null) {
	    WindowListener ownerShutdownListener =
		(WindowListener)SwingUtilities.getSharedOwnerFrameShutdownListener();
 	    addWindowListener(ownerShutdownListener);
	}
        windowInit();
    }

    /**
     * Creates a window with the specified owner window and
     * <code>GraphicsConfiguration</code> of a screen device. If
     * <code>owner</code> is <code>null</code>, the shared owner will be used
     * and this window will not be focusable.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.     
     * 
     * @param owner the window from which the window is displayed
     * @param gc the <code>GraphicsConfiguration</code> that is used
     * 		to construct the new window with; if gc is <code>null</code>,
     * 		the system default <code>GraphicsConfiguration</code>
     *		is assumed, unless <code>owner</code> is also null, in which
     *          case the <code>GraphicsConfiguration</code> from the
     *          shared owner frame will be used.
     * @throws HeadlessException if
     *         <code>GraphicsEnvironment.isHeadless()</code> returns true.
     * @throws IllegalArgumentException if <code>gc</code> is not from
     * 	       a screen device.
     *
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see #isFocusableWindow
     * @see JComponent#getDefaultLocale
     *
     * @since  1.3
     */
    public JWindow(Window owner, GraphicsConfiguration gc) {
        super(owner == null ? (Window)SwingUtilities.getSharedOwnerFrame() :
              owner, gc);
	if (owner == null) {
	    WindowListener ownerShutdownListener =
		(WindowListener)SwingUtilities.getSharedOwnerFrameShutdownListener();
 	    addWindowListener(ownerShutdownListener);
	}
        windowInit();
    }

    /**
     * Called by the constructors to init the <code>JWindow</code> properly.
     */
    protected void windowInit() {
        setLocale( JComponent.getDefaultLocale() );
        setRootPane(createRootPane());
        setRootPaneCheckingEnabled(true);
        sun.awt.SunToolkit.checkAndSetPolicy(this, true);
    }

    /**
     * Called by the constructor methods to create the default
     * <code>rootPane</code>.
     */
    protected JRootPane createRootPane() {
        JRootPane rp = new JRootPane();
        // NOTE: this uses setOpaque vs LookAndFeel.installProperty as there
        // is NO reason for the RootPane not to be opaque. For painting to
        // work the contentPane must be opaque, therefor the RootPane can
        // also be opaque.
        rp.setOpaque(true);
        return rp;
    }
 
    /**
     * Returns whether calls to <code>add</code> and 
     * <code>setLayout</code> are forwarded to the <code>contentPane</code>.
     *
     * @return true if <code>add</code> and <code>setLayout</code> 
     *         are fowarded; false otherwise
     *
     * @see #addImpl
     * @see #setLayout
     * @see #setRootPaneCheckingEnabled
     * @see javax.swing.RootPaneContainer
     */
    protected boolean isRootPaneCheckingEnabled() {
        return rootPaneCheckingEnabled;
    }


    /** 
     * Calls <code>paint(g)</code>.  This method was overridden to 
     * prevent an unnecessary call to clear the background.
     *
     * @param g  the <code>Graphics</code> context in which to paint
     */
    public void update(Graphics g) {
        paint(g);
    }

    /**
     * Sets whether calls to <code>add</code> and 
     * <code>setLayout</code> are forwarded to the <code>contentPane</code>.
     * 
     * @param enabled  true if <code>add</code> and <code>setLayout</code>
     *        are forwarded, false if they should operate directly on the
     *        <code>JWindow</code>.
     *
     * @see #addImpl
     * @see #setLayout
     * @see #isRootPaneCheckingEnabled
     * @see javax.swing.RootPaneContainer
     * @beaninfo
     *      hidden: true
     * description: Whether the add and setLayout methods are forwarded
     */
    protected void setRootPaneCheckingEnabled(boolean enabled) {
        rootPaneCheckingEnabled = enabled;
    }


    /**
     * Adds the specified child <code>Component</code>.
     * This method is overridden to conditionally forwad calls to the
     * <code>contentPane</code>.
     * By default, children are added to the <code>contentPane</code> instead
     * of the frame, refer to {@link javax.swing.RootPaneContainer} for
     * details.
     * 
     * @param comp the component to be enhanced
     * @param constraints the constraints to be respected
     * @param index the index
     * @exception IllegalArgumentException if <code>index</code> is invalid
     * @exception IllegalArgumentException if adding the container's parent
     *			to itself
     * @exception IllegalArgumentException if adding a window to a container
     * 
     * @see #setRootPaneCheckingEnabled
     * @see javax.swing.RootPaneContainer
     */
    protected void addImpl(Component comp, Object constraints, int index) 
    {
        if(isRootPaneCheckingEnabled()) {
            getContentPane().add(comp, constraints, index);
        }
        else {
            super.addImpl(comp, constraints, index);
        }
    }

    /** 
     * Removes the specified component from the container. If
     * <code>comp</code> is not the <code>rootPane</code>, this will forward
     * the call to the <code>contentPane</code>. This will do nothing if
     * <code>comp</code> is not a child of the <code>JWindow</code> or
     * <code>contentPane</code>.
     *
     * @param comp the component to be removed
     * @throws NullPointerException if <code>comp</code> is null
     * @see #add
     * @see javax.swing.RootPaneContainer
     */
    public void remove(Component comp) {
	if (comp == rootPane) {
	    super.remove(comp);
	} else {
	    getContentPane().remove(comp);
	}
    }


    /**
     * Sets the <code>LayoutManager</code>.
     * Overridden to conditionally forward the call to the
     * <code>contentPane</code>.
     * Refer to {@link javax.swing.RootPaneContainer} for
     * more information.
     *
     * @param manager the <code>LayoutManager</code>
     * @see #setRootPaneCheckingEnabled
     * @see javax.swing.RootPaneContainer
     */
    public void setLayout(LayoutManager manager) {
        if(isRootPaneCheckingEnabled()) {
            getContentPane().setLayout(manager);
        }
        else {
            super.setLayout(manager);
        }
    }


    /**
     * Returns the <code>rootPane</code> object for this window.
     * @return the <code>rootPane</code> property for this window
     *
     * @see #setRootPane
     * @see RootPaneContainer#getRootPane
     */
    public JRootPane getRootPane() { 
        return rootPane; 
    }


    /**
     * Sets the new <code>rootPane</code> object for this window.
     * This method is called by the constructor.
     *
     * @param root the new <code>rootPane</code> property
     * @see #getRootPane
     *
     * @beaninfo
     *        hidden: true
     *   description: the RootPane object for this window.
     */
    protected void setRootPane(JRootPane root) {
        if(rootPane != null) {
            remove(rootPane);
        }
        rootPane = root;
        if(rootPane != null) {
            boolean checkingEnabled = isRootPaneCheckingEnabled();
            try {
                setRootPaneCheckingEnabled(false);
                add(rootPane, BorderLayout.CENTER);
            }
            finally {
                setRootPaneCheckingEnabled(checkingEnabled);
            }
        }
    }


    /**
     * Returns the <code>Container</code> which is the <code>contentPane</code>
     * for this window.
     *
     * @return the <code>contentPane</code> property
     * @see #setContentPane
     * @see RootPaneContainer#getContentPane
     */
    public Container getContentPane() { 
        return getRootPane().getContentPane(); 
    }

    /**
     * Sets the <code>contentPane</code> property for this window.
     * This method is called by the constructor.
     *
     * @param contentPane the new <code>contentPane</code>
     *
     * @exception IllegalComponentStateException (a runtime
     *            exception) if the content pane parameter is <code>null</code>
     * @see #getContentPane
     * @see RootPaneContainer#setContentPane
     *
     * @beaninfo
     *     hidden: true
     *     description: The client area of the window where child 
     *                  components are normally inserted.
     */
    public void setContentPane(Container contentPane) {
        getRootPane().setContentPane(contentPane);
    }

    /**
     * Returns the <code>layeredPane</code> object for this window.
     *
     * @return the <code>layeredPane</code> property
     * @see #setLayeredPane
     * @see RootPaneContainer#getLayeredPane
     */
    public JLayeredPane getLayeredPane() { 
        return getRootPane().getLayeredPane(); 
    }

    /**
     * Sets the <code>layeredPane</code> property. 
     * This method is called by the constructor.
     *
     * @param layeredPane the new <code>layeredPane</code> object
     *
     * @exception IllegalComponentStateException (a runtime
     *            exception) if the content pane parameter is <code>null</code>
     * @see #getLayeredPane
     * @see RootPaneContainer#setLayeredPane
     *
     * @beaninfo
     *     hidden: true
     *     description: The pane which holds the various window layers.
     */
    public void setLayeredPane(JLayeredPane layeredPane) {
        getRootPane().setLayeredPane(layeredPane);
    }

    /**
     * Returns the <code>glassPane Component</code> for this window.
     *
     * @return the <code>glassPane</code> property
     * @see #setGlassPane
     * @see RootPaneContainer#getGlassPane
     */
    public Component getGlassPane() { 
        return getRootPane().getGlassPane(); 
    }

    /**
     * Sets the <code>glassPane</code> property. 
     * This method is called by the constructor.
     * @param glassPane the <code>glassPane</code> object for this window
     *
     * @see #getGlassPane
     * @see RootPaneContainer#setGlassPane
     *
     * @beaninfo
     *     hidden: true
     *     description: A transparent pane used for menu rendering.
     */
    public void setGlassPane(Component glassPane) {
        getRootPane().setGlassPane(glassPane);
    }

    /**
     * Returns a string representation of this <code>JWindow</code>.
     * This method 
     * is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between      
     * implementations. The returned string may be empty but may not 
     * be <code>null</code>.
     * 
     * @return  a string representation of this <code>JWindow</code>
     */
    protected String paramString() {
        String rootPaneCheckingEnabledString = (rootPaneCheckingEnabled ?
						"true" : "false");

        return super.paramString() +
	",rootPaneCheckingEnabled=" + rootPaneCheckingEnabledString;
    }


/////////////////
// Accessibility support
////////////////

    /** The accessible context property. */
    protected AccessibleContext accessibleContext = null;

    /**
     * Gets the AccessibleContext associated with this JWindow. 
     * For JWindows, the AccessibleContext takes the form of an 
     * AccessibleJWindow. 
     * A new AccessibleJWindow instance is created if necessary.
     *
     * @return an AccessibleJWindow that serves as the 
     *         AccessibleContext of this JWindow
     */
    public AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) {
            accessibleContext = new AccessibleJWindow();
        }
        return accessibleContext;
    }


    /**
     * This class implements accessibility support for the 
     * <code>JWindow</code> class.  It provides an implementation of the 
     * Java Accessibility API appropriate to window user-interface 
     * elements.
     */
    protected class AccessibleJWindow extends AccessibleAWTWindow {
        // everything is in the new parent, AccessibleAWTWindow
    }

}
