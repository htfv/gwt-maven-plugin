package org.codehaus.mojo.gwt.smartstalechecker.internal;

import java.io.File;

import org.codehaus.mojo.gwt.GwtModule;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.XmlStreamReader;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;

public class CheckModuleInDirectoryTask extends StaleCheckTask
{
    public static class Builder
    {
        private String directory;
        private String module;
        private File outputDirectory;

        public Builder directory(final String directory)
        {
            this.directory = directory;
            return this;
        }

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

        public CheckModuleInDirectoryTask build()
        {
            return new CheckModuleInDirectoryTask(this);
        }
    }

    private final String directory;
    private final String module;
    private final File outputDirectory;

    protected CheckModuleInDirectoryTask(final Builder builder)
    {
        this.directory       = builder.directory;
        this.module          = builder.module;
        this.outputDirectory = builder.outputDirectory;
    }

    public StaleCheckResult call() throws Exception
    {
        final boolean moduleStale = isModuleStale();

        return new StaleCheckResult.Builder()
                .build();
    }

    private boolean isModuleStale()
    {
        final File moduleFile = new File(directory, Utils.makeModulePath(module));

        if (!moduleFile.exists())
        {
            return false;
        }

        final GwtModule module = readModule(moduleFile);
    }

    private GwtModule readModule(final File moduleFile)
    {
        try
        {
            final XmlStreamReader moduleReader = ReaderFactory.newXmlReader(moduleFile);
            final Xpp3Dom         moduleDom    = Xpp3DomBuilder.build(moduleReader);

            return new GwtModule(module, moduleDom, moduleReader);
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }

        return null;
    }
}
