package usecase;

import material.Position;
import material.tree.iterators.PreorderIterator;
import material.tree.narytree.LinkedTree;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author vlt23
 *
 * TODO: fallan la mayoria de los test por problemas de espacios y tabulaciones
 *
 */
public class VirtualFileSystem {

    private LinkedTree<String> fileSystem;
    private Position<String>[] fileSystemArrayPos;
    private Integer size;

    public void loadFileSystem(String path) {
        fileSystem = new LinkedTree<>();
        fileSystemArrayPos = new Position[1024];
        Path p = Paths.get(path);
        String textPath = p.getFileName().toString();
        Position<String> rootPos = fileSystem.addRoot(0 + " " + textPath);
        fileSystemArrayPos[0] = rootPos;
        size = 1;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                loadingFileSystem(file, rootPos, 1);
            }
        }
    }

    private void loadingFileSystem(File fileOrDir, Position<String> parentPos, int level) {
        StringBuilder tab = new StringBuilder(level);
        for (int i = 0; i < level; i++) {
            tab.append('\t');
        }
        Position<String> childPos = fileSystem.add(size + " " + tab + fileOrDir.getName(), parentPos);
        fileSystemArrayPos[size] = childPos;
        size++;
        if (fileOrDir.isDirectory()) {
            File[] listOfFiles = fileOrDir.listFiles();
            if (listOfFiles != null) {
                Arrays.sort(listOfFiles);
                for (File file : listOfFiles) {
                    if (!file.isHidden()) {
                        level = level + 1;
                        loadingFileSystem(file, childPos, level);
                        level = level - 1;
                    }
                }
            }
        }
    }

    public String getFileSystem() {
        StringBuilder fileSystem = new StringBuilder(256);
        for (Position<String> fileSystemArrayPo : fileSystemArrayPos) {
            if (fileSystemArrayPo != null) {
                fileSystem.append(fileSystemArrayPo.getElement()).append('\n');
            }
        }
        return fileSystem.toString();
    }

    private void reOrderFileSystem(Position<String> position, int level) {
        StringBuilder tab = new StringBuilder();
        tab.append(' ');
        for (int i = 0; i < level; i++) {
            tab.append('\t');
        }
        for (Position<String> child : fileSystem.children(position)) {
            String element = position.getElement();
            char idElement = element.charAt(0);
            String elementWithoutTab = element.substring(2).replaceAll("\t", "");
            fileSystem.replace(position, Character.toString(idElement) + tab + elementWithoutTab);
            level = level + 1;
            reOrderFileSystem(child, level);
            level = level - 1;
        }
    }

    private void reOrderFSArrayPos(Position<String> parent) {
        for (Position<String> child : fileSystem.children(parent)) {
            fileSystemArrayPos[size] = child;
            size++;
            reOrderFSArrayPos(child);
        }
    }

    public void moveFileById(int idFile, int idTargetFolder) {
        try {
            if (fileSystemArrayPos[idFile] == null || fileSystemArrayPos[idTargetFolder] == null) {
                throw new RuntimeException("Invalid ID");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("Invalid ID.");
        }
        fileSystem.moveSubtree(fileSystemArrayPos[idFile], fileSystemArrayPos[idTargetFolder]);
        reOrderFileSystem(fileSystem.root(), 0);
        fileSystemArrayPos = new Position[1024];
        fileSystemArrayPos[0] = fileSystem.root();
        size = 1;
        reOrderFSArrayPos(fileSystem.root());
    }

    public void removeFileById(int idFile) {
        //fileSystem.remove(fileSystemArrayPos.get(idFile));
    }

    public Iterable<String> findBySubstring(int idStartFile, String substring) {
        ArrayList<String> result = new ArrayList<>();
        Position<String> findS = fileSystemArrayPos[idStartFile];
        PreorderIterator<String> it = new PreorderIterator<>(fileSystem, findS);
        while (it.hasNext()) {
            Position<String> next = it.next();
            if (next.getElement().contains(substring)) {
                String e = next.getElement();
                e = e.replaceAll("\t", "");
                result.add(e);
            }
        }
        return result;
    }

    public Iterable<String> findBySize(int idStartFile, long minSize, long maxSize) {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> aux = new ArrayList<>();
        Position<String> findS = fileSystemArrayPos[idStartFile];
        PreorderIterator<String> it = new PreorderIterator<>(fileSystem, findS);
        long size = 0;
        while (it.hasNext()) {
            Position<String> next =  it.next();
            aux.add(next.getElement());
            size++;
            if (fileSystem.isLeaf(next)) {
                if ((minSize <= size) <= maxSize) {
                    result.addAll(aux);
                    aux = new ArrayList<>();
                    size = 0;
                }
            }
        }
        return result;
    }

    public String getFileVirtualPath(int idFile) {
        throw new RuntimeException("Not yet implemented");
    }

    public String getFilePath(int idFile) {
        throw new RuntimeException("Not yet implemented");
    }

}
