/*
 * 23/04/2014
 *
 * Copyright (C) 1999-2014 Ugo Chirico - http://www.ugochirico.com
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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

final class CurrentAtom1 extends BuiltIn
{
    private Enumeration<Atom> m_enum = null;
    private Iterator<Atom> iterator;

    public final boolean unify(final Hashtable<Variable, Variable> varsTbl)
    {
        if(m_enum == null)
            m_enum = Atom.s_atomTable.elements();
        iterator = Atom.s_atomTable.values().iterator();

        if(iterator.hasNext())
            return getParam(1).unify(iterator.next(), varsTbl);
        else
            return false;
    }

    public final boolean hasMoreChoicePoints()
    {
        return m_enum == null || iterator.hasNext();
    }
}
