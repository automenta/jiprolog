package com.ugos.jiprolog;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by me on 1/21/16.
 */
public class JIPrologTest {

    @Test
    public void testLibraryUsage()  {
        JIPEngine jip = new JIPEngine();

        jip.asserta(jip.parse("abc(y)"));
        jip.asserta(jip.parse("abc(x)"));

        //System.out.println("running goal " + g);

        //jip.setTrace(true);

        JIPQuery query = jip.querySynch("abc(X).");

        //System.out.println(term);
        assertEquals("abc(x)", query.solveNext().toString());
        assertEquals("abc(y)", query.solveNext().toString());
        //System.out.println(term);

        query.close();

    }
    @Test
    public void testLibraryUsage2()  {
        JIPEngine jip = new JIPEngine();

        //System.out.println("running goal " + g);

        JIPQuery query = jip.querySynch("X=50.  X is 10 * 5?");



        //System.out.println(term);
        assertEquals("=(50,50)", query.solveNext().toString());

        //System.out.println(term);

        query.close();

    }

}