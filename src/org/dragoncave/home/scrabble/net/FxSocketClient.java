/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.scrabble.net;
import java.net.*;
import java.io.IOException;
/**
 *
 * @author Rider1
 */
public class FxSocketClient extends GenericSocket implements SocketListener {
    
    public String host;
    private SocketListener fxListener;

    /**
     * Called whenever a message is read from the socket.  In
     * JavaFX, this method must be run on the main thread and
     * is accomplished by the Platform.runLater() call.  Failure to do so
     * *will* result in strange errors and exceptions.
     * @param line Line of text read from the socket.
     */
    @Override
    public void onMessage(final String line) {
        javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {
                fxListener.onMessage(line);
            }
        });
    }

    /**
     * Called whenever the open/closed status of the Socket
     * changes.  In JavaFX, this method must be run on the main thread and
     * is accomplished by the Platform.runLater() call.  Failure to do so
     * will* result in strange errors and exceptions.
     * @param isClosed true if the socket is closed
     */
    @Override
    public void onClosedStatus(final boolean isClosed) {
        javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {
                fxListener.onClosedStatus(isClosed);
            }
        });
    }

    /**
     * Initialize the SocketClient up to and including issuing the accept()
     * method on its socketConnection.
     * @throws java.net.SocketException
     */
    @Override
    protected void initSocketConnection() throws SocketException {
        try {
            socketConnection = new Socket();
            /*
             * Allows the socket to be bound even though a previous
             * connection is in a timeout state.
             */
            socketConnection.setReuseAddress(true);
            /*
             * Create a socket connection to the server
             */
            socketConnection.connect(new InetSocketAddress(host, port));
            if (debugFlagIsSet(Constants.instance().DEBUG_STATUS)) {
                System.out.println("Connected to " + host
                        + "at port " + port);
            }
        } catch (IOException e) {
            if (debugFlagIsSet(Constants.instance().DEBUG_EXCEPTIONS)) {
                e.printStackTrace();
            }
            throw new SocketException();
        }
    }

    /**
     * For SocketClient class, no additional work is required.  Method
     * is null.
     */
    @Override
    protected void closeAdditionalSockets() {}
    
    public FxSocketClient(SocketListener fxListener,
            String host, int port, int debugFlags) {
        super(port, debugFlags);
        this.host = host;
        this.fxListener = fxListener;
    }

    public FxSocketClient(SocketListener fxListener) {
        this(fxListener, Constants.instance().DEFAULT_HOST,
                Constants.instance().DEFAULT_PORT,
                Constants.instance().DEBUG_NONE);
    }

    public FxSocketClient(SocketListener fxListener,
            String host, int port) {
        this(fxListener, host, port, Constants.instance().DEBUG_NONE);
    }
}
