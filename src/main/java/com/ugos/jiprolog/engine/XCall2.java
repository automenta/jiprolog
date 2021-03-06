/*
 * 23/04/2014
 *
 * Copyright (C) 1999-2014 Ugo Chirico
 *
 * This is free software; you can redistribute it and/or
 * modify it under the terms of the Affero GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Affero GNU General Public License for more details.
 *
 * You should have received a copy of the Affero GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package com.ugos.jiprolog.engine;

//import com.ugos.debug.*;

import java.util.Hashtable;

final class XCall2 extends BuiltIn
{
    private JIPXCall      m_exObj;
    private static final Hashtable<String, Class> classTable = new Hashtable<>();

    // Called by prolog engine when it tries to unify the goal
    // (in this case the goal is a call to a built in predicate)
    public final boolean unify(final Hashtable<Variable, Variable> varsTbl)
    {
        if (m_exObj == null)  // Called for the first time
        {
            // Get JIPXCall class Name (first parameter)
            final PrologObject exClass = getRealTerm(getParam(1));

            // extract the Atom related to the class name
            String strXClassName;
//            if(exClass instanceof PString)
//                strXClassName = ((PString)exClass).getString();
//            else
            if(exClass instanceof Atom)
                strXClassName = ((Atom)exClass).getName();
            else
                throw new JIPTypeException(JIPTypeException.ATOM_OR_STRING, exClass);

            // Create an instance of JIPXCall class
            m_exObj = createXCall(strXClassName);

            // Set current JIPEngine instance
            m_exObj.init(this);
        }

        final PrologObject params = getRealTerm(getParam(2));
        if(!(params instanceof List))
        	throw new JIPTypeException(JIPTypeException.LIST, params);

        JIPCons exParams = new JIPCons(((List)params).getConsCell());

        Hashtable<JIPVariable, JIPVariable> jipVarsTable = new Hashtable<>();
        // Invoke JIPXCall class
        boolean unify = m_exObj.unify(exParams, jipVarsTable);

        if(unify)
        {
            for(JIPVariable jvar : jipVarsTable.values())
        	{
                Variable var = (Variable) jvar.getTerm();
                varsTbl.put(var, var);
        	}
        }

        return unify;
    }


    // return true if the JIPXCall class is deterministic
    public final boolean hasMoreChoicePoints()
    {
        return m_exObj == null || m_exObj.hasMoreChoicePoints();
    }

    // Create an instance of JIPXCall class
    @SuppressWarnings("rawtypes")
    private static JIPXCall createXCall(String strXClassName)
    {
        try
        {
        	//System.out.println(strXClassName);
            // Get the correct class name
//            if(strXClassName.charAt(0) == 39 || strXClassName.charAt(0) == 34)
//            {
//                strXClassName = strXClassName.substring(1, strXClassName.length() - 1);
//            }

            Class xclass;
            if(classTable.containsKey(strXClassName))
            {
            	xclass = classTable.get(strXClassName);
            }
            else if((JIPEngine.getClassLoader()) != null)
            {
            	xclass = JIPEngine.getClassLoader().loadClass(strXClassName);
            	classTable.put(strXClassName, xclass);
            }
            else
            {
            	xclass = Class.forName(strXClassName);
            	classTable.put(strXClassName, xclass);
            }

            JIPXCall exObj = (JIPXCall) xclass.newInstance();

            return exObj;
        }
        catch(ClassNotFoundException | ClassCastException | InstantiationException | IllegalAccessException ex)
        {
        	throw JIPExistenceException.createProcedureException(Atom.createAtom(strXClassName));
        } catch(NoClassDefFoundError ex)
        {
        	ex.printStackTrace();
        	throw JIPExistenceException.createProcedureException(Atom.createAtom(strXClassName));
        }
    }
}
