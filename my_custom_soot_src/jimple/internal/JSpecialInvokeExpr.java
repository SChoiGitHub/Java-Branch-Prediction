/* Soot - a J*va Optimization Framework
 * Copyright (C) 1999 Patrick Lam
 * Copyright (C) 2004 Ondrej Lhotak
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

/*
 * Modified by the Sable Research Group and others 1997-1999.  
 * See the 'credits' file distributed with Soot for the complete list of
 * contributors.  (Soot is distributed at http://www.sable.mcgill.ca/soot)
 */






package soot.jimple.internal;

import soot.*;
import soot.jimple.*;
import soot.toolkits.graph.Block;

import java.util.*;

public class JSpecialInvokeExpr extends AbstractSpecialInvokeExpr 
{	
    public JSpecialInvokeExpr(Local base, SootMethodRef methodRef, List<? extends Value> args)
    {
        super(Jimple.v().newLocalBox(base), methodRef,
             new ImmediateBox[args.size()]);

        for(int i = 0; i < args.size(); i++) {
            this.argBoxes[i] = Jimple.v().newImmediateBox(args.get(i));
        }
    }

    public Object clone() 
    {
        List<Value> clonedArgs = new ArrayList<Value>(getArgCount());
 
        for(int i = 0; i < getArgCount(); i++) {
            clonedArgs.add(i, getArg(i));
        }
        
        return new  JSpecialInvokeExpr((Local) getBase(), methodRef, clonedArgs);
    }

	@Override
	public Block get_containing_block() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set_containing_block(Block b) {
		// TODO Auto-generated method stub
		
	}
}
