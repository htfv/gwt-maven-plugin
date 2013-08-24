package org.codehaus.mojo.gwt.smartstalechecker.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CheckModuleTask extends StaleCheckTask
{
    public static class Builder
    {
        private String module;
        private File outputDirectory;
        private List<String> resourceDirectories;
        private List<String> sourceDirectory;

        public Builder module(final String module)
        {
            this.module = module;
            return this;
        }

        public Builder outputDirectory(final File outputDirectory)
        {
            this.outputDirectory = outputDirectory;
            return this;
        }

        public Builder resourceDirectories(final List<String> resourceDirectories)
        {
            this.resourceDirectories = resourceDirectories;
            return this;
        }

        public Builder sourceDirectories(final List<String> sourceDirectories)
        {
            this.sourceDirectory = sourceDirectories;
            return this;
        }

        public CheckModuleTask build()
        {
            return new CheckModuleTask(this);
        }
    }

    private final String module;
    private final File outputDirectory;
    private final List<String> resourceDirectories;
    private final List<String> sourceDirectories;

    protected CheckModuleTask(final Builder builder)
    {
        this.module              = builder.module;
        this.outputDirectory     = builder.outputDirectory;
        this.resourceDirectories = builder.resourceDirectories;
        this.sourceDirectories   = builder.sourceDirectory;
    }

    public StaleCheckResult call() throws Exception
    {
        // TODO: find module file; search source directories, resources directories, then jars.

        // TODO: check if module file is stale; compare dates, if this is a file; compare jar date; calculate checksum.

        // TODO: if not stale, find and check source files.

        // TODO: if sources are not stale, push work items for inherited modules.

        return new StaleCheckResult.Builder()
                .workItems(createTasks())
                .build();
    }

    private List<StaleCheckTask> createTasks()
    {
        final List<StaleCheckTask> tasks = new ArrayList<StaleCheckTask>();

        createTasksForDirectories(tasks, sourceDirectories);
        createTasksForDirectories(tasks, resourceDirectories);

        return tasks;
    }

    private void createTasksForDirectories(final List<StaleCheckTask> tasks,
            final List<String> directories)
    {
        for (final String directory : directories)
        {
            tasks.add(
                    new CheckModuleInDirectoryTask.Builder()
                            .outputDirectory(outputDirectory)
                            .directory(directory)
                            .module(module)
                            .build());
        }
   }
}
