package org.dosomething.letsdothis.utils;

import android.content.Context;

import org.dosomething.letsdothis.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jon Uy, modified by izzyoji
 *
 * Convert integers to a string representation.
 *
 * Caveats/Limitations:
 *  - # of words to use in a code is dictated by # of arrays provided. It's basically array.length ^ (# of arrays).
 *  - Word arrays provided must be the same length
 *
 * @todo Throw exceptions instead of `assert false`
 */
public class Hashery {

    private static Hashery                      instance;

    private        int                          mBase;
    private        int                          mMaxValue;
    private        int                          mCodeLength;
    private        ArrayList<List<String>> mWords;

    @NotNull
    public static synchronized Hashery getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new Hashery(context);
        }

        return instance;
    }

    public Hashery(Context context)
    {

        ArrayList<List<String>> words = new ArrayList<>();
        words.add(Arrays.asList(context.getResources().getStringArray(R.array.word_1)));
        words.add(Arrays.asList(context.getResources().getStringArray(R.array.word_2)));
        words.add(Arrays.asList(context.getResources().getStringArray(R.array.word_3)));

        if(words.isEmpty())
        {
            assert false;
        }

        int testSize = 0;
        for (int i = 0; i < words.size(); i++) {
            if (i == 0) {
                testSize = words.get(i).size();
                if (testSize == 0) {
                    assert false;
                }
            }
            else if (testSize != words.get(i).size()) {
                assert false;
            }
        }

        mWords = words;
        mCodeLength = mWords.size();
        mBase = mWords.get(0).size();

        int base = mBase;
        for (int i = 1; i < mWords.size(); i++) {
            base *= mBase;
        }
        mMaxValue = base - 1; // - 1 because we start converting at 0
    }

    /**
     * Returns max possible value that can be decoded given the nested list of words provided in the ctor.
     *
     * @return int
     */
    public int getMaxValue() {
        return mMaxValue;
    }

    /**
     * Encode a base-10 number to a unique string representation.
     *
     * @param val Number to encode
     * @return String
     */
    public String encode(int val) {
        if (val > mMaxValue) {
            assert false;
        }

        ArrayList<Integer> baseConverted = baseConversion(val);

        return convertToWord(baseConverted);
    }

    /**
     * This is the first step in the process of converting a number to its String representation. The
     * number gets converted from base-10 to base-N, where N is the length of an item in mWords.
     *
     * For example, if mWords[0] is an array of length 40, then we're converting to base-40.
     *
     * This then gives us an array of integers where the length of the resulting array is equal to
     * the length of mWords.
     *
     * @param val Base-10 number to convert
     * @return ArrayList representation of the base-N conversion
     */
    private ArrayList<Integer> baseConversion(int val) {
        ArrayList<Integer> result = new ArrayList<Integer>();

        int quotient = val;
        while (quotient != 0) {
            int dividend = quotient;
            quotient = dividend / mBase;
            int remainder = dividend % mBase;

            result.add(0, new Integer(remainder));
        }

        return result;
    }

    /**
     * This is the second step of the conversion. Maps the integer values of the base-N conversion
     * to corresponding strings in the mWords array of arrays.
     *
     * @param vals ArrayList representation of the base-N converted number
     * @return String
     */
    private String convertToWord(ArrayList<Integer> vals) {
        String result = "";
        int valPos = vals.size() - 1;
        for (int i = mCodeLength - 1; i >= 0; i--) {
            int val = 0;
            if (valPos >= 0) {
                val = vals.get(valPos).intValue();
                valPos--;
            }

            List<String> words = mWords.get(i);
            result = words.get(val) + " " + result;
        }

        return result;
    }

    /**
     * Decode a string to its original base-10 number.
     *
     * @param val String to decode
     * @return int On success, returns the decoded number. On failure, returns -1.
     */
    public int decode(String val) {
        ArrayList<String> baseN = new ArrayList<String>();

        // With no clear delimiter between words, search for the word matches against the mWords arrays.

        int matchesFound = 0;

        String encoded = val;
        while (encoded.length() > 0) {
            boolean matchFound = false;

            // If text still remains and all word arrays have been gone through, then it's invalid.
            if (matchesFound >= mWords.size()) {
                return -1;
            }

            List<String> words = mWords.get(matchesFound);
            for (int i = 0; i < words.size() && !matchFound; i++) {
                String word = words.get(i);
                if (encoded.indexOf(word) == 0) {
                    matchFound = true;

                    // Add to baseN array
                    baseN.add(Integer.toString(i));

                    // Increment to move on to next mWords item
                    matchesFound++;

                    // Remove word from encoded string
                    encoded = encoded.substring(word.length());
                }
            }

            // If no match is found on any of the word checks, then we have an invalid code.
            if (!matchFound) {
                return -1;
            }
        }

        // Sanity check that length of baseN is same as mWords
        if (baseN.size() != mWords.size()) {
            return -1;
        }

        // The result of the previous step is a base-N representation of the original base-10 number.
        // Now we convert back to base-10.

        int result = 0;
        for (int i = 0; i < baseN.size(); i++) {
            int n = Integer.parseInt(baseN.get(i));
            int pow = baseN.size() - 1 - i;

            result += n * Math.pow((double)mBase, (double)pow);
        }

        return result;
    }

}
