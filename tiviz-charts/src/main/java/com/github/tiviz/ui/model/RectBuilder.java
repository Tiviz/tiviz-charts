package com.github.tiviz.ui.model;

public interface RectBuilder<T> {

    double x(T value);

    double y(T value);

    double width(T value);

    double height(T value);

    String styleNames(T value);
}
