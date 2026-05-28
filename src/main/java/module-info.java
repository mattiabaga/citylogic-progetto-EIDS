module com.citylogic {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.citylogic to javafx.fxml;
    opens com.citylogic.controller to javafx.fxml;

    exports com.citylogic;
    exports com.citylogic.controller;
    exports com.citylogic.view;
    exports com.citylogic.model;

    exports com.citylogic.observer;
    exports com.citylogic.engine;
    exports com.citylogic.engine.metrics;
    exports com.citylogic.infrastructure;
    opens com.citylogic.model to javafx.fxml;
    exports com.citylogic.model.core;
    opens com.citylogic.model.core to javafx.fxml;
    exports com.citylogic.model.core.buildings;
    opens com.citylogic.model.core.buildings to javafx.fxml;
    exports com.citylogic.model.core.policies;
    opens com.citylogic.model.core.policies to javafx.fxml;
    exports com.citylogic.model.observer;
    opens com.citylogic.model.observer to javafx.fxml;
    exports com.citylogic.model.engine;
    opens com.citylogic.model.engine to javafx.fxml;
    exports com.citylogic.model.infrastructure;
    opens com.citylogic.model.infrastructure to javafx.fxml;
}