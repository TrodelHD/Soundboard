package de.trodel.soundboard.server;

import static javafx.collections.FXCollections.unmodifiableObservableList;

import de.trodel.soundboard.App;
import de.trodel.soundboard.execution.SoundboardExecutionProvider;
import de.trodel.soundboard.model.MainModel;
import de.trodel.soundboard.model.ServerModel.ServerMode;
import de.trodel.soundboard.server.broadcast.BroadcastChannelServer;
import de.trodel.soundboard.server.rest.RestApplication;
import de.trodel.soundboard.server.rest.RestAuthService.TokenData;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;

public class ServerManager implements AutoCloseable {

    private final MainModel                   mainModel;
    private final SoundboardExecutionProvider executionProvider;
    private RestApplication                   restServer;
    private BroadcastChannelServer            broadcastServer;
    private final ObservableList<TokenData>   tokens;

    public ServerManager(MainModel mainModel, SoundboardExecutionProvider executionProvider) {
        this.mainModel = mainModel;
        this.executionProvider = executionProvider;
        this.tokens = FXCollections.observableArrayList();

        mainModel.getServer().getModeProperty().addListener((observable, oldV, newV) -> {
            switch (newV) {
                case enabled:
                    startServer();
                    return;
                case disabled:
                    stopServer();
                    return;
            }
            throw new IllegalArgumentException("Unexpected value: " + newV);
        });

        if (mainModel.getServer().getMode() == ServerMode.enabled) {
            startServer();
        }

    }

    private void startServer() {
        synchronized (ServerManager.this) {
            try {
                if (restServer == null) {
                    restServer = new RestApplication(mainModel, executionProvider);
                    restServer.getTokenData().addListener(new MapChangeListener<String, TokenData>() {
                        @Override
                        public void onChanged(Change<? extends String, ? extends TokenData> change) {
                            if (change.wasRemoved()) {
                                tokens.remove(change.getValueRemoved());
                            }
                            if (change.wasAdded()) {
                                tokens.add(change.getValueAdded());
                            }
                        }
                    });
                }
            } catch (Exception e) {
                App.throwable(e);
            }

            try {
                if (broadcastServer == null) {
                    broadcastServer = new BroadcastChannelServer(mainModel.getServer().getPort());
                }
            } catch (Exception e) {
                App.throwable(e);
            }
        }
    }

    private void stopServer() {
        synchronized (ServerManager.this) {
            try {
                if (restServer != null) {
                    restServer.close();
                    tokens.clear();
                    restServer = null;
                }
            } catch (Exception e) {
                App.throwable(e);
            }

            try {
                if (broadcastServer != null) {
                    broadcastServer.close();
                    broadcastServer = null;
                }
            } catch (Exception e) {
                App.throwable(e);
            }
        }
    }

    @Override
    public void close() throws Exception {
        stopServer();
    }

    public ObservableList<TokenData> getTokens() {
        return unmodifiableObservableList(tokens);
    }

}
