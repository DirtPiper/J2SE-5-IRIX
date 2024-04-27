/*
 * @(#)awtmsg.h	1.63 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

#ifndef AWTMSG_H
#define AWTMSG_H

#include <awt.h>

extern const UINT SYSCOMMAND_IMM;

/*
 * #defines for MouseWheel support
 *
 * Most of this is defined in winuser.h, however
 * it is enclosed by #ifdefs that aren't true
 * for all windows platforms.  To ensure that
 * necessary #defines are always available,
 * they're defined here as necessary.
 * See winuser.h for details.
 */

#ifndef WM_MOUSEWHEEL
#define WM_MOUSEWHEEL                   0x020A
#endif //WM_MOUSEWHEEL

#ifndef WHEEL_DELTA
#define WHEEL_DELTA                     120
#endif //WHEEL_DELTA

#ifndef WHEEL_PAGESCROLL
#define WHEEL_PAGESCROLL                (UINT_MAX)
#endif //WHEEL_PAGESCROLL

#ifndef SPI_GETWHEELSCROLLLINES
#define SPI_GETWHEELSCROLLLINES         104
#endif //SPI_GETWHEELSCROLLLINES

#ifndef SM_MOUSEWHEELPRESENT
#define SM_MOUSEWHEELPRESENT            75
#endif //SPI_GETWHEELSCROLLLINES

#ifndef COLOR_HOTLIGHT
#define COLOR_HOTLIGHT                  26
#endif //COLOR_HOTLIGHT

#ifndef COLOR_GRADIENTACTIVECAPTION
#define COLOR_GRADIENTACTIVECAPTION     27
#endif //COLOR_GRADIENTACTIVECAPTION

#ifndef COLOR_GRADIENTINACTIVECAPTION
#define COLOR_GRADIENTINACTIVECAPTION   28
#endif //COLOR_GRADIENTINACTIVECAPTION

#ifndef SPI_GETACTIVEWINDOWTRACKING
#define SPI_GETACTIVEWINDOWTRACKING     0x1000
#endif //SPI_GETACTIVEWINDOWTRACKING

#ifndef SPI_GETMENUANIMATION
#define SPI_GETMENUANIMATION            0x1002
#endif //SPI_GETMENUANIMATION

#ifndef SPI_GETCOMBOBOXANIMATION
#define SPI_GETCOMBOBOXANIMATION        0x1004
#endif //SPI_GETCOMBOBOXANIMATION

#ifndef SPI_GETLISTBOXSMOOTHSCROLLING
#define SPI_GETLISTBOXSMOOTHSCROLLING   0x1006
#endif //SPI_GETLISTBOXSMOOTHSCROLLING

#ifndef SPI_GETGRADIENTCAPTIONS
#define SPI_GETGRADIENTCAPTIONS         0x1008
#endif //SPI_GETGRADIENTCAPTIONS

#ifndef SPI_GETKEYBOARDCUES
#define SPI_GETKEYBOARDCUES             0x100A
#endif //SPI_GETKEYBOARDCUES

#ifndef SPI_GETACTIVEWNDTRKZORDER
#define SPI_GETACTIVEWNDTRKZORDER       0x100C
#endif //SPI_GETACTIVEWNDTRKZORDER

#ifndef SPI_GETHOTTRACKING
#define SPI_GETHOTTRACKING              0x100E
#endif //SPI_GETHOTTRACKING

#ifndef SPI_GETMENUFADE
#define SPI_GETMENUFADE                 0x1012
#endif //SPI_GETMENUFADE

#ifndef SPI_GETSELECTIONFADE
#define SPI_GETSELECTIONFADE            0x1014
#endif //SPI_GETSELECTIONFADE

#ifndef SPI_GETTOOLTIPANIMATION
#define SPI_GETTOOLTIPANIMATION         0x1016
#endif //SPI_GETTOOLTIPANIMATION

#ifndef SPI_GETTOOLTIPFADE
#define SPI_GETTOOLTIPFADE              0x1018
#endif //SPI_GETTOOLTIPFADE 

#ifndef SPI_GETFOREGROUNDLOCKTIMEOUT
#define SPI_GETFOREGROUNDLOCKTIMEOUT    0x2000
#endif //SPI_GETFOREGROUNDLOCKTIMEOUT

#ifndef SPI_GETACTIVEWNDTRKTIMEOUT
#define SPI_GETACTIVEWNDTRKTIMEOUT      0x2002
#endif //SPI_GETACTIVEWNDTRKTIMEOUT

#ifndef SPI_GETFOREGROUNDFLASHCOUNT
#define SPI_GETFOREGROUNDFLASHCOUNT     0x2004
#endif //SPI_GETFOREGROUNDFLASHCOUNT

#ifndef SPI_GETFONTSMOOTHINGTYPE
#define SPI_GETFONTSMOOTHINGTYPE        0x200A
#endif //SPI_GETFONTSMOOTHINGTYPE

