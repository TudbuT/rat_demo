package de.tudbut.ttd.ratdemo.payload


import tudbut.io.TypedInputStream
import tudbut.io.TypedOutputStream

class Receiver extends PayloadCommunicator {

    Receiver(Socket socket) {
        comm = socket
        inputStream = new TypedInputStream(comm.inputStream)
        outputStream = new TypedOutputStream(comm.outputStream)
        outputStream.writeString('de.tudbut.ttd.ratdemo')
        if (inputStream.readString() != 'de.tudbut.ttd.ratdemo') {
            throw new IllegalArgumentException("Not a matching client!")
        }
        outputStream.writeString('OK')
        if (inputStream.readString() != 'OK') {
            throw new IllegalArgumentException("Not a matching client!")
        }
        startListening()
    }
}
