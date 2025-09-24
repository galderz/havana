package org.example.byteman.logging;

import org.jboss.byteman.rule.Rule;
import org.jboss.byteman.rule.helper.Helper;

import java.io.IOException;

public class Logger extends Helper implements AutoCloseable
{
    private static final Log LOG = new Log();

    protected Logger(Rule rule)
    {
        super(rule);
    }

    public void log(Log.Context context, int count)
    {
        LOG.log(context,count);
    }

    @Override
    public void close() throws IOException
    {
        LOG.close();
    }
}
