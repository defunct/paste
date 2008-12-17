package com.goodworkalan.guicelet.validation;

import java.util.Map;
import java.util.ResourceBundle;

import com.goodworkalan.cassandra.Cassandra;
import com.goodworkalan.dspl.PropertyPath;
import com.goodworkalan.guicelet.Controller;
import com.goodworkalan.guicelet.ControllerScoped;
import com.goodworkalan.guicelet.GuiceletException;

@ControllerScoped
public class FaultFactory
{
    private final Cassandra cassandra;

    public FaultFactory(@Controller Object controller)
    {
        String bundleName = controller.getClass().getPackage().getName() + ".fault.properties";
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
        
        Cassandra.Variable variable = new Cassandra.Variable()
        {
            public Object get(Map<Object, Object> map, String property)
            {
                try
                {
                    PropertyPath path = new PropertyPath(property);
                    return path.get(map);
                }
                catch (PropertyPath.Error e)
                {
                    throw new GuiceletException(e);
                }
            }
        };
        
        Cassandra.BundleFormatter formatter = new Cassandra.BundleFormatter(bundle, variable);

        this.cassandra = new Cassandra(formatter);
    }

    public Fault newFault(String key, String path)
    {
        return new Fault(cassandra.newMessage(key), key, path);
    }
}
