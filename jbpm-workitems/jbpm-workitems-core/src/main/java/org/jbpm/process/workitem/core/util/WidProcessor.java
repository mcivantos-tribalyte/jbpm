/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.process.workitem.core.util;

import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import org.stringtemplate.v4.ST;

@SupportedAnnotationTypes("org.jbpm.process.workitem.core.util.Wid")
public class WidProcessor extends AbstractProcessor {

    public WidProcessor() {
        super();
    }

    private Map<String, List<Wid>> processingResults;
    private boolean resetResults = true;

    public static final String WID_ST_TEMPLATE = "[\n" +
            "$widInfo:{k|\n" +
            "[\n" +
            "    \"name\" : \"$widInfo.(k).name$\",\n" +
            "    \"displayName\" : \"$widInfo.(k).displayName$\",\n" +
            "    \"category\" : \"$widInfo.(k).category$\",\n" +
            "    \"description\" : \"$widInfo.(k).description$\",\n" +
            "    \"defaultHandler\" : \"$widInfo.(k).defaultHandler$\",\n" +
            "\n" +
            "    $if(widInfo.(k).parameters)$\n" +
            "    \"parameters\" : [\n" +
            "        $widInfo.(k).parameters:{k1|\n" +
            "            \"$k1$\" : new $widInfo.(k).parameters.(k1).type$()\n" +
            "        }; separator=\",\"$\n" +
            "    ],\n" +
            "    $endif$\n" +
            "    $if(widInfo.(k).results)$\n" +
            "    \"results\" : [\n" +
            "        $widInfo.(k).results:{k1|\n" +
            "            \"$k1$\" : new $widInfo.(k).results.(k1).type$()\n" +
            "        }; separator=\",\"$\n" +
            "    ],\n" +
            "    $endif$\n" +
            "    $if(widInfo.(k).mavenDepends)$\n" +
            "    \"mavenDependencies\" : [\n" +
            "        $widInfo.(k).mavenDepends:{k1|\n" +
            "             \"$widInfo.(k).mavenDepends.(k1).group$:$widInfo.(k).mavenDepends.(k1).artifact$:$widInfo.(k).mavenDepends.(k1).version$\"\n" +
            "        }; separator=\",\"$\n" +
            "    ],\n" +
            "    $endif$\n" +
            "    \"icon\" : \"$widInfo.(k).icon$\"\n" +
            "\n" +
            "]\n" +
            "}; separator=\",\"$\n" +
            "]";

