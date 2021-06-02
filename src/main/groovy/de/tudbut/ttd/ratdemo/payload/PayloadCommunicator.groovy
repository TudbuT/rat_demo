package de.tudbut.ttd.ratdemo.payload

import de.tudbut.ttd.ratdemo.Main
import tudbut.io.TypedInputStream
import tudbut.io.TypedOutputStream
import tudbut.tools.Lock

import java.lang.reflect.Method

abstract class PayloadCommunicator {

    Socket comm

    Lock sendLock = new Lock(), receiveLock = new Lock()
    TypedInputStream inputStream
    TypedOutputStream outputStream

    protected void startListening() {
        new Thread({
            Method method = PayloadCommunicator.getDeclaredMethod('receivePayloads')
            method.setAccessible(true)
            method.invoke(this)
        }).start()
    }

    private void receivePayloads() {
        try {
            while (!comm.closed) {
                String clazzID = inputStream.readString()
                receiveLock.lock()
                try {
                    Class<? extends Payload> payload = Class.forName(clazzID) as Class<? extends Payload>
                    Payload p = payload.newInstance()
                    p.onReceive(inputStream, comm.getRemoteSocketAddress().hostString as String, this)
                }
                catch (Exception e) {
                    if (Main.debug)
                        e.printStackTrace()
                }
                finally {
                    receiveLock.unlock()
                }
            }
        }
        catch (Exception e) {
            if (Main.debug)
                e.printStackTrace()
        }
    }

    void sendPayload(Payload payload) {
        if(Main.debug)
            println "Sending $payload.class.name..."
        sendLock.waitHere()
        sendLock.lock()
        try {
            payload.send(comm.outputStream)
        } catch (Exception ignore) {
        }
        sendLock.unlock()
        if(Main.debug)
            println "Sent $payload.class.name!"
    }
}
