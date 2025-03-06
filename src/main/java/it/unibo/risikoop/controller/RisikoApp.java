package it.unibo.risikoop.controller;

import java.util.Arrays;
import java.util.List;

import it.unibo.risikoop.model.Risiko;
import it.unibo.risikoop.model.RisikoImpl;
import it.unibo.risikoop.view.RisikoView;
import it.unibo.risikoop.view.RisikoViewImpl;

public class RisikoApp {
    private final Risiko model;
    private final List<RisikoView> views;

    /**
     * Starts the model and attaches the views.
     * 
     * @param views the views to attach
     */
    public RisikoApp(final RisikoView... views) {
        /*
         * Side-effect proof
         */
        this.views = Arrays.asList(Arrays.copyOf(views, views.length));
        for (final RisikoView view : views) {
            view.setObserver(this);
            view.start();
        }
        this.model = new RisikoImpl();
    }

    /**
     * @param args ignored
     */
    public static void main(final String... args) {
        new RisikoApp(new RisikoViewImpl());
    }
}