    public static final String INDEX_ST_TEMPLATE = "<html>\n" +
            "<head>\n" +
            "    <style type=\"text/css\">\n" +
            "        table.table-style {\n" +
            "            font-family: verdana, arial, sans-serif;\n" +
            "            font-size: 11px;\n" +
            "            color: #333333;\n" +
            "            border-width: 1px;\n" +
            "            border-color: #3A3A3A;\n" +
            "            border-collapse: collapse;\n" +
            "        }\n" +
            "        table.table-style th {\n" +
            "            border-width: 1px;\n" +
            "            padding: 8px;\n" +
            "            border-style: solid;\n" +
            "            border-color: #517994;\n" +
            "            background-color: #B2CFD8;\n" +
            "        }\n" +
            "        table.table-style tr:hover td {\n" +
            "            background-color: #DFEBF1;\n" +
            "        }\n" +
            "        table.table-style td {\n" +
            "            border-width: 1px;\n" +
            "            padding: 8px;\n" +
            "            border-style: solid;\n" +
            "            border-color: #517994;\n" +
            "            background-color: #ffffff;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "$widInfo:{k|\n" +
            "<h2>Workitem Info</h2>\n" +
            "<table class=\"table-style\">\n" +
            "    <thead>\n" +
            "    <tr>\n" +
            "        <th>Name</th>\n" +
            "        <th>Display Name</th>\n" +
            "        <th>Category</th>\n" +
            "        <th>Description</th>\n" +
            "        <th>Handler</th>\n" +
            "    </tr>\n" +
            "    </thead>\n" +
            "    <tbody>\n" +
            "    <tr>\n" +
            "        <td>$widInfo.(k).name$</td>\n" +
            "        <td>$widInfo.(k).displayName$</td>\n" +
            "        <td>$widInfo.(k).category$</td>\n" +
            "        <td>$widInfo.(k).description$</td>\n" +
            "        <td>$widInfo.(k).defaultHandler$</td>\n" +
            "    </tr>\n" +
            "    </tbody>\n" +
            "</table>\n" +
            "<br/>\n" +
            "$if(widInfo.(k).parameters)$\n" +
            "<h2>Parameters</h2>\n" +
            "<table class=\"table-style\">\n" +
            "    <thead>\n" +
            "    <tr>\n" +
            "        <th>Name</th>\n" +
            "        <th>Type</th>\n" +
            "    </tr>\n" +
            "    </thead>\n" +
            "    <tbody>\n" +
            "    $widInfo.(k).parameters:{k1|\n" +
            "    <tr>\n" +
            "        <td>$k1$</td>\n" +
            "        <td>$widInfo.(k).parameters.(k1).type$</td>\n" +
            "    </tr>\n" +
            "    }$\n" +
            "    </tbody>\n" +
            "</table>\n" +
            "<br/>\n" +
            "$endif$\n" +
            "$if(widInfo.(k).results)$\n" +
            "<h2>Results</h2>\n" +
            "<table class=\"table-style\">\n" +
            "    <thead>\n" +
            "    <tr>\n" +
            "        <th>Name</th>\n" +
            "        <th>Type</th>\n" +
            "    </tr>\n" +
            "    </thead>\n" +
            "    <tbody>\n" +
            "    $widInfo.(k).results:{k1|\n" +
            "    <tr>\n" +
            "        <td>$k1$</td>\n" +
            "        <td>$widInfo.(k).results.(k1).type$</td>\n" +
            "    </tr>\n" +
            "    }$\n" +
            "    </tbody>\n" +
            "</table>\n" +
            "<br/>\n" +
            "$endif$\n" +
            "$if(widInfo.(k).mavenDepends)$\n" +
            "<h2>Maven Dependencies</h2>\n" +
            "<table class=\"table-style\">\n" +
            "    <thead>\n" +
            "    <tr>\n" +
            "        <th>Group</th>\n" +
            "        <th>Artifact</th>\n" +
            "        <th>Version</th>\n" +
            "    </tr>\n" +
            "    </thead>\n" +
            "    <tbody>\n" +
            "    $widInfo.(k).mavenDepends:{k1|\n" +
            "    <tr>\n" +
            "        <td>$widInfo.(k).mavenDepends.(k1).group$</td>\n" +
            "        <td>$widInfo.(k).mavenDepends.(k1).artifact$</td>\n" +
            "        <td>$widInfo.(k).mavenDepends.(k1).version$</td>\n" +
            "    </tr>\n" +
            "    }$\n" +
            "    </tbody>\n" +
            "</table>\n" +
            "<br/>\n" +
            "$endif$\n" +
            "}$\n" +
            "</body>\n" +
            "</html>";

    public boolean process(final Set<? extends TypeElement> annotations,
                           final RoundEnvironment roundEnv) {

        if (resetResults) {
            processingResults = new HashMap<>();
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(Wid.class)) {

            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) element;

                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                                                         MessageFormat.format("Wid Processor : processing class {0}.",
                                                                              typeElement.asType().toString()));

                processingResults.put(typeElement.asType().toString(),
                                      new ArrayList<>());

                if (typeElement.getInterfaces() != null && typeElement.getInterfaces().size() > 0) {
                    for (TypeMirror mirror : typeElement.getInterfaces()) {
                        if (mirror.getAnnotation(Wid.class) != null) {
                            processingResults.get(typeElement.asType().toString()).add(mirror.getAnnotation(Wid.class));
                        }
                    }
                }

                processingResults.get(typeElement.asType().toString()).add(typeElement.getAnnotation(Wid.class));
            }
        }

        return postProcessWorkItemDefinition();
    }

    public boolean postProcessWorkItemDefinition() {
        if (processingResults == null || processingResults.size() < 1) {
            return false;
        }

        try {

            Map<String, WidInfo> wrappedResults = new HashMap<>();
            for (String key : processingResults.keySet()) {
                wrappedResults.put(key,
                                   new WidInfo(processingResults.get(key)));
            }

            String widName = "WorkDefinitions.wid";
            if (processingEnv.getOptions().containsKey("widName")) {
                widName = processingEnv.getOptions().get("widName");
            } else {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                                                         "Unable to find option \"widName\", using default.");
            }

            writeStream(getFileObject("",
                                      widName),
                        getTemplateData(WID_ST_TEMPLATE,
                                        wrappedResults));
            writeStream(getFileObject("",
                                      "index.html"),
                        getTemplateData(INDEX_ST_TEMPLATE,
                                        wrappedResults));
        } catch (Exception e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                                                     MessageFormat.format("Error post-processing workitem annotations: {0}.",
                                                                          e.getMessage()));
        }
        return true;
    }

    public byte[] getTemplateData(String templateStr,
                                   Map<String, WidInfo> widInfoMap) {
        ST stTemplate = new ST(templateStr,
                               '$',
                               '$');

        stTemplate.add("widInfo",
                       widInfoMap);

        return stTemplate.render().getBytes();
    }

    public void writeStream(FileObject fileObject,
                             byte[] data) throws IOException {
        OutputStream stream = fileObject.openOutputStream();
        stream.write(data);
        stream.close();
    }

    public FileObject getFileObject(String pkg,
                                     String name) throws IOException {
        return processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT,
                                                       pkg,
                                                       name);
    }

    public void setResetResults(boolean resetResults) {
        this.resetResults = resetResults;
    }

    // for testing
    public Map<String, List<Wid>> getProcessingResults() {
        return processingResults;
    }

    // for testing
    public void setProcessingResults(Map<String, List<Wid>> processingResults) {
        this.processingResults = processingResults;
    }
}