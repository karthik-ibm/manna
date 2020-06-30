package com.ibm.manna.pojo;

import java.io.Serializable;

public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String name;

    public final String image;

    public Photo(String name, String image)
    {
        this.name = name;
        this.image = image;
    }
}
