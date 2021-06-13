package com.Allocation;

import com.FileSystem.File;

public interface Allocator {
    public boolean allocate(File file);

    public void deAllocate(File file);

    public int getEmptySpace();

    public int getAllocatedSpace();

    public String getSpace();
}