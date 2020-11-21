package com.slb.file.api.listner;

import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;

@Component
public class MyFileChangeListener implements FileChangeListener {
    public void onChange(Set<ChangedFiles> changeSet) {
        for (ChangedFiles files : changeSet) {
            for (ChangedFile file : files.getFiles()) {
                if ((file.getType().equals(ChangedFile.Type.MODIFY)
                        || file.getType().equals(ChangedFile.Type.ADD)
                        || !isLocked(file.getFile().toPath()))) {
                    System.out.println("Operation: "
                            + file.getType()
                            + " On file: "
                            + file.getFile().getName()
                            + " is done");
                }
            }
        }
    }

    private boolean isLocked(Path path) {
        try (FileChannel ch = FileChannel.open(path, StandardOpenOption.WRITE); FileLock lock = ch.tryLock()) {
            return lock == null;
        } catch (IOException e) {
            return true;
        }
    }
}
