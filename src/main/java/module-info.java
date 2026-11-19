module com.citylogic {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.citylogic.controller to javafx.fxml;
    opens com.citylogic.model to javafx.fxml;
    opens com.citylogic.model.core to javafx.fxml;
    opens com.citylogic.model.core.buildings to javafx.fxml;
    opens com.citylogic.model.core.policies to javafx.fxml;

    exports com.citylogic.controller;
    exports com.citylogic.model;
    exports com.citylogic.model.core;
    exports com.citylogic.model.core.buildings;
    exports com.citylogic.model.core.policies;
    exports com.citylogic.model.engine;
    exports com.citylogic.model.infrastructure;
    exports com.citylogic.model.observer;
}
