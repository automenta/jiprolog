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

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

//#ifndef _MIDP
//#endif

abstract class PrologObject implements Clearable, Serializable
{
    final static long serialVersionUID = 300000001L;

    private int line;
    private int column;
    private int position;

    public void setPosition(int line, int column, int position)
    {
    	this.line = line;
    	this.column = column;
    	this.position = position;
    }


    public int getLine() {
		return line;
	}


	public int getColumn() {
		return column;
	}


	public int getPosition() {
		return position;
	}


	public final boolean unifiable(final PrologObject obj)
    {
        Hashtable<Variable, Variable> vartbl = new Hashtable<>(10);
        boolean bUnify = _unify(obj, vartbl);

        Iterator<Variable> iterator = vartbl.keySet().iterator();
        while(iterator.hasNext())
        {
            iterator.next().clear();
        }

        return bUnify;
    }

    public final boolean unify(final PrologObject obj, final Hashtable<Variable, Variable> varTbl)
    {
//        System.out.println(toString() + " == " + obj.toString());
//        System.out.println(getClass().toString() + " == " + obj.getClass().toString());

        final Hashtable<Variable, Variable> _varTbl = new Hashtable<>(10);
        Enumeration<Variable> en;
        if(_unify(obj, _varTbl))
        {
            // riporta le variabili instanziate nella vartable
            Iterator<Variable> iterator = _varTbl.keySet().iterator();
            while(iterator.hasNext())
            {
                Variable var = iterator.next();
                varTbl.put(var, var);
            }

            return true;
        }
        else
        {
            // ripulisce le variabili eventualmente instanziate
            Iterator<Variable> iterator = _varTbl.keySet().iterator();
            while(iterator.hasNext())
                iterator.next().clear();

            return false;
        }
    }

    public final String toString()
    {
        return PrettyPrinter.printTerm(this, null, true);
    }

    public final String toString(final JIPEngine engine)
    {
        return PrettyPrinter.printTerm(this, engine.getOperatorManager(),false);
    }

    public final String toStringq(final JIPEngine engine)
    {
        return PrettyPrinter.printTerm(this, engine.getOperatorManager(),true);
    }

    final String toString(final OperatorManager opMan)
    {
        return PrettyPrinter.printTerm(this, opMan, false);
    }

    public final PrologObject copy(boolean flat)
    {
        return copy(flat, new Hashtable(10));
    }

    public final PrologObject getRealTerm()
    {
        if(this instanceof Variable)
            return ((Variable)this).getObject();

        return this;
    }

    public abstract void clear();
    protected abstract PrologObject copy(boolean flat, Hashtable<Variable, PrologObject> varTable);
    protected abstract boolean lessThen(PrologObject obj);
    protected abstract boolean _unify(PrologObject obj, Hashtable<Variable, Variable> varTbl);
    public abstract boolean termEquals(PrologObject obj);
}

























