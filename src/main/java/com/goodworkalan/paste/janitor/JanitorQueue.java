package com.goodworkalan.paste.janitor;

import java.util.List;

// TODO Document.
public class JanitorQueue
{
    // TODO Document.
    private final List<Janitor> listOfJanitors;

    // TODO Document.
    public JanitorQueue(List<Janitor> listOfJanitors)
    {
        this.listOfJanitors = listOfJanitors;
    }
    
    // TODO Document.
    public void add(Janitor janitor)
    {
        synchronized (listOfJanitors)
        {
            listOfJanitors.add(janitor);
        }
    }
}
