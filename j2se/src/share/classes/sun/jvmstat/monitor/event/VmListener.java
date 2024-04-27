/*
 * @(#)VmListener.java	1.1 04/02/23
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package sun.jvmstat.monitor.event;

import java.util.EventListener;

/**
 * Interface for listeners of MonitoredVm events.
 *
 * @author Brian Doherty
 * @version 1.1, 02/23/04
 * @since 1.5
 * @see sun.jvmstat.monitor.MonitoredVm
 */
public interface VmListener extends EventListener {

    /**
     * Invoked when instrumentation objects are inserted into or removed
     * from the MonitoredVm.
     *
     * @param event the object describing the event.
     */
    void monitorStatusChanged(MonitorStatusChangeEvent event);

    /**
     * Invoked when instrumentation objects are updated. This event is
     * generated at a fixed interval as determined by the polling rate
     * of the MonitoredVm that the VmListener is registered with.
     *
     * @param event the object describing the event.
     */
    void monitorsUpdated(VmEvent event);

    /**
     * Invoked when the connection to the MonitoredVm has disconnected
     * due to communication errors.
     *
     * @param event the object describing the event.
     */
    void disconnected(VmEvent event);
}
