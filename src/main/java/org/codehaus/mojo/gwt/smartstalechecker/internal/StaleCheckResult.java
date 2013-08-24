package org.codehaus.mojo.gwt.smartstalechecker.internal;

import java.util.Collections;
import java.util.List;

public class StaleCheckResult
{
    public static class Builder
    {
        private boolean stale;
        private List<StaleCheckTask> tasks;

        public Builder stale(final boolean stale)
        {
            this.stale = stale;
            return this;
        }

        public Builder workItems(final List<StaleCheckTask> tasks)
        {
            this.tasks = tasks;
            return this;
        }

        public StaleCheckResult build()
        {
            return new StaleCheckResult(this);
        }

        private List<StaleCheckTask> tasks()
        {
            if (tasks != null)
            {
                return Collections.unmodifiableList(tasks);
            }
            else
            {
                return Collections.emptyList();
            }
        }
    }

    private final boolean stale;
    private final List<StaleCheckTask> tasks;

    protected StaleCheckResult(final Builder builder)
    {
        this.stale = builder.stale;
        this.tasks = builder.tasks();
    }

    public boolean isStale()
    {
        return stale;
    }

    public List<StaleCheckTask> getTasks()
    {
        return tasks;
    }
}
