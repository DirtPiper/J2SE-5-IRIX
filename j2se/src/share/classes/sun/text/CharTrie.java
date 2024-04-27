/*
 * @(#)CharTrie.java	1.1 03/08/12
 *
 * Portions Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 *******************************************************************************
 * (C) Copyright IBM Corp. 1996-2003 - All Rights Reserved                     *
 *                                                                             *
 * The original version of this source code and documentation is copyrighted   *
 * and owned by IBM, These materials are provided under terms of a License     *
 * Agreement between IBM and Sun. This technology is protected by multiple     *
 * US and International patents. This notice and attribution to IBM may not    *
 * to removed.                                                                 *
 *******************************************************************************
 */

package sun.text;

import java.io.InputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * <p>
 * Trie implementation which stores data in char, 16 bits.
 * @author synwee
 * @see com.ibm.icu.util.Trie
 * @since release 2.1, Jan 01 2002
 *</p>
 */

// note that i need to handle the block calculations later, since chartrie
// in icu4c uses the same index array.
/// problem: public class access changed to package private
class CharTrie extends Trie
{
    
    // public constructors ---------------------------------------------

    /**
    * <p>Creates a new Trie with the settings for the trie data.</p>
    * <p>Unserialize the 32-bit-aligned input stream and use the data for the 
    * trie.</p>
    * @param inputStream file input stream to a ICU data file, containing 
    *                    the trie
    * @param dataManipulate, object which provides methods to parse the char 
    *                        data
    * @exception IOException thrown when data reading fails
    * @draft 2.1
    */
    public CharTrie(InputStream inputStream, 
                    DataManipulate dataManipulate) throws IOException
    {
        super(inputStream, dataManipulate);
        
        if (!isCharTrie()) {
            throw new IllegalArgumentException(
                               "Data given does not belong to a char trie.");
        }
    }
    
    // public methods --------------------------------------------------
     
    /**
     * Gets the value associated with the codepoint.
     * If no value is associated with the codepoint, a default value will be
     * returned.
     * @param ch codepoint
     * @return offset to data
    * @draft 2.1
     */
    public final char getCodePointValue(int ch)
    {
        int offset = getCodePointOffset(ch);
        
        // return -1 if there is an error, in this case we return the default
        // value: m_initialValue_
        return (offset >= 0) ? m_data_[offset] : m_initialValue_;
    }

    /**
     * Gets the value to the data which this lead surrogate character points
     * to.
     * Returned data may contain folding offset information for the next
     * trailing surrogate character.
     * This method does not guarantee correct results for trail surrogates.
     * @param ch lead surrogate character
     * @return data value
    * @draft 2.1
     */
    public final char getLeadValue(char ch)
    {
       return m_data_[getLeadOffset(ch)];
    }

    /**
    * Get the value associated with the BMP code point.
    * Lead surrogate code points are treated as normal code points, with
    * unfolded values that may differ from getLeadValue() results.
    * @param ch the input BMP code point
    * @return trie data value associated with the BMP codepoint
    * @draft 2.1
    */
    public final char getBMPValue(char ch)
    {
        return m_data_[getBMPOffset(ch)];
    }

    /**
     * Get the value associated with a pair of surrogates.
     * @param lead a lead surrogate
     * @param trail a trail surrogate
     * @param trie data value associated with the surrogate characters
    * @draft 2.1
     */
    public final char getSurrogateValue(char lead, char trail)
    {
    	int offset = getSurrogateOffset(lead, trail);
    	if (offset > 0) {
            return m_data_[offset];
        }
        return m_initialValue_;
    }
    
    /**
    * <p>Get a value from a folding offset (from the value of a lead surrogate)
    * and a trail surrogate.</p>
    * <p>If the 
    * @param leadvalue value associated with the lead surrogate which contains
    *        the folding offset
    * @param trail surrogate
    * @return trie data value associated with the trail character
    * @draft 2.1
    */
    public final char getTrailValue(int leadvalue, char trail)
    {
        if (m_dataManipulate_ == null) {
            throw new NullPointerException(
                             "The field DataManipulate in this Trie is null");
        }
        int offset = m_dataManipulate_.getFoldingOffset(leadvalue);
        if (offset > 0) {
	        return m_data_[getRawOffset(offset, 
                                        (char)(trail & SURROGATE_MASK_))];
        }
        return m_initialValue_;
    }
    
    /**
     * <p>Gets the latin 1 fast path value.</p>
     * <p>Note this only works if latin 1 characters have their own linear 
     * array.</p>
     * @param ch latin 1 characters
     * @return value associated with latin character
     */
   public final char getLatin1LinearValue(char ch) 
    {
    	return m_data_[INDEX_STAGE_3_MASK_ + 1 + m_dataOffset_ + ch];
    }
    
    
    // protected methods -----------------------------------------------

    /**
    * <p>Parses the input stream and stores its trie content into a index and
    * data array</p>
    * @param inputStream data input stream containing trie data
    * @exception IOException thrown when data reading fails
    */
    protected final void unserialize(InputStream inputStream) 
                                                throws IOException
    {
        DataInputStream input = new DataInputStream(inputStream);
        int indexDataLength = m_dataOffset_ + m_dataLength_;
        m_index_ = new char[indexDataLength];

        byte[] bb = new byte[indexDataLength * 2];
        input.read(bb);
        int j = 0;
        for (int i = 0; i < indexDataLength; i ++) {
            m_index_[i] = (char)((bb[j++] << 8) | (bb[j++] & 0xff));
        }

        m_data_           = m_index_;
        m_initialValue_   = m_data_[m_dataOffset_];
    }
    
    /**
     * Gets the offset to the data which the surrogate pair points to.
     * @param lead lead surrogate
     * @param trail trailing surrogate
     * @return offset to data
    * @draft 2.1
     */
    protected final int getSurrogateOffset(char lead, char trail)
    {
        if (m_dataManipulate_ == null) {
            throw new NullPointerException(
                             "The field DataManipulate in this Trie is null");
        }
        
        // get fold position for the next trail surrogate
        int offset = m_dataManipulate_.getFoldingOffset(getLeadValue(lead));

        // get the real data from the folded lead/trail units
        if (offset > 0) {
            return getRawOffset(offset, (char)(trail & SURROGATE_MASK_));
        }

        // return -1 if there is an error, in this case we return the default
        // value: m_initialValue_
        return -1;
    }
    
    /**
     * Gets the value at the argument index.
     * For use internally in com.ibm.icu.util.TrieEnumeration.
     * @param index value at index will be retrieved
     * @return 32 bit value
     * @see com.ibm.icu.util.TrieEnumeration
    * @draft 2.1
     */
    protected final int getValue(int index)
    {
        return m_data_[index];
    }

    /**
     * Gets the default initial value
     * @return 32 bit value 
    * @draft 2.1
     */
    protected final int getInitialValue()
    {
        return m_initialValue_;
    }
  
    // private data members --------------------------------------------

    /**
    * Default value
    */
    private char m_initialValue_;
    /**
    * Array of char data
    */
    private char m_data_[];
}