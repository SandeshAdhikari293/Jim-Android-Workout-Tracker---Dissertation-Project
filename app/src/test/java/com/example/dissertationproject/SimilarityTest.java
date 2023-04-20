package com.example.dissertationproject;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import org.junit.Test;

public class SimilarityTest {

    /**
     *
     * Test cases to check whether the similarity of two strings function is accurate
     *
     */

    @Test
    public void testSimilarity1() {
        assertEquals(1.0, Utils.findSimilarity("test", "test"));
    }

    @Test
    public void testSimilarity2() {
        assertEquals(1.0, Utils.findSimilarity("", ""));
    }

    @Test
    public void testSimilarity3() {
        assertEquals(0.0, Utils.findSimilarity("", "somerandomstring"));
    }

    @Test
    public void testSimilarity4() {
        assertEquals(0.0, Utils.findSimilarity("", "test"));
    }

    @Test
    public void testSimilarity5() {
        assertEquals(0.5, Utils.findSimilarity("test", "testtest"));
    }
    @Test
    public void testSimilarity6() {
        assertEquals(0.5, Utils.findSimilarity("test", "te"));
    }

    @Test
    public void testSimilarity7() {
        assertEquals(0.5, Utils.findSimilarity("te", "test"));
    }

    @Test
    public void testSimilarity8() {
        assertEquals(0.25, Utils.findSimilarity("test", "fourtestfourfour"));
    }

    @Test
    public void testSimilarity9() {
        assertEquals(0.1, Utils.findSimilarity("i", "qiqqqqqqqq"));
    }

    @Test
    public void testSimilarity10() {
        assertEquals(1.0, Utils.findSimilarity("biglongstringhellohitest",
                "biglongstringhellohitest"));
    }
}
