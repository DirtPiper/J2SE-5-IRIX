/*
 * @(#)Package.java	1.20 04/01/12
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package sun.tools.java;

import java.util.Enumeration;
import java.io.File;
import java.io.IOException;

/**
 * This class is used to represent the classes in a package.
 *
 * WARNING: The contents of this source file are not part of any
 * supported API.  Code that depends on them does so at its own risk:
 * they are subject to change or removal without notice.
 */
public
class Package {
    /**
     * The path which we use to locate source files.
     */
    ClassPath sourcePath;

    /**
     * The path which we use to locate class (binary) files.
     */
    ClassPath binaryPath;

    /**
     * The path name of the package.
     */
    String pkg;

    /**
     * Create a package given a class path, and package name.
     */
    public Package(ClassPath path, Identifier pkg) throws IOException {
	this(path, path, pkg);
    }

    /**
     * Create a package given a source path, binary path, and package
     * name.
     */
    public Package(ClassPath sourcePath,
		   ClassPath binaryPath,
		   Identifier pkg)
    throws IOException {
	if (pkg.isInner())
	    pkg = Identifier.lookup(pkg.getQualifier(), pkg.getFlatName());
	this.sourcePath = sourcePath;
	this.binaryPath = binaryPath;
	this.pkg = pkg.toString().replace('.', File.separatorChar);
    }

    /**
     * Check if a class is defined in this package.
     * (If it is an inner class name, it is assumed to exist
     * only if its binary file exists.  This is somewhat pessimistic.)
     */
    public boolean classExists(Identifier className) {
	return getBinaryFile(className) != null ||
		!className.isInner() &&
	       getSourceFile(className) != null;
    }

    /**
     * Check if the package exists
     */
    public boolean exists() {
	// Look for the directory on our binary path.
	ClassFile dir = binaryPath.getDirectory(pkg);
	if (dir != null && dir.isDirectory()) {
	    return true;
	}

	if (sourcePath != binaryPath) {
	    // Look for the directory on our source path.
	    dir = sourcePath.getDirectory(pkg);
	    if (dir != null && dir.isDirectory()) {
		return true;
	    }
	}

	/* Accommodate ZIP files without CEN entries for directories
	 * (packages): look on class path for at least one binary
	 * file or one source file with the right package prefix
	 */
	String prefix = pkg + File.separator;

	return binaryPath.getFiles(prefix, ".class").hasMoreElements()
	    || sourcePath.getFiles(prefix, ".java").hasMoreElements();
    }

    private String makeName(String fileName) {
	return pkg.equals("") ? fileName : pkg + File.separator + fileName;
    }

    /**
     * Get the .class file of a class
     */
    public ClassFile getBinaryFile(Identifier className) {
	className = Type.mangleInnerType(className);
	String fileName = className.toString() + ".class";
	return binaryPath.getFile(makeName(fileName));
    }

    /**
     * Get the .java file of a class
     */
    public ClassFile getSourceFile(Identifier className) {
	// The source file of an inner class is that of its outer class.
	className = className.getTopName();
	String fileName = className.toString() + ".java";
	return sourcePath.getFile(makeName(fileName));
    }

    public ClassFile getSourceFile(String fileName) {
	if (fileName.endsWith(".java")) {
	    return sourcePath.getFile(makeName(fileName));
	}
	return null;
    }

    public Enumeration getSourceFiles() {
	return sourcePath.getFiles(pkg, ".java");
    }

    public Enumeration getBinaryFiles() {
	return binaryPath.getFiles(pkg, ".class");
    }

    public String toString() {
	if (pkg.equals("")) {
	    return "unnamed package";
	}
	return "package " + pkg;
    }
}
