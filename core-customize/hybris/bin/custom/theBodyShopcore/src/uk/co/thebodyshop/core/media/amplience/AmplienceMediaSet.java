/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.media.amplience;

import java.util.List;

public class AmplienceMediaSet {

    private String name;
    private List<AmplienceMediaSetItem> items;

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public List<AmplienceMediaSetItem> getItems()
    {
        return items;
    }

    public void setItems(final List<AmplienceMediaSetItem> items)
    {
        this.items = items;
    }

}
