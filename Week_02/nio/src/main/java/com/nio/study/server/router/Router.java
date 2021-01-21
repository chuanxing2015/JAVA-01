package com.nio.study.server.router;

import java.util.List;

public interface Router<T> {
    String route(T[] nodes);
}