#ifndef SPI_GETFONTSMOOTHINGCONTRAST
#define SPI_GETFONTSMOOTHINGCONTRAST    0x200C
#endif //SPI_GETFONTSMOOTHINGCONTRAST


//
// Flags for AnimateWindow
//
#ifndef AW_HOR_POSITIVE
#define AW_HOR_POSITIVE             0x00000001
#endif //AW_HOR_POSITIVE

#ifndef AW_HOR_NEGATIVE
#define AW_HOR_NEGATIVE             0x00000002
#endif //AW_HOR_NEGATIVE

#ifndef AW_VER_POSITIVE
#define AW_VER_POSITIVE             0x00000004
#endif //AW_VER_POSITIVE

#ifndef AW_VER_NEGATIVE
#define AW_VER_NEGATIVE             0x00000008
#endif //AW_VER_NEGATIVE

#ifndef AW_CENTER
#define AW_CENTER                   0x00000010
#endif //AW_CENTER

#ifndef AW_HIDE
#define AW_HIDE                     0x00010000
#endif //AW_HIDE

#ifndef AW_ACTIVATE
#define AW_ACTIVATE                 0x00020000
#endif //AW_ACTIVATE

#ifndef AW_SLIDE
#define AW_SLIDE                    0x00040000
#endif //AW_SLIDE

#ifndef AW_BLEND
#define AW_BLEND                    0x00080000
#endif //AW_BLEND




// WM_MOUSEWHEEL should be WM_MOUSELAST, but
// is not being defined.  See winuser.h
#ifdef WM_MOUSELAST
#if WM_MOUSELAST <= 0x020A
#define WM_AWT_MOUSELAST                0x020A
#else
#error Unexpected value of WM_MOUSELAST
#endif //WM_MOUSELAST <= 0x0209
#endif //WM_MOUSELAST

// AwtComponent messages
enum {
    WM_AWT_COMPONENT_CREATE = WM_APP,
    WM_AWT_DESTROY_WINDOW,
    WM_AWT_MOUSEENTER,
    WM_AWT_MOUSEEXIT,
    WM_AWT_COMPONENT_SHOW,
    WM_AWT_COMPONENT_HIDE,
    WM_AWT_POPUPMENU_SHOW,
    WM_AWT_COMPONENT_SETFOCUS,
    WM_AWT_LIST_SETMULTISELECT,
    WM_AWT_HANDLE_EVENT,
    WM_AWT_PRINT_COMPONENT,
    WM_AWT_RESHAPE_COMPONENT,
    WM_AWT_SETALWAYSONTOP,
    WM_AWT_BEGIN_VALIDATE,
    WM_AWT_END_VALIDATE,
    WM_AWT_FORWARD_CHAR,
    WM_AWT_FORWARD_BYTE,
    WM_AWT_SET_SCROLL_INFO,
    WM_AWT_CREATECONTEXT,
    WM_AWT_DESTROYCONTEXT,
    WM_AWT_ASSOCIATECONTEXT,
    WM_AWT_PRE_KEYDOWN,
    WM_AWT_PRE_KEYUP,
    WM_AWT_PRE_SYSKEYDOWN,
    WM_AWT_PRE_SYSKEYUP,

    /* deleted DND mesg's */

    WM_AWT_ENDCOMPOSITION, 
    WM_AWT_DISPOSE, 
    WM_AWT_SETCONVERSIONSTATUS, 
    WM_AWT_GETCONVERSIONSTATUS, 
    WM_AWT_SETOPENSTATUS, 
    WM_AWT_GETOPENSTATUS,
    WM_AWT_ACTIVATEKEYBOARDLAYOUT,
    WM_AWT_OPENCANDIDATEWINDOW,
    WM_AWT_DLG_SHOWMODAL, 
    WM_AWT_DLG_ENDMODAL, 
    WM_AWT_SETCURSOR, 
    WM_AWT_WAIT_FOR_SINGLE_OBJECT, 
    WM_AWT_INVOKE_METHOD, 
    WM_AWT_INVOKE_VOID_METHOD, 
    WM_AWT_EXECUTE_SYNC, 
    
    WM_AWT_CURSOR_SYNC,
    WM_AWT_GETDC,
    WM_AWT_RELEASEDC,
    WM_AWT_RELEASE_ALL_DCS,
    WM_AWT_SHOWCURSOR,
    WM_AWT_HIDECURSOR,
    WM_AWT_CREATE_PRINTED_PIXELS,

    /* Synchronize ddraw fullscreen events on Windows event thread */
    WM_AWT_DD_CREATE_SURFACE,
    WM_AWT_DD_ENTER_FULLSCREEN,
    WM_AWT_DD_EXIT_FULLSCREEN,
    WM_AWT_DD_SET_DISPLAY_MODE,
    WM_AWT_DD_RESTORE_DISPLAY_MODE
};

#endif  // AWTMSG_H
