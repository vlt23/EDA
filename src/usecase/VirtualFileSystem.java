package usecase;

import material.Position;
import material.tree.narytree.LinkedTree;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class VirtualFileSystem {

    private LinkedTree<String> fileSystem;
    private ArrayList<Position<String>> fileSystemArrayPos;

    public void loadFileSystem(String path) {
        fileSystem = new LinkedTree<>();
        fileSystemArrayPos = new ArrayList<>();
        Path p = Paths.get(path);
        String textPath = p.getFileName().toString();
        Position<String> rootPos = fileSystem.addRoot(textPath);
        fileSystemArrayPos.add(rootPos);
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
        Position<String> childPos = fileSystem.add(tab + fileOrDir.getName(), parentPos);
        fileSystemArrayPos.add(childPos);
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
        for (int i = 0; i < fileSystemArrayPos.size(); i++) {
            fileSystem.append(i).append(" ").append(fileSystemArrayPos.get(i).getElement()).append('\n');
        }
        return fileSystem.toString();
    }

    private void reOrderFileSystem(Position<String> position, int level) {
        StringBuilder tab = new StringBuilder();
        for (int i = 0; i < level; i++) {
            tab.append('\t');
        }
        for (Position<String> child : fileSystem.children(position)) {
            String element = position.getElement();
            String elementWithoutTab = element.replaceAll("\t", "");
            fileSystem.replace(position, tab + elementWithoutTab);
            level = level + 1;
            reOrderFileSystem(child, level);
            level = level - 1;
        }
    }

    private void reOrderFSArrayPos(Position<String> parent) {
        for (Position<String> child : fileSystem.children(parent)) {
            fileSystemArrayPos.add(child);
            reOrderFSArrayPos(child);
        }
    }

    public void moveFileById(int idFile, int idTargetFolder) {
        try {
            if (fileSystemArrayPos.get(idFile) == null || fileSystemArrayPos.get(idTargetFolder) == null) {
                throw new RuntimeException("Invalid ID");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("Invalid ID.");
        }
        fileSystem.moveSubtree(fileSystemArrayPos.get(idFile), fileSystemArrayPos.get(idTargetFolder));
        reOrderFileSystem(fileSystem.root(), 0);
        fileSystemArrayPos = new ArrayList<>();
        fileSystemArrayPos.add(fileSystem.root());
        reOrderFSArrayPos(fileSystem.root());
    }

    public void removeFileById(int idFile) {
        throw new RuntimeException("Not yet implemented");
    }


    public Iterable<String> findBySubstring(int idStartFile, String substring) {
        throw new RuntimeException("Not yet implemented");
    }

    public Iterable<String> findBySize(int idStartFile, long minSize, long maxSize) {
        throw new RuntimeException("Not yet implemented");
    }

    public String getFileVirtualPath(int idFile) {
        throw new RuntimeException("Not yet implemented");
    }

    public String getFilePath(int idFile) {
        throw new RuntimeException("Not yet implemented");
    }

}
