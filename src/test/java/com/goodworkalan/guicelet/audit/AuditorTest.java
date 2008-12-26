package com.goodworkalan.guicelet.audit;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.goodworkalan.dspl.PropertyPath;
import com.goodworkalan.dspl.PathException;


public class AuditorTest
{
    private Map<Object, Object> newTree() throws PathException
    {
        Map<Object, Object> map = new HashMap<Object, Object>();
        new PropertyPath("foo.quantity").set(map, "1", true);
        new PropertyPath("foo.password").set(map, "password", true);
        new PropertyPath("foo.confirm").set(map, "password", true);
        return map;
    }

    @Test
    public void audit() throws PathException
    {
        Auditor auditor = new Auditor();
        auditor.audit("foo").with(Required.class);
        auditor.audit("foo", "quantity").with(Required.class)
                                 .then().with(ValueInRange.class)
                                        .min(0)
                                        .max(10);
        auditor.audit("foo", "password").with(Confirm.class)
                                        .compare("confirm");
        auditor.audit(newTree());
    }
}
