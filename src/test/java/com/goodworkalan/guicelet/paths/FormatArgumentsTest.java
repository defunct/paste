package com.goodworkalan.guicelet.paths;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static com.goodworkalan.guicelet.paths.FormatArguments.*;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.goodworkalan.guicelet.Transfer;

public class FormatArgumentsTest
{
    private FormatArgument[] args(FormatArgument...formatArguments)
    {
        return formatArguments;
    }

    @Test
    public void ControllerClassAsPath()
    {
        Object controller = new Object();

        Transfer transfer = mock(Transfer.class);
        when(transfer.getController()).thenReturn(controller);

        PathFormatter formatter = new PathFormatter(transfer);
        assertEquals(formatter.format("/%s.ftl", args(CONTROLLER_CLASS_AS_PATH)), "/java/lang/Object.ftl");
    }

    @Test
    public void ControllerPackageAsPath()
    {
        Object controller = new Object();

        Transfer transfer = mock(Transfer.class);
        when(transfer.getController()).thenReturn(controller);

        PathFormatter formatter = new PathFormatter(transfer);
        assertEquals(formatter.format("/%s.ftl", args(CONTROLLER_PACKAGE_AS_PATH)), "/java/lang.ftl");
    }

    @Test
    public void ControllerClassName()
    {
        Object controller = new Object();

        Transfer transfer = mock(Transfer.class);
        when(transfer.getController()).thenReturn(controller);

        PathFormatter formatter = new PathFormatter(transfer);
        assertEquals(formatter.format("/%s.ftl", args(CONTROLLER_CLASS_NAME)), "/Object.ftl");
    }
}
