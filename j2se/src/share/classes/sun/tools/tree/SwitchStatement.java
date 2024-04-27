/*
 * @(#)SwitchStatement.java	1.39 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package sun.tools.tree;

import sun.tools.java.*;
import sun.tools.asm.Assembler;
import sun.tools.asm.Label;
import sun.tools.asm.SwitchData;
import java.io.PrintStream;
import java.util.Hashtable;

/**
 * WARNING: The contents of this source file are not part of any
 * supported API.  Code that depends on them does so at its own risk:
 * they are subject to change or removal without notice.
 */
public
class SwitchStatement extends Statement {
    Expression expr;
    Statement args[];

    /**
     * Constructor
     */
    public SwitchStatement(long where, Expression expr, Statement args[]) {
	super(SWITCH, where);
	this.expr = expr;
	this.args = args;
    }

    /**
     * Check statement
     */
    Vset check(Environment env, Context ctx, Vset vset, Hashtable exp) {
	checkLabel(env, ctx);
	CheckContext newctx = new CheckContext(ctx, this);
	vset = expr.checkValue(env, newctx, reach(env, vset), exp);
	Type switchType = expr.type;

	expr = convert(env, newctx, Type.tInt, expr);

	Hashtable tab = new Hashtable();
	boolean hasDefault = false;
	// Note that vs is reset to vset.copy() on every case label.
	// If the first substatement is not a case label, it is unreached.
	Vset vs = DEAD_END;

	for (int i = 0 ; i < args.length ; i++) {
	    Statement s = args[i];
	    
	    if (s.op == CASE) {

		vs = s.check(env, newctx, vs.join(vset.copy()), exp);

		Expression lbl = ((CaseStatement)s).expr;
		if (lbl != null) {
		    if (lbl instanceof IntegerExpression) {
			Integer Ivalue = 
			    (Integer)(((IntegerExpression)lbl).getValue());
			int ivalue = Ivalue.intValue();
			if (tab.get(lbl) != null) {
			    env.error(s.where, "duplicate.label", Ivalue);
			} else {
			    tab.put(lbl, s);
			    boolean overflow;
			    switch (switchType.getTypeCode()) { 
			        case TC_BYTE:
				    overflow = (ivalue != (byte)ivalue); break;
			        case TC_SHORT: 
				    overflow = (ivalue != (short)ivalue); break;
			        case TC_CHAR:
				    overflow = (ivalue != (char)ivalue); break;
			        default: 
				    overflow = false;
			    }
			    if (overflow) { 
				env.error(s.where, "switch.overflow", 
					  Ivalue, switchType);
			    }
			}
		    } else {
			// Suppose a class got an error early on during
			// checking.  It will set all of its members to
			// have the status "ERROR".  Now suppose that a
			// case label refers to one of this class's
			// fields.  When we check the case label, the
			// compiler will try to inline the FieldExpression.
			// Since the expression has ERROR status, it doesn't
			// inline.  This means that instead of the case
			// label being an IntegerExpression, it will still
			// be a FieldExpression, and we will end up in this
			// else block.  So, before we just assume that
			// the expression isn't constant, do a check to
			// see if it was constant but unable to inline.
			// This eliminates some spurious error messages.
			// (Bug id 4067498).
			if (!lbl.isConstant() ||
			    lbl.getType() != Type.tInt) {
			    env.error(s.where, "const.expr.required");
			}
		    }
		} else {
		    if (hasDefault) {
			env.error(s.where, "duplicate.default");
		    }
		    hasDefault = true;
		}
	    } else {
		vs = s.checkBlockStatement(env, newctx, vs, exp);
	    }
	}
	if (!vs.isDeadEnd()) {
	    newctx.vsBreak = newctx.vsBreak.join(vs);
	}
	if (hasDefault) 
	    vset = newctx.vsBreak;
	return ctx.removeAdditionalVars(vset);
    }

    /**
     * Inline
     */
    public Statement inline(Environment env, Context ctx) {
	ctx = new Context(ctx, this);
	expr = expr.inlineValue(env, ctx);
	for (int i = 0 ; i < args.length ; i++) {
	    if (args[i] != null) {
		args[i] = args[i].inline(env, ctx);
	    }
	}
	return this;
    }

    /**
     * Create a copy of the statement for method inlining
     */
    public Statement copyInline(Context ctx, boolean valNeeded) {
	SwitchStatement s = (SwitchStatement)clone();
	s.expr = expr.copyInline(ctx);
	s.args = new Statement[args.length];
	for (int i = 0 ; i < args.length ; i++) {
	    if (args[i] != null) {
		s.args[i] = args[i].copyInline(ctx, valNeeded);
	    }
	}
	return s;
    }

    /**
     * The cost of inlining this statement
     */
    public int costInline(int thresh, Environment env, Context ctx) {
	int cost = expr.costInline(thresh, env, ctx);
	for (int i = 0 ; (i < args.length) && (cost < thresh) ; i++) {
	    if (args[i] != null) {
		cost += args[i].costInline(thresh, env, ctx);
	    }
	}
	return cost;
    }

    /**
     * Code
     */
    public void code(Environment env, Context ctx, Assembler asm) {
	CodeContext newctx = new CodeContext(ctx, this);

	expr.codeValue(env, newctx, asm);

	SwitchData sw = new SwitchData();
	boolean hasDefault = false;

	for (int i = 0 ; i < args.length ; i++) {
	    Statement s = args[i];
	    if ((s != null) && (s.op == CASE)) {
		Expression e = ((CaseStatement)s).expr;
		if (e != null) {
		    sw.add(((IntegerExpression)e).value, new Label());
		}
// JCOV
		else {
		    hasDefault = true;
		}
// end JCOV
	    } 
	}

// JCOV
	if (env.coverage())
            sw.initTableCase();
// end JCOV
	asm.add(where, opc_tableswitch, sw);

	for (int i = 0 ; i < args.length ; i++) {
	    Statement s = args[i];
	    if (s != null) {
		if (s.op == CASE) {
		    Expression e = ((CaseStatement)s).expr;
		    if (e != null) {
			asm.add(sw.get(((IntegerExpression)e).value));
// JCOV
                        sw.addTableCase(((IntegerExpression)e).value, s.where);
// end JCOV
		    } else {
			asm.add(sw.getDefaultLabel());
// JCOV
                        sw.addTableDefault(s.where);
// end JCOV
/* JCOV                 hasDefault = true;   end JCOV */
		    }
		} else {
		    s.code(env, newctx, asm);
		}
	    }
	}

	if (!hasDefault) {
	    asm.add(sw.getDefaultLabel());
	}
	asm.add(newctx.breakLabel);
    }

    /**
     * Print
     */
    public void print(PrintStream out, int indent) {
	super.print(out, indent);
	out.print("switch (");
	expr.print(out);
	out.print(") {\n");
	for (int i = 0 ; i < args.length ; i++) {
	    if (args[i] != null) {
		printIndent(out, indent + 1);
		args[i].print(out, indent + 1);
		out.print("\n");
	    }
	}
	printIndent(out, indent);
	out.print("}");
    }
}
