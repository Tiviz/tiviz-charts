package com.github.tiviz.ui.model;

/**
 * Converts an arbytrary T object into rectangular informations displayable
 * on 2 secant axis.
 * It delegates to a {@link RectBuilder} that can convert a object T into domain dimensions.
 * 
 * @author SCHIOCA
 * 
 * @param <T>
 */
public class AxisRectBuilder<T> implements RectBuilder<T> {

    private final AxisModel<?> xModel;
    private final AxisModel<?> yModel;
    private final RectBuilder<T> domainBuilder;

    public AxisRectBuilder(final AxisModel<?> xModel, final AxisModel<?> yModel, final RectBuilder<T> domainBuilder) {
        super();
        this.xModel = xModel;
        this.yModel = yModel;
        this.domainBuilder = domainBuilder;
    }

    @Override
    public double x(final T value) {
        return xModel.toPixel(domainBuilder.x(value));
    }

    @Override
    public double y(final T value) {
        return yModel.toPixel(domainBuilder.y(value));
    }

    @Override
    public double width(final T value) {
        return xModel.toPixelSize(domainBuilder.width(value));
    }

    @Override
    public double height(final T value) {
        return yModel.toPixelSize(domainBuilder.height(value));
    }

    @Override
    public String styleNames(final T value) {
        return domainBuilder.styleNames(value);
    }

}
