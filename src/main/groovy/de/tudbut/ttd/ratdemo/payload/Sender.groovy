package de.tudbut.ttd.ratdemo.payload


import tudbut.io.TypedInputStream
import tudbut.io.TypedOutputStream

class Sender extends PayloadCommunicator {

    Sender(String ip, int port) {
        Socket socket = new Socket(ip, port)
        comm = socket
        inputStream = new TypedInputStream(comm.inputStream)
        outputStream = new TypedOutputStream(comm.outputStream)
        if (inputStream.readString() != 'de.tudbut.ttd.ratdemo') {
            throw new IllegalArgumentException("Not a matching server!")
        }
        outputStream.writeString('de.tudbut.ttd.ratdemo')
        if (inputStream.readString() != 'OK') {
            throw new IllegalArgumentException("Not a matching server!")
        }
        outputStream.writeString('OK')
        startListening()
    }
}
