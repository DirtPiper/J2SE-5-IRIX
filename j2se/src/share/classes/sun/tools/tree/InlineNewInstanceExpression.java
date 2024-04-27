/*
 * @(#)InlineNewInstanceExpression.java	1.22 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package sun.tools.tree;

import sun.tools.java.*;
import sun.tools.asm.Label;
import sun.tools.asm.Assembler;
import java.io.PrintStream;
import java.util.Vector;

/**
 * WARNING: The contents of this source file are not part of any
 * supported API.  Code that depends on them does so at its own risk:
 * they are subject to change or removal without notice.
 */
public
class InlineNewInstanceExpression extends Expression {
    MemberDefinition field;
    Statement body;

    /**
     * Constructor
     */
    InlineNewInstanceExpression(long where, Type type, MemberDefinition field, Statement body) {
	super(INLINENEWINSTANCE, where, type);
	this.field = field;
	this.body = body;
    }
    /**
     * Inline
     */
    public Expression inline(Environment env, Context ctx) {
	return inlineValue(env, ctx);
    } 
    public Expression inlineValue(Environment env, Context ctx) {
	if (body != null) {
	    LocalMember v = (LocalMember)field.getArguments().elementAt(0);
	    Context newctx = new Context(ctx, this);
	    newctx.declare(env, v);
	    body = body.inline(env, newctx);
	}
	if ((body != null) && (body.op == INLINERETURN)) {
	    body = null;
	}
	return this;
    }

    /**
     * Create a copy of the expression for method inlining
     */
    public Expression copyInline(Context ctx) {
	InlineNewInstanceExpression e = (InlineNewInstanceExpression)clone();
	e.body = body.copyInline(ctx, true);
	return e;
    }

    /**
     * Code
     */
    public void code(Environment env, Context ctx, Assembler asm) {
	codeCommon(env, ctx, asm, false);
    }
    public void codeValue(Environment env, Context ctx, Assembler asm) {
	codeCommon(env, ctx, asm, true);
    }
    private void codeCommon(Environment env, Context ctx, Assembler asm,
			    boolean forValue) {
	asm.add(where, opc_new, field.getClassDeclaration());
	if (body != null) {
	    LocalMember v = (LocalMember)field.getArguments().elementAt(0);
	    CodeContext newctx = new CodeContext(ctx, this);
	    newctx.declare(env, v);
	    asm.add(where, opc_astore, new Integer(v.number));
	    body.code(env, newctx, asm);
	    asm.add(newctx.breakLabel);
	    if (forValue) {
		asm.add(where, opc_aload, new Integer(v.number));
	    }
	}
    }

    /**
     * Print
     */
    public void print(PrintStream out) {
	LocalMember v = (LocalMember)field.getArguments().elementAt(0);
	out.println("(" + opNames[op] + "#" + v.hashCode() + "=" + field.hashCode());
	if (body != null) {
	    body.print(out, 1);
	} else {
	    out.print("<empty>");
	}
	out.print(")");
    }
}
