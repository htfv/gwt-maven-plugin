package org.codehaus.mojo.gwt.smartstalechecker.internal;

import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StaleCheckContext
{
    private final CompletionService<StaleCheckResult> completionService;
    private final ExecutorService executorService;
    private int tasksInQueue;

    public StaleCheckContext()
    {
        this.executorService   = Executors.newSingleThreadExecutor();
        this.completionService = new ExecutorCompletionService<StaleCheckResult>(executorService);
    }

    public boolean hasMoreTasks()
    {
        return tasksInQueue > 0;
    }

    public void stop()
    {
        executorService.shutdownNow();
    }

    public void submitTask(final StaleCheckTask task)
    {
        completionService.submit(task);
        tasksInQueue += 1;
    }

    public void submitTasks(final List<StaleCheckTask> tasks)
    {
        for (final StaleCheckTask task : tasks)
        {
            submitTask(task);
        }
    }

    public StaleCheckResult takeNextResult() throws ExecutionException, InterruptedException
    {
        final Future<StaleCheckResult> result = completionService.take();
        tasksInQueue -= 1;
        return result.get();
    }
}
