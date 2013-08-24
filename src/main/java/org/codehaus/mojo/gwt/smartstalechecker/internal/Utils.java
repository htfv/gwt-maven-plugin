package org.codehaus.mojo.gwt.smartstalechecker.internal;

import org.codehaus.mojo.gwt.utils.DefaultGwtModuleReader;

public class Utils
{
    public static String makeModulePath(final String module)
    {
        return module.replace('.', '/') + DefaultGwtModuleReader.GWT_MODULE_EXTENSION;
    }
}
