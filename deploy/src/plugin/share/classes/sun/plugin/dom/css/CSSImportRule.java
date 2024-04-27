/*
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package sun.plugin.dom.css;

import org.w3c.dom.stylesheets.MediaList;
import org.w3c.dom.DOMException;
import sun.plugin.dom.DOMObject;
import sun.plugin.dom.DOMObjectHelper;
import sun.plugin.dom.DOMObjectFactory;

/**
 *  The <code>CSSImportRule</code> interface represents a @import rule within 
 * a CSS style sheet. The <code>@import</code> rule is used to import style 
 * rules from other style sheets. 
 * <p>See also the <a href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Style-20001113'>Document Object Model (DOM) Level 2 Style Specification</a>.
 * @since DOM Level 2
 */
public final class CSSImportRule extends CSSRule 
				 implements org.w3c.dom.css.CSSImportRule {
    /**
     * Construct a CSSImportRule object.
     */
    public CSSImportRule(DOMObject obj,
			org.w3c.dom.Document doc,
			org.w3c.dom.Node owner,
			org.w3c.dom.css.CSSStyleSheet parentStyleSheet, 
			org.w3c.dom.css.CSSRule parentRule) {
	super(obj, doc, owner, parentStyleSheet, parentRule);
    }

    public short getType() {
	return IMPORT_RULE;
    }

    /**
     *  The location of the style sheet to be imported. The attribute will not 
     * contain the <code>"url(...)"</code> specifier around the URI. 
     */
    public String getHref() {
	return DOMObjectHelper.getStringMember(obj, CSSConstants.ATTR_HREF);
    }

    /**
     *  A list of media types for which this style sheet may be used. 
     */
    public org.w3c.dom.stylesheets.MediaList getMedia() {
	try {
	    Object result = obj.getMember(CSSConstants.ATTR_MEDIA);
	    if(result != null && result instanceof DOMObject) {
		Object ret = DOMObjectFactory.createStyleSheetObject((DOMObject)result,
							document, ownerNode);
		if(ret != null && ret instanceof org.w3c.dom.stylesheets.MediaList) 
		    return (org.w3c.dom.stylesheets.MediaList)ret;
	    }
	}catch(DOMException e) {
	}

	return null;
    }

    /**
     * The style sheet referred to by this rule, if it has been loaded. The 
     * value of this attribute is <code>null</code> if the style sheet has 
     * not yet been loaded or if it will not be loaded (e.g. if the style 
     * sheet is for a media type not supported by the user agent). 
     */
    public org.w3c.dom.css.CSSStyleSheet getStyleSheet() {
	Object result = obj.getMember(CSSConstants.ATTR_STYLESHEET);
	if(result != null && result instanceof DOMObject) {
	    Object ret = DOMObjectFactory.createStyleSheetObject((DOMObject)result,
							    document, ownerNode);
	    if(ret != null && ret instanceof org.w3c.dom.css.CSSStyleSheet)
		return (org.w3c.dom.css.CSSStyleSheet)ret;
	}
	return null;	
    }
}

