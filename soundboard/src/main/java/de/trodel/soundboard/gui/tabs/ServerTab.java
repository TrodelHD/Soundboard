package de.trodel.soundboard.gui.tabs;

import static java.time.ZoneId.systemDefault;

import java.time.format.DateTimeFormatter;

import de.trodel.soundboard.gui.AbstractMainTab;
import de.trodel.soundboard.gui.InputUtils;
import de.trodel.soundboard.server.rest.RestAuthService.TokenData;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ServerTab extends AbstractMainTab {
    private static final Insets INSETS_10         = new Insets(10, 10, 10, 10);
    private static final Insets INSETS_10_NOT_TOP = new Insets(0, 10, 10, 10);

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private final TextField            port           = new TextField();
    private final TextField            key            = new TextField();
    private final CheckBox             enabled        = new CheckBox();
    private final TableView<TokenData> authorizations = new TableView<>();

    public ServerTab() {
        super("Server");

        port.setMinWidth(200);
        key.setMinWidth(200);
        enabled.setText("Enable server");

        authorizations.setEditable(false);

        TableColumn<TokenData, String> issueTimeCol = new TableColumn<>("Issue time");
        TableColumn<TokenData, String> expipreTimeCol = new TableColumn<>("Expipre time");
        TableColumn<TokenData, String> sourceCol = new TableColumn<>("Source");
        TableColumn<TokenData, String> tokenCol = new TableColumn<>("Token");

        authorizations.getColumns().add(sourceCol);
        authorizations.getColumns().add(issueTimeCol);
        authorizations.getColumns().add(expipreTimeCol);
        authorizations.getColumns().add(tokenCol);

        authorizations.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        issueTimeCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(DTF.format(param.getValue().creationTime().atZone(systemDefault()))));
        expipreTimeCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(DTF.format(param.getValue().expireTime().atZone(systemDefault()))));
        sourceCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().source()));
        tokenCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().token()));

        VBox vBox = new VBox();

        vBox.getChildren().add(withHeader("Port:", port, false, true));
        vBox.getChildren().add(withHeader("Key:", key, false, false));
        vBox.getChildren().add(enabled);
        vBox.getChildren().add(withHeader("Authorizations:", authorizations, true, false));

        authorizations.prefHeightProperty().bind(vBox.heightProperty());
        setContent(vBox);

        VBox.setMargin(enabled, INSETS_10_NOT_TOP);
        InputUtils.onlyInt(port);
    }

    private VBox withHeader(String header, Region node, boolean grow, boolean withTop) {
        VBox vBox = new VBox(5);
        Label label = new Label(header);
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().addAll(label, node);

        node.prefWidthProperty().bind(vBox.widthProperty());

        VBox.setMargin(vBox, withTop ? INSETS_10 : INSETS_10_NOT_TOP);

        return vBox;
    }

    public TextField getPort() {
        return port;
    }

    public TextField getKey() {
        return key;
    }

    public CheckBox getEnabled() {
        return enabled;
    }

    public TableView<TokenData> getAuthorizations() {
        return authorizations;
    }

}
