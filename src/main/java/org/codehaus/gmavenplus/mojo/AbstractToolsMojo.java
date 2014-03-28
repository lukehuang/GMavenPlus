/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codehaus.gmavenplus.mojo;

import org.codehaus.gmavenplus.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;


/**
 * The base tools mojo, which all tool mojos extend.
 *
 * @author Keegan Witt
 * @since 1.1
 */
public abstract class AbstractToolsMojo extends AbstractGroovyMojo {

    /**
     * Properties to make available in scripts as variables using the property
     * name.  By default will include
     * <dl>
     *   <dt>settings</dt>
     *     <dd>A org.apache.maven.settings.Settings object of the current Maven settings.</dd>
     *   <dt>project</dt>
     *     <dd>A org.apache.maven.project.Project object of the current Maven project.</dd>
     *   <dt>session</dt>
     *     <dd>A org.apache.maven.execution.MavenSession object of the current Maven session.</dd>
     *   <dt>localRepository</dt>
     *     <dd>A org.apache.maven.artifact.repository.ArtifactRepository object of Maven's local repository.</dd>
     *   <dt>reactorProjects</dt>
     *     <dd>A list of org.apache.maven.project.MavenProject objects currently loaded by the reactor.</dd>
     *   <dt>pluginArtifacts</dt>
     *     <dd>A list of org.apache.maven.artifact.Artifact objects of this plugin's artifacts.</dd>
     *   <dt>log</dt>
     *     <dd>A org.apache.maven.plugin.logging.Log object of Maven's log.</dd>
     *   <dt>ant</dt>
     *     <dd>A groovy.util.AntBuilder object.</dd>
     * </dl>
     * These can be overridden.
     * @since 1.0-beta-3
     *
     * @parameter
     */
    protected Properties properties = new Properties();

    protected void initializeProperties() {
        if (settings != null && !properties.containsKey("settings")) {
            properties.put("settings", settings);
        }
        if (project != null && !properties.containsKey("project")) {
            properties.put("project", project);
        }
        if (session != null && !properties.containsKey("session")) {
            properties.put("session", session);
        }
        if (localRepository != null && !properties.containsKey("localRepository")) {
            properties.put("localRepository", localRepository);
        }
        if (reactorProjects != null && !properties.containsKey("reactorProjects")) {
            properties.put("reactorProjects", reactorProjects);
        }
        if (pluginArtifacts != null && !properties.containsKey("pluginArtifacts")) {
            properties.put("pluginArtifacts", pluginArtifacts);
        }
        if (!properties.containsKey("reactorProjects")) {
            properties.put("log", getLog());
        }
        if (!properties.containsKey("ant")) {
            try {
                Object antBuilder = ReflectionUtils.invokeConstructor(ReflectionUtils.findConstructor(Class.forName("groovy.util.AntBuilder")));
                properties.put("ant", antBuilder);
            } catch (InvocationTargetException e) {
                logUnableToInitializeAntBuilder(e);
            } catch (IllegalAccessException e) {
                logUnableToInitializeAntBuilder(e);
            } catch (InstantiationException e) {
                logUnableToInitializeAntBuilder(e);
            } catch (ClassNotFoundException e) {
                logUnableToInitializeAntBuilder(e);
            }
        }
    }

    private void logUnableToInitializeAntBuilder(Throwable e) {
        getLog().error("Unable to initialize 'ant' with a new AntBuilder object.  Is Ant a dependency?", e);
    }

}
