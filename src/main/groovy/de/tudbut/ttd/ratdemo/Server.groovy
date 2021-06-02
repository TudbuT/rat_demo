package de.tudbut.ttd.ratdemo

import de.tudbut.ttd.ratdemo.payload.Receiver

class Server {

    static void start(int port) {
        if(Main.debug)
            println 'Starting server...'
        ServerSocket socket = new ServerSocket(port)
        new Thread({
            if(Main.debug)
                println 'Server started...'
            while (true) {
                try {
                    Socket s = socket.accept()
                    if(Main.debug)
                        //noinspection GroovyAssignabilityCheck
                        println 'CONNECT ' + s.getRemoteSocketAddress().hostString
                    Receiver rec = new Receiver(s)
                }
                catch (Exception e) {
                    e.printStackTrace()
                }
            }
        }).start()
    }
}
