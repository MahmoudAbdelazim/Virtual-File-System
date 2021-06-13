package com;

import java.io.Serializable;
import java.util.ArrayList;

public interface Allocator extends Serializable {
    public boolean allocate(File file);
    public void deAllocate(File file);
    public int getEmptySpace();
    public int getAllocatedSpace();
    public String getSpace();
}