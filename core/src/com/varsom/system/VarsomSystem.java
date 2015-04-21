package com.varsom.system;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import static com.esotericsoftware.minlog.Log.*;

import com.varsom.system.games.car_game.com.varsomgames.cargame.CarGame;
import com.varsom.system.games.other_game.OtherGame;
import com.varsom.system.network.MPServer;
import com.varsom.system.network.NetworkListener;
import com.varsom.system.network.Packet;
import com.varsom.system.screens.VarsomSplash;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class VarsomSystem extends /*ApplicationAdapter*/Game {
    public static final String TITLE= "Varsom-System";

    public static final int WIDTH = 800;
    public static final int HEIGHT= 480; // used later to set window size

    //an array that stores the games IDs
    public static final int SIZE = 2;
    public static int[] games = new int[SIZE];

    MPServer mpServer;
    static protected Server server;

    private String serverIPAddress;

    public VarsomSystem(){

    }

    @Override
	public void create () {
        startServer();
        setScreen(new VarsomSplash(this));
        //setScreen(new VarsomMenu());

        //TODO ful-lösning, vi borde inte behöva berätta vilka spel eller hur många utan systemet ska känna av det
        //Handle games
        //For CarGame
        games[0] = CarGame.ID;

        //For OtherGame
        games[1] = OtherGame.ID;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private void startServer(){
        int TCP = 54555, UDP = 54777;
        String labelMessage = "Hello world!!!!!";

        // The following code loops through the available network interfaces
        // Keep in mind, there can be multiple interfaces per device, for example
        // one per NIC, one per active wireless and the loopback
        // In this case we only care about IPv4 address ( x.x.x.x format )
        List<String> addresses = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            for(NetworkInterface ni : Collections.list(interfaces)){
                for(InetAddress address : Collections.list(ni.getInetAddresses()))
                {
                    if(address instanceof Inet4Address){
                        addresses.add(address.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        // Print the contents of our array to a string.  Yeah, should have used StringBuilder
        String ipAddress = new String("");
        for(String str:addresses)
        {
            ipAddress = ipAddress + str + "\n";
        }
        serverIPAddress = addresses.get(1);
        Gdx.app.log("NETWORK", "NETWORK: IP-address is " + ipAddress);

        // Now we create a thread that will listen for incoming socket connections
        new Thread(new Runnable(){

            @Override
            public void run() {
                ServerSocketHints serverSocketHint = new ServerSocketHints();
                // 0 means no timeout.  Probably not the greatest idea in production!
                serverSocketHint.acceptTimeout = 0;

                // Create the socket server using TCP protocol and listening on 9021
                // Only one app can listen to a port at a time, keep in mind many ports are reserved
                // especially in the lower numbers ( like 21, 80, etc )
                ServerSocket serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, 9021, serverSocketHint);

                // Loop forever
                while(true){
                    // Create a socket
                    Socket socket = serverSocket.accept(null);

                    // Read data from the socket into a BufferedReader
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    /*try {
                        // Read to the next newline (\n) and display that text on labelMessage
                        labelMessage.setText(buffer.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        }).start(); // And, start the thread running
    }

    private void startServer2() {
        int TCP = 54555, UDP = 54777;
        server = new Server();
        registerPackets();
        server.addListener(new NetworkListener());
/*
        NetworkListener nl = new NetworkListener();
        nl.init(b);
        server.addListener(nl);
*/
        //TCP, UDP
        //server.bind(54555, 64555);
       // server.start();


    }

    private void startServer3(){
        // Now we create a thread that will listen for incoming socket connections
        new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    mpServer = new MPServer();
                    Log.set(LEVEL_DEBUG);;
                } catch (IOException e) {
                    e.printStackTrace();
                    Gdx.app.log("NETWORK","NETWORK: Something went wrong, couldn't create server");
                }
                // Loop forever
                while(true){

                   /* try {
                        // Read to the next newline (\n) and display that text on labelMessage
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        }).start(); // And, start the thread running

    }

    private void registerPackets() {
        Kryo kryo = server.getKryo();
        // Register packets
        kryo.register(Packet.LoginRequest.class);
        kryo.register(Packet.LoginAnswer.class);
        kryo.register(Packet.Message.class);
    }

    public String getServerIP(){
        return serverIPAddress;
    }
}

