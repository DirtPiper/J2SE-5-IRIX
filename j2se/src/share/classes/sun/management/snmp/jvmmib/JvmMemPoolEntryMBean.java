/*
 * @(#)JvmMemPoolEntryMBean.java	1.6 04/07/26
 * 
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package sun.management.snmp.jvmmib;

//
// Generated by mibgen version 5.0 (06/02/03) when compiling JVM-MANAGEMENT-MIB in standard metadata mode.
//


// jmx imports
//
import com.sun.jmx.snmp.SnmpStatusException;

/**
 * This interface is used for representing the remote management interface for the "JvmMemPoolEntry" MBean.
 */
public interface JvmMemPoolEntryMBean {

    /**
     * Getter for the "JvmMemPoolCollectMaxSize" variable.
     */
    public Long getJvmMemPoolCollectMaxSize() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolCollectCommitted" variable.
     */
    public Long getJvmMemPoolCollectCommitted() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolCollectUsed" variable.
     */
    public Long getJvmMemPoolCollectUsed() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolCollectThreshdSupport" variable.
     */
    public EnumJvmMemPoolCollectThreshdSupport getJvmMemPoolCollectThreshdSupport() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolCollectThreshdCount" variable.
     */
    public Long getJvmMemPoolCollectThreshdCount() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolCollectThreshold" variable.
     */
    public Long getJvmMemPoolCollectThreshold() throws SnmpStatusException;

    /**
     * Setter for the "JvmMemPoolCollectThreshold" variable.
     */
    public void setJvmMemPoolCollectThreshold(Long x) throws SnmpStatusException;

    /**
     * Checker for the "JvmMemPoolCollectThreshold" variable.
     */
    public void checkJvmMemPoolCollectThreshold(Long x) throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolMaxSize" variable.
     */
    public Long getJvmMemPoolMaxSize() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolCommitted" variable.
     */
    public Long getJvmMemPoolCommitted() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolUsed" variable.
     */
    public Long getJvmMemPoolUsed() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolInitSize" variable.
     */
    public Long getJvmMemPoolInitSize() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolThreshdSupport" variable.
     */
    public EnumJvmMemPoolThreshdSupport getJvmMemPoolThreshdSupport() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolThreshdCount" variable.
     */
    public Long getJvmMemPoolThreshdCount() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolThreshold" variable.
     */
    public Long getJvmMemPoolThreshold() throws SnmpStatusException;

    /**
     * Setter for the "JvmMemPoolThreshold" variable.
     */
    public void setJvmMemPoolThreshold(Long x) throws SnmpStatusException;

    /**
     * Checker for the "JvmMemPoolThreshold" variable.
     */
    public void checkJvmMemPoolThreshold(Long x) throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolPeakReset" variable.
     */
    public Long getJvmMemPoolPeakReset() throws SnmpStatusException;

    /**
     * Setter for the "JvmMemPoolPeakReset" variable.
     */
    public void setJvmMemPoolPeakReset(Long x) throws SnmpStatusException;

    /**
     * Checker for the "JvmMemPoolPeakReset" variable.
     */
    public void checkJvmMemPoolPeakReset(Long x) throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolState" variable.
     */
    public EnumJvmMemPoolState getJvmMemPoolState() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolType" variable.
     */
    public EnumJvmMemPoolType getJvmMemPoolType() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolName" variable.
     */
    public String getJvmMemPoolName() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolPeakMaxSize" variable.
     */
    public Long getJvmMemPoolPeakMaxSize() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolIndex" variable.
     */
    public Integer getJvmMemPoolIndex() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolPeakCommitted" variable.
     */
    public Long getJvmMemPoolPeakCommitted() throws SnmpStatusException;

    /**
     * Getter for the "JvmMemPoolPeakUsed" variable.
     */
    public Long getJvmMemPoolPeakUsed() throws SnmpStatusException;

}
