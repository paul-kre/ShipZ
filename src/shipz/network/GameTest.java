package shipz.network;

import shipz.util.GameEventListener;
import shipz.util.ShootEvent;

import java.util.EventObject;

/**
 * Created by Paul on 05.05.2016.
 */
public class GameTest implements GameEventListener {
    Network _player;

    public void setupNetwork(char c) {
        if(c == 's')
            _player = setupServer();
        else
            _player = setupClient();

        if(_player.connected()) {
            // Server is running and connected to the client

            _player.setEventListener(this);

            (new Thread(_player)).start();

            //_player.disconnect();
        } else {
            // Something went wrong.
            System.err.println(_player.error());
        }
    }

    public void send(String msg) {
        _player.send(msg);
    }

    public Network setupServer() {
        Network server = new Network(true);
        server.connect(5555);
        return server;
    }

    public Network setupClient() {
        Network client = new Network(false);
        client.connect("localhost", 5555);
        return client;
    }

    @Override
    public void onShoot(ShootEvent e) {
        System.out.println(e.getSource() + " shot at: X:" + e.x() + ", Y: " + e.y());
    }

    @Override
    public void onSurrender(EventObject e) {
        Object source = e.getSource();
        System.out.println(source + " surrenders.");
    }

    @Override
    public void onDisconnect(EventObject e) {
        Network source = (Network) e.getSource();
        System.err.println("The opponent disconnected. \nReconnecting ...");
        source.reconnect();
        if(source.connected()) {
            System.out.println("The opponent reconnected successfully!");
            source.run();
        } else {
            System.err.println("Failed to reconnect: " + source.error());
            source.close();
        }
    }


}
