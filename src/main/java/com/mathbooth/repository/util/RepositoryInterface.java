package com.mathbooth.repository.util;

import java.util.ArrayList;

public interface RepositoryInterface<T> {
    ArrayList<T> fetchAll();
    String getNameById(int id);
    Boolean existsById(int id);
}
