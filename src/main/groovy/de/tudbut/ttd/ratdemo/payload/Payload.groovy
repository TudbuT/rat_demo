package de.tudbut.ttd.ratdemo.payload

import tudbut.io.TypedInputStream
import tudbut.io.TypedOutputStream

interface Payload {


    default final void send(OutputStream stream) {
        TypedOutputStream t = new TypedOutputStream(stream)

        t.writeString(getClass().getName())
        serialize(t)
    }

    void serialize(TypedOutputStream stream)

    void onReceive(TypedInputStream stream, String ip, PayloadCommunicator communicator)
}