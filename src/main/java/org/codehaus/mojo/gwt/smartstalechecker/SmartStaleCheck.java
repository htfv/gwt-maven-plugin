package org.codehaus.mojo.gwt.smartstalechecker;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.codehaus.mojo.gwt.smartstalechecker.internal.CheckModuleTask;
import org.codehaus.mojo.gwt.smartstalechecker.internal.StaleCheckContext;
import org.codehaus.mojo.gwt.smartstalechecker.internal.StaleCheckResult;

public class SmartStaleCheck
{
    public static class Builder
    {
        private boolean enabled;
        private List<String> resourceDirectories;
        private List<String> sourceDirectories;

        public Builder enabled(final boolean enabled)
        {
            this.enabled = enabled;
            return this;
        }

        public Builder resourceDirectories(final List<String> resourceDirectories)
        {
            this.resourceDirectories = resourceDirectories;
            return this;
        }

        public Builder sourceDirectories(final List<String> sourceDirectories)
        {
            this.sourceDirectories = sourceDirectories;
            return this;
        }

        public SmartStaleCheck build()
        {
            return new SmartStaleCheck(this);
        }

        private List<String> resourceDirectories()
        {
            return directories(resourceDirectories);
        }

        private List<String> sourceDirectories()
        {
            return directories(sourceDirectories);
        }

        private List<String> directories(final List<String> directories)
        {
            if (directories != null)
            {
                return Collections.unmodifiableList(directories);
            }
            else
            {
                return Collections.emptyList();
            }
        }
    }

    private final boolean enabled;
    private final List<String> resourceDirectories;
    private final List<String> sourceDirectories;

    protected SmartStaleCheck(final Builder builder)
    {
        this.enabled             = builder.enabled;
        this.resourceDirectories = builder.resourceDirectories();
        this.sourceDirectories   = builder.sourceDirectories();
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public boolean compilationRequired(final String module, final File outputDirectory)
    {
        final CheckModuleTask task = new CheckModuleTask.Builder()
                .module(module)
                .outputDirectory(outputDirectory)
                .resourceDirectories(resourceDirectories)
                .sourceDirectories(sourceDirectories)
                .build();

        final StaleCheckContext context = new StaleCheckContext();
        context.submitTask(task);

        while (context.hasMoreTasks())
        {
            try
            {
                final StaleCheckResult result = context.takeNextResult();

                if (result.isStale())
                {
                    context.stop();
                    return true;
                }

                context.submitTasks(result.getTasks());
            }
            catch (final ExecutionException e)
            {
                //
                // The task threw an exception, should we continue?
                //
            }
            catch (final InterruptedException e)
            {
                break;
            }
        }

        return false;
    }
}
