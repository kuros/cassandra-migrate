/**
 * Copyright 2010-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.kuros.cassandra.migrate.option;


import java.io.File;

import static com.github.kuros.cassandra.migrate.utils.Util.file;

public class SelectedPaths {
    private File basePath = new File("./");
    private File scriptPath;

    public File getBasePath() {
        return basePath;
    }

    public File getEnvPath() {
        return file(basePath, "./environments");
    }

    public File getScriptPath() {
        return scriptPath == null ? file(basePath, "./scripts") : scriptPath;
    }

    public void setBasePath(File aBasePath) {
        basePath = aBasePath;
    }

    public void setScriptPath(File aScriptPath) {
        scriptPath = aScriptPath;
    }

}
