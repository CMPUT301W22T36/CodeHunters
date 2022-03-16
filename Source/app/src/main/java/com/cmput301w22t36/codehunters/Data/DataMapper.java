package com.cmput301w22t36.codehunters.Data;

public abstract class DataMapper<D extends Data> {
    public abstract D get(int i);
    public abstract void set(D data);
    public abstract void update(D data);
    public abstract void delete(D data);
}

