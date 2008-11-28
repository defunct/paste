package com.goodworkalan.guicelet;

import java.util.List;

public class JanitorQueue
{
    private final List<Janitor> listOfJanitors;

    public JanitorQueue(List<Janitor> listOfJanitors)
    {
        this.listOfJanitors = listOfJanitors;
    }
    
    public void add(Janitor janitor)
    {
        synchronized (listOfJanitors)
        {
            listOfJanitors.add(janitor);
        }
    }
}
