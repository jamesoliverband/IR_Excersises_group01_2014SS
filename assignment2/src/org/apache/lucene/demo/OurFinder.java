package org.apache.lucene.demo;

import java.io.File;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

class OurFinder extends SimpleFileVisitor<Path> {
	public ArrayList<File> foundFiles;
	
	public OurFinder() {
		this.foundFiles = new ArrayList<File>();
	}
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        if (".svn".equals(dir.getFileName().toString())) {
            return FileVisitResult.SKIP_SUBTREE;
        }
        //System.out.println(dir);
        return FileVisitResult.CONTINUE;
    }
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        //System.out.println(file);
        this.foundFiles.add(file.toFile());
        return FileVisitResult.CONTINUE;
    }
}
