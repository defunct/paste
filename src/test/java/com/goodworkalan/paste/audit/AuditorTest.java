package com.goodworkalan.paste.audit;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.goodworkalan.infuse.Infusion;
import com.goodworkalan.infuse.PathException;


public class AuditorTest
{
    private Map<Object, Object> newTree() throws PathException
    {
        Map<Object, Object> map = new HashMap<Object, Object>();
        new Infusion("foo.quantity", "1").infuse(map);
        new Infusion("foo.password", "password").infuse(map);
        new Infusion("foo.confirm", "password").infuse(map);
        return map;
    }

    @Test(enabled = false)
    public void audit() throws PathException
    {
        // TODO Search the path for faults before testing nested properties.
        Auditor auditor = new Auditor(new Object());
        auditor.audit("foo").with(Required.class);
        auditor.audit("foo", "quantity").with(Required.class)
                                 .then().with(ValueInRange.class)
                                        .min(0)
                                        .max(10);
        auditor.audit("foo", "password").with(Confirm.class)
                                        .compare("confirm");
        auditor.audit(new HashMap<Object, Object>(), newTree());
    }
}
