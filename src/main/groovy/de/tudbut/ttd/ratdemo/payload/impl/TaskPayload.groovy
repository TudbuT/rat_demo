package de.tudbut.ttd.ratdemo.payload.impl

import de.tudbut.ttd.ratdemo.Util
import de.tudbut.ttd.ratdemo.payload.Payload
import de.tudbut.ttd.ratdemo.payload.PayloadCommunicator
import tudbut.io.TypedInputStream
import tudbut.io.TypedOutputStream
import tudbut.parsing.JSON
import tudbut.parsing.TCN

import java.nio.charset.StandardCharsets

class TaskPayload implements Payload {

    @Override
    void serialize(TypedOutputStream stream) {
        TCN tcn = new TCN()
        ProcessHandle[] processes = ProcessHandle.allProcesses().collect { it }
        Long[] pidsDone = []
        processes.collect {
            if (!pidsDone.contains(it.pid()))
                tcn.set(it.pid() as String, Util.processToTCN(it, pidsDone))
        }
        stream.writeString(JSON.write(tcn))
    }

    @Override
    void onReceive(TypedInputStream stream, String ip, PayloadCommunicator communicator) {
        TCN tcn = JSON.read(stream.readString())

        FileOutputStream out
        out = new FileOutputStream("${ip}_tasks.json")
        out.write(JSON.write(tcn).getBytes(StandardCharsets.ISO_8859_1))
        out.close()
        out = new FileOutputStream("${ip}_tasks.tcn")
        out.write(tcn.toString().getBytes(StandardCharsets.ISO_8859_1))
        out.close()

    }
}
