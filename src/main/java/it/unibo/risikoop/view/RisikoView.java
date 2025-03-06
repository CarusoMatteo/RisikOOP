package it.unibo.risikoop.view;

import it.unibo.risikoop.controller.RisikoApp;

public interface RisikoView {

    void setObserver(RisikoApp risikoApp);

    void start();

}
