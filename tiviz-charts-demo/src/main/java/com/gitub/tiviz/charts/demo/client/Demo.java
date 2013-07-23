package com.gitub.tiviz.charts.demo.client;

import com.github.gwtd3.api.D3;
import com.gitub.tiviz.charts.demo.client.charts.AxisDemo;
import com.gitub.tiviz.charts.demo.client.charts.LineChartDemo;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.debug.client.DebugInfo;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Demo implements EntryPoint {

    public static final String DEMO_CONTAINER_ID = "demoContainer";
    private ComplexPanel demoContainer;
    private DemoCase currentDemo;

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
        DebugInfo.setDebugIdPrefix("");
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void onUncaughtException(final Throwable e) {
                GWT.log("Uncaught error", e);
                Window.alert("Error. Go to see the logs");
            }
        });

        DockLayoutPanel container = new DockLayoutPanel(Unit.PX);
        container.setSize("100%", "100%");

        container.addNorth(new Label("GWT-D3 : A thin GWT wrapper around D3.", false), 20);
        container.addNorth(new Label("D3 API version: " + D3.version(), false), 20);

        FlowPanel p = new FlowPanel();
        ComplexPanel buttonContainer = new VerticalPanel();
        buttonContainer.add(new DemoButton("Axis Demo", AxisDemo.factory()));
        buttonContainer.add(new DemoButton("Line Chart", LineChartDemo.factory()));

        p.add(buttonContainer);
        container.addWest(p, 200);

        demoContainer = new FlowPanel();
        demoContainer.ensureDebugId(DEMO_CONTAINER_ID);
        demoContainer.setSize("100%", "100%");
        container.add(demoContainer);

        RootLayoutPanel.get().add(container);

        container.forceLayout();
    }

    public class DemoButton extends Button {

        public DemoButton(final String title, final Factory demoClass) {
            super(title, new DemoClickHandler(demoClass));
            ensureDebugId(demoClass.id());
        }

    }

    public class DemoClickHandler implements ClickHandler {
        private final Factory demoClass;

        public DemoClickHandler(final Factory demoClass) {
            super();
            this.demoClass = demoClass;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt
         * .event.dom.client.ClickEvent)
         */
        @Override
        public void onClick(final ClickEvent event) {
            stopCurrentDemo();

            DemoCase demo = demoClass.newInstance();
            demoContainer.add(demo);
            currentDemo = demo;
            demo.start();
        }

    }

    private void stopCurrentDemo() {
        if (currentDemo != null) {
            currentDemo.stop();
            demoContainer.remove(currentDemo);
            currentDemo = null;
        }
    }
}
