/**

   Copyright 2010 OpenEngSB Division, Vienna University of Technology

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.openengsb.ekb.analyzer;

import java.io.File;
import java.io.FilenameFilter;

import org.openengsb.util.WorkingDirectory;

public class DirectoryWatcher {

    private File directory;

    private ModelAnalyzer analyzer;

    private static final long SLEEP_TIME = 5000;

    private DirectoryWatcherRunnable runnable = new DirectoryWatcherRunnable();

    public DirectoryWatcher(String directory) {
        this.directory = new File(WorkingDirectory.getDirectory("ekb"), directory);
        if (this.directory.exists() && !this.directory.isDirectory()) {
            throw new IllegalArgumentException("Under the given path a file exists that is no directory.");
        }
        if (!this.directory.exists()) {
            this.directory.mkdirs();
        }
    }

    public void start() {
        runnable.active = true;
        new Thread(runnable).start();
    }

    public void stop() {
        runnable.active = false;
    }

    public void setAnalyzer(ModelAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    private class DirectoryWatcherRunnable implements Runnable {

        private File last = null;

        private boolean active = true;

        public void run() {
            while (active) {
                File[] jars = directory.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".jar");
                    }
                });
                File modelFile = getNewest(jars);
                if (!modelFile.equals(last) || modelFile.lastModified() != last.lastModified()) {
                    analyzer.analyzeAndStore(modelFile);
                }
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
        }

        private File getNewest(File[] jars) {
            File newest = null;
            for (File jar : jars) {
                if (getLastModified(jar) > getLastModified(newest)) {
                    newest = jar;
                }
            }
            return newest;
        }

        private long getLastModified(File file) {
            if (file == null) {
                return Long.MIN_VALUE;
            }
            return file.lastModified();
        }
    }

}
