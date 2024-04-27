/* 
 * @OSF_COPYRIGHT@
 * (c) Copyright 1990, 1991, 1992, 1993, 1994 OPEN SOFTWARE FOUNDATION, INC.
 * ALL RIGHTS RESERVED
 *  
*/ 
/*
 * HISTORY
 * Motif Release 1.2.5
*/
/************************************************************************* 
 **  (c) Copyright 1993, 1994 Hewlett-Packard Company
 **  (c) Copyright 1993, 1994 International Business Machines Corp.
 **  (c) Copyright 2002 Sun Microsystems, Inc.
 **  (c) Copyright 1993, 1994 Novell, Inc.
 *************************************************************************/
#ifdef REV_INFO
#ifndef lint
static char rcsid[] = "$XConsortium: XpmRdFToI.c /main/cde1_maint/2 1995/08/18 19:39:50 drk $"
#endif
#endif
/* Copyright 1990-92 GROUPE BULL -- See license conditions in file COPYRIGHT */
/*****************************************************************************\
* XpmRdFToI.c:                                                                *
*                                                                             *
*  XPM library                                                                *
*  Parse an XPM file and create the image and possibly its mask               *
*                                                                             *
*  Developed by Arnaud Le Hors                                                *
\*****************************************************************************/

#include <Xm/XpmI.h>

int
_XmXpmReadFileToImage(display, filename, image_return,
		   shapeimage_return, attributes)
    Display *display;
    char *filename;
    XImage **image_return;
    XImage **shapeimage_return;
    XpmAttributes *attributes;
{
    xpmData mdata;
    char buf[BUFSIZ];
    int l, n = 0;
    int ErrorStatus;
    xpmInternAttrib attrib;

    /*
     * initialize return values 
     */
    if (image_return)
	*image_return = NULL;
    if (shapeimage_return)
	*shapeimage_return = NULL;

    if ((ErrorStatus = _XmxpmReadFile(filename, &mdata)) != XpmSuccess)
	return (ErrorStatus);

    _XmxpmInitInternAttrib(&attrib);

    /*
     * parse the header file 
     */
    mdata.Bos = '\0';
    mdata.Eos = '\n';
    mdata.Bcmt = mdata.Ecmt = NULL;
    _XmxpmNextWord(&mdata, buf, BUFSIZ);	/* skip the first word */
    l = _XmxpmNextWord(&mdata, buf, BUFSIZ);	/* then get the second word */
    if ((l == 3 && !strncmp("XPM", buf, 3)) ||
	(l == 4 && !strncmp("XPM2", buf, 4))) {
	if (l == 3)
	    n = 1;			/* handle XPM as XPM2 C */
	else {
	    l = _XmxpmNextWord(&mdata, buf, BUFSIZ);/* get the type key word */

	    /*
	     * get infos about this type 
	     */
	    while (_XmxpmDataTypes[n].type
		   && strncmp(_XmxpmDataTypes[n].type, buf, l))
		n++;
	}
	if (_XmxpmDataTypes[n].type) {
	    if (n == 0) {		/* natural type */
		mdata.Bcmt = _XmxpmDataTypes[n].Bcmt;
		mdata.Ecmt = _XmxpmDataTypes[n].Ecmt;
		_XmxpmNextString(&mdata);	/* skip the end of headerline */
		mdata.Bos = _XmxpmDataTypes[n].Bos;
	    } else {
		_XmxpmNextString(&mdata);	/* skip the end of headerline */
		mdata.Bcmt = _XmxpmDataTypes[n].Bcmt;
		mdata.Ecmt = _XmxpmDataTypes[n].Ecmt;
		mdata.Bos = _XmxpmDataTypes[n].Bos;
		mdata.Eos = '\0';
		_XmxpmNextString(&mdata);	/* skip the assignment line */
	    }
	    mdata.Eos = _XmxpmDataTypes[n].Eos;

	    ErrorStatus = _XmxpmParseData(&mdata, &attrib, attributes);

	    if (ErrorStatus == XpmSuccess)
		ErrorStatus = _XmxpmCreateImage(display, &attrib, image_return,
                                             shapeimage_return, attributes);
	} else
	    ErrorStatus = XpmFileInvalid;
    } else
	ErrorStatus = XpmFileInvalid;

    if (ErrorStatus >= 0)
	_XmxpmSetAttributes(&attrib, attributes);
    else if (attributes)
	_XmXpmFreeAttributes(attributes);

    _XmxpmFreeInternAttrib(&attrib);
    _XmXpmDataClose(&mdata);

    return (ErrorStatus);
}

int
_XmXpmFreeImage(display, filename, attributes)
    Display *display;
    char *filename;
    XpmAttributes *attributes;
{
    xpmData mdata;
    char buf[BUFSIZ];
    int l, n = 0;
    int ErrorStatus;
    xpmInternAttrib attrib;

    if ((ErrorStatus = _XmxpmReadFile(filename, &mdata)) != XpmSuccess)
	return (ErrorStatus);

    _XmxpmInitInternAttrib(&attrib);

    /*
     * parse the header file 
     */
    mdata.Bos = '\0';
    mdata.Eos = '\n';
    mdata.Bcmt = mdata.Ecmt = NULL;
    _XmxpmNextWord(&mdata, buf, BUFSIZ);	/* skip the first word */
    l = _XmxpmNextWord(&mdata, buf, BUFSIZ);	/* then get the second word */
    if ((l == 3 && !strncmp("XPM", buf, 3)) ||
	(l == 4 && !strncmp("XPM2", buf, 4))) {
	if (l == 3)
	    n = 1;			/* handle XPM as XPM2 C */
	else {
	    l = _XmxpmNextWord(&mdata, buf, BUFSIZ);/* get the type key word */

	    /*
	     * get infos about this type 
	     */
	    while (_XmxpmDataTypes[n].type
		   && strncmp(_XmxpmDataTypes[n].type, buf, l))
		n++;
	}
	if (_XmxpmDataTypes[n].type) {
	    if (n == 0) {		/* natural type */
		mdata.Bcmt = _XmxpmDataTypes[n].Bcmt;
		mdata.Ecmt = _XmxpmDataTypes[n].Ecmt;
		_XmxpmNextString(&mdata);	/* skip the end of headerline */
		mdata.Bos = _XmxpmDataTypes[n].Bos;
	    } else {
		_XmxpmNextString(&mdata);	/* skip the end of headerline */
		mdata.Bcmt = _XmxpmDataTypes[n].Bcmt;
		mdata.Ecmt = _XmxpmDataTypes[n].Ecmt;
		mdata.Bos = _XmxpmDataTypes[n].Bos;
		mdata.Eos = '\0';
		_XmxpmNextString(&mdata);	/* skip the assignment line */
	    }
	    mdata.Eos = _XmxpmDataTypes[n].Eos;

	    ErrorStatus = _XmxpmParseData(&mdata, &attrib, attributes);

	    if (ErrorStatus == XpmSuccess)
		ErrorStatus = _XmxpmDestroyImage(display, &attrib, attributes);
	} else
	    ErrorStatus = XpmFileInvalid;
    } else
	ErrorStatus = XpmFileInvalid;

    if (attributes)
	_XmXpmFreeAttributes(attributes);

    _XmxpmFreeInternAttrib(&attrib);
    _XmXpmDataClose(&mdata);

    return (ErrorStatus);
}
